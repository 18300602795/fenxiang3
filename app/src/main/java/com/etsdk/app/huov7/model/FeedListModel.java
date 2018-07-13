package com.etsdk.app.huov7.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/7/9.
 */

public class FeedListModel implements Serializable {
    private List<FeedInfoModel> list;
    private int count;

    public List<FeedInfoModel> getList() {
        return list;
    }

    public void setList(List<FeedInfoModel> list) {
        this.list = list;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
