package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.AileApplication;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.AppPage;
import com.etsdk.app.huov7.model.HomePage;
import com.etsdk.app.huov7.model.HomePage1Data;
import com.etsdk.app.huov7.service.HuoSdkService;
import com.etsdk.app.huov7.util.ImgUtil;
import com.game.sdk.log.L;
import com.jaeger.library.StatusBarUtil;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.application.BaseApplication;
import com.liang530.log.SP;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;
import com.liang530.utils.BaseAppUtil;
import com.sh.sdk.shareinstall.ShareInstall;
import com.sh.sdk.shareinstall.listener.AppGetInfoListener;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.etsdk.app.huov7.R.mipmap.service;

public class StartActivity extends ImmerseActivity {

    @BindView(R.id.iv_start_img)
    ImageView ivStartImg;
    @BindView(R.id.skip_time)
    TextView skip_time;
    @BindView(R.id.activity_start)
    RelativeLayout activityStart;
    private Timer timer;
    private long startTime;
    //设定倒计时时长 n 单位 s
    private int time = 5;
    private AppPage page;
    //初始化 Handler
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    skip_time.setText("跳过 " + time );
                    break;
                case 1:
                    MainActivity.start(mContext, 0);
                    finish();
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
        setupUI();
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);
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
        timer.cancel();
        super.onDestroy();
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, Color.parseColor("#5DC9F7"));
    }

    public void getPageData() {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.homeListApi);
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.homeListApi), new HttpJsonCallBackDialog<HomePage>() {
            @Override
            public void onDataSuccess(HomePage data) {
                if (data != null && data.getData() != null && data.getData().getApp_index() != null && data.getData().getApp_index().getList().size() != 0){
                    page = data.getData().getApp_index();
                    L.i("333", "url：" + page.getList().get(0).getImage());
                    ImgUtil.loadImage(mContext, page.getList().get(0).getImage(), R.mipmap.app_splash, ivStartImg);
                }
            }

            @Override
            public void onJsonSuccess(int code, String msg, String data) {
            }

            @Override
            public void onFailure(int errorNo, String strMsg, String completionInfo) {
            }
        });
    }

    private void setupUI() {
        startTime = System.currentTimeMillis();
        AileApplication.initListener = new HuoSdkService.InitListener() {
            @Override
            public void initSuccess() {
                L.i("333", "开始获取数据");
                getPageData();
            }

            @Override
            public void initError(String code, String msg) {

            }
        };
        Intent intent = new Intent(this, HuoSdkService.class);
        startService(intent);
//        if (isFirstRun(mContext)) {
//            GuideActivity.start(mContext);
//            finish();
//            return;
//        }

        timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                time--;
                handler.sendEmptyMessage((time == 0 ? 1 : 0));
            }
        }, 1000, 1000);
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                StartActivity.this.finish();
//                if (isFirstRun(mContext)) {
//                    GuideActivity.start(mContext);
//                } else {
//                    MainActivity.start(mContext, 0);
//                }
//                finish();
//            }
//        }, 3000);
    }

    @OnClick({R.id.skip_ll, R.id.iv_start_img})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.skip_ll:
              handler.sendEmptyMessage(1);
                break;
            case R.id.iv_start_img:
                if (page != null){
                    GameDetailV2Activity.start(mContext, page.getList().get(0).getTarget(), true);
                    finish();
                }
                break;
        }
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
