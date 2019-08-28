package com.anonym.common.exception;

/**
 * 自定义异常
 *
 * @author lizongliang
 * @date 2019-07-18 18:03
 */
public class ImportException extends RuntimeException {

    public ImportException() {
    }

    public ImportException(String message) {
        super(message);
    }

    public ImportException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImportException(Throwable cause) {
        super(cause);
    }

    public ImportException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
