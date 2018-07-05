package com.etsdk.app.huov7.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.ServiceListAdapter;
import com.etsdk.app.huov7.base.ImmerseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/7/4.
 */

public class ServiceListActivity extends ImmerseActivity {
    @BindView(R.id.tv_titleName)
    TextView tvTitleName;
    @BindView(R.id.service_list)
    ListView service_list;
    ServiceListAdapter listAdapter;
    int title;
    int item;
    String[] titles;
    String[] heads;
    String[] conts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_list);
        ButterKnife.bind(this);
        title = getIntent().getIntExtra("title", 0);
        item = getIntent().getIntExtra("item", 0);
        setUp();
        tvTitleName.setText(titles[title]);
        listAdapter = new ServiceListAdapter(heads, conts);
        service_list.setAdapter(listAdapter);
        service_list.setSelection(item);
    }

    private void setUp() {
        titles = getResources().getStringArray(R.array.service_title);
        switch (title) {
            case 0:
                heads = getResources().getStringArray(R.array.service_item1);
                conts = getResources().getStringArray(R.array.service_cont1);
                break;
            case 1:
                heads = getResources().getStringArray(R.array.service_item2);
                conts = getResources().getStringArray(R.array.service_cont2);
                break;
            case 2:
                heads = getResources().getStringArray(R.array.service_item3);
                conts = getResources().getStringArray(R.array.service_cont3);
                break;
            case 3:
                heads = getResources().getStringArray(R.array.service_item4);
                conts = getResources().getStringArray(R.array.service_cont4);
                break;
            default:
                heads = getResources().getStringArray(R.array.service_item1);
                conts = getResources().getStringArray(R.array.service_cont1);
                break;
        }
    }

    @OnClick({R.id.iv_titleLeft})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
        }
    }


    public static void start(Context context, int title, int item) {
        Intent starter = new Intent(context, ServiceListActivity.class);
        starter.putExtra("title", title);
        starter.putExtra("item", item);
        context.startActivity(starter);
    }
}
