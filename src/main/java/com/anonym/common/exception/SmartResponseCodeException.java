package com.anonym.common.exception;

/**
 * [ 全局异常拦截后保留ResponseCode码的异常]
 */
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
