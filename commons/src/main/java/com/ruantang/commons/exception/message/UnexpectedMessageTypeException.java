package com.ruantang.commons.exception.message;

public class UnexpectedMessageTypeException extends RuntimeException{

    public UnexpectedMessageTypeException(String message) {
        super(message);
    }

    public UnexpectedMessageTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
