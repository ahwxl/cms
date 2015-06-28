package com.bplow.look.bass;

public class ServiceAccessException extends RuntimeException{

	public static final long serialVersionUID = 20080128L;
    private String errorCode;
    private Object errorCodeMessageParams[];
    private String currentMessage;
    
    public ServiceAccessException(String message)
    {
        super(message);
        currentMessage = message;
    }

    public ServiceAccessException(String message, Throwable cause)
    {
        super(message, cause);
        currentMessage = message;
    }

    public ServiceAccessException(String message, Throwable cause, String errorCode, Object errorCodeMessageParams[])
    {
        super(message, cause);
        this.errorCode = errorCode;
        this.errorCodeMessageParams = errorCodeMessageParams;
        currentMessage = message;
    }
    
}
