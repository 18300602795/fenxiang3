package com.etsdk.app.huov7.model;

import java.util.List;

/**
 * Created by Administrator on 2018/7/18.
 */

public class IntegralList {
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private double count;
        private List<IntegralModel> list;

        public double getCount() {
            return count;
        }

        public void setCount(double count) {
            this.count = count;
        }

        public List<IntegralModel> getList() {
            return list;
        }

        public void setList(List<IntegralModel> list) {
            this.list = list;
        }
    }
}
