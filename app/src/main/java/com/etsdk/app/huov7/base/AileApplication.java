package com.etsdk.app.huov7.base;

import android.app.ActivityManager;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.telephony.TelephonyManager;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.etsdk.app.huov7.BuildConfig;
import com.etsdk.app.huov7.model.InstallApkRecord;
import com.game.sdk.log.L;
import com.liang530.application.BaseApplication;
import com.liulishuo.filedownloader.FileDownloader;
import com.mob.MobSDK;
import com.sh.sdk.shareinstall.ShareInstall;

import java.util.HashMap;
import java.util.Map;

import static com.alipay.api.AlipayConstants.FORMAT;
import static com.alipay.api.AlipayConstants.SIGN_TYPE;

/**
 * Created by liu hong liang on 2016/12/1.
 */

public class AileApplication extends BaseApplication {
    private Map<String, InstallApkRecord> installingApkList = new HashMap<>();
    public static String agent;
    public static String imei;
    public static String selectH5 = "1";

    @Override
    public void onCreate() {
        super.onCreate();
        MobSDK.init(getApplicationContext(), "23eef8ceee721", "0d1fab9808cfd3c136ea9326678f685d");
        MultiTypeInstaller.start();
        L.init(BuildConfig.LOG_DEBUG);
        com.liang530.log.L.init(BuildConfig.LOG_DEBUG);
        //设置同时最大下载数量
        FileDownloader.init(getApplicationContext());
        FileDownloader.getImpl().setMaxNetworkThreadCount(8);
        imei = getIMEI(this);
        L.i("333", "imei：" + imei);
        if (isMainProcess()) {
            ShareInstall.getInstance().init(getApplicationContext());
        }
    }

    /**
     * 判断当前进程是否是应用的主进程
     *
     * @return
     */
    public boolean isMainProcess() {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return getApplicationInfo().packageName.equals(appProcess.processName);
            }
        }
        return false;
    }

    @Override
    public Class getLoginClass() {
        return null;
    }

    public Map<String, InstallApkRecord> getInstallingApkList() {
        return installingApkList;
    }

    /**
     * 获取手机IMEI号
     * <p>
     * 需要动态权限: android.permission.READ_PHONE_STATE
     */
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();

        return imei;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
