package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.service.HuoSdkService;
import com.fm.openinstall.OpenInstall;
import com.fm.openinstall.listener.AppInstallAdapter;
import com.fm.openinstall.listener.AppWakeUpAdapter;
import com.fm.openinstall.model.AppData;
import com.game.sdk.log.L;
import com.jaeger.library.StatusBarUtil;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.liang530.application.BaseApplication;
import com.liang530.log.SP;
import com.liang530.utils.BaseAppUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StartActivity extends ImmerseActivity {

    @BindView(R.id.iv_start_img)
    ImageView ivStartImg;
    @BindView(R.id.activity_start)
    RelativeLayout activityStart;
    private long startTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
        setupUI();
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);
        L.i("333", "开始获取数据");
        //获取唤醒参数
        OpenInstall.getWakeUp(getIntent(), wakeUpAdapter);
        //获取OpenInstall安装数据
        OpenInstall.getInstall(new AppInstallAdapter() {
            @Override
            public void onInstall(AppData appData) {
                //获取渠道数据
                String channelCode = appData.getChannel();
                //获取自定义数据
                String bindData = appData.getData();
//                Toast.makeText(mContext, "channelCode：" + channelCode + " bindData：" + bindData, Toast.LENGTH_LONG).show();
                Log.d("OpenInstall", "getInstall : installData2 = " + appData.toString());
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 此处要调用，否则App在后台运行时，会无法截获
        OpenInstall.getWakeUp(intent, wakeUpAdapter);
    }
    AppWakeUpAdapter wakeUpAdapter = new AppWakeUpAdapter() {
        @Override
        public void onWakeUp(AppData appData) {
            //获取渠道数据
            String channelCode = appData.getChannel();
            //获取绑定数据
            String bindData = appData.getData();
            L.i("333", "channelCode：" + channelCode + " bindData：" + bindData);
            Log.d("OpenInstall", "getWakeUp : wakeupData = " + appData.toString());
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        wakeUpAdapter = null;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, Color.parseColor("#5DC9F7"));
    }

    private void setupUI() {
        startTime=System.currentTimeMillis();
        Intent intent=new Intent(this,HuoSdkService.class);
        startService(intent);
        if(isFirstRun(mContext)){
            GuideActivity.start(mContext);
            finish();
            return;
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                StartActivity.this.finish();
                if(isFirstRun(mContext)){
                    GuideActivity.start(mContext);
                }else{
                    MainActivity.start(mContext,0);
                }
                finish();
            }
        },3000);
    }
    public static boolean isFirstRun(Context context) {
        boolean isFirstRun = false;
        if(SP.getSp() == null) {
            SP.init(BaseApplication.getInstance());
        }

        int versionCode = SP.getInt("versionCode", 0);
        int newAppVersion = BaseAppUtil.getAppVersionCode();
        if(versionCode != newAppVersion) {
            SP.putInt("versionCode", newAppVersion).commit();
            isFirstRun = true;
        }
        return isFirstRun;
    }
}
