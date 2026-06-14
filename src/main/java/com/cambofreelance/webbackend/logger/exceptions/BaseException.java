package com.cambofreelance.webbackend.logger.exceptions;

public abstract class BaseException extends RuntimeException {

    protected BaseException() {
        super();
    }

    protected BaseException(String message) {
        super(message);
    }

    protected BaseException(Exception exception) {
        super(exception);
    }

    protected BaseException(Throwable throwable) {
        super(throwable);
    }
}
