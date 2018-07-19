package com.etsdk.app.huov7.model;

import java.util.List;

/**
 * Created by liu hong liang on 2017/1/15.
 */

public class CouponBeanList extends BaseModel{
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private double count;
        private List<CouponListItem> list;

        public double getCount() {
            return count;
        }

        public void setCount(double count) {
            this.count = count;
        }

        public List<CouponListItem> getList() {
            return list;
        }

        public void setList(List<CouponListItem> list) {
            this.list = list;
        }
    }
}
