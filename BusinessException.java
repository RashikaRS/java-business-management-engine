package com.bizengine.exception;

/** Root of all CHECKED business-rule failures the caller is expected to handle. */
public class BusinessException extends Exception {
    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
