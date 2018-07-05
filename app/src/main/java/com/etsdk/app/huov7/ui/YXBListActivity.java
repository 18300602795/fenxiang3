package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.ChargeRecordListBean;
import com.etsdk.app.huov7.model.ChargeRrcordListRequestBean;
import com.etsdk.app.huov7.model.GameBean;
import com.etsdk.app.huov7.model.GameBeanList;
import com.etsdk.app.huov7.model.SplitLine;
import com.etsdk.app.huov7.model.YXBBean;
import com.etsdk.app.huov7.model.YXBListBean;
import com.etsdk.app.huov7.provider.GameItemViewProvider;
import com.etsdk.app.huov7.provider.SplitLineViewProvider;
import com.etsdk.app.huov7.provider.YXBViewProvider;
import com.etsdk.hlrefresh.AdvRefreshListener;
import com.etsdk.hlrefresh.BaseRefreshLayout;
import com.etsdk.hlrefresh.MVCSwipeRefreshHelper;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.log.L;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

import static android.R.attr.category;

public class YXBListActivity extends ImmerseActivity implements AdvRefreshListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    BaseRefreshLayout baseRefreshLayout;
    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.swrefresh)
    SwipeRefreshLayout swrefresh;
    private Items items = new Items();
    private MultiTypeAdapter multiTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        tvTitleName.setText("游戏币");
        baseRefreshLayout = new MVCSwipeRefreshHelper(swrefresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        // 设置适配器
        multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(SplitLine.class, new SplitLineViewProvider());
        multiTypeAdapter.register(YXBBean.class, new YXBViewProvider());
        baseRefreshLayout.setAdapter(multiTypeAdapter);
        baseRefreshLayout.setAdvRefreshListener(this);
        baseRefreshLayout.refresh();
    }

    @Override
    public void getPageData(final int requestPageNo) {
        final ChargeRrcordListRequestBean requestBean = new ChargeRrcordListRequestBean();
        requestBean.setPage(requestPageNo);
        requestBean.setOffset(10);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<YXBListBean>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(final YXBListBean data) {
                if (data != null && data.getList() != null) {
                    int maxPage = (int) Math.ceil(data.getCount() / 20.);
                    Items resultItems = new Items();
                    resultItems.add(new SplitLine());
                    resultItems.addAll(data.getList());
                    baseRefreshLayout.resultLoadData(items, resultItems, maxPage);
                } else {
                    baseRefreshLayout.resultLoadData(items, new ArrayList(), requestPageNo - 1);
                }
            }

            @Override
            public void onFailure(String code, String msg) {
                L.e(TAG, code + " " + msg);
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(AppApi.getUrl(AppApi.balance), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, YXBListActivity.class);
        context.startActivity(starter);
    }

    @OnClick({R.id.iv_titleLeft, R.id.tv_titleRight})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.tv_titleRight:
                finish();
                break;
        }
    }

}
