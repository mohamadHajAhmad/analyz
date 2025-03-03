package sdk.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import sdk.model.ServiceExecutionResult;
import sdk.util.SessionVariablesController;
import sdk.util.restBusinessDataValidation.ErrorMessagesLocal;
import sdk.util.restBusinessDataValidation.SuccessMessagesLocal;

import java.util.Arrays;

@Aspect
@Component
public class ServiceAspect {


    /** The Constant LOGGER. */
    private static final Logger logger = LogManager.getLogger(ServiceAspect.class);

    @Before(value = "execution(* sdk.service..*(..)))")
    public void beforeAdvice(JoinPoint joinPoint) {
        String[] keys = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        Object[] values = joinPoint.getArgs();
        System.out.println("Before method:" + Arrays.stream(keys).toArray());
        System.out.println("Before method:" + values.toString());
        System.out.println("Before method:" + joinPoint.getSignature());
        //System.out.println("Creating Employee with first name ");
    }


    @After(value = "execution(* sdk.service..*(..)))")
    public void after(JoinPoint joinPoint) {
    System.out.println("after execution of {}:" + joinPoint.getSignature());
    }

    @AfterReturning(value="execution(* sdk.service..*(..)))",returning="serviceExecutionResult")
    public <X> X afterReturningAdvice(JoinPoint joinPoint, ServiceExecutionResult serviceExecutionResult)
    {
        System.out.println("After Returing method:"+joinPoint.getSignature());

        return (X) getResponseResult(serviceExecutionResult);
    }

    private ResponseEntity<ServiceExecutionResult> getResponseResult(ServiceExecutionResult result) {
        if (result.isExecutionSuccessful()) {
            if (result.getSuccessCode() != null) {
                String local = SessionVariablesController.getCurrentUserLocal();
                result.setMessage(getSuccessMessageCode(result.getSuccessCode(), local));
            }
        } else {
            if (result.getErrorCode() != null) {
                if(!result.getErrorCode().equals("DB"))
                {     String local = SessionVariablesController.getCurrentUserLocal();
                        result.setMessage(getMessageCode(result.getErrorCode(), local));
                    }
            }

        }
        return new ResponseEntity<ServiceExecutionResult>(result, HttpStatus.OK);
    }

    /**
     * Gets the message code.
     *
     * @param errorCode the error code
     * @param local the local
     * @return the message code
     */
    private String getMessageCode(String errorCode, String local) {
        String message = null;
        if(local.equals(ErrorMessagesLocal.Locals.English)){
            message = ErrorMessagesLocal.English.get(errorCode);
        }else if(local.equals(ErrorMessagesLocal.Locals.Spanish)){
            message = ErrorMessagesLocal.Spanish.get(errorCode);
        }
        return message;
    }


    /**
     * Gets the success message code.
     *
     * @param successCode the success code
     * @param local the local
     * @return the success message code
     */
    private String getSuccessMessageCode(String successCode, String local) {
        String message = null;
        if(local.equals(SuccessMessagesLocal.Locals.English)){
            message = SuccessMessagesLocal.English.get(successCode);
        }else if(local.equals(SuccessMessagesLocal.Locals.Spanish)){
            message = SuccessMessagesLocal.Spanish.get(successCode);
        }
        return message;
    }
}
