package com.learn.common.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author LD
 * @date 2021/5/15 18:43
 */
@Data
public class PageData<T> implements Serializable {

    private long total;

    private List<T> rows;

    public PageData() {
    }

    public PageData(long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }



}