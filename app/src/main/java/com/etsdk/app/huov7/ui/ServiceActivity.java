package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.ServiceQqAdapter;
import com.etsdk.app.huov7.adapter.ServiceQqGroupAdapter;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.HelpInfoBean;
import com.etsdk.app.huov7.view.StrokeTextView;
import com.game.sdk.domain.BaseRequestBean;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.log.T;
import com.game.sdk.util.GsonUtil;
import com.jaeger.library.StatusBarUtil;
import com.kymjs.rxvolley.RxVolley;
import com.liang530.log.L;
import com.liang530.system.BasePhone;

import java.net.URI;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ServiceActivity extends ImmerseActivity {
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.qq_list)
    ListView lvServiceQq;
    @BindView(R.id.group_list)
    ListView lvQqGroup;
    @BindView(R.id.phone_tv)
    TextView tvTel;
    @BindView(R.id.time_tv)
    TextView time_tv;
    @BindView(R.id.email_tv)
    TextView email_tv;
    @BindView(R.id.qq_tv)
    TextView qq_tv;
    private ServiceQqAdapter serviceQqAdapter;
    private ServiceQqGroupAdapter serviceQqGroupAdapter;
    HelpInfoBean infoBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        ButterKnife.bind(this);
        tvTitleName.setText("客服中心");
        setupUI();
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.bg_blue), 0);
    }


    public void setupUI() {
        serviceQqGroupAdapter = new ServiceQqGroupAdapter();
        lvQqGroup.setAdapter(serviceQqGroupAdapter);
        serviceQqAdapter = new ServiceQqAdapter();
        lvServiceQq.setAdapter(serviceQqAdapter);
        getAboutUsInfo();
    }

    public void updateServiceInfoData(HelpInfoBean helpInfo) {
        if (helpInfo == null) {
            return;
        }
        infoBean = helpInfo;
        tvTel.setText(helpInfo.getTel());
        time_tv.setText("工作日：" + helpInfo.getService_time().replace("| ", "\n").replace('|', '\n'));//以竖线分隔
        serviceQqAdapter.setQqList(helpInfo.getQq());
        serviceQqGroupAdapter.setServiceQqGroupList(helpInfo.getQqgroup(), helpInfo.getQqgroupkey());
        email_tv.setText("邮箱：" + helpInfo.getEmail());
        if (helpInfo.getQq().length != 0) {
            qq_tv.setText("QQ：" + helpInfo.getQq()[0]);
        }
    }

    private void getAboutUsInfo() {
        BaseRequestBean requestBean = new BaseRequestBean();
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<HelpInfoBean>(mContext, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(HelpInfoBean data) {
                if (data != null) {
                    updateServiceInfoData(data);
                }
            }

            @Override
            public void onFailure(String code, String msg) {
                L.e(TAG, code + " " + msg);
            }
        };
        httpCallbackDecode.setShowTs(false);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);//对话框继续使用install接口，在startup联网结束后，自动结束等待loading
        RxVolley.post(AppApi.getUrl(AppApi.getHelpInfo), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }


    public static void start(Context context) {
        Intent starter = new Intent(context, ServiceActivity.class);
        context.startActivity(starter);
    }

    public static Intent getIntent(Context context) {
        Intent starter = new Intent(context, ServiceActivity.class);
        return starter;
    }

    @OnClick({R.id.iv_titleLeft, R.id.call_tv,
            R.id.item0, R.id.item0_1, R.id.item0_2,
            R.id.item1, R.id.item1_1, R.id.item1_2,
            R.id.item02, R.id.item02_1, R.id.item02_2,
            R.id.item2, R.id.item2_1, R.id.item2_2,
            R.id.item3, R.id.item3_1, R.id.item3_2,
            R.id.item4, R.id.item4_1, R.id.item4_2,
            R.id.email_tv, R.id.qq_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.call_tv:
                BasePhone.callDial(this, tvTel.getText().toString());
                break;
            case R.id.item0:
                ServiceListActivity.start(mContext, 0, 0);
                break;
            case R.id.item0_1:
                ServiceListActivity.start(mContext, 0, 0);
                break;
            case R.id.item0_2:
                ServiceListActivity.start(mContext, 0, 1);
                break;
            case R.id.item1:
                ServiceListActivity.start(mContext, 1, 0);
                break;
            case R.id.item1_1:
                ServiceListActivity.start(mContext, 1, 0);
                break;
            case R.id.item1_2:
                ServiceListActivity.start(mContext, 1, 1);
                break;
            case R.id.item02:
                ServiceListActivity.start(mContext, 2, 0);
                break;
            case R.id.item02_1:
                ServiceListActivity.start(mContext, 2, 0);
                break;
            case R.id.item02_2:
                ServiceListActivity.start(mContext, 2, 1);
                break;
            case R.id.item2:
                ServiceListActivity.start(mContext, 3, 0);
                break;
            case R.id.item2_1:
                ServiceListActivity.start(mContext, 3, 0);
                break;
            case R.id.item2_2:
                ServiceListActivity.start(mContext, 3, 1);
                break;
            case R.id.item3:
                ServiceListActivity.start(mContext, 4, 0);
                break;
            case R.id.item3_1:
                ServiceListActivity.start(mContext, 4, 0);
                break;
            case R.id.item3_2:
                ServiceListActivity.start(mContext, 4, 1);
                break;
            case R.id.item4:
                ServiceListActivity.start(mContext, 5, 0);
                break;
            case R.id.item4_1:
                ServiceListActivity.start(mContext, 5, 0);
                break;
            case R.id.item4_2:
                ServiceListActivity.start(mContext, 5, 1);
                break;
            case R.id.email_tv:
                if (infoBean != null)
                    sendMail(infoBean.getEmail());
                break;
            case R.id.qq_tv:
                if (infoBean != null && infoBean.getQq().length != 0) {
                    openQq(mContext, infoBean.getQq()[0]);
                }
                break;
        }
    }

    protected void openQq(Context context, String qq) {
        try {
            String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + qq;
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (Exception e) {
            e.printStackTrace();
            T.s(context, "请先安装最新版qq");
        }
    }

    protected void sendMail(String email) {
        Intent i = new Intent(Intent.ACTION_SEND);
//         i.setType("text/plain"); //模拟器请使用这行
        i.setType("message/rfc822"); // 真机上使用这行
        i.putExtra(Intent.EXTRA_EMAIL,
                new String[]{email});
        i.putExtra(Intent.EXTRA_SUBJECT, "");
        i.putExtra(Intent.EXTRA_TEXT, "");
        startActivity(Intent.createChooser(i,
                "请选择邮箱"));
    }
}
