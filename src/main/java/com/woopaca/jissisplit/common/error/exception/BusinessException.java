package com.woopaca.jissisplit.common.error.exception;

import com.woopaca.jissisplit.common.error.ErrorType;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class BusinessException extends RuntimeException {

    private final ErrorType errorType;

    protected BusinessException(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }

    public HttpStatus getErrorHttpStatus() {
        HttpStatus httpStatus = errorType.getHttpStatus();
        if (httpStatus == null) {
            return HttpStatus.BAD_REQUEST;
        }
        return httpStatus;
    }
}
