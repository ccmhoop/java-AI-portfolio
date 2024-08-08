package com.conner.assistant.utils;

public class Result<T> {

    private final T value;
    private final String errorMessage;
    private final boolean isSuccess;

    private Result(T value, String errorMessage, boolean isSuccess) {
        this.value = value;
        this.errorMessage = errorMessage;
        this.isSuccess = isSuccess;
    }

    public static <T> Result<T> success(T value) {
        return new Result<>(value,null,true);
    }

    public static <T> Result<T> failure(String errorMessage) {
        return new Result<>(null, errorMessage, false);
    }

    public boolean isSuccess(){
        return isSuccess;
    }

    public T getValue() throws ResultException {
        if (isSuccess){
            return value;
        }
        throw new ResultException("Operation failed. Result is not a success.");
    }

    public String getErrorMessage() throws ResultException {
        if (!isSuccess){
            return errorMessage;
        }
        throw new ResultException("Operation failed. Success result does not have error message.");
    }

    public static class ResultException extends RuntimeException {
        public ResultException(String message) {
            super(message);
        }
    }

}