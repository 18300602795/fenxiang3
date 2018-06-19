package com.etsdk.app.huov7.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.down.BaseDownView;
import com.etsdk.app.huov7.down.DownloadHelper;
import com.etsdk.app.huov7.down.TasksManager;
import com.etsdk.app.huov7.down.TasksManagerModel;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.GameBean;
import com.etsdk.app.huov7.model.GameDownRequestBean;
import com.etsdk.app.huov7.model.GameDownResult;
import com.etsdk.app.huov7.ui.DownloadManagerActivity;
import com.etsdk.app.huov7.ui.GameDetailV2Activity;
import com.etsdk.app.huov7.ui.SettingActivity;
import com.etsdk.app.huov7.ui.WebViewActivity;
import com.etsdk.app.huov7.ui.WebViewH5Activity;
import com.etsdk.app.huov7.ui.dialog.DownAddressSelectDialogUtil;
import com.etsdk.app.huov7.ui.dialog.Open4gDownHintDialog;
import com.etsdk.app.huov7.util.ImgUtil;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.log.L;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.liang530.log.T;
import com.liang530.views.imageview.roundedimageview.RoundedImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018\3\29 0029.
 */

public class GameItemView extends BaseDownView {
    @BindView(R.id.item_game)
    LinearLayout item_game;
    @BindView(R.id.iv_game_img)
    RoundedImageView iv_game_img;
    @BindView(R.id.tv_game_title)
    TextView tv_game_title;
    private Context mContext;
    GameBean gameBean;
    @BindView(R.id.pb_down)
    ProgressBar pbDown;
    @BindView(R.id.tv_down_status)
    TextView tvDownStatus;
    boolean isH5 = false;

    public GameItemView(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.item_game, this);
        ButterKnife.bind(this);
    }

    public void setData(GameBean gameBean, int category) {
        this.gameBean = gameBean;
        ImgUtil.setImg(getContext(), gameBean.getIcon(), R.mipmap.icon_load, iv_game_img);
        tv_game_title.setText(gameBean.getGamename());
        if (category == 2){
            isH5 = true;
            tvDownStatus.setText("开启");
        }else {
            tvDownStatus.setText(TasksManager.getImpl().getStatusText(gameBean.getGameid()));
        }
        pbDown.setProgress(100);
        TasksManager.getImpl().addDownloadListenerById(gameBean.getGameid(), this);
    }

    @OnClick({R.id.item_game, R.id.tv_down_status})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_game:
                if (gameBean == null) {
                    return;
                }
                GameDetailV2Activity.start(getContext(), gameBean.getGameid(), isH5);
                break;
            case R.id.tv_down_status:
                if (gameBean == null) {
                    return;
                }
                GameDetailV2Activity.start(getContext(), gameBean.getGameid(), isH5);
                break;
        }
    }

    @Override
    public void pending(TasksManagerModel tasksManagerModel, int soFarBytes, int totalBytes) {
//        L.e(TAG, tasksManagerModel.getGameName()+" pending");
        if (isH5){
            return;
        }
        pbDown.setProgress(TasksManager.getImpl().getProgress(tasksManagerModel.getId()));
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(tasksManagerModel.getGameId()));
        updateDownLoadManagerActivity();
    }

    @Override
    public void progress(TasksManagerModel tasksManagerModel, int soFarBytes, int totalBytes) {
//        L.e(TAG, tasksManagerModel.getGameName()+" progress");
        if (isH5){
            return;
        }
        pbDown.setProgress(TasksManager.getImpl().getProgress(tasksManagerModel.getId()));
        tvDownStatus.setText(TasksManager.getImpl().getProgress(tasksManagerModel.getId()) + "%");
    }

    @Override
    public void completed(TasksManagerModel tasksManagerModel) {
//        L.e(TAG, tasksManagerModel.getGameName()+" completed");
        if (isH5){
            return;
        }
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(tasksManagerModel.getGameId()));
        pbDown.setProgress(100);
        updateDownLoadManagerActivity();
    }

    @Override
    public void paused(TasksManagerModel tasksManagerModel, int soFarBytes, int totalBytes) {
//        L.e(TAG, tasksManagerModel.getGameName()+" paused");
        if (isH5){
            return;
        }
        pbDown.setProgress(TasksManager.getImpl().getProgress(tasksManagerModel.getId()));
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(tasksManagerModel.getGameId()));
    }

    @Override
    public void error(TasksManagerModel tasksManagerModel, Throwable e) {
//        L.e(TAG, tasksManagerModel.getGameName()+" error");
        if (isH5){
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
            tasksManagerModel.setGameId(gameBean.getGameid());
            tasksManagerModel.setGameIcon(gameBean.getIcon());
            tasksManagerModel.setGameName(gameBean.getGamename());
            tasksManagerModel.setOnlyWifi(noWifiHint == true ? 0 : 1);
            tasksManagerModel.setGameType(gameBean.getType());
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
                                if(!TextUtils.isEmpty(gameDown.getUrl())) {
                                    tasksManagerModel.setUrl(gameDown.getUrl());
                                    L.i("333" , "" + gameDown.getUrl());
                                    tasksManagerModel.setDowncnt(data.getDowncnt() + "");
                                    DownloadHelper.start(tasksManagerModel);
                                }else{
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
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(gameBean.getGameid()));
        pbDown.setProgress(100);

    }

    @Override
    public void netRecover() {

    }

    @Override
    public void installSuccess() {
        tvDownStatus.setText(TasksManager.getImpl().getStatusText(gameBean.getGameid()));
        pbDown.setProgress(100);
    }

}
