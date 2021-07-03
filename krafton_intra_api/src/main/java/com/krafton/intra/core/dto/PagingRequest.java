package com.krafton.intra.core.dto;

import lombok.Data;

/**
 * request로 부터 들어온 요청
 * @param <T>
 */
@Data
public class PagingRequest<T> {

    private T payload;
    private int limit;
    private int page;

}
