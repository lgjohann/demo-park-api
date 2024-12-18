package com.johann.demoparkapi.exception;

public class CodigoUniqueViolationException extends RuntimeException {
    public CodigoUniqueViolationException(String msg) {
        super(msg);
    }
}
