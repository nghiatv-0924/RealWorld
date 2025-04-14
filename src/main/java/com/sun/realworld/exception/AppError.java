package com.sun.realworld.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AppError {
    DUPLICATED_USER("There is duplicated user information", HttpStatus.UNPROCESSABLE_ENTITY),
    LOGIN_INFO_INVALID("Login information is invalid", HttpStatus.UNPROCESSABLE_ENTITY),
    ALREADY_FOLLOWED_USER("Already followed user", HttpStatus.UNPROCESSABLE_ENTITY),
    USER_NOT_FOUND("User not found", HttpStatus.NOT_FOUND),
    FOLLOW_NOT_FOUND("Follow not found", HttpStatus.NOT_FOUND),
    ;

    private final String message;

    private final HttpStatus status;

    AppError(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
