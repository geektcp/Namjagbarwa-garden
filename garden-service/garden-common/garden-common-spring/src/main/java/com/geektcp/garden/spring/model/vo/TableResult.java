package com.geektcp.garden.spring.model.vo;

import java.io.Serializable;
import java.util.List;

public class TableResult<T> implements Serializable {
    private static final long serialVersionUID = 5953520552248335003L;
    long total;
    List<T> rows;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
