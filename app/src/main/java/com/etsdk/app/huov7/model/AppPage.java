package com.etsdk.app.huov7.model;

import java.util.List;

public  class AppPage {
        private double count;
        private List<PageInfo> list;
        public double getCount() {
            return count;
        }

        public void setCount(double count) {
            this.count = count;
        }

        public List<PageInfo> getList() {
            return list;
        }
        public void setList(List<PageInfo> list) {
            this.list = list;
        }
    }