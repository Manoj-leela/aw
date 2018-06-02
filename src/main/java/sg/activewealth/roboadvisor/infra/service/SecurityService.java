package sg.activewealth.roboadvisor.infra.service;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.activewealth.roboadvisor.common.model.User;
import sg.activewealth.roboadvisor.common.service.UserService;
import sg.activewealth.roboadvisor.infra.exception.SystemException;
import sg.activewealth.roboadvisor.infra.helper.PropertiesHelper;
import sg.activewealth.roboadvisor.infra.utils.HashUtils;

@Service
public class SecurityService {
	
	@Autowired
	protected PropertiesHelper propertiesHelper;
	
	@Autowired
	protected UserSessionService userSessionService;
	
	@Autowired
	private UserService userService;

	public String generateSecureLink(User user, String forwardUrl, boolean noExpire) {
		String queryString = "u=" + user.getId() + "&ts=" + new Date().getTime() + ((noExpire)?"&ne=true":"");
		String signature = HashUtils.getInstance().getHash(queryString, propertiesHelper.securityStandardHashSalt);
		
		return propertiesHelper.appWebUrl + "/signIn?" + queryString + "&s=" + signature + ((forwardUrl != null)?"&fu=" + forwardUrl:"");
	}

	public void verifySecureLink(String userId, String timestamp, Boolean noExpire, String signature, HttpServletRequest request) throws SystemException {
		if (userId == null || timestamp == null || signature == null) throw new SystemException("message.security.invalidlink");
		
		long difference = new Date().getTime() - Long.valueOf(timestamp);
		long EXPIRY_TIME = 1000 * 60 * 20; //20 minutes
		if ((noExpire != null && noExpire) || (difference > 0 && difference < (EXPIRY_TIME))) {
			String queryString = "u=" + userId + "&ts=" + timestamp;
			if (noExpire != null) queryString += "&ne=" + noExpire;
			
			//this comes from SecurityService.generateSecureLink()
			String rightSignature = HashUtils.getInstance().getHash(queryString, propertiesHelper.securityStandardHashSalt);	
			if (signature.equals(rightSignature)) {
				//insert user into session
				User user = userService.retrieve(userId);
				if (user != null){
					userSessionService.authenticate(request, user, true, false);
					// set user verified flag
					/*
					 TODO-Not being used for current project
					user.setVerified(Boolean.TRUE);
					*/
					userService.saveWithoutValidation(user);
				}
				
				else
					throw new SystemException("message.security.invalidlink");
			}
			else
				throw new SystemException("message.security.invalidlink");
		}
		else
			throw new SystemException("message.emailsignin.expired");
	}
}
