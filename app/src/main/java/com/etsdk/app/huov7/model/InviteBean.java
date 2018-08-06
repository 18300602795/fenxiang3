package com.etsdk.app.huov7.model;

import com.game.sdk.domain.BaseRequestBean;

/**
 * Created by Administrator on 2018/7/23.
 */

public class InviteBean extends BaseRequestBean {
    private String introducer;

    public String getIntroducer() {
        return introducer;
    }

    public void setIntroducer(String introducer) {
        this.introducer = introducer;
    }
}
