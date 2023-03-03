package com.github.ynverxe.structured.exception;

public class MissingValueException extends RuntimeException {
    public MissingValueException() {
    }

    public MissingValueException(String message) {
        super(message);
    }
}