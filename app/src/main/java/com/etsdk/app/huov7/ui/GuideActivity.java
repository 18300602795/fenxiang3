package com.etsdk.app.huov7.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.GuideAdapter;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.util.StringUtils;
import com.game.sdk.log.L;
import com.jude.swipbackhelper.SwipeBackHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.etsdk.app.huov7.R.id.ll_dots;
import static com.etsdk.app.huov7.base.AileApplication.imgs;

/**
 * Created by liu hong liang on 2017/1/16.
 * 引导页
 */
public class GuideActivity extends ImmerseActivity {

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.activity_guid)
    RelativeLayout activityGuid;
    @BindView(R.id.ll_dots)
    LinearLayout ll_dots;
    public GestureDetector mGestureDetector;
    private int currentItem = 0;
    private int flaggingWidth;
    private List<View> mDots = new ArrayList<>();// 存放圆点视图的集合
    private int[] imageIds=new int[]{R.mipmap.guide_item1,R.mipmap.guide_item2,R.mipmap.guide_item3, R.mipmap.guide_item4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        setupUI();
        slipToMain();
        addDot();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        flaggingWidth = dm.widthPixels / 3;
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);
    }


    /**
     * 显示广告条
     */
    private void addDot() {
        // 创建ViewPager对应的点
        ll_dots.removeAllViews();
        mDots.clear();
        for (int i = 0; i < imageIds.length; i++) {
            View dot = new View(mContext);
            int size = StringUtils.dip2px(mContext, 8);
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


    private void setupUI() {
        viewpager.setAdapter(new GuideAdapter(imageIds));
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentItem = position;
                L.i("333", "position：" + position);
                int count = mDots.size();
                for (int i = 0; i < count; i++) {
                    if (position == i) {
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
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event)) {
            event.setAction(MotionEvent.ACTION_CANCEL);
        }
        return super.dispatchTouchEvent(event);
    }


    private void slipToMain() {
        mGestureDetector = new GestureDetector(this,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2,
                                           float velocityX, float velocityY) {
                        if (currentItem == 3) {
                            L.i("333", "flaggingWidth：" + flaggingWidth);
                            if ((e1.getRawX() - e2.getRawX()) >= flaggingWidth) {
                                MainActivity.start(mContext, 0);
                                finish();
                                return true;
                            }
                        }
                        return false;
                    }

                });
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, GuideActivity.class);
        context.startActivity(starter);
    }

    @Override
    public void onBackPressed() {

    }
}
