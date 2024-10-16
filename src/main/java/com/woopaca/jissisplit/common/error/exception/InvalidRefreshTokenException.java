package com.woopaca.jissisplit.common.error.exception;

import com.woopaca.jissisplit.common.error.ErrorType;

public class InvalidRefreshTokenException extends BusinessException {

    private static final String MESSAGE = "유효하지 않은 refresh token입니다.";

    public InvalidRefreshTokenException() {
        super(MESSAGE, ErrorType.INVALID_REFRESH_TOKEN);
    }
}
