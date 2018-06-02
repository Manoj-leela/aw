package sg.activewealth.roboadvisor.infra.controller.interceptor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import sg.activewealth.roboadvisor.infra.controller.AbstractController;
import sg.activewealth.roboadvisor.infra.controller.CRUDController;
import sg.activewealth.roboadvisor.infra.utils.WebUtils;

@Component
public class CommonInterceptor extends AbstractInterceptor {

    @Override
    @SuppressWarnings("rawtypes")
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
        String requestPage = WebUtils.getInstance().getRequestPage(request);

        if (handler instanceof HandlerMethod) {
            Object handlerBean = ((HandlerMethod) handler).getBean();
            if (handlerBean instanceof AbstractController) {
                request.setAttribute("controller", handlerBean);
            }
            if (handlerBean instanceof CRUDController) {
                CRUDController crudController = (CRUDController) handlerBean;
                if (crudController.getModelName() != null) {
                    request.setAttribute("modelName", crudController.getModelName().toLowerCase());
                }
            }
        }

        Map<String, Object> commonProperties = new HashMap<String, Object>();
        commonProperties.put("DATE_FORMAT_FOR_FORM", AbstractController.DATE_FORMAT_FOR_FORM);
        commonProperties.put("DATE_FORMAT_FOR_PRINT", AbstractController.DATE_FORMAT_FOR_PRINT);
        commonProperties.put("DATE_TIME_FORMAT_FOR_PRINT",
                AbstractController.DATE_TIME_FORMAT_FOR_PRINT);
        commonProperties.put("DOUBLE_FORMAT_FOR_PRINT", AbstractController.DOUBLE_FORMAT_FOR_PRINT);
        commonProperties.put("INTEGER_FORMAT_FOR_PRINT",
                AbstractController.INTEGER_FORMAT_FOR_PRINT);
        commonProperties.put("INTEGER_FORMAT_WITH_SIGNS_FOR_PRINT",
                AbstractController.INTEGER_FORMAT_WITH_SIGNS_FOR_PRINT);
        commonProperties.put("DATE_FORMAT_FOR_GRAPH", AbstractController.DATE_FORMAT_FOR_GRAPH);
        request.setAttribute("commonProperties", commonProperties);
        // end common properties

        request.setAttribute("userSession", userSessionService.get());
        request.setAttribute("propertiesHelper", propertiesHelper);
        request.setAttribute("isMobile", WebUtils.getInstance().isMobile(request));

        request.setAttribute("requestPage", requestPage);
        request.setAttribute("requestPageWithoutQueryString",
                WebUtils.getInstance().getRequestPage(request, false));

        request.setAttribute("appContextName", propertiesHelper.appContextName);
        request.setAttribute("appContextNameRoot", propertiesHelper.appContextNameRoot);
        request.setAttribute("resourcesBase", propertiesHelper.appContextNameRoot + "/resources");
        request.setAttribute("assetsBase", propertiesHelper.appContextNameRoot + "/assets");
        request.setAttribute("isProduction", propertiesHelper.appIsProduction);
        return super.preHandle(request, response, handler);
    }

}
