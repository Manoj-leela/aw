package sg.activewealth.roboadvisor.infra.service;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.activewealth.roboadvisor.common.model.User;
import sg.activewealth.roboadvisor.common.service.UserService;
import sg.activewealth.roboadvisor.infra.dto.UserSessionDto;
import sg.activewealth.roboadvisor.infra.helper.MailSenderHelper;
import sg.activewealth.roboadvisor.infra.helper.PropertiesHelper;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;

@Service
public class UserSessionService {

	protected Logger logger = Logger.getLogger(UserSessionService.class);

	private static final long SESSION_TIMEOUT_IN_MILLIS = 1000 * 60 * 20; // 20mins

	private static final String COOKIE_TOKEN_VALUE_DELIM = "|";

	@Autowired
	public UserService userService;

	@Autowired
	private UserOperationContextService userOperationContextService;

	@Autowired
	protected PropertiesHelper propertiesHelper;

	@Autowired
	protected MailSenderHelper mailSenderHelper;

	private final ThreadLocal<UserSessionDto> userSession = new ThreadLocal<UserSessionDto>();

	public UserSessionDto set(UserSessionDto userSession) {
		this.userSession.set(userSession);
		return userSession;
	}

	public void clear() {
		this.userSession.set(null);
		this.userSession.remove();
	}

	public UserSessionDto get() {
		return this.userSession.get();
	}

	public UserSessionDto initUserSession(User user, boolean isAccessingNonAuth) {
		UserSessionDto userSession = new UserSessionDto(user.getId(), new Timestamp(new Date().getTime()),
				new Timestamp(new Date().getTime()), user.getHashSalt());
		userSession.setIsAccessingNonAuth(isAccessingNonAuth);
		this.set(userSession);
		return get();
	}

	public String buildFreshCookieValue() {
		StringBuilder sb = new StringBuilder();

		get().setLastAccessedOn(new Timestamp(new Date().getTime()));
		// update the last accessed time

		sb.append(get().getUserId());
		sb.append(COOKIE_TOKEN_VALUE_DELIM);
		sb.append(get().getLoggedInOn().getTime());
		sb.append(COOKIE_TOKEN_VALUE_DELIM);
		sb.append(get().getLastAccessedOn().getTime());
		sb.append(COOKIE_TOKEN_VALUE_DELIM);
		sb.append(get().buildValiditySignature());
		sb.append(COOKIE_TOKEN_VALUE_DELIM);

		// find out if there are any valuestopersistontocookie that was set by
		// controller? if yes, append onto string
		for (Iterator<String> itr = get().getValuesToPersistAcrossRequests().keySet().iterator(); itr.hasNext();) {
			String key = itr.next();
			sb.append(key);
			sb.append(COOKIE_TOKEN_VALUE_DELIM);
			sb.append(get().getValuesToPersistAcrossRequests().get(key));
			if (itr.hasNext())
				sb.append(COOKIE_TOKEN_VALUE_DELIM);
		}

		return sb.toString();
	}

	public UserSessionDto authenticate(HttpServletRequest request, User user, boolean updateDatabase,
			boolean isAccessingNonAuth) {
		UserSessionDto ret = this.initUserSession(user, isAccessingNonAuth);
		// refresh last logged in fields
		boolean reading = userOperationContextService.getReading();
		// set on local only just in case. value is not passed as param.
		/*
		 TODO-Not being used for current project
		user.setLastLoggedInRead(LocalDateTime.now());
		*/

		user.setLastLoggedInIpAddress(request.getRemoteAddr());
		String useragent = request.getHeader("user-agent");
		/*
		 * TODO-Not being used for current project
		user.setLastLoggedInUserAgent(useragent);
		
		if (updateDatabase)
			userService.updateLastLoggedIn(user.getId(), user.getLastLoggedInIpAddress(),
					user.getLastLoggedInUserAgent());
		*/
		return ret;
	}

	public UserSessionDto buildUserSession(HttpServletRequest request, String cookieValue, boolean updateDatabase,
			boolean isAccessingNonAuth) {
		String userId = null, signature = null;
		Long loggedInOn = null, lastAccessedOn = null;
		Map<String, Object> remainingValues = new LinkedHashMap<String, Object>();
		// extract values out
		try {
			StringTokenizer st = new StringTokenizer(cookieValue, COOKIE_TOKEN_VALUE_DELIM);
			userId = st.nextToken();
			loggedInOn = Long.valueOf(st.nextToken());
			lastAccessedOn = Long.valueOf(st.nextToken());
			signature = st.nextToken();

			while (st.hasMoreTokens()) {
				remainingValues.put(st.nextToken(), st.nextToken()); // remaining
																		// values
																		// are
																		// key/value
																		// pairs.
																		// property
																		// name=value
			}
		} catch (Exception e) {
			// logger.error because this can be offset by user relogging in.
			// noneed to warn.
			logger.error("exception during the parsing of cookie value: " + cookieValue, e);
			return null;
		}

		// if expireSession, check for difference
		if (!propertiesHelper.securityNeverExpireSession) {
			// check if timed out
			long difference = new Date().getTime() - lastAccessedOn;
			if (difference > SESSION_TIMEOUT_IN_MILLIS || difference < 0)
				return null;
		}

		// extract user model based on ID
		User user = userService.retrieve(userId);
		UserSessionDto userSession = null;
		if (user == null)
			return null;
		else {
			// check cookie authenticity
			if (!new UserSessionDto(user.getId(), new Timestamp(loggedInOn), new Timestamp(lastAccessedOn),
					user.getHashSalt()).buildValiditySignature().equals(signature))
				return null;

			userSession = this.authenticate(request, user, updateDatabase, isAccessingNonAuth);
			setValuesOntoUserSession(userSession, user, remainingValues);
		}

		return userSession;
	}

	private void setValuesOntoUserSession(UserSessionDto userSession, User user, Map<String, Object> remainingValues) {
		// no problems, go on and set the values
		// copy ALL matching properties onto DTO
		BeanUtils.copyProperties(user, userSession);
		userSession.setUser(user);
		userSession.setValuesToPersistAcrossRequests(remainingValues);
	}

}
