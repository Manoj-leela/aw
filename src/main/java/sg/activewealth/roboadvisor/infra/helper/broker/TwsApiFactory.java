package sg.activewealth.roboadvisor.infra.helper.broker;

public class TwsApiFactory {

	private static TwsApiHelper demoInstance;
	
	private static TwsApiHelper sgawInstance;

	private TwsApiFactory() {
	}

	public static TwsApiHelper getInstance(String host, int port, int clientId) {
		if (demoInstance == null) {
			demoInstance = new TwsApiHelper(host, port, clientId);
		}
		return demoInstance;
	}
	
	public static TwsApiHelper getSGAWEInstance(String host, int port, int clientId) {
		if (sgawInstance == null) {
			sgawInstance = new TwsApiHelper(host, port, clientId);
		}
		return sgawInstance;
	}

}
