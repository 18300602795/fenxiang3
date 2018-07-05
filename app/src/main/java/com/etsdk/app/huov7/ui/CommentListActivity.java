package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.ArticleListAdapter;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.ArticleBean;
import com.etsdk.app.huov7.model.ArticleList;
import com.game.sdk.log.T;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/6/26.
 */

public class CommentListActivity extends ImmerseActivity {
    @BindView(R.id.tv_titleName)
    TextView tv_titleName;
    @BindView(R.id.recyclerView)
    XRecyclerView fragment_recycle;
    int currentPage = 1;
    ArticleListAdapter articleListAdapter;
    @BindView(R.id.nodate_ll)
    LinearLayout nodate_ll;
    @BindView(R.id.nodate_tv)
    TextView nodate_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);
        ButterKnife.bind(this);
        initDate();
    }

    private void initDate() {
        tv_titleName.setText("论坛");
        fragment_recycle.setLayoutManager(new LinearLayoutManager(mContext));
        articleListAdapter = new ArticleListAdapter(mContext, new ArrayList<ArticleBean>());
        fragment_recycle.setAdapter(articleListAdapter);
        fragment_recycle.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                getPageData();
            }

            @Override
            public void onLoadMore() {
                currentPage += 1;
                getPageData();
            }
        });
        getPageData();
    }

    @OnClick({R.id.fab, R.id.nodate_ll, R.id.iv_titleLeft})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                startActivity(new Intent(mContext, PostedActivity.class));
                break;
            case R.id.nodate_ll:
                getPageData();
                break;
            case R.id.iv_titleLeft:
                finish();
                break;
        }
    }


    public void getPageData() {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.postListApi);
        httpParams.put("page", currentPage);
        httpParams.put("offset", 20);
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).post(AppApi.getUrl(AppApi.postListApi), new HttpJsonCallBackDialog<ArticleList>() {
            @Override
            public void onDataSuccess(ArticleList data) {
                fragment_recycle.refreshComplete();
                if (data != null && data.getData() != null) {
                    if (currentPage == 1) {
                        articleListAdapter.upDate(data.getData());
                    } else {
                        articleListAdapter.addDate(data.getData());
                    }

                } else {
                    if (currentPage == 1) {
                        T.s(mContext, "");
                        nodate_ll.setVisibility(View.VISIBLE);
                        nodate_tv.setText("还没有人发布过内容");
                    } else {
                        fragment_recycle.setNoMore(true);
                    }
                }
            }

            @Override
            public void onJsonSuccess(int code, String msg, String data) {
                fragment_recycle.refreshComplete();
                if (currentPage != 1) {
                    fragment_recycle.setNoMore(true);
                } else {
                    nodate_ll.setVisibility(View.VISIBLE);
                    nodate_tv.setText("还没有人发布过内容");
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg, String completionInfo) {
                fragment_recycle.refreshComplete();
                if (currentPage != 1) {
                    fragment_recycle.setNoMore(true);
                } else {
                    nodate_ll.setVisibility(View.VISIBLE);
                    nodate_tv.setText("连接失败，请检查网络");
                }
            }
        });
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, CommentListActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
