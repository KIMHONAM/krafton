package com.krafton.intra.core.dto;

import lombok.Data;

@Data
public class ApiRequest<T> {
    private T payload;
}
