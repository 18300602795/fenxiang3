package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.BackBean;
import com.etsdk.app.huov7.model.BackResult;
import com.etsdk.app.huov7.shop.model.SelectGameEvent;
import com.etsdk.app.huov7.shop.ui.MyGameListActivity;
import com.etsdk.app.huov7.ui.dialog.BackHintDialogUtil;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.liang530.log.T;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/28.
 */

public class ChangeGameActivity extends ImmerseActivity {
    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.tv_titleRight)
    TextView ivTitleRight;
    @BindView(R.id.old_money)
    EditText back_money;
    @BindView(R.id.old_game)
    EditText back_game_name;
    @BindView(R.id.old_person)
    EditText back_person_name;
    @BindView(R.id.old_area)
    EditText back_area;
    @BindView(R.id.new_game)
    EditText new_game;
    @BindView(R.id.new_person)
    EditText new_person;
    @BindView(R.id.new_area)
    EditText new_area;
    @BindView(R.id.new_explain)
    EditText back_explain;
    @BindView(R.id.back_post)
    Button back_post;
    private String gameIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_game);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        tvTitleName.setText("转游申请");
        ivTitleRight.setVisibility(View.VISIBLE);
        ivTitleRight.setText("转游规则");
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, ChangeGameActivity.class);
        context.startActivity(starter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSelectGameEvent(SelectGameEvent selectGameEvent) {
        switch (selectGameEvent.getType()){
            case "1":
                back_game_name.setText(selectGameEvent.getGameName());
                break;
            case "2":
                new_game.setText(selectGameEvent.getGameName());
                break;
        }
    }


    @OnClick({R.id.iv_titleLeft, R.id.back_post, R.id.old_game, R.id.new_game})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.back_post:
                new BackHintDialogUtil().show(ChangeGameActivity.this, new BackHintDialogUtil.Listener() {
                    @Override
                    public void ok() {
                        sumitLogin();
                    }

                    @Override
                    public void cancel() {
                    }
                });

                break;
            case R.id.old_game:
                MyGameListActivity.start(mContext, "1");
                break;
            case R.id.new_game:
                MyGameListActivity.start(mContext, "2");
                break;
        }
    }

    private void sumitLogin() {
        if (TextUtils.isEmpty(back_game_name.getText().toString())) {
            T.s(mActivity, "游戏名不能为空");
            return;
        }

        if (TextUtils.isEmpty(back_person_name.getText().toString())) {
            T.s(mActivity, "角色名称不能为空");
            return;
        }

        if (TextUtils.isEmpty(back_area.getText().toString())) {
            T.s(mActivity, "游戏区服不能为空");
            return;
        }

        if (TextUtils.isEmpty(back_money.getText().toString())) {
            T.s(mActivity, "充值金额不能为空");
            return;
        }

        final BackBean backBean = new BackBean();
        backBean.setGame_name(back_game_name.getText().toString());
        backBean.setPerson_name(back_person_name.getText().toString());
        backBean.setArea(back_area.getText().toString());
        backBean.setMoney(back_money.getText().toString());
        backBean.setGame_icon(gameIcon);
        backBean.setReq(back_explain.getText().toString());

        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(backBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<BackResult>(this, httpParamsBuild.getAuthkey(), true) {
            @Override
            public void onDataSuccess(BackResult data) {
                if (data.getStatus() == 200) {
                    T.s(mContext, "申请成功，请耐心等待审核通过");
//                    EventBus.getDefault().post(new ShopListRefreshEvent());
                } else {
                    T.s(mContext, "申请失败： " + data.getMsg());
                }
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        httpCallbackDecode.setLoadMsg("正在申请...");
        RxVolley.post(AppApi.getUrl(AppApi.backup), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

}
