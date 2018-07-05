package com.etsdk.app.huov7.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.ui.dialog.ExitGameDialog;
import com.game.sdk.log.L;
import com.jude.swipbackhelper.SwipeBackHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liu hong liang on 2016/8/31.
 * 网页activity
 */
public class WebViewH5Activity extends ImmerseActivity {
    public final static String URL = "url";
    private String rootUrl = "";
    @BindView(R.id.webView)
    WebView gameWebView;
    ExitGameDialog gameDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消标题栏
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_webview_h5);
        ButterKnife.bind(this);
        rootUrl = getIntent().getStringExtra(URL);
        L.i("333", "url：" + rootUrl);
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);//禁掉滑动finish，会和webview冲突
        gameDialog = new ExitGameDialog();
        init();
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    private void init() {
        gameWebView.loadUrl(rootUrl);
        //com.tencent.smtt.sdk.WebView
        WebSettings webSettings = gameWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);  //支持js
        webSettings.setDomStorageEnabled(true);
        webSettings.setSupportZoom(true);  //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。
//若上面是false，则该WebView不可缩放，这个不管设置什么都不能缩放。
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
//        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);  //关闭webview中缓存
        webSettings.setAllowFileAccess(true);  //设置可以访问文件
        webSettings.setNeedInitialFocus(true); //当webview调用requestFocus时为webview设置节点

        gameWebView.setWebViewClient(new WebViewClient());

        gameWebView.setWebChromeClient(new WebChromeClient());

        gameWebView.loadUrl(rootUrl);
        gameWebView.requestFocus();
    }

    @Override
    protected void onDestroy() {
        gameWebView.removeAllViews();
        gameWebView.destroy();
        super.onDestroy();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            gameDialog.show(mContext, new ExitGameDialog.Listener() {
                @Override
                public void ok() {
                    finish();
                }

                @Override
                public void cancel() {

                }
            });
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

//    public static void start(Context context, String titleName, String url) {
//        if (!BaseAppUtil.isOnline(context)) {
//            T.s(context, "网络不通，请稍后再试！");
//            return;
//        }
//        L.e("webactivity url: " + url);
//        Intent starter = new Intent(context, WebViewActivity.class);
//        starter.putExtra(TITLE_NAME, titleName);
//        starter.putExtra(URL, url);
//        context.startActivity(starter);
//    }

}
