package com.krafton.intra.core.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PagingDto<T> {

    private T paramData;
    private int offset;
    private int limit;

}
