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
import com.etsdk.app.huov7.model.GameBean;
import com.etsdk.app.huov7.model.GameBeanList;
import com.etsdk.app.huov7.model.SplitLine;
import com.etsdk.app.huov7.model.TryGameBean;
import com.etsdk.app.huov7.model.TryGameBeanList;
import com.etsdk.app.huov7.provider.GameItemViewProvider;
import com.etsdk.app.huov7.provider.SplitLineViewProvider;
import com.etsdk.app.huov7.provider.TryItemViewProvider;
import com.etsdk.hlrefresh.AdvRefreshListener;
import com.etsdk.hlrefresh.BaseRefreshLayout;
import com.etsdk.hlrefresh.MVCSwipeRefreshHelper;
import com.game.sdk.log.L;
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
import static android.R.attr.data;
import static android.R.attr.type;

public class TryGameListActivity extends ImmerseActivity implements AdvRefreshListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    BaseRefreshLayout baseRefreshLayout;
    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.swrefresh)
    SwipeRefreshLayout swrefresh;
    private Items items=new Items();
    private MultiTypeAdapter multiTypeAdapter;
    private boolean requestTopSplit = true;//是否需要顶部分割线
    private boolean showRank = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);
        ButterKnife.bind(this);
        setupUI();
    }
    private void setupUI() {
        tvTitleName.setText("试玩游戏");
        Intent intent = getIntent();
        if (intent != null) {
            String title =intent.getStringExtra("title");
            if (!TextUtils.isEmpty(title)) {
                tvTitleName.setText(title);
            }
        }

        baseRefreshLayout = new MVCSwipeRefreshHelper(swrefresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        // 设置适配器
        multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(SplitLine.class, new SplitLineViewProvider());
        multiTypeAdapter.register(TryGameBean.class, new TryItemViewProvider(showRank));
        baseRefreshLayout.setAdapter(multiTypeAdapter);
        baseRefreshLayout.setAdvRefreshListener(this);
        baseRefreshLayout.refresh();
    }
    @Override
    public void getPageData(final int requestPageNo) {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.trygameApi);
        httpParams.put("page", requestPageNo);
        httpParams.put("offset", 20);
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.trygameApi), new HttpJsonCallBackDialog<TryGameBeanList>() {
            @Override
            public void onDataSuccess(TryGameBeanList data) {
                if (data != null && data.getData().getList() != null) {
                    int maxPage = (int) Math.ceil(data.getData().getCount() / 20.);
                    Items resultItems = new Items();
                    if (requestTopSplit && requestPageNo == 1) {//新游热门第一页第一个顶部有分割线
                        resultItems.add(new SplitLine());
                    }
                    resultItems.addAll(data.getData().getList());
                    baseRefreshLayout.resultLoadData(items, resultItems, maxPage);
                } else {
                    baseRefreshLayout.resultLoadData(items, new ArrayList(), requestPageNo - 1);
                }
            }

            @Override
            public void onJsonSuccess(int code, String msg, String data) {
                baseRefreshLayout.resultLoadData(items, new ArrayList(), requestPageNo - 1);
                L.i("333", "code：" + code + " mag：" + msg + " data：" + data);
            }

            @Override
            public void onFailure(int errorNo, String strMsg, String completionInfo) {
                baseRefreshLayout.resultLoadData(items, new ArrayList(), requestPageNo - 1);
                L.i("333", "errorNo：" + errorNo + " strMsg：" + strMsg + " completionInfo：" + completionInfo);
            }
        });
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, TryGameListActivity.class);
        context.startActivity(starter);
    }

    public static Intent getIntent(Context context, String title) {
        Intent starter = new Intent(context, TryGameListActivity.class);
        starter.putExtra("title",title);
        return starter;
    }

    public static void start(Context context, String title) {
        Intent starter = new Intent(context, TryGameListActivity.class);
        starter.putExtra("title",title);
        context.startActivity(starter);
    }

    @OnClick({R.id.iv_titleLeft,R.id.tv_titleRight})
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
