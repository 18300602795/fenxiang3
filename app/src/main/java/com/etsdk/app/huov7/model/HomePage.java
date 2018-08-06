package com.etsdk.app.huov7.model;

/**
 * Created by liu hong liang on 2017/1/21.
 */

public class HomePage extends BaseModel{
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private AppPage app_index;//	广告图片对象	手游风向标广告图	详见 轮播图、广告图

        public AppPage getApp_index() {
            return app_index;
        }

        public void setApp_index(AppPage app_index) {
            this.app_index = app_index;
        }
    }
}
