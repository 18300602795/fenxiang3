package com.etsdk.app.huov7.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.InviteBean;
import com.etsdk.app.huov7.model.UserInfoResultBean;
import com.etsdk.app.huov7.ui.dialog.BindDialogUtil;
import com.etsdk.app.huov7.util.EncodingUtils;
import com.etsdk.app.huov7.util.StringUtils;
import com.game.sdk.domain.BaseRequestBean;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpNoLoginCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.liang530.log.L;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.etsdk.app.huov7.R.id.qr_iv;

/**
 * Created by Administrator on 2017/11/15.
 */

public class InviteActivity extends ImmerseActivity {
    private String url;
    private String text;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;

    @BindView(R.id.code_mine)
    TextView code_mine;
    @BindView(R.id.code_other)
    EditText code_other;
    private UserInfoResultBean resultBean;
    private BindDialogUtil bindDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
        ButterKnife.bind(this);
        tvTitleName.setText("邀请好友");
        text = "纷享游戏_花最少的钱玩最爽的游戏";
        bindDialog = new BindDialogUtil();
        getUserInfoData();
    }

    private void setQr(String str) {
        Bitmap qrCode = EncodingUtils.createQRCode(str, 200, 200,
                BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
//        qr_iv.setImageBitmap(qrCode);
    }

    @OnClick({R.id.iv_titleLeft, R.id.copy_mine, R.id.put_btn, R.id.wechat_ll, R.id.moments_ll, R.id.weibo_ll, R.id.qq_ll, R.id.qzone_ll, R.id.copy_ll})
    public void onClick(View view) {
        if (view.getId() == R.id.iv_titleLeft) {
            finish();
        }
        if (resultBean == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.copy_mine:
                copy(code_mine.getText().toString(), "复制邀请码成功");
                break;
            case R.id.put_btn:
                if (StringUtils.isEmpty(code_other.getText().toString())) {
                    Toast.makeText(mContext, "请输入邀请码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (StringUtils.isEmpty(resultBean.getMobile())) {
                    bindDialog.showHintDialog(mContext, new BindDialogUtil.BindDialogListener() {
                        @Override
                        public void ok() {
                            BindPhoneActivity.start(mContext, false);
                        }

                        @Override
                        public void cancel() {
                            bindDialog.dismiss();
                        }
                    });
                } else {
                getUserInfoData1();
                }
                break;
            case R.id.wechat_ll:
                share(Wechat.NAME);
                break;
            case R.id.moments_ll:
                share(WechatMoments.NAME);
                break;
            case R.id.weibo_ll:
                share(SinaWeibo.NAME);
                break;
            case R.id.qq_ll:
                share(QQ.NAME);
                break;
            case R.id.qzone_ll:
                share(QZone.NAME);
                break;
            case R.id.copy_ll:
                copy(url, "链接复制成功");
                break;
        }
    }

    public void getUserInfoData1() {
        final InviteBean inviteBean = new InviteBean();
        String str2 = "";
        try {
            str2 = new String(Base64.decode(code_other.getText().toString(), Base64.DEFAULT));
        } catch (Exception e) {
            Toast.makeText(mContext, "请输入正确的邀请码", Toast.LENGTH_SHORT).show();
            return;
        }
        inviteBean.setIntroducer(str2);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(inviteBean));
        HttpNoLoginCallbackDecode httpCallbackDecode = new HttpNoLoginCallbackDecode<String>(mContext, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(String data) {
                if (data != null) {
                    L.i("333", "intro：" + data);
                }
            }

            @Override
            public void onDataSuccess(String data, String code, String msg) {
                super.onDataSuccess(data, code, msg);
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
            }

            @Override
            public void onFailure(String code, String msg) {
                super.onFailure(code, msg);
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(AppApi.getUrl(AppApi.introAdd), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }


    private void copy(String cont, String tip) {
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", cont);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
        Toast.makeText(this, tip, Toast.LENGTH_SHORT).show();
    }

    private void share(String name) {
        OnekeyShare oks = new OnekeyShare();
        oks.setImageUrl("http://gm.idielian.com/upload/20171227/5a4376ac19412.png");
        oks.setTitleUrl(url);
        oks.setUrl(url);
        oks.setSiteUrl(url);
        oks.setText(text);
        oks.setTitle("纷享游戏");
        oks.setPlatform(name);
        oks.show(this);
    }

    public void getUserInfoData() {
        final BaseRequestBean baseRequestBean = new BaseRequestBean();
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(baseRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<UserInfoResultBean>(InviteActivity.this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(UserInfoResultBean data) {
                if (data != null) {
                    resultBean = data;
                    String strBase64 = Base64.encodeToString(data.getUsername().getBytes(), Base64.NO_WRAP);
                    code_mine.setText(strBase64);
                    L.i("333", "strBase64：" + strBase64);
                    share();
                }
            }

            @Override
            public void onFailure(String code, String msg) {

            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(AppApi.getUrl(AppApi.userDetailApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    private void share() {
//        url = "http://tui.idielian.com/agent.php/Front/Appdown/prelink/name/VEdaSw==/username/" + code_mine.getText().toString();
        url = "http://tui.idielian.com/agent.php/Front/Appdown/prelink/name/";
        if (resultBean.getName().equals("官网")) {
            url = url + "VEdaSw==/username/" + code_mine.getText().toString() + "/guanwang/1";
        } else {
            String name_code = Base64.encodeToString(resultBean.getName().getBytes(), Base64.DEFAULT);
            url = url + name_code + "/username/" + code_mine.getText().toString();
        }
        L.i("333", "downlink：" + url);
//        setQr(url);
    }


    public static void start(Context context) {
        Intent starter = new Intent(context, InviteActivity.class);
        context.startActivity(starter);
    }

    public static Intent getIntent(Context context) {
        Intent starter = new Intent(context, InviteActivity.class);
        return starter;
    }
}
