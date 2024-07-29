package com.harenako.api.endpoint.rest.controller;

import java.util.List;

public final class Pagination {
    public static <T> List<T> getPage(List<T> sourceList, Integer page, Integer pageSize) {
        if (sourceList.isEmpty()) {
            return sourceList;
        }

        if (pageSize == null || pageSize <= 0) {
            pageSize = sourceList.size();
        }
        if (page == null || page < 0) {
            page = 0;
        }

        int fromIndex = page * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, sourceList.size());
        return sourceList.subList(fromIndex, toIndex);
    }
}
