package com.github.ynverxe.structured.exception;

import org.jetbrains.annotations.Nullable;

public class PathHolderException extends RuntimeException {

    private @Nullable String path;

    public PathHolderException() {
    }

    public PathHolderException(String message) {
        super(message);
    }

    public PathHolderException(String message, Throwable cause) {
        super(message, cause);
    }

    public PathHolderException(Throwable cause) {
        super(cause);
    }

    public PathHolderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public @Nullable String getPath() {
        return path;
    }

    public PathHolderException setPath(String path) {
        this.path = path;
        return this;
    }

    public PathHolderException insertMissingPath(String path) {
        if (this.path == null) {
            this.path = path;
            return this;
        }

        this.path = path + "." + this.path;
        return this;
    }

    @Override
    public String getMessage() {
        if (path == null) return super.getMessage();

        return path + ": " + super.getMessage();
    }
}