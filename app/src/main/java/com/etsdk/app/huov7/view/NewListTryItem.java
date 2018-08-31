package com.etsdk.app.huov7.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.AileApplication;
import com.etsdk.app.huov7.down.BaseDownView;
import com.etsdk.app.huov7.down.DownloadHelper;
import com.etsdk.app.huov7.down.TasksManager;
import com.etsdk.app.huov7.down.TasksManagerModel;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.GameBean;
import com.etsdk.app.huov7.model.GameDownRequestBean;
import com.etsdk.app.huov7.model.GameDownResult;
import com.etsdk.app.huov7.model.GameGiftItem;
import com.etsdk.app.huov7.model.TryGameBean;
import com.etsdk.app.huov7.model.UserInfoResultBean;
import com.etsdk.app.huov7.ui.DownloadManagerActivity;
import com.etsdk.app.huov7.ui.GameDetailV2Activity;
import com.etsdk.app.huov7.ui.GiftListActivity;
import com.etsdk.app.huov7.ui.SettingActivity;
import com.etsdk.app.huov7.ui.WebViewActivity;
import com.etsdk.app.huov7.ui.WebViewH5Activity;
import com.etsdk.app.huov7.ui.dialog.DownAddressSelectDialogUtil;
import com.etsdk.app.huov7.ui.dialog.Open4gDownHintDialog;
import com.etsdk.app.huov7.util.StringUtils;
import com.game.sdk.SdkConstant;
import com.game.sdk.domain.BaseRequestBean;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.log.L;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.liang530.log.T;
import com.liang530.views.imageview.roundedimageview.RoundedImageView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liu hong liang on 2016/12/6.
 */

public class NewListTryItem extends BaseDownView {
    private static final String TAG = NewListTryItem.class.getSimpleName();
    @BindView(R.id.v_line)
    View vLine;
    @BindView(R.id.tv_hot_rank)
    TextView tvHotRank;
    @BindView(R.id.tv_game_name)
    TextView tvGameName;
    @BindView(R.id.pb_down)
    ProgressBar pbDown;
    @BindView(R.id.tv_down_status)
    TextView tvDownStatus;
    @BindView(R.id.game_list_item)
    RelativeLayout gameListItem;
    @BindView(R.id.iv_game_img)
    RoundedImageView ivGameImg;
    @BindView(R.id.tv_oneword)
    TextView tvOneword;
    @BindView(R.id.tv_size)
    TextView tvSize;
    @BindView(R.id.gameTagView)
    GameTagView gameTagView;
    @BindView(R.id.btn_gift)
    Button btnGift;
    @BindView(R.id.ll_time_line)
    LinearLayout leftTimeLine;
    @BindView(R.id.v_time_line)
    View bottomTimeLine;
    @BindView(R.id.ll_game_name)
    LinearLayout llGameName;
    @BindView(R.id.tv_rate)
    TextView tvRate;
    @BindView(R.id.tv_gift)
    TextView tvGift;
    @BindView(R.id.tv_send_first)
    TextView tvSendFirst;
    private TryGameBean tryGameBean;//游戏本身属性
    private boolean isHotRank;
    private boolean isH5 = false;
    private HashMap<String, Boolean> gameDown = new HashMap<>();

    Context context;

    public NewListTryItem(Context context) {
        super(context);
        this.context = context;
        initUI();
    }

    public NewListTryItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initUI();
    }

    public NewListTryItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initUI();
    }
    private void setDown(String gameId, boolean isDown){
        gameDown.put(gameId, isDown);
    }

    private void initUI() {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            setLayoutParams(layoutParams);
        } else {
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        LayoutInflater.from(getContext()).inflate(R.layout.item_game_list_item, this, true);
        ButterKnife.bind(this);
    }

    public void setGameBean(TryGameBean tryGameBean) {
        this.tryGameBean = tryGameBean;
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(tryGameBean.getGameid()));
        pbDown.setProgress(100);
        TasksManager.getImpl().addDownloadListenerById(tryGameBean.getGameid(), this);
        tvGameName.setText(tryGameBean.getGamename());
        tvOneword.setText(tryGameBean.getOneword());
//        GlideDisplay.display(ivGameImg, gameBean.getIcon(), R.mipmap.icon_load);
        Glide.with(context).load(tryGameBean.getIcon()).placeholder(R.mipmap.icon_load).into(ivGameImg);
        gameTagView.setGameType(tryGameBean.getType());
        tvRate.setVisibility(VISIBLE);
        tvRate.setText("+" + tryGameBean.getIntegral());
        tvSendFirst.setVisibility(VISIBLE);
        if (!StringUtils.isEmpty(tryGameBean.getStatu()) && tryGameBean.getStatu().equals("1")) {
            tvSendFirst.setText("已完成");
            tvSendFirst.setBackgroundResource(R.color.green1);
        } else {
            tvSendFirst.setText("待完成");
            tvSendFirst.setBackgroundResource(R.color.light_red);
        }
    }

    /**
     * 设置游戏状态信息显示，如开服信息，或者开测信息(主要在开服列表和开测列表设置)
     */
    public void setGameStatusInfo(String statusInfo, Integer color) {
        tvOneword.setText(statusInfo);
        if (color != null) {
            tvOneword.setTextColor(color);
        }
    }

    public void setIsHotRank(boolean isHotRank, int position) {
        this.isHotRank = isHotRank;
        if (isHotRank) {
            tvHotRank.setVisibility(VISIBLE);
            if (position <= 3) {
                if (position == 1) {
                    tvHotRank.setBackgroundResource(R.mipmap.no1);
                } else if (position == 2) {
                    tvHotRank.setBackgroundResource(R.mipmap.no2);
                } else {
                    tvHotRank.setBackgroundResource(R.mipmap.no3);
                }
//                layoutParams.width = layoutParams.height = BaseAppUtil.dip2px(tvHotRank.getContext(), 40);
                tvHotRank.setText("");
            } else {
                tvHotRank.setText(position + "");
                tvHotRank.setBackgroundColor(Color.WHITE);
            }
        } else {
            tvHotRank.setVisibility(GONE);
        }
    }

    public void showLine(boolean showLine) {
        if (showLine) {
            vLine.setVisibility(VISIBLE);
        } else {
            vLine.setVisibility(GONE);
        }
    }

    /**
     * 用于开服开测
     *
     * @param showLeftLine   左边代原点的线
     * @param showBottomLine 底部分割线
     */
    public void showTimeLine(boolean showLeftLine, boolean showBottomLine) {
        showLine(false);
        if (showLeftLine) {
            leftTimeLine.setVisibility(VISIBLE);
        } else {
            leftTimeLine.setVisibility(GONE);
        }
        if (showBottomLine) {
            bottomTimeLine.setVisibility(VISIBLE);
        } else {
            bottomTimeLine.setVisibility(GONE);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (tryGameBean == null) return;
        TasksManager.getImpl().addDownloadListenerById(tryGameBean.getGameid(), this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (tryGameBean == null) return;
        TasksManager.getImpl().removeDownloadListenerById(tryGameBean.getGameid(), this);
    }

    @Override
    public void pending(TasksManagerModel tasksManagerModel, int soFarBytes, int totalBytes) {
//        L.e(TAG, tasksManagerModel.getGameName()+" pending");
        if (isH5) {
            return;
        }
        pbDown.setProgress(TasksManager.getImpl().getProgress(tasksManagerModel.getId()));
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(tasksManagerModel.getGameId()));
        updateDownLoadManagerActivity();
    }

    @Override
    public void progress(TasksManagerModel tasksManagerModel, int soFarBytes, int totalBytes) {
//        L.e(TAG, tasksManagerModel.getGameName()+" progress");
        if (isH5) {
            return;
        }
        setDown(tasksManagerModel.getGameId(), true);
        pbDown.setProgress(TasksManager.getImpl().getProgress(tasksManagerModel.getId()));
        tvDownStatus.setText(TasksManager.getImpl().getProgress(tasksManagerModel.getId()) + "%");
    }

    @Override
    public void completed(TasksManagerModel tasksManagerModel) {
//        L.e(TAG, tasksManagerModel.getGameName()+" completed");
        if (isH5) {
            return;
        }
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(tasksManagerModel.getGameId()));
        pbDown.setProgress(100);
        if (gameDown.containsKey(tasksManagerModel.getGameId()) && gameDown.get(tasksManagerModel.getGameId())){
            DownloadHelper.installOrOpen(tasksManagerModel);
        }
        updateDownLoadManagerActivity();
    }

    @Override
    public void paused(TasksManagerModel tasksManagerModel, int soFarBytes, int totalBytes) {
//        L.e(TAG, tasksManagerModel.getGameName()+" paused");
        if (isH5) {
            return;
        }
        pbDown.setProgress(TasksManager.getImpl().getProgress(tasksManagerModel.getId()));
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(tasksManagerModel.getGameId()));
    }

    @Override
    public void error(TasksManagerModel tasksManagerModel, Throwable e) {
//        L.e(TAG, tasksManagerModel.getGameName()+" error");
        if (isH5) {
            return;
        }
        pbDown.setProgress(TasksManager.getImpl().getProgress(tasksManagerModel.getId()));
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(tasksManagerModel.getGameId()));
    }

    @Override
    public void prepareDown(TasksManagerModel tasksManagerModel, boolean noWifiHint) {
//        L.e(TAG, tasksManagerModel.getGameName()+" prepareDown");
        if (noWifiHint) {//需要提示跳转到设置去打开非wifi下载
            new Open4gDownHintDialog().showDialog(getContext(), new Open4gDownHintDialog.ConfirmDialogListener() {
                @Override
                public void ok() {
                    SettingActivity.start(getContext());
                }

                @Override
                public void cancel() {

                }
            });
            return;
        }
        if (tasksManagerModel == null) {
            tasksManagerModel = new TasksManagerModel();
            tasksManagerModel.setGameId(tryGameBean.getGameid());
            tasksManagerModel.setGameIcon(tryGameBean.getIcon());
            tasksManagerModel.setGameName(tryGameBean.getGamename());
            tasksManagerModel.setOnlyWifi(noWifiHint == true ? 0 : 1);
            tasksManagerModel.setGameType(tryGameBean.getType());
//            tasksManagerModel.setUrl(gameBean.getDownlink());
            getDownUrl(tasksManagerModel);
        } else {
            DownloadHelper.start(tasksManagerModel);
        }
    }

    private void getDownUrl(final TasksManagerModel tasksManagerModel) {
        final GameDownRequestBean gameDownRequestBean = new GameDownRequestBean();
        gameDownRequestBean.setGameid(tasksManagerModel.getGameId());
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(gameDownRequestBean));
        L.i("333", httpParamsBuild.getAuthkey());
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<GameDownResult>(getContext(), httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(final GameDownResult data) {
                if (data != null) {
                    if (data.getList() != null && data.getList().size() != 0) {
                        if (data.getList().size() == 1) {//只有一个直接下载
                            GameDownResult.GameDown gameDown = data.getList().get(0);
                            if ("1".equals(gameDown.getType())) {//可以直接下载
                                if (!TextUtils.isEmpty(gameDown.getUrl())) {
                                    tasksManagerModel.setUrl(gameDown.getUrl());
                                    L.i("333", "" + gameDown.getUrl());
                                    tasksManagerModel.setDowncnt(data.getDowncnt() + "");
                                    DownloadHelper.start(tasksManagerModel);
                                } else {
                                    T.s(getContext(), "暂无下载地址");
                                }
                            } else {//跳转到网页下载
                                WebViewActivity.start(getContext(), "游戏下载", gameDown.getUrl());
                            }
                        } else {//多个下载地址，弹出选择
                            //弹出对话框，进行地址选择
                            DownAddressSelectDialogUtil.showAddressSelectDialog(getContext(), data.getList(), new DownAddressSelectDialogUtil.SelectAddressListener() {
                                @Override
                                public void downAddress(String url) {
                                    tasksManagerModel.setUrl(url);
                                    tasksManagerModel.setDowncnt(data.getDowncnt() + "");
                                    DownloadHelper.start(tasksManagerModel);
                                }
                            });
                        }
                    } else {
                        T.s(getContext(), "暂无下载地址");
                    }
                } else {
                    T.s(getContext(), "暂无下载地址");
                }
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        L.i("333", "url:" + AppApi.getUrl(AppApi.gameDownApi));
        RxVolley.post(AppApi.getUrl(AppApi.gameDownApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    /**
     * 当前是下载管理界面，需要更新界面
     */
    private void updateDownLoadManagerActivity() {
        Context context = getContext();
        if (context instanceof DownloadManagerActivity) {
            ((DownloadManagerActivity) context).updateDownListData();
        }
    }

    @Override
    public void netOff() {

    }

    @Override
    public void delete() {
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(tryGameBean.getGameid()));
        pbDown.setProgress(100);

    }

    @Override
    public void netRecover() {

    }

    @Override
    public void installSuccess() {
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(tryGameBean.getGameid()));
        pbDown.setProgress(100);
    }

    @OnClick({R.id.tv_down_status, R.id.game_list_item, R.id.btn_gift})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_down_status:
                if (tryGameBean == null) {
                    return;
                }
                getUserInfoData();
                break;
            case R.id.game_list_item:
                if (tryGameBean == null) {
                    return;
                }
                GameDetailV2Activity.start(getContext(), tryGameBean.getGameid());
//                NewGameDetailActivity.start(getContext(),gameBean.getGameid());
                break;
            case R.id.btn_gift:
                if (tryGameBean == null) {
                    return;
                }
                GiftListActivity.start(getContext(), tryGameBean.getGamename(), tryGameBean.getGameid(), 0, 0, 0, 0);
                break;
        }
    }

    public void getUserInfoData() {
        final BaseRequestBean baseRequestBean = new BaseRequestBean();
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(baseRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<UserInfoResultBean>(getContext(), httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(UserInfoResultBean data) {
                if (isH5) {
                    Intent intent = new Intent(context, WebViewH5Activity.class);
                    intent.putExtra("url", tryGameBean.getDownlink());
//                    intent.putExtra("titleName", gameBean.getGamename());
                    context.startActivity(intent);
                } else {
                    DownloadHelper.onClick(tryGameBean.getGameid(), NewListTryItem.this);
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
}
