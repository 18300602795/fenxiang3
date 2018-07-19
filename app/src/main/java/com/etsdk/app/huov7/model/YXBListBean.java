package com.etsdk.app.huov7.model;

import java.util.List;

/**
 * 充值、消费记录都可以用
 * Created by Administrator on 2017/5/16 .
 */

public class YXBListBean {

    private double count;
    private List<YXBBean> list;

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }

    public List<YXBBean> getList() {
        return list;
    }

    public void setList(List<YXBBean> list) {
        this.list = list;
    }
}
