package com.krafton.intra.core.exception;

import com.krafton.intra.core.dto.ApiResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@RestControllerAdvice
public class CommonControllerAdvice {

    private static final Logger LOGGER = LogManager.getLogger(CommonControllerAdvice.class);

    @Resource(name = "messageSourceAccessor")
    protected MessageSourceAccessor message;

    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity<ApiResponse> handleBusinessException(HttpServletRequest request, Exception e){
        String errorCode = "";
        String errorMessage = "";
        try {
            errorMessage = message.getMessage(errorCode, Locale.KOREA);
        } catch (NoSuchMessageException ee) {
            errorMessage = e.getMessage();
        }
        return errorResponse(e, errorMessage, errorCode);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse> handleEtcException(HttpServletRequest request, Exception e){
        String errorCode = "ERR000000";
        String errorMessage = "";
        try {
            errorMessage = message.getMessage(errorCode, Locale.KOREA);
        } catch (NoSuchMessageException ee) {
            errorMessage = e.getMessage();
        }
        return errorResponse(e, errorMessage, errorCode);
    }

    public ResponseEntity<ApiResponse> errorResponse(Exception e, String errorMessage, String errorCode) {
        LOGGER.error(e.getMessage(), e);
        return ResponseEntity.ok(ApiResponse.builder().errorCode(errorCode).errorMessage(errorMessage).isSuccess(false).data("").build());
    }

}
