package com.etsdk.app.huov7.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.ViewPagerAdapter;
import com.etsdk.app.huov7.base.AutoLazyFragment;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.AdImage;
import com.etsdk.app.huov7.model.HomePage1Data;
import com.etsdk.app.huov7.ui.DownloadManagerActivity;
import com.etsdk.app.huov7.ui.MainActivity;
import com.etsdk.app.huov7.ui.MainActivity2;
import com.etsdk.app.huov7.ui.SearchActivity;
import com.etsdk.app.huov7.ui.ServiceActivity;
import com.etsdk.app.huov7.util.StringUtils;
import com.game.sdk.SdkConstant;
import com.game.sdk.log.L;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.R.attr.data;
import static com.etsdk.app.huov7.R.id.kefu_img;

/**
 * Created by Administrator on 2018\2\28 0028.
 */

public class HomeFragment extends AutoLazyFragment {
    @BindView(R.id.fragment_pager)
    ViewPager mViewPager;
    List<Fragment> fragments;
    FragmentPagerAdapter mAdapter;
    @BindView(R.id.ll_dots)
    LinearLayout ll_dots;
    @BindView(R.id.hunter_pager)
    ViewPager hunter_pager;
    @BindView(R.id.text1)
    TextView text1;
    @BindView(R.id.text2)
    TextView text2;
    @BindView(R.id.text3)
    TextView text3;
    @BindView(R.id.text4)
    TextView text4;
    @BindView(R.id.text5)
    TextView text5;
    @BindView(R.id.ll_gift)
    LinearLayout ll_gift;

    @BindView(R.id.bg1)
    View bg1;
    @BindView(R.id.bg2)
    View bg2;
    @BindView(R.id.bg3)
    View bg3;
    @BindView(R.id.bg4)
    View bg4;
    @BindView(R.id.bg5)
    View bg5;
    private List<View> bgs;
    private List<TextView> txts;

    private ViewPagerAdapter pagerAdapter;
    private List<AdImage> imgs;
    private List<View> mDots = new ArrayList<>();// 存放圆点视图的集合

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_home);
        addList();
        initDate();
    }

    private void addList() {
        bgs = new ArrayList<>();
        txts = new ArrayList<>();
        bgs.add(bg1);
        bgs.add(bg2);
        bgs.add(bg3);

        txts.add(text1);
        txts.add(text2);
        txts.add(text3);
        bgs.add(bg4);
        txts.add(text4);
        bgs.add(bg5);

        txts.add(text5);
    }

    private void select(int position) {
        mViewPager.setCurrentItem(position);
        for (int i = 0; i < bgs.size(); i++) {
            if (i == position) {
                bgs.get(i).setBackgroundResource(R.color.bg_selected);
            } else {
                bgs.get(i).setBackgroundResource(R.color.bg_common);
            }
        }

        for (int i = 0; i < txts.size(); i++) {
            if (i == position) {
                txts.get(i).setTextColor(getResources().getColor(R.color.bg_selected));
            } else {
                txts.get(i).setTextColor(getResources().getColor(R.color.text_black));
            }
        }
    }


    private void initDate() {
        ll_gift.setVisibility(View.VISIBLE);
        fragments = new ArrayList<>();
        fragments.add(new MainTjFragment(5));
        fragments.add(new MainTjFragment(4));
        fragments.add(new MainTjFragment(3));
        fragments.add(new MainTjFragment(1));
        text5.setText("赚钱");
        fragments.add(new TryGameListFragment());

        mAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

        };
        mViewPager.setAdapter(mAdapter);
        imgs = new ArrayList<>();
        hunter_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int count = mDots.size();
                for (int i = 0; i < count; i++) {
                    if (position % imgs.size() == i) {
                        mDots.get(i).setBackgroundResource(
                                R.mipmap.dot_focus);
                    } else {
                        mDots.get(i).setBackgroundResource(
                                R.mipmap.dot_normal);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                abc.removeCallbacksAndMessages(null);
                abc.sendEmptyMessageDelayed(0, 4000);
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                L.i("333", "position：" + position);
                select(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        getPageData();
    }

    public void getPageData() {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.hompageApi);
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).get(AppApi.getUrl(AppApi.hompageApi), new HttpJsonCallBackDialog<HomePage1Data>() {
            @Override
            public void onDataSuccess(HomePage1Data data) {
                mViewPager.setCurrentItem(2);
                if (data != null && data.getData() != null) {
                    imgs = data.getData().getHometopper().getList();
                    showBanner();
                    startScrollView();
                } else {
                }
            }

            @Override
            public void onJsonSuccess(int code, String msg, String data) {
                if (StringUtils.isEmpty(SdkConstant.HS_AGENT)) {
                    mViewPager.setCurrentItem(2);
                } else {
                    mViewPager.setCurrentItem(0);
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg, String completionInfo) {
                mViewPager.setCurrentItem(2);
            }
        });
    }

    @OnClick({R.id.rl_goto_mine, R.id.main_gameSearch, R.id.iv_tj_downManager, R.id.ll_hotGame, R.id.ll_newGame, R.id.ll_startGame, R.id.ll_gift, R.id.ll_h5, kefu_img})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_goto_mine:
                ((MainActivity2) getActivity()).show(4);
                break;
            case R.id.main_gameSearch:
                SearchActivity.start(mContext);
                break;
            case R.id.iv_tj_downManager:
                DownloadManagerActivity.start(mContext);
                break;
            case R.id.ll_hotGame:
                select(0);
                break;
            case R.id.ll_newGame:
                select(1);
                break;
            case R.id.ll_startGame:
                select(2);
                break;
            case R.id.ll_gift:
                select(3);
                break;
            case R.id.ll_h5:
                select(4);
                break;
            case kefu_img:
                ServiceActivity.start(mContext);
                break;
        }
    }


    Handler abc = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int currentItem = hunter_pager.getCurrentItem();
            hunter_pager.setCurrentItem(currentItem + 1);
        }
    };


    /**
     * 开始轮播图片
     */
    private void startScrollView() {
        if (pagerAdapter == null) {
            pagerAdapter = new ViewPagerAdapter(imgs, getContext());
            hunter_pager.setAdapter(pagerAdapter);
            hunter_pager.setCurrentItem(10000 * imgs.size());
        } else {
            pagerAdapter.notifyDataSetChanged();
        }
        // 实现轮播效果
        abc.sendEmptyMessageDelayed(0, 4000);
    }

    /**
     * 显示广告条
     */
    private void showBanner() {
        // 创建ViewPager对应的点
        ll_dots.removeAllViews();
        mDots.clear();
        for (int i = 0; i < imgs.size(); i++) {
            View dot = new View(getActivity());
            int size = StringUtils.dip2px(getActivity(), 8);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    size, size);
            params.leftMargin = size;
            if (i == 0) {
                dot.setBackgroundResource(R.mipmap.dot_focus);// 默认选择第1个点
            } else {
                dot.setBackgroundResource(R.mipmap.dot_normal);
            }
            dot.setLayoutParams(params);
            ll_dots.addView(dot);
            mDots.add(dot);
        }
    }


    @Override
    protected void onDestroyViewLazy() {
        super.onDestroyViewLazy();
        abc.removeCallbacksAndMessages(null);
    }
}
