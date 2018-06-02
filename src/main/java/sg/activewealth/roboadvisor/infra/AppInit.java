package sg.activewealth.roboadvisor.infra;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import sg.activewealth.roboadvisor.infra.helper.PropertiesHelper;
import sg.activewealth.roboadvisor.infra.helper.broker.TwsApiFactory;
import sg.activewealth.roboadvisor.infra.helper.broker.TwsApiHelper;

@Component
public class AppInit {

	private Logger logger = Logger.getLogger(AppInit.class);

	@Autowired
	private ApplicationContext ctx;

	@Autowired
	private PropertiesHelper propertiesHelper;

	@PostConstruct
	public void init() {
		logger.debug("FireExternalCallToBroker is:" + propertiesHelper.fireExternalCallToBroker);
		logger.debug("Broker Type is:" + propertiesHelper.brokerType);
		if (propertiesHelper.fireExternalCallToBroker
				&& PropertiesHelper.BROKER_TYPE_IB.equalsIgnoreCase(propertiesHelper.brokerType)) {
			TwsApiHelper.setApplicationContext(ctx);
//			TwsApiHelper demoInstaceTwsApiHelper = TwsApiFactory.getInstance(propertiesHelper.ibDemoInstanceHostName,
//					propertiesHelper.ibDemoInstancePort, 1);
//			demoInstaceTwsApiHelper.connect();
			try {
				TwsApiHelper sgaweInstanceApiHelper = TwsApiFactory.getSGAWEInstance(propertiesHelper.ibSgaweInstanceHostName,
						propertiesHelper.ibSgaweInstancePort, 22);
				sgaweInstanceApiHelper.connect();
			}
			catch (Exception e) {
				logger.warn("Unable to connect to TWS");
			}
		}
	}

	@PreDestroy
	public void close() {
		logger.debug("FireExternalCallToBroker is:" + propertiesHelper.fireExternalCallToBroker);
		logger.debug("Broker Type is:" + propertiesHelper.brokerType);
		if (propertiesHelper.fireExternalCallToBroker
				&& PropertiesHelper.BROKER_TYPE_IB.equalsIgnoreCase(propertiesHelper.brokerType)) {
//			TwsApiHelper demoInstancetwsApiHelper = TwsApiFactory.getInstance(propertiesHelper.ibDemoInstanceHostName,
//					propertiesHelper.ibDemoInstancePort, 1);
//			demoInstancetwsApiHelper.disconnect();

			try {
				TwsApiHelper sgaweInstanceApiHelper = TwsApiFactory.getSGAWEInstance(propertiesHelper.ibSgaweInstanceHostName,
						propertiesHelper.ibSgaweInstancePort, 22);
				sgaweInstanceApiHelper.disconnect();
			}
			catch (Exception e) {
				logger.warn("Unable to connect to TWS");
			}
		}
	}

}
