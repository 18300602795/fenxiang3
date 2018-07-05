package com.etsdk.app.huov7.model;

/**
 * Created by liu hong liang on 2016/12/21.
 * 最普通的游戏列表item
 */
public class YXBBean {
    private String app_id;
    private String icon;
    private String name = "";
    private float remain;//	FLOAT	游戏币数量	游戏币数量

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRemain() {
        return remain;
    }

    public void setRemain(float remain) {
        this.remain = remain;
    }
}