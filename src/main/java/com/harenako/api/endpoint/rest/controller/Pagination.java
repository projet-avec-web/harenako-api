package com.harenako.api.endpoint.rest.controller;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class Pagination {
  public static <T> Page<T> convertToPage(List<T> data, int pageNumber, int pageSize) {
    Pageable pageable = PageRequest.of(pageNumber, pageSize);

    int start = (int) pageable.getOffset();
    int end = Math.min((start + pageable.getPageSize()), data.size());

    List<T> pageContent = data.subList(start, end);
    return new PageImpl<>(pageContent, pageable, data.size());
  }
}
