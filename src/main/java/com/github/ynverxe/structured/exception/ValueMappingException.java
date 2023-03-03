package com.github.ynverxe.structured.exception;

import org.jetbrains.annotations.Nullable;

public class ValueMappingException extends PathHolderException {

    public ValueMappingException() {
    }

    public ValueMappingException(String message) {
        super(message);
    }

    public ValueMappingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValueMappingException(Throwable cause) {
        super(cause);
    }

    public ValueMappingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ValueMappingException setPath(@Nullable String path) {
        return (ValueMappingException) super.setPath(path);
    }

    public ValueMappingException insertMissingPath(String path) {
        return (ValueMappingException) super.insertMissingPath(path);
    }
}