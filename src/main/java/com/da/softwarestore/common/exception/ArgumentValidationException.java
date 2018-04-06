package com.da.softwarestore.common.exception;

public class ArgumentValidationException extends IllegalArgumentException {
    public ArgumentValidationException(String argumentName) {
        super(formatMessage(argumentName));
    }

    public ArgumentValidationException(String argumentName, Throwable cause) {
        super(formatMessage(argumentName), cause);
    }

    public ArgumentValidationException(String argumentName, String message) {
        super(formatMessage(argumentName) + " " + message);
    }

    private static String formatMessage(String argumentName) {
        return "Argument \"" + argumentName + "\" is invalid.";
    }
}
