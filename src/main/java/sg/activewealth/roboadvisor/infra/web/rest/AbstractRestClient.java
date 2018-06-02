package sg.activewealth.roboadvisor.infra.web.rest;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import sg.activewealth.roboadvisor.infra.helper.PropertiesHelper;

/**
 * The Class AbstractRestClient.
 *
 */
public abstract class AbstractRestClient {

	private Logger logger = Logger.getLogger(AbstractRestClient.class);
	/** The rest http client. */
	@Autowired
	private RestHttpClient restHttpClient;

	/** The accept. */
	private String accept = MediaType.APPLICATION_JSON_VALUE;

	/** The content type. */
	private MediaType contentType = MediaType.APPLICATION_JSON;

	/** The error handler. */
	private ResponseErrorHandler errorHandler;

	/** The Constant ACCEPT. */
	private final static String ACCEPT = "Accept";

	@Autowired
	protected PropertiesHelper propertiesHelper;
	
	private Map<String,String> httpHeaders;
	
	/**
	 * Service method to invoke RESTful web services and returns results from
	 * the web service call.
	 *
	 * @param <T>
	 *            the generic type
	 * @param <R>
	 *            the generic type
	 * @param uri
	 *            target RESTful web service URI string to invoke.
	 * @param method
	 *            GET or POST
	 * @param entity
	 *            a serializable instance to be used as argument in data
	 *            exchange with RESTful web service.
	 * @param responseType
	 *            type of response.
	 * @return results returned from the web service invocation.
	 * @throws RestClientException
	 *             when the RESTful web service invocation fails.
	 */
	public <T, R> ResponseEntity<R> exchange(final String uri, HttpMethod method, T entity,
			Class<R> responseType) {

		HttpHeaders headers = new HttpHeaders();
		headers.set(ACCEPT, getAccept() == null ? this.accept : getAccept());
		Map<String,String> extraHeaders = getHttpHeaders();
		
		if(extraHeaders!=null && !extraHeaders.isEmpty()) {
			headers.setAll(getHttpHeaders());
		}
		
		headers.setContentType(getContentType() == null ? this.contentType : getContentType());

		HttpEntity<?> httpEntity = null;

		if (entity == null) {
			httpEntity = new HttpEntity<Object>(headers);
		} else {
			httpEntity = new HttpEntity<T>(entity, headers);
		}

		RestTemplate restTemplate = restHttpClient.getRestTemplate();

		if (getErrorHandler() != null) {
			restTemplate.setErrorHandler(getErrorHandler());
		}
		
		logger.trace("Http request uri: "+uri);
		logger.trace("Http request method: "+method);
		logger.trace("Http reqeuest entity: "+httpEntity);
		logger.trace("http request type: "+responseType);
		
		ResponseEntity<R> result = restTemplate.exchange(uri, method, httpEntity, responseType);
		
		logger.trace("Http response code: "+result.getStatusCode());
		logger.trace("Http response body: "+result.getBody());
		logger.trace("Http response header: "+result.getHeaders());
		
		return result;
	}

	/**
	 * Gets the accept.
	 *
	 * @return the accept
	 */
	protected String getAccept() {
		return accept;
	}

	/**
	 * Sets the accept.
	 *
	 * @param accept
	 *            the accept to set
	 */
	protected void setAccept(String accept) {
		this.accept = accept;
	}

	/**
	 * Gets the content type.
	 *
	 * @return the contentType
	 */
	protected MediaType getContentType() {
		return contentType;
	}

	/**
	 * Sets the content type.
	 *
	 * @param contentType
	 *            the contentType to set
	 */
	protected void setContentType(MediaType contentType) {
		this.contentType = contentType;
	}

	/**
	 * Gets the error handler.
	 *
	 * @return the errorHandler
	 */
	public ResponseErrorHandler getErrorHandler() {
		return errorHandler;
	}

	/**
	 * Sets the error handler.
	 *
	 * @param errorHandler
	 *            the errorHandler to set
	 */
	public void setErrorHandler(ResponseErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}

	/**
	 * @return the httpHeaders
	 */
	public Map<String, String> getHttpHeaders() {
		return httpHeaders;
	}

	/**
	 * @param httpHeaders the httpHeaders to set
	 */
	public void setHttpHeaders(Map<String, String> httpHeaders) {
		this.httpHeaders = httpHeaders;
	}

}
