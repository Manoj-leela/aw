package sg.activewealth.roboadvisor.infra.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import sg.activewealth.roboadvisor.infra.controller.interceptor.SecurityInterceptor;

public class CookieUtils {
	
	private Logger logger = Logger.getLogger(CookieUtils.class);
		
	private static CookieUtils me;

	public static CookieUtils getInstance() {
		if (me == null) me = new CookieUtils();

		return me;
	}

	public void addCookieToResponse(HttpServletResponse response, String name, String value) {
		addCookieToResponse(response, name, value, -1);
	}
	
	private void addCookieToResponse(HttpServletResponse response, String name, String value, int secondsToLive) {
		Cookie cookie = null;
	/*	try {
			cookie = new Cookie(name, URLEncoder.encode(value, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e.getMessage(),e);
		}*/
		if(SecurityInterceptor.SECURITY_COOKIE_NAME.equals(name)){
			value = value.replace(" ", "_");
		}
		cookie = new Cookie(name, value);
		cookie.setMaxAge(secondsToLive);
		cookie.setSecure(false);
		cookie.setPath("/");
		response.addCookie(cookie);
	}

	public void clearCookie(HttpServletResponse response, String name) {
		addCookieToResponse(response, name, "", 0);
	}

	public String getCookieValue(HttpServletRequest request, String name) {
		List<Cookie> cookieList = new ArrayList<Cookie>();

		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					cookieList.add(cookie);
				}
			}
		}

		if (cookieList.size() == 0 || ValidationUtils.getInstance().isEmptyString(cookieList.get(0).getValue())) return null;
		else return cookieList.get(0).getValue();
	}
}
