package sg.activewealth.roboadvisor.infra.controller.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import sg.activewealth.roboadvisor.infra.dto.UserSessionDto;
import sg.activewealth.roboadvisor.infra.exception.UnauthorisedAccessException;
import sg.activewealth.roboadvisor.infra.utils.CookieUtils;
import sg.activewealth.roboadvisor.infra.utils.StringUtils;
import sg.activewealth.roboadvisor.infra.utils.ValidationUtils;
import sg.activewealth.roboadvisor.infra.utils.WebUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class SecurityInterceptor extends AbstractInterceptor {

	public static final String SECURITY_COOKIE_NAME = "roboadvisor_sec";

	public static final String SECURE_COOKIE_PARAMETER_NAME = "av";
	public static final String MOBILE_APP_CONTEXT = "MobileApp";

	private String[] webNoauthUrls;

	@PostConstruct
	public void init() {
		webNoauthUrls = StringUtils.getInstance().buildStringArrayFromString(propertiesHelper.securityWebNoAuthUrls,
				",");
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (propertiesHelper.getSecurityBlockedIpsList().contains(request.getRemoteAddr()))
			return false;
		// Thread.sleep(3000);

		WebUtils.getInstance().setPrehandleTiming(request);

		// if (true) return true;
		String requestURL = request.getRequestURL().toString();
		// bypass all showimages
		if (requestURL.contains(WebUtils.SHOW_IMAGE_URL) && "GET".equalsIgnoreCase(request.getMethod())) {
			// Thread.sleep(5000);
			return true;
		}

		// try reading cookie
		UserSessionDto userSession = null;

		String cookieValue = null;
		// param takes precedence
		String paramCookieValue = request.getHeader(SECURE_COOKIE_PARAMETER_NAME);
		/*if(paramCookieValue == null){
			paramCookieValue = request.getHeader(SECURE_COOKIE_PARAMETER_NAME);
		}*/
		boolean isFromSecureCookieParameter = false;
		if (!ValidationUtils.getInstance().isEmptyString(paramCookieValue)) {
			cookieValue = paramCookieValue;
			isFromSecureCookieParameter = true;
		}

		// check if is in noauth. shifted noauthurls here so that session can be
		// instantiated first even for noauthurls.
		boolean noAuthUrl = determineIsNoAuthUrl(requestURL, request);

		//Validate whether mobileApp context has av parameter or not
		final String context = request.getParameter("c");
		if(MOBILE_APP_CONTEXT.equalsIgnoreCase(context) && !noAuthUrl && !StringUtils.getInstance()
						.hasValue(paramCookieValue)){
			throw new UnauthorisedAccessException();
		}

		// if paramcookievalue not set, try reading from cookie
		if (cookieValue == null)
			cookieValue = CookieUtils.getInstance().getCookieValue(request, SECURITY_COOKIE_NAME);
		if (cookieValue != null) {
			// for jquery cookie plugin, we can't use raw. using raw would
			// encounter an issue with the = after base64 signing.
			// therefore, for JS with encoded cookie value, we need to decode.
			// no issues for cookie values which are not encoded. decoding twice
			// will not encounter any problem.
			// update DB everytime we authenticate - better tracking over the
			// last_login and ip.

			boolean updateDatabase = true;
			if (requestURL.contains(WebUtils.SHOW_IMAGE_URL) || requestURL.contains(WebUtils.LOGIN_URL))
				updateDatabase = false; // do not update database because
										// /fblogin, /fbloginStart,
										// /api/r/attachment#POST does not
										// contain c and cv
			userSession = userSessionService.buildUserSession(request,
					WebUtils.getInstance().getDecodedValue(cookieValue), updateDatabase, noAuthUrl);
		}
		if (userSession != null)
			userSession.setIsFromSecureCookieParameter(isFromSecureCookieParameter);

		if (noAuthUrl) {
			if (userSession != null)
				userSession.setIsAccessingNonAuth(true);
			return true;
		}

		// at this point, all urls are secure.
		// if not logged in, forward to login page
		if (userSession == null) {
			//Return 403 in case of MobileApp
			if(MOBILE_APP_CONTEXT.equalsIgnoreCase(context)){
				throw new UnauthorisedAccessException();
			}
			if (!requestURL.contains("facebook") && !requestURL.contains("callback")) {
				return forwardToLoginPageAndNullifyUserSessionAndClearCookie(request, response);
			}
		}

		return true;
	}

	private boolean determineIsNoAuthUrl(String requestURL, HttpServletRequest request) {
		for (int i = 0; i < webNoauthUrls.length; i++) {
			String webNoauthUrl = webNoauthUrls[i];

			// assume method match
			boolean httpMethodMatched = true;
			if (webNoauthUrl.contains("#")) {
				// check if it actually match if configured with # sign
				String httpMethodToMatch = webNoauthUrl.substring(webNoauthUrl.indexOf("#") + 1, webNoauthUrl.length());
				httpMethodMatched = request.getMethod().equalsIgnoreCase(httpMethodToMatch);

				// extract url part
				webNoauthUrl = webNoauthUrl.substring(0, webNoauthUrl.indexOf("#"));
			}

			boolean urlMatched = false;
			// eg: /account/feedbackResponse/search/
			if ((webNoauthUrl.endsWith("/") && requestURL.endsWith(webNoauthUrl))
					|| (!webNoauthUrl.endsWith("/") && requestURL.contains(webNoauthUrl)))
				urlMatched = true;

			if (urlMatched && httpMethodMatched) {
				return httpMethodMatched;
			}
		}
		return false;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if (userSessionService.get() != null) { // if already signed in
			CookieUtils.getInstance().clearCookie(response, SECURITY_COOKIE_NAME);
			CookieUtils.getInstance().addCookieToResponse(response, SECURITY_COOKIE_NAME,
					userSessionService.buildFreshCookieValue());
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		if (Boolean.valueOf(propertiesHelper.securityLogAccess))
			WebUtils.getInstance().logAccess(request, userSessionService.get(), true);
		userSessionService.clear();
	}

	@Override
	public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		userSessionService.clear();
	}

	private boolean forwardToLoginPageAndNullifyUserSessionAndClearCookie(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		userSessionService.clear();
		CookieUtils.getInstance().clearCookie(response, SECURITY_COOKIE_NAME);
		CookieUtils.getInstance().clearCookie(response, UserOperationContextInterceptor.UOC_COOKIE_NAME);

		// response.setStatus(HttpStatus.UNAUTHORIZED.value());

		String requestPage = WebUtils.getInstance().getRequestPage(request);
		String url = request.getContextPath() + "/r/admin/login?forwardUrl="
				+ WebUtils.getInstance().getEncodedValue(requestPage);
		if (request.getParameter("isEmbeddedPage") != null && request.getParameter("isEmbeddedPage").equals("true"))
			url += "&isEmbeddedPage=true";
		response.sendRedirect(url);

		return false;
	}

}
