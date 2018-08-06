package com.etsdk.app.huov7.model;

import java.util.List;

/**
 * 充值、消费记录都可以用
 * Created by Administrator on 2017/5/16 .
 */

public class ChargeRecordListBean {

    private double count;
    private List<DataBean> list;

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }

    public List<DataBean> getList() {
        return list;
    }

    public void setList(List<DataBean> list) {
        this.list = list;
    }

    public static class DataBean {

        private String gameid;
        private String gamename;
        private String paytype;
        private String status;
        private String orderid;
        private String pay_time;
        private float amount;

        public String getGameid() {
            return gameid;
        }

        public void setGameid(String gameid) {
            this.gameid = gameid;
        }

        public String getGamename() {
            return gamename;
        }

        public void setGamename(String gamename) {
            this.gamename = gamename;
        }

        public String getPaytype() {
            return paytype;
        }

        public void setPaytype(String paytype) {
            this.paytype = paytype;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getOrderid() {
            return orderid;
        }

        public void setOrderid(String orderid) {
            this.orderid = orderid;
        }

        public String getPay_time() {
            return pay_time;
        }

        public void setPay_time(String pay_time) {
            this.pay_time = pay_time;
        }

        public float getAmount() {
            return amount;
        }

        public void setAmount(float amount) {
            this.amount = amount;
        }
    }
}
