package sg.activewealth.roboadvisor.infra.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sg.activewealth.roboadvisor.infra.service.UserOperationContextService;

@Component
public abstract class AbstractHelper {

	@Autowired
	protected PropertiesHelper propertiesHelper;

	@Autowired
	protected UserOperationContextService userOperationContextService;

}
