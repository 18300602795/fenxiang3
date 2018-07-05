package com.etsdk.app.huov7.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.game.sdk.log.T;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liu hong liang on 2017/3/1.
 */

public class ServiceQqAdapter extends BaseAdapter {
    private List<String> qqList = new ArrayList<>();

    public void setQqList(String[] qqList) {
        for (String qq : qqList) {
            this.qqList.add(qq);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return qqList == null ? 0 : qqList.size();
    }

    @Override
    public Object getItem(int position) {
        return qqList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_item_qq, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvQqGroupHint.setText(new StringBuffer("客服").append((position + 1)).append("号"));
        viewHolder.qqGroupTV.setText(qqList.get(position));
        viewHolder.qqGroupStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQq(v.getContext(), qqList.get(position));
            }
        });
        return convertView;
    }

    public static void openQq(Context context, String qq) {
        try {
            String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + qq;
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (Exception e) {
            e.printStackTrace();
            T.s(context, "请先安装最新版qq");
        }
    }

    static class ViewHolder {
        @BindView(R.id.tv_qq_group_hint)
        TextView tvQqGroupHint;
        @BindView(R.id.qqGroupTV)
        TextView qqGroupTV;
        @BindView(R.id.qqGroupStatus)
        TextView qqGroupStatus;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
