package sg.activewealth.roboadvisor.infra.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.activewealth.roboadvisor.infra.dto.UserOperationContextDto;
import sg.activewealth.roboadvisor.infra.dto.UserSessionDto;
import sg.activewealth.roboadvisor.infra.enums.ApplicationContext;
import sg.activewealth.roboadvisor.infra.enums.UserOperationContextResultType;
import sg.activewealth.roboadvisor.infra.helper.MailSenderHelper;
import sg.activewealth.roboadvisor.infra.helper.PropertiesHelper;
import sg.activewealth.roboadvisor.infra.utils.ValidationUtils;
import sg.activewealth.roboadvisor.infra.utils.WebUtils;

@Service
public class UserOperationContextService {

	protected Logger logger = Logger.getLogger(UserOperationContextService.class);

	private static final String CONTEXT_PARAMETER_NAME = "c";

	private static final String VERSION_PARAMETER_NAME = "cv";

	private static final String DELIM = "|";
	
	private final ThreadLocal<UserOperationContextDto> context = new ThreadLocal<UserOperationContextDto>();
	
	private final ThreadLocal<HttpServletRequest> request = new ThreadLocal<HttpServletRequest>();

	@Autowired
	private UserSessionService userSessionService;

	@Autowired
	protected PropertiesHelper propertiesHelper;
	
	@Autowired
	protected MailSenderHelper mailSenderHelper;

	public void set(UserOperationContextResultType resultType, String errorKey) {
		if (this.context.get() == null) {
			this.context.set(new UserOperationContextDto());
		}
		this.context.get().set(resultType, errorKey);
	}

	public void set(HttpServletRequest request) {
		this.request.set(request);
	}
	
	public void clear() {
		this.context.set(null);
		this.request.set(null);
		this.context.remove();
		this.request.remove();
	}
	
	public UserOperationContextDto get() {
		return this.context.get();
	}
	
	public ApplicationContext getApplicationContext() {
		HttpServletRequest request = this.request.get();
		if (request != null) {
			String context = request.getParameter(CONTEXT_PARAMETER_NAME);
			if (!ValidationUtils.getInstance().isEmptyString(context)) {
				ApplicationContext enums = ApplicationContext.valueOf(context);
				if (enums != null) return enums;
			}
		}
		
		return ApplicationContext.WebRoot;
	}

	public String getApplicationContextVersionString() {
		HttpServletRequest request = this.request.get();
		String version = request.getParameter(VERSION_PARAMETER_NAME);
		if (version != null) return version;

		//cannot find version
		return "1.0.0";		
	}

	public Boolean getReading() {
		HttpServletRequest request = this.request.get();
		return request.getMethod().equals("GET");
	}
	
	public String buildCookieValue() {
		return get().getResultType().toString() + DELIM + get().getKey();
	}
	
	public void setCookieValue(String cookieValue) {
		StringTokenizer st = new StringTokenizer(cookieValue, DELIM);
		String resultType = st.nextToken();
		String message = st.nextToken();

		if (!ValidationUtils.getInstance().isEmptyString(message) && !message.equals("null"))
			this.set(UserOperationContextResultType.valueOf(resultType), message);
	}
	
	/* logging code */
	public void warn(Exception e) {
		this.warn(e, null);
	}
	
	public void warn(Exception e, String message) {
		if (e != null && (e.getClass().getSimpleName().equals("ClientAbortException") ||
			e instanceof org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException)) {} //ignore certain exception type
		else {
			if (e != null) logger.warn(e.getMessage(), e);
			mailSenderHelper.sendMail(propertiesHelper.appNameStylizedShort + ((!propertiesHelper.appIsProduction)?" staging":"") + " - System Warning!", new String[]{propertiesHelper.notificationSystemErrors},
					((message != null)?"Message: " + message + "\n":"") + buildExceptionLog(e, this.request.get(), userSessionService.get()), false, false);
		}
	}

	public void error(Exception e) {
		this.error(e, null);
	}
	
	public void error(Exception e, String message) {
		if (e.getClass().getSimpleName().equals("ClientAbortException") ||
			e instanceof org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException) {} //ignore certain exception type
		else {
			logger.error(e.getMessage(), e);
			mailSenderHelper.sendMail(propertiesHelper.appNameStylizedShort + ((!propertiesHelper.appIsProduction)?" staging":"") + " - System Exception!", new String[]{propertiesHelper.notificationSystemErrors},
					((message != null)?"Message: " + message + "\n":"") + buildExceptionLog(e, this.request.get(), userSessionService.get()), false, false);
		}
	}

	public void jserror(String message) {
		/*if (message.contains("[object Event]")) {
			//ignore certain messages
		}
		else {
			logger.error(message);
			mailSenderHelper.sendMail("roboadvisor" + ((!propertiesHelper.appIsProduction)?" staging":"") + " - Javascript Exception!", new String[]{propertiesHelper.notificationSystemErrors}, 
					((message != null)?"Message: " + message + "\n":"") + WebUtils.getInstance().buildLog(new StringBuffer(), this.request.get(), WebUtils.getInstance().getRequestPage(this.request.get()), userSessionService.get(), false, true).toString(), false, false);
		}*/ //do nothing for JS errors!
	}

	private String buildExceptionLog(Exception e, HttpServletRequest request, UserSessionDto userSession) {
		if (e == null) return "";
		
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String exceptionAsString = sw.toString();
				
		StringBuffer sb = new StringBuffer();
		if (request != null) sb.append(WebUtils.getInstance().buildLog(new StringBuffer(), request, WebUtils.getInstance().getRequestPage(request), userSession, true, true));
		sb.append("stack trace:\n" + exceptionAsString + "\n");
		return sb.toString();
	}
}
