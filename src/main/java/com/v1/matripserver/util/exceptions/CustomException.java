package com.v1.matripserver.util.exceptions;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {
    private final BaseResponseStatus responseStatus;
    private final HttpStatus httpStatus;

    public CustomException(BaseResponseStatus responseStatus, HttpStatus httpStatus) {
        super(responseStatus.getMessage());
        this.responseStatus = responseStatus;
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public BaseResponseStatus getResponseStatus() {
        return responseStatus;
    }
}
