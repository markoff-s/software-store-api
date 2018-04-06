package com.da.softwarestore.common.exception;

public class InvalidArchiveException extends ValidationException {
    public InvalidArchiveException(String message) {
        super(message);
    }

    public InvalidArchiveException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
