package com.krafton.intra.core.dto;

import lombok.Builder;
import lombok.Data;

/**
 * DB 조회 전달 클래스
 * @param <T>
 */
@Data
@Builder
public class PagingDto<T> {

    private T paramData;
    private int offset;
    private int limit;

}
