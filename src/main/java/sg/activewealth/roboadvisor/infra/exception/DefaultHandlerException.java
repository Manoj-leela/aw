package sg.activewealth.roboadvisor.infra.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Component
@ControllerAdvice
public class DefaultHandlerException extends ResponseEntityExceptionHandler { 

	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
	    HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status,
	    WebRequest request) {
	  return super.handleHttpRequestMethodNotSupported(ex, headers, status, request);
	}
	
	
}
