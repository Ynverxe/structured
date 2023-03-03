package com.github.ynverxe.structured.exception;

import org.jetbrains.annotations.Nullable;

public class UnexpectedValueStateException extends PathHolderException {

    public UnexpectedValueStateException() {
    }

    public UnexpectedValueStateException(String message) {
        super(message);
    }

    public UnexpectedValueStateException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnexpectedValueStateException setPath(@Nullable String path) {
        return (UnexpectedValueStateException) super.setPath(path);
    }

    public UnexpectedValueStateException insertMissingPath(String path) {
        return (UnexpectedValueStateException) super.insertMissingPath(path);
    }
}