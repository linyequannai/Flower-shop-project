package com.flowershop.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {
    private long total;
    private long pages;
    private long current;
    private long size;
    private List<T> records;

    public static <T> PageResult<T> of(long total, long current, long size, List<T> records) {
        long pages = total % size == 0 ? total / size : total / size + 1;
        return new PageResult<>(total, pages, current, size, records);
    }
}
