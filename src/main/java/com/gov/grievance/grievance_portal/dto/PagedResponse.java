package com.gov.grievance.grievance_portal.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagedResponse<T> {
    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean isFirst;
    private boolean isLast;
    private boolean hasNext;
    private boolean hasPrevious;

    // ---- Static Factory Method ----
    public static <T> PagedResponse<T> of(
            List<T> content,
            int pageNumber,
            int pageSize,
            long totalElements,
            int totalPages){
        return PagedResponse.<T>builder()
                .content(content)
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .isFirst(pageNumber == 0)
                .isLast(pageNumber == totalPages-1)
                .hasNext(pageNumber < totalPages-1)
                .hasPrevious(pageNumber > 0)
                .build();
    }
}
