package com.etsdk.app.huov7.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/7/9.
 */

public class FeedListModel implements Serializable {
    private List<FeedInfoModel> list;
    private double count;

    public List<FeedInfoModel> getList() {
        return list;
    }

    public void setList(List<FeedInfoModel> list) {
        this.list = list;
    }

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }
}
