package com.etsdk.app.huov7.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsdk.app.huov7.BuildConfig;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.MineGameImgAdapter;
import com.etsdk.app.huov7.base.AutoLazyFragment;
import com.etsdk.app.huov7.getcash.ui.AccountListActivity;
import com.etsdk.app.huov7.getcash.ui.CommRecordActivity;
import com.etsdk.app.huov7.getcash.ui.GetCashActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.AddressList;
import com.etsdk.app.huov7.model.GameBean;
import com.etsdk.app.huov7.model.MessageEvent;
import com.etsdk.app.huov7.model.ShowMsg;
import com.etsdk.app.huov7.model.UserInfoResultBean;
import com.etsdk.app.huov7.pay.ChargeActivityForWap;
import com.etsdk.app.huov7.ui.AccountManageActivity;
import com.etsdk.app.huov7.ui.AccountSaveActivity;
import com.etsdk.app.huov7.ui.AwardRecordActivity;
import com.etsdk.app.huov7.ui.DownloadManagerActivity;
import com.etsdk.app.huov7.ui.FeedBackActivity;
import com.etsdk.app.huov7.ui.IntegralRecordActivity;
import com.etsdk.app.huov7.ui.LoginActivity;
import com.etsdk.app.huov7.ui.MessageActivity;
import com.etsdk.app.huov7.ui.MineGiftCouponListActivityNew;
import com.etsdk.app.huov7.ui.MineGiftRecordActivity;
import com.etsdk.app.huov7.ui.MyWalletActivity;
import com.etsdk.app.huov7.ui.NewScoreShopActivity;
import com.etsdk.app.huov7.ui.ScoreRankActivity;
import com.etsdk.app.huov7.ui.ServiceActivity;
import com.etsdk.app.huov7.ui.SettingActivity;
import com.etsdk.app.huov7.ui.SignInActivity;
import com.etsdk.app.huov7.ui.UserChargeRecordActivity;
import com.etsdk.app.huov7.ui.UserSpendRecordActivity;
import com.etsdk.app.huov7.ui.VIPInfoActivity;
import com.etsdk.app.huov7.ui.dialog.CouponExchangeDialogUtil;
import com.etsdk.app.huov7.util.ImgUtil;
import com.etsdk.app.huov7.util.StringUtils;
import com.game.sdk.domain.BaseRequestBean;
import com.game.sdk.http.HttpNoLoginCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.log.L;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.control.LoginControl;
import com.liang530.log.T;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;
import com.liang530.utils.GlideDisplay;
import com.liang530.views.imageview.roundedimageview.RoundedImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.etsdk.app.huov7.R.id.loadview;
import static com.etsdk.app.huov7.util.ImgUtil.setPhoto;

/**
 * 2017/5/5.
 */

public class MainMineFragmentNew2 extends AutoLazyFragment {
    @BindView(R.id.iv_msg_tip)
    ImageView iv_msg_tip;
    @BindView(R.id.iv_gotoMsg)
    ImageView ivGotoMsg;
    @BindView(R.id.iv_mineHead)
    RoundedImageView ivMineHead;
    @BindView(R.id.tv_vip)
    TextView tv_vip;
    @BindView(R.id.tv_nickName)
    TextView tvNickName;
    @BindView(R.id.tv_yue)
    TextView tv_yue;
    @BindView(R.id.today_income)
    TextView today_income;
    @BindView(R.id.friend_num)
    TextView friend_num;
    @BindView(R.id.all_income)
    TextView all_income;
    private List<GameBean> gameBeanList = new ArrayList<>();
    private boolean bindPhoneClickable = false;
    private String phone = null;
    private String email = null;
    public boolean isRed = true;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_main_mine_new2);
        EventBus.getDefault().register(this);
        setupUI();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMsgShow(ShowMsg showMsg) {
        if (showMsg.isShow()) {
//            iv_msg_tip.setVisibility(View.VISIBLE);
            ivGotoMsg.setImageResource(R.mipmap.syxiaoxi_red);
        } else {
//            iv_msg_tip.setVisibility(View.GONE);
            ivGotoMsg.setImageResource(R.mipmap.syxiaoxi_nomal);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEvent(Boolean isLogin) {
        L.i("333", "登录成功");
        if (isLogin) {
            EventBus.getDefault().post(new ShowMsg(true));
            getUserInfoData();
        }
    }

    private void setupUI() {
        iv_msg_tip.setVisibility(View.GONE);
    }

    @Override
    protected void onFragmentStartLazy() {
        super.onFragmentStartLazy();
        updateData();
    }

    private void updateUserInfoData(UserInfoResultBean userInfoResultBean) {
        int errorImage = R.mipmap.ic_launcher;
        if (userInfoResultBean != null) {
//            tv_vip.setVisibility(View.VISIBLE);
            bindPhoneClickable = true;
            phone = userInfoResultBean.getMobile();
            email = userInfoResultBean.getEmail();
            tvNickName.setText("ID：" + userInfoResultBean.getUsername());
            tv_yue.setText(userInfoResultBean.getNickname());
            today_income.setText(userInfoResultBean.getPtbcnt() + "");
            friend_num.setText(userInfoResultBean.getMyintegral());
            if (StringUtils.isEmpty(userInfoResultBean.getIntegral_total())) {
                all_income.setText("0");
            } else {
                all_income.setText(userInfoResultBean.getIntegral_total() + "");
            }

//            GlideDisplay.display(ivMineHead, userInfoResultBean.getPortrait(), errorImage);
            int error = ImgUtil.setPhoto(userInfoResultBean.getPortrait(), ivMineHead);
            if (StringUtils.isEmpty(userInfoResultBean.getPortrait()) || userInfoResultBean.getPortrait().equals("http://static.idielian.com")) {
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), error);
                L.i("333", "开始上传头像");
                updateHeadImage(savePhoto(bmp));
            }
//            Glide.with(getActivity()).load(userInfoResultBean.getPortrait()).placeholder(errorImage).into(ivMineHead);
            //存入用户信息
            LoginControl.saveKey(GsonUtil.getGson().toJson(userInfoResultBean));
            //处理未读消息
//            EventBus.getDefault().postSticky(new MessageEvent(userInfoResultBean.getNewmsg()));
        } else {
            tvNickName.setText(getString(R.string.default_login_hint));
            tv_yue.setText("");
            setPhoto(null, ivMineHead);
//            GlideDisplay.display(ivMineHead, null, errorImage);
        }
    }

    private File savePhoto(Bitmap bitmap) {
        //进行图片保存
        try {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, bitmap.getWidth(), bitmap.getHeight(),
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
            File file = new File(getContext().getCacheDir(), System.currentTimeMillis() + "temp.jpg");
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void updateHeadImage(File file) {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.userHeadImgApi);
        httpParams.put("portrait", file);
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).post(AppApi.getUrl(AppApi.userHeadImgApi), new HttpJsonCallBackDialog<AddressList>() {
            @Override
            public void onDataSuccess(AddressList data) {
//                T.s(getContext(), "上传成功");
                L.i("333", "上传头像成功");
            }
        });
    }

    /**
     * 更新数据
     */
    public void updateData() {
        getUserInfoData();
    }

    public void getUserInfoData() {
        bindPhoneClickable = false;
        final BaseRequestBean baseRequestBean = new BaseRequestBean();
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(baseRequestBean));
        HttpNoLoginCallbackDecode httpCallbackDecode = new HttpNoLoginCallbackDecode<UserInfoResultBean>(getActivity(), httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(UserInfoResultBean data) {
                if (data != null) {
                    updateUserInfoData(data);
                }
            }

            @Override
            public void onFailure(String code, String msg) {
                if (CODE_SESSION_ERROR.equals(code)) {
                    updateUserInfoData(null);
                } else {
                    super.onFailure(code, msg);
                }
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(AppApi.getUrl(AppApi.userDetailApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }


    @Override
    protected void onDestroyViewLazy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroyViewLazy();
    }

    @Override
    protected void onResumeLazy() {
        super.onResumeLazy();
        getUserInfoData();
    }

    @OnClick({R.id.iv_gotoMsg, R.id.iv_msg_tip, R.id.user_info_ll,
            R.id.recharge_btn, R.id.item_ll1, R.id.sign_btn,
            R.id.item_ll2, R.id.item_ll3, R.id.item_ll4, R.id.item_ll5,
            R.id.item_ll6, R.id.item_ll7, R.id.item_ll8, R.id.item_ll9,
            R.id.item_ll10, R.id.item_ll11, R.id.item_ll12, R.id.item_ll13,
            R.id.item_ll14})
    public void onClickRequestLogin(View view) {
        if (getString(R.string.default_login_hint).equals(tvNickName.getText().toString())) {
            LoginActivity.start(getContext());
            return;
        }
        switch (view.getId()) {
            case R.id.iv_gotoMsg:
                //消息
                MessageActivity.start(mContext);
                break;
            case R.id.iv_msg_tip:
                //消息
                MessageActivity.start(mContext);
                break;
            case R.id.sign_btn:
                //消息
                SignInActivity.start(mContext);
                break;
            case R.id.user_info_ll:
                //个人中心
                AccountManageActivity.start(mContext);
                break;
            case R.id.recharge_btn:
                //充值
                ChargeActivityForWap.start(mContext, AppApi.getUrl(AppApi.chargePingtaibi), "平台币充值", null);
                break;
            case R.id.item_ll1:
                //我的游戏
                DownloadManagerActivity.start(mContext);
                break;
            case R.id.item_ll2:
                //我的VIP
                NewScoreShopActivity.start(mContext);
//                new CouponExchangeDialogUtil().showExchangeDialog(getActivity(), "友情提示", "该功能正在开发中，敬请期待");
//                VIPInfoActivity.start(mContext);
                break;
            case R.id.item_ll3:
                //我的礼包
                MineGiftCouponListActivityNew.start(mContext, MineGiftCouponListActivityNew.TYPE_GIFT, "礼包");
//                MineGiftRecordActivity.start(mContext);
                break;
            case R.id.item_ll4:
                //账号安全
                AccountSaveActivity.start(mContext);
                break;
            case R.id.item_ll5:
                //提现账户
                //意见反馈
                FeedBackActivity.start(mContext);
//                new CouponExchangeDialogUtil().showExchangeDialog(getActivity(), "友情提示", "该功能正在开发中，敬请期待");
//                AccountListActivity.start(mContext);
                break;
            case R.id.item_ll6:
                //提现记录
                //我的钱包
                MyWalletActivity.start(mContext);
//                UserChargeRecordActivity.start(mContext);
//                GetCashActivity.start(mContext);
//                CommRecordActivity.start(mContext, CommRecordActivity.TYPE_GET_CASH_RECORD_LIST);
                break;
            case R.id.item_ll7:
                //我的设置
                SettingActivity.start(mContext);
                break;
            case R.id.item_ll8:
                //积分记录
                IntegralRecordActivity.start(mContext);
//                ScoreRankActivity.start(mContext);
//                UserSpendRecordActivity.start(mContext);
                break;
            case R.id.item_ll9:
                //我的客服
                ServiceActivity.start(mContext);
                break;
            case R.id.item_ll10:
                //每日签到
                new CouponExchangeDialogUtil().showExchangeDialog(getActivity(), "友情提示", "该功能正在开发中，敬请期待");
                break;
            case R.id.item_ll11:
                //我要赚钱
                new CouponExchangeDialogUtil().showExchangeDialog(getActivity(), "友情提示", "该功能正在开发中，敬请期待");
            case R.id.item_ll12:
                //邀请好友
                new CouponExchangeDialogUtil().showExchangeDialog(getActivity(), "友情提示", "该功能正在开发中，敬请期待");
                break;
            case R.id.item_ll13:
                //参与活动
                new CouponExchangeDialogUtil().showExchangeDialog(getActivity(), "友情提示", "该功能正在开发中，敬请期待");
                break;
            case R.id.item_ll14:
                //我的收藏
                new CouponExchangeDialogUtil().showExchangeDialog(getActivity(), "友情提示", "该功能正在开发中，敬请期待");
                break;
        }
    }

}
