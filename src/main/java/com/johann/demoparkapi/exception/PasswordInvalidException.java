package com.johann.demoparkapi.exception;

public class PasswordInvalidException extends RuntimeException {
    public PasswordInvalidException(String msg) {
        super(msg);
    }
}
