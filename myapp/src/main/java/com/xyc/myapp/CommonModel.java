package com.xyc.myapp;

import java.util.List;

/**
 * Created by hasee on 2018/1/9.
 */

public class CommonModel<T> {
    private int total;
    private List<T> data;

    public CommonModel() {
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CommomModel{" +
                "total=" + total +
                ", data=" + data +
                '}';
    }
}
