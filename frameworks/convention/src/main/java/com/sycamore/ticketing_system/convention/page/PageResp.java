package com.sycamore.ticketing_system.convention.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * THIS IS A CLASS
 *
 * @PROJECT_NAME: ticketing_system
 * @CLASS_NAME: PageResp
 * @DESCRIPTION:
 * @CREATER: 桑运昌
 * @DATE: 2023/10/13 12:29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResp<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 当前页
     */
    private Long current;

    /**
     * 每页显示条数
     */
    private Long size = 10L;

    /**
     * 总数
     */
    private Long total;

    /**
     * 查询数据列表
     */
    private List<T> records = Collections.emptyList();

    public PageResp(long current, long size) {
        this(current, size, 0);
    }

    public PageResp(long current, long size, long total) {
        if (current > 1) {
            this.current = current;
        }
        this.size = size;
        this.total = total;
    }

    public PageResp setRecords(List<T> records) {
        this.records = records;
        return this;
    }

    public <R> PageResp<R> convert(Function<? super T, ? extends R> mapper) {
        List<R> collect = this.getRecords().stream().map(mapper).collect(Collectors.toList());
        return ((PageResp<R>) this).setRecords(collect);
    }
}
