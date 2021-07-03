package com.krafton.intra.core.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PagingResponse {
    private int totalItems;
    private List<Object> list;

}
