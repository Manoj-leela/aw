package sg.activewealth.roboadvisor.infra.web.jackson;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SuppressWarnings("serial")
public class ThirdPartyAwareObjectMapper extends ObjectMapper {
	
	
	public ThirdPartyAwareObjectMapper() {
		configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		registerModule(new JavaTimeModule());
		configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	    configure(SerializationFeature.INDENT_OUTPUT, true);
	}

}