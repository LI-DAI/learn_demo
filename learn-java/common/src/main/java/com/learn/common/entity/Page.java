package com.learn.common.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author LD
 * @date 2021/5/15 18:43
 */
@Data
public class Page<T> implements Serializable {

    private Integer pageNum;

    private Integer pageSize;

    private Long total;

    private List<T> rows;

    public Page() {
    }

    public Page(Long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    public Page(Integer pageNum, Integer pageSize, Long total, List<T> rows) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.rows = rows;
    }
}