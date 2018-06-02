package sg.activewealth.roboadvisor.infra.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import sg.activewealth.roboadvisor.infra.controller.binder.CustomDateEditorWithoutTime;
import sg.activewealth.roboadvisor.infra.controller.binder.CustomLocalDateEditor;
import sg.activewealth.roboadvisor.infra.controller.binder.StringReplaceTrimmerEditor;
import sg.activewealth.roboadvisor.infra.controller.interceptor.SecurityInterceptor;
import sg.activewealth.roboadvisor.infra.dto.ErrorDto;
import sg.activewealth.roboadvisor.infra.dto.ErrorsDto;
import sg.activewealth.roboadvisor.infra.enums.UserOperationContextResultType;
import sg.activewealth.roboadvisor.infra.exception.AbstractException;
import sg.activewealth.roboadvisor.infra.exception.JsonObjectNotFoundException;
import sg.activewealth.roboadvisor.infra.exception.ObjectNotFoundException;
import sg.activewealth.roboadvisor.infra.exception.ValidateException;
import sg.activewealth.roboadvisor.infra.helper.IAttachmentableHelper;
import sg.activewealth.roboadvisor.infra.helper.PropertiesHelper;
import sg.activewealth.roboadvisor.infra.service.SecurityService;
import sg.activewealth.roboadvisor.infra.service.UserOperationContextService;
import sg.activewealth.roboadvisor.infra.service.UserSessionService;
import sg.activewealth.roboadvisor.infra.utils.SystemUtils;
import sg.activewealth.roboadvisor.infra.utils.WebUtils;

@Controller
public abstract class AbstractController {

    protected Logger logger = Logger.getLogger(AbstractController.class);

    public SimpleDateFormat DATE_FORMAT_FOR_FORM_OBJECT =
            new SimpleDateFormat(AbstractController.DATE_FORMAT_FOR_FORM);

    public DateTimeFormatter LOCAL_DATE_FORMAT_FOR_FORM_OBJECT =
            DateTimeFormatter.ofPattern(AbstractController.DATE_FORMAT_FOR_FORM);

    public static final String DATE_FORMAT_FOR_FORM = "yyyy-MM-dd";

    public static final String DATE_FORMAT_FOR_PRINT = "dd MMM yyyy";

    public static final String DATE_FORMAT_FOR_GRAPH = "dd MMM";

    public static final String DATE_TIME_FORMAT_FOR_PRINT = "dd MMM yyyy, HH:mm'h'";

    public static final String DOUBLE_FORMAT_FOR_PRINT = "#0.00";

    public static final String INTEGER_FORMAT_FOR_PRINT = "#0";

    public static final String INTEGER_FORMAT_FOR_REWARD_CUSTOMER_SERIAL_NUMBER = "#000000";

    public static final String INTEGER_FORMAT_WITH_SIGNS_FOR_PRINT = "'+'#0;'-'#0";
   
    @Autowired
    protected ApplicationContext context;

    @Autowired
    protected UserOperationContextService userOperationContextService;

    @Autowired
    protected UserSessionService userSessionService;

    @Autowired
    protected PropertiesHelper propertiesHelper;

    @Autowired
    protected SecurityService securityService;

    @Autowired
    protected IAttachmentableHelper attachmentHelper;
    
    @InitBinder
    public void initBinder(WebDataBinder binder, HttpServletRequest request) {
        DATE_FORMAT_FOR_FORM_OBJECT.setLenient(false);
        binder.registerCustomEditor(Date.class,
                new CustomDateEditorWithoutTime(DATE_FORMAT_FOR_FORM_OBJECT, true));
        binder.registerCustomEditor(String.class,
                new StringReplaceTrimmerEditor(WebUtils.getInstance().getRequestPage(request)));
        binder.registerCustomEditor(LocalDate.class,
                new CustomLocalDateEditor(LOCAL_DATE_FORMAT_FOR_FORM_OBJECT, true));

    }

    // ********************* COMMON JAVA METHODS TO BE SHARED ACROSS ALL
    // CONTROLLERS
    // *********************
    protected ErrorsDto convertValidateExceptionToErrors(ValidateException ex,
            Errors springErrors) {
        ErrorsDto errors = ex.getErrors();
        if (ex.getMessage() != null) {
            addRejectError(springErrors, ex.getMessage());
        }

        for (ErrorDto error : errors.getErrors()) {
            if (error.getIsFieldError()) {
                springErrors.rejectValue(error.getErrorField(), error.getErrorKey(),
                        error.getErrorArgs(), error.getErrorKey());
            } else {
                springErrors.reject(error.getErrorKey());
            }
        }

        if (errors.hasErrors()) {
            logger.info("errors: " + errors.toString());
        }
        return errors;
    }

    protected boolean addRejectError(Errors springErrors, String message) {
        if (!springErrors.hasFieldErrors("")) {
            springErrors.reject("error.nonfielderror", new String[] {message}, message);
            return true;
        }
        return false;
    }

    protected void addStandardSuccessMessage() {
        if (userOperationContextService.get() == null) {
            userOperationContextService.set(UserOperationContextResultType.Success,
                    "message.crud.success");
        }
    }

    protected void addStandardFailureMessage() {
        if (userOperationContextService.get() == null) {
            userOperationContextService.set(UserOperationContextResultType.Failure,
                    "message.crud.failure");
        }
    }

    protected ModelAndView modelAndView(String view, Object... model) {
        return new ModelAndView(view,
                SystemUtils.getInstance().buildMap(new HashMap<String, Object>(), model));
    }
    
  @ExceptionHandler(ValidateException.class)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<?> handleValidationException(ValidateException validateException) {
    Map<String, Object> errorMessages = new LinkedHashMap<>(2);
    for (ErrorDto errorDto : validateException.getErrors().getErrors()) {
      String errorKey = errorDto.getErrorKey();
      String errorField = errorDto.getErrorField();
      Object[] errorArg = errorDto.getErrorArgs();
      if (errorKey != null && errorArg != null && errorField != null) {
    	errorMessages.put("success", Boolean.FALSE);  
        errorMessages.put(errorField, this.getContextMessage(errorKey, errorArg));

      } else if (errorKey != null && errorArg != null) {
    	errorMessages.put("success", Boolean.FALSE);
        errorMessages.put("errorField", this.getContextMessage(errorKey, errorArg));
      }
    }
    return new ResponseEntity<>(errorMessages, HttpStatus.OK);
  }
  
  @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Object handleException(Exception e, HttpServletRequest request) {
	  	ModelAndView view = modelAndView("error");
        //Since mobile client is dealing with JSON response,we can not return ModelAndView
        if(SecurityInterceptor.MOBILE_APP_CONTEXT.equalsIgnoreCase(request.getParameter("c"))){
            logger.error(e.getMessage(),e);
            if(e instanceof AbstractException){
                final ResponseStatus responseStatus = e.getClass().getAnnotation(ResponseStatus.class);
                if(responseStatus != null){
                    return new ResponseEntity<>(responseStatus.reason(),responseStatus.value());
                }
            }
            return SystemUtils.getInstance().buildMap(new HashMap<String, Object>(), "success", false, "message",
    				e.getMessage());
        }
        else{
            userOperationContextService.error(e);
            view.addObject("msg", getContextMessage("message.exception"));
            request.setAttribute("exceptionObject", e);
            return modelAndView("error");
        }
    }

    public String getContextMessage(String code) {
        return context.getMessage(code, null, LocaleContextHolder.getLocale());
    }

    public String getContextMessage(String code, Object[] args) {
        return context.getMessage(code, args, LocaleContextHolder.getLocale());
    }
    
    @ExceptionHandler(JsonObjectNotFoundException.class)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody Object handleJsonException(Exception e, HttpServletRequest request) {
		
		if (e instanceof JsonObjectNotFoundException) {
			return SystemUtils.getInstance().buildMap(new HashMap<String, Object>(), "success", false, "message",
				e.getMessage());
		}
		return null;
	}
    
    @ExceptionHandler({ ObjectNotFoundException.class, MissingServletRequestParameterException.class})
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Object handleObjectNotFoundException(Exception e, HttpServletRequest request) {
		userOperationContextService.warn(e);
		userOperationContextService.set(UserOperationContextResultType.Failure, "error.generic");
		return modelAndView("404");
	}
    
    @ExceptionHandler(org.springframework.web.servlet.NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody Object NoHandlerFoundException(Exception e, HttpServletRequest request) {
    	if(!SecurityInterceptor.MOBILE_APP_CONTEXT.equalsIgnoreCase(request.getParameter("c"))){
    		return modelAndView("404");
    	}
    	return null;
    }
    
}
