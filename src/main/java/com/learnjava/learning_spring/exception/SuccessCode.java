package com.learnjava.learning_spring.exception;

public enum SuccessCode {
    SUCCESS(200, "Success"),
    USER_CREATED(200, "User Created successfully."),
    ;
     SuccessCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
