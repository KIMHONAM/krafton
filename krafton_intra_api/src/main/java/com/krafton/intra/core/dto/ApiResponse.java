package com.krafton.intra.core.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ApiResponse<T> {

    private Boolean isSuccess;
    @Builder.Default private String errorMessage = "";
    @Builder.Default private String errorCode = "";
    private T data;

}
