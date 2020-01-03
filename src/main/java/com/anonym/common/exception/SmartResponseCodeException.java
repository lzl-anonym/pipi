package com.anonym.common.exception;


public class SmartResponseCodeException extends RuntimeException {

    private Integer code;

    public SmartResponseCodeException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
