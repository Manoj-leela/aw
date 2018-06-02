package sg.activewealth.roboadvisor.infra.utils;

import org.springframework.social.connect.Connection;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.web.servlet.view.RedirectView;

public class SocialUtils {

	private static OAuth2Operations oAuth2Operations;
	private static FacebookConnectionFactory connectionFactory;

	public static RedirectView forwardToFacebook(String apiKey, String secretKey, String redirectUrl, String scope) {

		connectionFactory = new FacebookConnectionFactory(apiKey, secretKey);
		OAuth2Parameters params = new OAuth2Parameters();
		params.setRedirectUri(redirectUrl);
		params.setScope(scope);
		params.setState("recivedfromfacebooktoken");
		oAuth2Operations = connectionFactory.getOAuthOperations();

		String authorizeUrl = oAuth2Operations.buildAuthenticateUrl(GrantType.AUTHORIZATION_CODE, params);
		RedirectView redirectView = new RedirectView(authorizeUrl, true, true, true);

		return redirectView;
	}

	public static User getFBUser(String code, String redirectUrl) {

		// upon receiving the callback from the provider:
		AccessGrant accessGrant = oAuth2Operations.exchangeForAccess(code, redirectUrl, null);
		Connection<Facebook> connection = connectionFactory.createConnection(accessGrant);

		User fbUser = connection.getApi().userOperations().getUserProfile();
		return fbUser;
	}

}
