package com.etsdk.app.huov7.model;

import java.util.List;

public  class AdImageKeyBean {
        private double count;
        private List<AdImage> list;
        public double getCount() {
            return count;
        }

        public void setCount(double count) {
            this.count = count;
        }

        public List<AdImage> getList() {
            return list;
        }
        public void setList(List<AdImage> list) {
            this.list = list;
        }
    }