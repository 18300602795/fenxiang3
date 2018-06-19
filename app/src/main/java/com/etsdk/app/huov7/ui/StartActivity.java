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
import com.game.sdk.log.L;
import com.jaeger.library.StatusBarUtil;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.liang530.application.BaseApplication;
import com.liang530.log.SP;
import com.liang530.utils.BaseAppUtil;
import com.sh.sdk.shareinstall.ShareInstall;
import com.sh.sdk.shareinstall.listener.AppGetInfoListener;

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
        // 获取唤醒参数
        ShareInstall.getInstance().getInfo(getIntent(), listener);
    }

    private AppGetInfoListener listener = new AppGetInfoListener() {
        @Override
        public void onGetInfoFinish(String info) {
            Log.d("ShareInstall", "info = " + info);
        }
    };

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 此处要调用，否则app在后台运行时，会无法截获
        ShareInstall.getInstance().getInfo(intent, listener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, Color.parseColor("#5DC9F7"));
    }

    private void setupUI() {
        startTime = System.currentTimeMillis();
        Intent intent = new Intent(this, HuoSdkService.class);
        startService(intent);
        if (isFirstRun(mContext)) {
            GuideActivity.start(mContext);
            finish();
            return;
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                StartActivity.this.finish();
                if (isFirstRun(mContext)) {
                    GuideActivity.start(mContext);
                } else {
                    MainActivity.start(mContext, 0);
                }
                finish();
            }
        }, 3000);
    }

    public static boolean isFirstRun(Context context) {
        boolean isFirstRun = false;
        if (SP.getSp() == null) {
            SP.init(BaseApplication.getInstance());
        }

        int versionCode = SP.getInt("versionCode", 0);
        int newAppVersion = BaseAppUtil.getAppVersionCode();
        if (versionCode != newAppVersion) {
            SP.putInt("versionCode", newAppVersion).commit();
            isFirstRun = true;
        }
        return isFirstRun;
    }
}
