package sg.activewealth.roboadvisor.infra.web.jackson;

import java.lang.reflect.Type;

import org.springframework.http.MediaType;

public class MappingJackson2HttpMessageConverter extends org.springframework.http.converter.json.MappingJackson2HttpMessageConverter {
	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		if (clazz == String.class) return false;
		return super.canRead(clazz, mediaType);		
	}
	
	@Override
	public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
		if (type == String.class) return false;
		return super.canRead(type, contextClass, mediaType);
	}
	
}
