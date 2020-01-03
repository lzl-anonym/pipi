package com.anonym.common.exception;


public class SmartBusinessException extends RuntimeException {

    public SmartBusinessException() {
    }

    public SmartBusinessException(String message) {
        super(message);
    }

    public SmartBusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public SmartBusinessException(Throwable cause) {
        super(cause);
    }

    public SmartBusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
