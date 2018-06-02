package sg.activewealth.roboadvisor.infra.helper;

import java.util.Map;

public interface SocialLoginHelper {
	Map<String,Object> login(String token);
}
