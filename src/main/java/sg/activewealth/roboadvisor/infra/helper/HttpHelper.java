package sg.activewealth.roboadvisor.infra.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import sg.activewealth.roboadvisor.infra.exception.SystemException;
import sg.activewealth.roboadvisor.infra.utils.SystemUtils;

@Component
public class HttpHelper extends AbstractHelper {

	protected Logger logger = Logger.getLogger(HttpHelper.class);

	@SuppressWarnings("deprecation")
	protected ThreadSafeClientConnManager cm;

	@SuppressWarnings("deprecation")
	@PostConstruct
	public void init() {
		if (cm != null) return;

		cm = new ThreadSafeClientConnManager(); 
		// Increase max total connection to 200
		cm.setMaxTotal(200);
		//		// Increase default max connection per route to 20
		//		cm.setDefaultMaxPerRoute(20);
		// Increase max connections for localhost:80 to 50
		//		HttpHost localhost = new HttpHost(propertiesHelper.nodeDomain, Integer.valueOf(propertiesHelper.nodePort));
		//		cm.setMaxPerRoute(new HttpRoute(localhost), 200);
	}

	public byte[] getContent(String url) {
		return _getContent(url);
	}

	private byte[] _getContent(final String url) {
		final long startTiming = System.currentTimeMillis();
		logger.info("accessing, url: " + url);

		HttpClient httpClient = new DefaultHttpClient(cm);
		try {
			HttpGet httpGet = new HttpGet(url);
			// Create a custom response handler
			ResponseHandler<byte[]> responseHandler = new ResponseHandler<byte[]>() {
				public byte[] handleResponse(final HttpResponse response) throws IOException {
					int status = response.getStatusLine().getStatusCode();
					HttpEntity entity = response.getEntity();
					logger.info("done accessing, process: " + (System.currentTimeMillis() - startTiming) + "ms, url: " + url);
					return entity != null ? EntityUtils.toByteArray(entity) : null;
				}
			};
			return httpClient.execute(httpGet, responseHandler);
		}
		catch (Exception e) {
			throw new SystemException(url, e);
		}
		//		finally {
		//			try {
		//				httpClient.close();
		//			} catch (IOException e) {
		//				throw new SystemException(e);
		//			}
		//		}
	}

	public byte[] postContent(final String url, Object... data) {
		Map<String, String> keyValuePairs = SystemUtils.getInstance().buildMap(new HashMap<String, String>(), data);
		final long startTiming = System.currentTimeMillis();
		logger.info("posting, url: " + url);

		HttpClient httpClient = new DefaultHttpClient(cm);
		try {
			HttpPost httpPost = new HttpPost(url);
			List <NameValuePair> nvps = new ArrayList <NameValuePair>();
			for (String key : keyValuePairs.keySet()) nvps.add(new BasicNameValuePair(key, keyValuePairs.get(key)));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			
			ResponseHandler<byte[]> responseHandler = new ResponseHandler<byte[]>() {
				public byte[] handleResponse(final HttpResponse response) throws IOException {
					int status = response.getStatusLine().getStatusCode();
					HttpEntity entity = response.getEntity();
					logger.info("done posting, process: " + (System.currentTimeMillis() - startTiming) + "ms, url: " + url);
					return entity != null ? EntityUtils.toByteArray(entity) : null;
				}
			};
			return httpClient.execute(httpPost, responseHandler);
		}
		catch (Exception e) {
			throw new SystemException(url + ", " + keyValuePairs.toString(), e);
		}
		//	finally {
		//		try {
		//			httpClient.close();
		//		} catch (IOException e) {
		//			throw new SystemException(e);
		//		}
		//	}

	}

	private HttpRequestRetryHandler buildRetryHandler() {
		return new HttpRequestRetryHandler() {
			public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
				// Do not retry if over max retry count
				if (executionCount >= 5) return false;
				return true;
			}
		};
	}

	//	public void getContentConcurrent(String[] urls) {
	//		int TIMEOUT = 5000;
	//		RequestConfig requestConfig = RequestConfig.custom()
	//				.setSocketTimeout(TIMEOUT)
	//				.setConnectTimeout(TIMEOUT).build();
	//		CloseableHttpAsyncClient httpClient = HttpAsyncClients.custom()
	//				.setDefaultRequestConfig(requestConfig)
	//				.build();
	//
	//		httpClient.start();
	//		try {
	//			HttpGet[] requests = new HttpGet[urls.length];
	//			for (int i = 0; i < urls.length; i++) requests[i] = new HttpGet(urls[i]);
	//			final CountDownLatch latch = new CountDownLatch(requests.length);
	//			for (final HttpGet request: requests) {
	//				httpClient.execute(request, new FutureCallback<HttpResponse>() {
	//					public void completed(HttpResponse response) {
	//						latch.countDown();
	////						System.out.println(request.getRequestLine() + "->" + response.getStatusLine());
	//					}
	//					public void failed(Exception ex) {
	//						latch.countDown();
	////						System.out.println(request.getRequestLine() + "->" + ex);
	//					}
	//					public void cancelled() {
	//						latch.countDown();
	////						System.out.println(request.getRequestLine() + " cancelled");
	//					}
	//				});
	//			}
	//			latch.await();
	//		}
	//		catch (Exception e) {
	//			throw new SystemException(e);
	//		}
	//		finally {
	//			try {
	//				httpClient.close();
	//			} catch (IOException e) {
	//				throw new SystemException(e);
	//			}
	//		}
	//	}

	public void close() {
		cm.shutdown();
	}

}
