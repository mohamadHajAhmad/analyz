package sdk.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import sdk.model.ServiceExecutionResult;
import sdk.util.JSONHelper;
import sdk.util.SessionVariablesController;
import sdk.util.SystemConstants;

import java.io.IOException;

public class RequestInterceptor implements HandlerInterceptor {

    /** The Constant LOGGER. */
    private static final Logger logger = LogManager.getLogger(RequestInterceptor.class);


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {

            addToSessionVriablesAndRequestAttributes(request, SystemConstants.IS_HTTPS, false);
            addToSessionVriablesAndRequestAttributes(request, SystemConstants.CURRENT_ACTIVE_USER_LOCAL, "en");

            System.out.println("Pre Handle method is Calling");
        } catch (Exception e) {
            logger.error("********BluLogix******** Error Found While Auth APP 380");
            return getErrorMessage(response);
        }

        return true;
    }



    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object arg2, Exception arg3) throws Exception {
        System.out.println("Pre Handle method is Calling");
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object arg2, ModelAndView arg3) throws Exception {
        System.out.println("Pre Handle method is Calling");
    }

    private boolean getErrorMessage(HttpServletResponse response, String ...messages) throws IOException {
        ServiceExecutionResult result = new ServiceExecutionResult();

        String message = "Error Found While Auth APP";
        if (messages.length > 0 && !messages[0].trim().isEmpty())
            message = messages[0];
        result.setMessage(message);
        result.setExecutionSuccessful(false);
        result.setErrorCode("1000");
        JSONHelper.toJsonFromObject(result);
        String json = JSONHelper.toJsonFromObject(result);// new
        // Gson().toJson(result);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader("Content-Security-Policy", "script-src 'self'");
        response.setHeader("Strict-Transport-Security", "max-age=31536000 ; includeSubDomains");
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setHeader("X-Frame-Options", "SAMEORIGIN");
        response.setHeader("Referrer-Policy", "strict-origin");
        response.setHeader("Permissions-Policy", "fullscreen 'self'");
        response.getWriter().write(json);
        return false;
    }

    private void addToSessionVriablesAndRequestAttributes(HttpServletRequest request, String systemConstant, Object value){
        SessionVariablesController.addSessionVariable(systemConstant, value);
        request.setAttribute(systemConstant, value);
    }

}
