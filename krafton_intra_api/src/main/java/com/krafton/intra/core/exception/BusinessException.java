package com.krafton.intra.core.exception;

import lombok.Data;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{

    private String errorCode;
    private String errorMessage;

    public BusinessException() {
        super();
    }
    public BusinessException(String errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }


    public BusinessException(String errorCode, String errorMessage) {
        super(errorCode);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public BusinessException(String errorCode, String errorMessage, Throwable t) {
        super(errorCode, t);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

}
