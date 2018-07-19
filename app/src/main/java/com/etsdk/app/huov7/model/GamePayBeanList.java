package com.etsdk.app.huov7.model;

import java.util.List;

/**
 * Created by liu hong liang on 2017/1/15.
 */

public class GamePayBeanList extends BaseModel{
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private double count;
        private List<GameItemPay> list;

        public double getCount() {
            return count;
        }

        public void setCount(double count) {
            this.count = count;
        }

        public List<GameItemPay> getList() {
            return list;
        }

        public void setList(List<GameItemPay> list) {
            this.list = list;
        }
    }
}
