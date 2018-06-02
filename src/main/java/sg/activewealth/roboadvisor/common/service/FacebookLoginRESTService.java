package sg.activewealth.roboadvisor.common.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import sg.activewealth.roboadvisor.common.jobs.runner.PlaceOrderJobRunner;
import sg.activewealth.roboadvisor.infra.helper.PropertiesHelper;
import sg.activewealth.roboadvisor.infra.helper.SocialLoginHelper;
import sg.activewealth.roboadvisor.infra.web.rest.AbstractRestClient;

@Component
public class FacebookLoginRESTService extends AbstractRestClient implements SocialLoginHelper {
	
	private Logger logger = Logger.getLogger(PlaceOrderJobRunner.class);
	
	@Autowired
	private PropertiesHelper propertiesHelper;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map<String, Object> login(String accessToken) {
		String uri = propertiesHelper.facebookGraphUrl+"?access_token="+accessToken;
		ResponseEntity<Map> graphResponse = null;
		Map<String, Object> ret = new HashMap<>(2);
		try{
			graphResponse = exchange(uri, HttpMethod.GET, null, Map.class);
		} catch (HttpClientErrorException e) {
			ret.put("message", e.getResponseBodyAsString());
			logger.error("An unexpected error Status code: "+e.getStatusCode());
			logger.error("An unexpected error Message: "+e.getResponseBodyAsString());
			logger.error("An unexpected error Header: "+e.getResponseHeaders());
			return ret;
		}
		return graphResponse.getBody();
	}


}
