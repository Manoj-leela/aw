package sg.activewealth.roboadvisor.infra.controller.interceptor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import sg.activewealth.roboadvisor.infra.helper.PropertiesHelper;
import sg.activewealth.roboadvisor.infra.service.UserOperationContextService;
import sg.activewealth.roboadvisor.infra.service.UserSessionService;

@Component
public class AbstractInterceptor extends HandlerInterceptorAdapter {

    protected Logger logger = Logger.getLogger(AbstractInterceptor.class);

    @Autowired
    protected ApplicationContext context;

    @Autowired
    protected UserOperationContextService userOperationContextService;

    @Autowired
    protected UserSessionService userSessionService;

    @Autowired
    protected PropertiesHelper propertiesHelper;

    public String getContextMessage(String code) {
        return context.getMessage(code, null, LocaleContextHolder.getLocale());
    }

    public String getContextMessage(String code, Object[] args) {
        return context.getMessage(code, args, LocaleContextHolder.getLocale());
    }

}
