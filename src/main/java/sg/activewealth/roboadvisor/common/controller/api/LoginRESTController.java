package sg.activewealth.roboadvisor.common.controller.api;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import sg.activewealth.roboadvisor.common.dto.UserDto;
import sg.activewealth.roboadvisor.common.enums.AccountStatus;
import sg.activewealth.roboadvisor.common.model.User;
import sg.activewealth.roboadvisor.common.service.FacebookLoginRESTService;
import sg.activewealth.roboadvisor.common.service.UserService;
import sg.activewealth.roboadvisor.infra.controller.AbstractController;
import sg.activewealth.roboadvisor.infra.controller.interceptor.SecurityInterceptor;
import sg.activewealth.roboadvisor.infra.exception.JsonObjectNotFoundException;
import sg.activewealth.roboadvisor.infra.utils.CookieUtils;

@RestController
@RequestMapping("/api/v1")
public class LoginRESTController extends AbstractController {

	@Autowired
	private UserService userService;

	@Autowired
	FacebookLoginRESTService facebookLoginRestService;

	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> apiLogin(@RequestBody User user, HttpServletResponse response) {
		CookieUtils.getInstance().clearCookie(response, SecurityInterceptor.SECURE_COOKIE_PARAMETER_NAME);
		Map<String, Object> ret = new HashMap<>();
		User userDetails = userService.retrieveByPhoneOrEmail(user.getEmail(), user.getPassword());
		if (userDetails == null) {
			ret.put("success", false);
			ret.put("message", "Invalid User Credentials");
			return new ResponseEntity<>(ret, HttpStatus.OK);
		}else if(userDetails.getAccountStatus() != null && (userDetails.getAccountStatus().equals(AccountStatus.UnApproved) || userDetails.getAccountStatus().equals(AccountStatus.Locked))){
			ret.put("success", false);
			ret.put("message", "Profile is Locked.Please contact administrator.");
			return new ResponseEntity<>(ret, HttpStatus.OK);
		}
		// Generate token
		userSessionService.initUserSession(userDetails, false);
		ret.put("token", userSessionService.buildFreshCookieValue());
		return new ResponseEntity<>(ret, HttpStatus.OK);
	}

	@RequestMapping(value = "/logout", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> apiLogout() throws ServletException {
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = "/resetPassword", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> resetPassword(@RequestBody User user) {
		Map<String, Object> ret = new HashMap<>();
		if (user.getEmailAddress() == null) {
			throw new JsonObjectNotFoundException("Email Address is required.");
		}
		User loginUser = userService.retrieveByEmailAddress(user.getEmailAddress());
		if (loginUser != null) {
			userService.resetPassword(loginUser);
			ret.put("success", true);
			ret.put("newpassword", "Check your email for new Password");
		} else {
			ret.put("success", false);
			ret.put("message", "User not found with given Identifier");
			return new ResponseEntity<>(ret, HttpStatus.OK);
		}
		return new ResponseEntity<>(ret, HttpStatus.OK);
	}

	@RequestMapping(value = "/fblogin", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> apiFBLogin(@RequestBody UserDto userDto, HttpServletResponse response) {
		CookieUtils.getInstance().clearCookie(response, SecurityInterceptor.SECURE_COOKIE_PARAMETER_NAME);

		String accessToken = userDto.getToken();
		Map<String, Object> responseGraph = facebookLoginRestService.login(accessToken);
		Map<String, Object> ret = new HashMap<>();
		Object apiKey = responseGraph.get("id");
		User userDetails = null;
		if (apiKey != null) {
			userDetails = userService.getUserBySocialId(apiKey.toString(), userDto.getEmailAddress());
			if (userDetails != null && userDetails.getAccountStatus() != null) {
				if(userDetails.getAccountStatus().equals(AccountStatus.UnApproved) || userDetails.getAccountStatus().equals(AccountStatus.Locked)){
					ret.put("success", false);
					ret.put("message", "Profile is Locked.Please contact administrator.");
				}else {
					// Generate token
					userSessionService.initUserSession(userDetails, false);
					ret.put("token", userSessionService.buildFreshCookieValue());
				}
			}  else{
				ret.put("success", false);
				ret.put("message", "User not found");
			}
		} else {
			ret.put("success", false);
			ret.put("message", responseGraph.get("message"));
		}
		return new ResponseEntity<>(ret, HttpStatus.OK);
	}
}
