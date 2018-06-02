package sg.activewealth.roboadvisor.infra.controller;

import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import sg.activewealth.roboadvisor.common.model.User;
import sg.activewealth.roboadvisor.common.service.UserService;
import sg.activewealth.roboadvisor.infra.controller.interceptor.SecurityInterceptor;
import sg.activewealth.roboadvisor.infra.enums.UserOperationContextResultType;
import sg.activewealth.roboadvisor.infra.exception.SystemException;
import sg.activewealth.roboadvisor.infra.exception.ValidateException;
import sg.activewealth.roboadvisor.infra.helper.MailSenderHelper;
import sg.activewealth.roboadvisor.infra.helper.PropertiesHelper;
import sg.activewealth.roboadvisor.infra.utils.CookieUtils;
import sg.activewealth.roboadvisor.infra.utils.ValidationUtils;
import sg.activewealth.roboadvisor.infra.utils.WebUtils;

@Controller
public class SecurityController extends AbstractController {

	@Autowired
	private UserService userService;

	// For spring social security
	@Autowired
	PropertiesHelper propertiesHelper;

	@Autowired
	protected VelocityEngine velocityEngine;

	@Autowired
	protected MailSenderHelper mailSenderHelper;

	@ModelAttribute
	public LoginForm setupDefaultModelAttribute() {
		return new LoginForm();
	}

	private void clearAllCookies(HttpServletResponse response) {
		CookieUtils.getInstance().clearCookie(response, SecurityInterceptor.SECURITY_COOKIE_NAME);
		CookieUtils.getInstance().clearCookie(response, SecurityInterceptor.SECURE_COOKIE_PARAMETER_NAME);
	}

	// ########################### LOGOUT ########################## //

	@RequestMapping(value = "/admin/logout", method = RequestMethod.GET)
	public Object logout(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "forwardUrl", required = false) String forwardUrl) throws ServletException {
		userSessionService.clear();
		clearAllCookies(response);

		// Facebook logout
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		request.logout();

		if (!ValidationUtils.getInstance().isEmptyString(forwardUrl)) {
			return "redirect:" + WebUtils.getInstance().getFullUrl(request, forwardUrl);
		} else {
			return "redirect:/r/admin/login/";
		}
	}

	// ########################### LOGIN GET ########################## //

	@RequestMapping(value = "/admin/login", method = RequestMethod.GET)
	public Object loginGet(@ModelAttribute LoginForm model, Errors errors, HttpServletRequest request,
			HttpServletResponse response) {
		clearAllCookies(response);
		return modelAndView("/admin/login");
	}

	// ########################### LOGIN POST ############################ //

	@RequestMapping(value = "/admin/login", method = RequestMethod.POST)
	public Object loginPost(@ModelAttribute LoginForm model, Errors errors, HttpServletRequest request,
			HttpServletResponse response) {
		Object ret = doLoginPost(model, errors, request, response);
		if (ret != null) {
			return ret;
		} else {
			return loginGet(model, errors, request, response);
		}
	}

	// ########################### SHARED METHODS ############################//

	private Object doLoginPost(LoginForm model, Errors errors, HttpServletRequest request,
			HttpServletResponse response) {
		String emailAddress = model.getEmailAddress();
		String password = model.getPassword();
		String forwardUrl = request.getParameter("forwardUrl");// model.getForwardUrl();

		boolean proceed = false;
		// user session may be populated thru' authvalue
		if (userSessionService.get() != null) {
			proceed = true;
			// email and password must be entered. otherwise
			// userService.retrieveObjectByEmailAddressAndPassword's getHash
			// would
			// get NPE.
		} else {
			proceed = !ValidationUtils.getInstance().isEmptyString(emailAddress)
					&& !ValidationUtils.getInstance().isEmptyString(password);
		}

		if (proceed) {
			User user = userService.retrieveByEmailAddressAndPassword(emailAddress, password);
			if ((user != null) || (userSessionService.get() != null)) {
				// check user verified or not
				//TODO I think we do not require this logic for this assignment
				/*if (user.getVerified() == false) {
					userOperationContextService.set(UserOperationContextResultType.Failure,
							super.getContextMessage("message.signin.unverified"));
					return null;
				}*/
				//To Check if the user is admin else
				//won't allow to login from UI.
				if(user.getIsAdmin().equals(Boolean.FALSE)){
					userOperationContextService.set(UserOperationContextResultType.Failure,
							super.getContextMessage("message.signin.user.failure"));
					return null;
				}

				// set into usersession. interceptor's postHandle will settle
				// the rest
				if (userSessionService.get() == null) {
					userSessionService.authenticate(request, user, true, true);
				}

				if (!ValidationUtils.getInstance().isEmptyString(forwardUrl)) {
					return "redirect:" + WebUtils.getInstance().getFullUrl(request, forwardUrl);
				} else {
					return "redirect:/r/admin/";
				}
			}
		}
		userOperationContextService.set(UserOperationContextResultType.Failure,
				super.getContextMessage("message.signin.failure"));
		return null;
	}

	@RequestMapping(value = { "/admin/signIn" }, method = RequestMethod.GET)
	public Object signIn(@RequestParam(value = "u") String userId, @RequestParam(value = "ts") String timestamp,
			@RequestParam(value = "s") String signature, @RequestParam(value = "ne", required = false) Boolean noExpire,
			@RequestParam(value = "fu", required = false) String forwardUrl, HttpServletRequest request) {

		try {
			securityService.verifySecureLink(userId, timestamp, noExpire, signature, request);
			if (!ValidationUtils.getInstance().isEmptyString(forwardUrl)) {
				return "redirect:" + WebUtils.getInstance().getFullUrl(request, forwardUrl);
			} else {
				return "redirect:";
			}
		} catch (SystemException e) {
			userOperationContextService.set(UserOperationContextResultType.Failure, getContextMessage(e.getMessage()));
			String forwardUrlToAppendForLogin = ((!ValidationUtils.getInstance().isEmptyString(forwardUrl))
					? "?forwardUrl=" + WebUtils.getInstance().getEncodedValue(forwardUrl) : "");
			return "redirect:admin/login" + forwardUrlToAppendForLogin;
		}
	}

	@RequestMapping(value = "/admin/resetPassword", method = RequestMethod.GET)
	public Object resetPasswordGet(@ModelAttribute ResetPasswordForm model, Errors errors, HttpServletRequest request,
			HttpServletResponse response) {
		return modelAndView("/admin/reset_password");
	}

	@RequestMapping(value = "/admin/resetPassword", method = RequestMethod.POST)
	public Object resetPasswordPost(ResetPasswordForm model, Errors errors, HttpServletRequest request,
			HttpServletResponse response) {
		/* TODO- Not being used for current project

		String emailAddress = model.getEmailAddress();
		User user = userService.retrieveByEmailAddress(emailAddress);
		if (user == null) {
			userOperationContextService.set(UserOperationContextResultType.Failure,
					super.getContextMessage("message.resetPassword.noEmail"));
		} else {
			String url = WebUtils.getInstance().getFullUrl(request,
					propertiesHelper.appContextName + "/resetPasswordProcess?&t=" + user.getToken());
			userService.resetPassword(user.getId());
			userOperationContextService.set(UserOperationContextResultType.Success,
					super.getContextMessage("message.resetPassword.emailSent"));

			HashMap<String, Object> templateModelValues = new HashMap<>();
			templateModelValues.put("url", url);
			templateModelValues.put("name", user.getFirstname());
			String content = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
					propertiesHelper.mailResetpasswordTemplate, "UTF-8", templateModelValues);
			// send mail
			mailSenderHelper.sendMail(propertiesHelper.forgotPasswordmailSubject,
					new String[] { user.getEmailAddress() }, content, true);
		}
		*/
		User user = userService.retrieveByEmailAddress(model.getEmailAddress());
		if (user != null) {
	        userService.resetPassword(user);
	    }else{
	    	userOperationContextService.set(UserOperationContextResultType.Failure,
					super.getContextMessage("message.resetPassword.noEmail"));
	    	return "redirect:resetPassword";
	    }
		userOperationContextService.set(UserOperationContextResultType.Success,
				super.getContextMessage("message.newpassword.emailSent"));
		return "redirect:resetPassword";
		// return "redirect:";
	}

	@RequestMapping(value = "/admin/resetPasswordProcess", method = RequestMethod.GET)
	public Object resetPasswordProcessGet(@ModelAttribute ChangePasswordForm model,
			@RequestParam(value = "t", required = true) String token, Errors errors, HttpServletRequest request,
			HttpServletResponse response) {

		User user = userService.retrieveByToken(token);

		if (user == null) {
			userOperationContextService.set(UserOperationContextResultType.Failure,
					super.getContextMessage("message.resetPassword.tokenNotFound"));
			return "redirect:resetPassword";
		}

		LocalDateTime todayDate = LocalDateTime.now();
		LocalDateTime afterOneMonthDateTime = user.getUpdatedOn().plusDays(1);
		if (afterOneMonthDateTime.isAfter(todayDate)) {
			ModelAndView view = modelAndView("change_password");
			view.addObject("token", token);
			return view;

		} else {
			userOperationContextService.set(UserOperationContextResultType.Failure,
					super.getContextMessage("message.resetPassword.linkExpired"));
			return "redirect:resetPassword";
		}
	}

	@RequestMapping(value = "/admin/resetPasswordProcess", method = RequestMethod.POST)
	public Object resetPasswordProcessPost(@ModelAttribute ChangePasswordForm model,
			@RequestParam(value = "t", required = true) String token, Errors errors, HttpServletRequest request,
			HttpServletResponse response) {
		User user = userService.retrieveByToken(token);

		if (user == null) {
			userOperationContextService.set(UserOperationContextResultType.Failure,
					super.getContextMessage("message.resetPassword.tokenNotFound"));
			return "redirect:resetPassword";
		}

		String password = model.getPassword();
		String rePassword = model.getRePassword();

		if (!password.equals(rePassword)) {
			userOperationContextService.set(UserOperationContextResultType.Failure,
					super.getContextMessage("error.password.notMatch"));
			return resetPasswordProcessGet(model, token, errors, request, response);
		} else {
			user.setPassword(password);
			user.setNeedToRehashPassword(true);
			user.setCreatingNewObject(false);

			try {
				userService.save(user);
			} catch (ValidateException e) {
				userOperationContextService.set(UserOperationContextResultType.Failure,
						super.getContextMessage("message.resetPassword.minLength"));
				return resetPasswordProcessGet(model, token, errors, request, response);
			}

			userOperationContextService.set(UserOperationContextResultType.Success,
					super.getContextMessage("message.resetPassword.success"));
			return modelAndView("/admin/login");
		}
	}

	public class LoginForm {

		private String emailAddress;

		private String password;

		private String forwardUrl;

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getForwardUrl() {
			return forwardUrl;
		}

		public void setForwardUrl(String forwardUrl) {
			this.forwardUrl = forwardUrl;
		}

		public String getEmailAddress() {
			return emailAddress;
		}

		public void setEmailAddress(String emailAddress) {
			this.emailAddress = emailAddress;
		}
	}

	public static class ResetPasswordForm {
		public ResetPasswordForm() {
		}

		private String emailAddress;

		public String getEmailAddress() {
			return emailAddress;
		}

		public void setEmailAddress(String emailAddress) {
			this.emailAddress = emailAddress;
		}
	}

	public static class ChangePasswordForm {
		private String password;
		private String rePassword;
		private String forwardUrl;

		public ChangePasswordForm() {
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getRePassword() {
			return rePassword;
		}

		public void setRePassword(String rePassword) {
			this.rePassword = rePassword;
		}

		public String getForwardUrl() {
			return forwardUrl;
		}

		public void setForwardUrl(String forwardUrl) {
			this.forwardUrl = forwardUrl;
		}
	}
}
