package com.xyc.okutils.model;

/**
 * Created by hasee on 2018/3/30.
 */

public class SpinnerSelectModel {
    private int id;
    private String selectItem;

    public SpinnerSelectModel() {
    }

    public SpinnerSelectModel(int id, String selectItem) {
        this.id = id;
        this.selectItem = selectItem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSelectItem() {
        return selectItem;
    }

    public void setSelectItem(String selectItem) {
        this.selectItem = selectItem;
    }

    @Override
    public String toString() {
        return "SpinnerSelectModel{" +
                "id=" + id +
                ", selectItem='" + selectItem + '\'' +
                '}';
    }
}
