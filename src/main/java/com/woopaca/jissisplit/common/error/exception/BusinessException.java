package com.woopaca.jissisplit.common.error.exception;

import com.woopaca.jissisplit.common.error.ErrorType;
import com.woopaca.jissisplit.common.error.ErrorType.ErrorHttpStatus;
import lombok.Getter;

@Getter
public abstract class BusinessException extends RuntimeException {

    private final ErrorType errorType;

    protected BusinessException(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }

    public ErrorHttpStatus getErrorHttpStatus() {
        ErrorHttpStatus ErrorHttpStatus = errorType.getErrorHttpStatus();
        if (ErrorHttpStatus == null) {
            return ErrorType.ErrorHttpStatus.BAD_REQUEST;
        }
        return ErrorHttpStatus;
    }
}
