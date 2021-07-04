package com.krafton.intra.core.dto;

import lombok.Builder;
import lombok.Data;

/**
 * API 공통 응답 - 에러 포함
 * @param <T>
 */
@Builder
@Data
public class ApiResponse<T> {

    private Boolean isSuccess;
    @Builder.Default private String errorMessage = "";
    @Builder.Default private String errorCode = "";
    private T payload;

}
