package com.etsdk.app.huov7.model;

import java.util.List;

/**
 * 充值、消费记录都可以用
 * Created by Administrator on 2017/5/16 .
 */

public class YXBListBean {

    private int count;
    private List<YXBBean> list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<YXBBean> getList() {
        return list;
    }

    public void setList(List<YXBBean> list) {
        this.list = list;
    }
}
