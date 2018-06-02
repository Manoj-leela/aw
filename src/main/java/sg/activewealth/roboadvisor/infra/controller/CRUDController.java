package sg.activewealth.roboadvisor.infra.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import sg.activewealth.roboadvisor.infra.dto.ErrorsDto;
import sg.activewealth.roboadvisor.infra.dto.FilterDto;
import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.infra.enums.UserOperationContextResultType;
import sg.activewealth.roboadvisor.infra.exception.ObjectNotFoundException;
import sg.activewealth.roboadvisor.infra.exception.ValidateException;
import sg.activewealth.roboadvisor.infra.model.AbstractModel;
import sg.activewealth.roboadvisor.infra.service.AbstractService;

@Controller
public abstract class CRUDController<T extends AbstractModel, S extends AbstractService<T>> extends AbstractController {

	protected Logger logger = Logger.getLogger(CRUDController.class);

	protected S service;

	private Class<T> modelClass;

	public CRUDController() {
		this(null, null);
	}

	public CRUDController(Class<T> modelClass, S service) {
		this.modelClass = modelClass;
		this.service = service;
	}

	// ********************* COMMON JAVA METHODS TO BE SHARED ACROSS ALL
	// CONTROLLERS *********************
	public String getFullJspPath(String jspFilename) {
		String ret = "";
		if (getModelName() != null)
			ret += "/" + getModelName();
		ret += "/" + jspFilename;

		return "admin/" + ret;
	}

	public String getModelName() {
		if (modelClass == null)
			return null;
		return modelClass.getSimpleName();
	}

	// ********************* COMMON SPRING COMPONENTS *********************
	public T initDefaultModelAttribute() {
		try {
			return modelClass.newInstance();
		} catch (Exception e) {
			userOperationContextService.warn(e);
		}
		return null;
	}

	@ModelAttribute
	public T setupDefaultModelAttribute(@RequestParam(value = "id", required = false) String id) {
		T model = (id != null ? service.retrieve(id) : initDefaultModelAttribute());
		if (model == null) {
            throw new ObjectNotFoundException("error.object.notfound");
        }
		return model;
	}

	@ModelAttribute
	public PagingDto<T> setupPagingDtoModelAttribute(
			@RequestParam(value = "resultsPerPage", required = false) Integer resultsPerPage,
			@RequestParam(value = "currentPage", required = false) Integer currentPage) {
		PagingDto<T> pagingDto = setupPagingDto();
		if (resultsPerPage != null && currentPage != null) {
			pagingDto.setResultsPerPage(resultsPerPage);
			pagingDto.setCurrentPage(currentPage);
		}

		return pagingDto;
	}

	protected PagingDto<T> setupPagingDto() {
		return new PagingDto<T>();
	}

	// ********************* OVERWRITEABLE METHODS *********************
	public Object[] preViewGet(T model, HttpServletRequest request) {
		return preCreateUpdateGet(model, request);
	}

	public Object[] preCreateUpdateGet(T model, HttpServletRequest request) {
		return new Object[] {};
	}

	public T preCreateUpdatePost(T model, HttpServletRequest request) {
		return model;
	}

	public T postCreateUpdatePost(T model, HttpServletRequest request) {
		return model;
	}

	public Object redirect(T model, HttpServletRequest request) {
		return "redirect:list";
	}

	// ********************* COMMON REQUEST MAPPINGS *********************
	@RequestMapping(value = { "/view" }, method = RequestMethod.GET)
	public Object viewGet(@ModelAttribute T model, HttpServletRequest request) {
		Object[] ret = preViewGet(model, request);
		return modelAndView(getFullJspPath("view"), ret).addObject("model", model);
	}

	@RequestMapping(value = { "/create", "/update" }, method = RequestMethod.GET)
	public Object createUpdateGet(@ModelAttribute T model, HttpServletRequest request) {
		Object[] ret = preCreateUpdateGet(model, request);
		return modelAndView(getFullJspPath("fields"), ret).addObject("model", model);
	}

	@SuppressWarnings("unused")
	@RequestMapping(value = { "/create", "/update" }, method = RequestMethod.POST)
	public Object createUpdatePost(@ModelAttribute T model, Errors springErrors, HttpServletRequest request) {
		model = preCreateUpdatePost(model, request);

		ErrorsDto errors = null;
		try {
			model = service.save(model);
		} catch (ValidateException e) {
			errors = super.convertValidateExceptionToErrors(e, springErrors);
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			userOperationContextService.warn(e);
			addRejectError(springErrors, e.getMessage());
		}

		if (springErrors.hasErrors()) {
			addStandardFailureMessage();
			return createUpdateGet(model, request);
		} else
			addStandardSuccessMessage();

		model = postCreateUpdatePost(model, request);
		return redirect(model, request);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Object delete(@ModelAttribute T model, HttpServletRequest request) {
        service.delete(model);
        userOperationContextService.set(UserOperationContextResultType.Success,
                getContextMessage("message.crud.delete"));
        return redirect(model, request);
    }

	@RequestMapping("/list")
	public Object list(@ModelAttribute PagingDto<T> pagingDto,
			@RequestParam(value = "ids", required = false) String ids, HttpServletRequest request) {
		if (ids != null && !ids.equals("null")) {
			StringTokenizer st = new StringTokenizer(ids, ",");
			while (st.hasMoreTokens())
				pagingDto.getIds().add(st.nextToken());
		}
		String queryparams = request.getParameterMap().entrySet().stream()
				.filter(p -> !p.getKey().equalsIgnoreCase("currentPage")
						&& !p.getKey().equalsIgnoreCase("resultsPerPage"))
				.map(p -> p.getKey() + "=" + p.getValue()[0]).reduce((p1, p2) -> p1 + "&" + p2).orElse(null);
		  request.setAttribute("filterPage", request.getAttribute("requestPageWithoutQueryString")
	                + (queryparams == null ? "?" : "?" + queryparams + "&"));
		List<FilterDto> filters = buildFilters(request);
		if (!filters.isEmpty()) {
			pagingDto.setFilters(filters);
		}
		pagingDto = service.retrieve("createdOn desc", pagingDto, false);
		listPostProcesser(pagingDto);
		return modelAndView(getFullJspPath("list"), "list", pagingDto);
	}
	
	/**
	 * Hook to process list after retrieval
	 * 
	 * @param pagingDto
	 */
	protected void listPostProcesser(PagingDto<T> pagingDto) {
		return;
	}

	protected List<FilterDto> buildFilters(HttpServletRequest request) {
		List<FilterDto> filters = new ArrayList<>();
		Map<String, String[]> params = request.getParameterMap();
		boolean isDate = false;
		for (String key : params.keySet()) {
			if (key.startsWith("dt_")) {
				isDate = true;
			}
			if (key.equalsIgnoreCase("currentPage") || key.equalsIgnoreCase("resultsPerPage")) {
				continue;
			}
			String[] values = params.get(key);
			for (String value : values) {
				if (isDate) {
					String dates[] = value.split("-");
					String column = key.substring(key.indexOf("dt_") + 3, key.length());
					if (dates.length == 2) {
						LocalDate from = LocalDate.parse(dates[0], DateTimeFormatter.ofPattern(DATE_FORMAT_FOR_FORM));
						LocalDate to = LocalDate.parse(dates[1], DateTimeFormatter.ofPattern(DATE_FORMAT_FOR_FORM));
						Object localDate[] = { from, to };
						filters.add(new FilterDto(column, FilterDto.Operetor.BETWEEN, localDate));
					} else {
						LocalDate date = LocalDate.parse(value, DateTimeFormatter.ofPattern(DATE_FORMAT_FOR_FORM));
						Object localDate[] = { date };
						filters.add(new FilterDto(column, localDate));
					}
				} else {
					filters.add(new FilterDto(key, values));
				}
			}
		}
		return filters;
	}
}
