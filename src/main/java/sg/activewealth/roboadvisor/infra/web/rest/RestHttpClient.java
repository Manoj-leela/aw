package sg.activewealth.roboadvisor.infra.web.rest;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * This class declares bean for creating custom rest client.
 *
 */
@Component
@Configuration
public class RestHttpClient {

	/** The max total connections. */
	@Value("${max.total.connections}")
	private int maxTotalConnections;

	/** The max connection per route. */
	@Value("${max.connection.per.route}")
	private int maxConnectionPerRoute;

	/** The connection time out. */
	@Value("${connection.time.out}")
	private int connectionTimeOut;

	/** The socket time out. */
	@Value("${socket.time.out}")
	private int socketTimeOut;

	/** The logger. */
	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(RestHttpClient.class);

	/**
	 * Instantiates a new rest http client.
	 */
	public RestHttpClient() {
	}

	/**
	 * Gets the rest template.
	 *
	 * @return the rest template
	 */
	@Bean
	public RestTemplate getRestTemplate() {

		// PoolingHttpClientConnectionManager to create pool of connections
		// configured through properties file.
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
		connManager.setMaxTotal(maxTotalConnections);
		connManager.setDefaultMaxPerRoute(maxConnectionPerRoute);

		// Create HttpClient with connectionTimeout and socketTimeOut
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		httpClientBuilder.setConnectionManager(connManager);
		RequestConfig config = RequestConfig.custom().setConnectTimeout(connectionTimeOut)
				.setSocketTimeout(socketTimeOut).build();
		httpClientBuilder.setDefaultRequestConfig(config);

		HttpClient httpClient = httpClientBuilder.build();
		// Create ClientRequestFactory that needs to assigned to rest template.
		ClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);

		RestTemplate restTemplate = new RestTemplate(factory);
		return restTemplate;
	}

}
