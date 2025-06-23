package com.gll.blog.exceptions;

public class DataValidityException extends RuntimeException {
    public DataValidityException(String message) {
        super(message);
    }
}
