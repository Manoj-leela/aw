package sg.activewealth.roboadvisor.infra.controller.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import sg.activewealth.roboadvisor.infra.dto.UserOperationContextDto;
import sg.activewealth.roboadvisor.infra.enums.UserOperationContextResultType;
import sg.activewealth.roboadvisor.infra.utils.CookieUtils;
import sg.activewealth.roboadvisor.infra.utils.ValidationUtils;

@Component
public class UserOperationContextInterceptor extends AbstractInterceptor {

	public static final String UOC_COOKIE_NAME = "roboadvisor_uoc";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		userOperationContextService.set(request);

		String messageCookie = CookieUtils.getInstance().getCookieValue(request, UOC_COOKIE_NAME);

		//if there is a token, extract the values, and set it onto threadlocal
		//removed  "|| request.getHeader("referer") == null". because direct access and intercepted by security, followed by redirect will cause referer to be null.
		if (messageCookie != null) //if nothing, or direct access, clear. 
			userOperationContextService.setCookieValue(messageCookie);

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		UserOperationContextDto dto = userOperationContextService.get();
		CookieUtils.getInstance().clearCookie(response, UOC_COOKIE_NAME);

		if (dto != null) { //if there is something, write it back to the cookie
			CookieUtils.getInstance().addCookieToResponse(response, UOC_COOKIE_NAME, userOperationContextService.buildCookieValue());
			if (!ValidationUtils.getInstance().isEmptyString(dto.getKey())) {
				try { dto.setMessageFromBundle(getContextMessage(dto.getKey())); }
				catch (NoSuchMessageException e) { dto.setMessageFromBundle(dto.getKey()); }
			}
			request.setAttribute("userOperationContext", dto);
			request.setAttribute("userOperationContextCookieName", UOC_COOKIE_NAME);

			if (dto.getResultType() ==  UserOperationContextResultType.Failure)
				logger.info("failure message: " + dto.getKey());
		}
		
		userOperationContextService.clear();
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		userOperationContextService.clear(); //always clear
	}

	@Override
	public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		userOperationContextService.clear(); //always clear
	}

}
