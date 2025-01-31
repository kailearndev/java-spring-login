package com.learnjava.learning_spring.exception;


public enum ErrorCode {
    NAME_INVALID(400, "Name is invalid, min 1 character"),
    PASSWORD_INVALID(400, "Password is invalid min 6 characters and no spaces"),
    USER_INVALID(400, "Username min 3 characters."),
    UNEXPECTED_ERROR(500, "Unexpected error"),
    UNAUTHORIZED(401, "Unauthorized"),
    USER_EXISTED(400, "User already existed"),
    USER_NOT_FOUND(400, "User Not existed"),
    ROLE_IS_EXISTED(400, "Role already existed"),
    IS_ADMIN(400, "Admin user cannot update"),
    ;
     ErrorCode(int code, String message) {
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
