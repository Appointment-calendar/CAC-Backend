package com.careandcure.cac.Exception;

public class InvalidEntityException extends Exception {

    public InvalidEntityException(String message) {
        super(message);
    }

    public InvalidEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}
