package com.springboot.billchange.exception;

public class InsufficientCoinException extends Exception {
    public InsufficientCoinException() {
        super();
    }
    public InsufficientCoinException(String message) {
        super(message);
    }
}
