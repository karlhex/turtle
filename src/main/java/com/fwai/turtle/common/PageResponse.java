package com.fwai.turtle.common;

import lombok.Data;
import java.util.List;

@Data
public class PageResponse<T> {
    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean isLast;
    private boolean isFirst;

    public static <T> PageResponse<T> of(List<T> content, int pageNumber, int pageSize, long totalElements, int totalPages) {
        PageResponse<T> response = new PageResponse<>();
        response.setContent(content);
        response.setPageNumber(pageNumber);
        response.setPageSize(pageSize);
        response.setTotalElements(totalElements);
        response.setTotalPages(totalPages);
        response.setFirst(pageNumber == 0);
        response.setLast(pageNumber == totalPages - 1);
        return response;
    }
}
