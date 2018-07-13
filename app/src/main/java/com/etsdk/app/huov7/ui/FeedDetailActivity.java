package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.model.FeedInfoModel;
import com.etsdk.app.huov7.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/7/9.
 */

public class FeedDetailActivity extends ImmerseActivity {
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.feed_time)
    TextView feed_time;
    @BindView(R.id.result_time)
    TextView result_time;
    @BindView(R.id.game_tv)
    TextView game_tv;
    @BindView(R.id.msg_tv)
    TextView msg_tv;
    @BindView(R.id.result_tv)
    TextView result_tv;
    FeedInfoModel infoModel;
    SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_detail);
        ButterKnife.bind(this);
        infoModel = (FeedInfoModel) getIntent().getSerializableExtra("model");
        setupUI();
    }

    private void setupUI() {
        tvTitleName.setText("反馈详情");
        feed_time.setText("反馈时间：" + format.format(new Date((Long.valueOf(infoModel.getCreate_time()) * 1000))));
        result_time.setText("处理时间：" + format.format(new Date((Long.valueOf(infoModel.getUpdate_time()) * 1000))));
        if (StringUtils.isEmpty(infoModel.getName())){
            game_tv.setText("反馈游戏：纷享游戏APP");
        }else {
            game_tv.setText("反馈游戏：" + infoModel.getName());
        }

        msg_tv.setText(infoModel.getMsg());
        result_tv.setText(Html.fromHtml(infoModel.getRemark()));
    }

    @OnClick({R.id.iv_titleLeft})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, FeedDetailActivity.class);
        context.startActivity(starter);
    }
}
