package com.woopaca.jissisplit.common.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {

    ALREADY_OWNED_STOCK("", HttpStatus.BAD_REQUEST),

    INVALID_REFRESH_TOKEN("", HttpStatus.UNAUTHORIZED);

    private final String errorCode;
    private final HttpStatus httpStatus;

    ErrorType(String errorCode, HttpStatus httpStatus) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
}
