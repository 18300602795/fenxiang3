package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.ui.fragment.MineCardFragment;
import com.etsdk.app.huov7.ui.fragment.MineCouponFragment;
import com.etsdk.app.huov7.ui.fragment.MineGiftFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MineScoreListActivity extends ImmerseActivity {

    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_coupon_list_new);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        String title = getIntent().getStringExtra("title");
        tvTitleName.setText(title);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment, new MineCardFragment()).commit();
    }

    @OnClick({R.id.iv_titleLeft})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
        }
    }

    public static void start(Context context, String title) {
        Intent starter = new Intent(context, MineScoreListActivity.class);
        starter.putExtra("title", title);
        context.startActivity(starter);
    }
}
