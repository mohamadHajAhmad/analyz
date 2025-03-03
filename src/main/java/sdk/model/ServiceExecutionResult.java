package sdk.model;

import java.io.Serializable;
import java.util.Map;

public class ServiceExecutionResult implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The execution successful. */
    private boolean executionSuccessful = true;

    /** The success code. */
    private String successCode;

    /** The error code. */
    private String errorCode;

    /** The return value. */
    private Object returnValue;

    /** The message. */
    private String message;





    /**
     * Instantiates a new service execution result.
     */
    public ServiceExecutionResult() {

    }

    /**
     * Checks if is execution successful.
     *
     * @return true, if is execution successful
     */
    public boolean isExecutionSuccessful() {
        return executionSuccessful;
    }

    /**
     * Sets the execution successful.
     *
     * @param executionSuccessfult the new execution successful
     */
    public void setExecutionSuccessful(boolean executionSuccessfult) {
        this.executionSuccessful = executionSuccessfult;
    }

    /**
     * Gets the success code.
     *
     * @return the success code
     */
    public String getSuccessCode() {
        return successCode;
    }

    /**
     * Sets the success code.
     *
     * @param successCode the new success code
     */
    public void setSuccessCode(String successCode) {
        this.successCode = successCode;
    }

    /**
     * Gets the error code.
     *
     * @return the error code
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Sets the error code.
     *
     * @param errorCode the new error code
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * Gets the return value.
     *
     * @return the return value
     */
    public Object getReturnValue() {
        return returnValue;
    }

    /**
     * Sets the return value.
     *
     * @param returnValue the new return value
     */
    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }


    /**
     * Gets the message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }


    /**
     * Sets the message.
     *
     * @param message the new message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Clean result.
     */
    public void cleanResult() {
        setReturnValue(null);
    }





}
