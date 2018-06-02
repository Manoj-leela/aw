package sg.activewealth.roboadvisor.infra.helper.broker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import com.fasterxml.jackson.databind.ObjectMapper;

import sg.activewealth.roboadvisor.common.jobs.runner.PlaceOrderJobRunner;
import sg.activewealth.roboadvisor.common.model.User;
import sg.activewealth.roboadvisor.common.service.UserService;
import sg.activewealth.roboadvisor.infra.helper.PropertiesHelper;
import sg.activewealth.roboadvisor.infra.web.rest.AbstractRestClient;

@Component
public class InstrumentHelper extends AbstractRestClient{
	
	@Autowired
	private PropertiesHelper propertiesHelper;
	
	@Autowired
	private UserService userService;
	
	private Logger logger = Logger.getLogger(PlaceOrderJobRunner.class);
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String,Object> getInstrument(String instrumentCode) {
		ObjectMapper objectMapper = new ObjectMapper();
		String uri = propertiesHelper.saxoApiFindInstrumentUrl + "?KeyWords="+ instrumentCode;

		List<Map<String, Object>> resultMapList = null;
		User user = userService.retrieveByEmailAddress(propertiesHelper.systemUserEmailId);
		if (user.getBrokerSaxoApiSecret() != null) {
			Map<String, String> httpHeaders = new HashMap<String, String>();
			httpHeaders.put("Authorization", "Bearer " + user.getBrokerSaxoApiSecret());
			setHttpHeaders(httpHeaders);
			
			try{
				ResponseEntity<Map> response = exchange(uri, HttpMethod.GET, null, Map.class);
				resultMapList = objectMapper.convertValue(response.getBody().get("Data"),
						List.class);
			} catch (HttpClientErrorException e) {
				logger.error("An unexpected error Status code: "+e.getStatusCode());
				logger.error("An unexpected error Message: "+e.getResponseBodyAsString());
				logger.error("An unexpected error Header: "+e.getResponseHeaders());
				return null;
			}
		}
		if(resultMapList!=null && resultMapList.size()>0) {
			return resultMapList.get(0);
		} else {
			return null;
		}
	}
}
