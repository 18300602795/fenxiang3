package com.etsdk.app.huov7.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.etsdk.app.huov7.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Administrator on 2018/7/4.
 */

public class ServiceListAdapter extends BaseAdapter {
    private String[] titles;
    private String[] conts;

    public ServiceListAdapter(String[] titles, String[] conts) {
        this.titles = titles;
        this.conts = conts;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Object getItem(int position) {
        return titles[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ServiceHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false);
            holder = new ServiceHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ServiceHolder) convertView.getTag();
        }
        holder.title_tv.setText(titles[position]);
        holder.cont_tv.setText(conts[position]);
        return convertView;
    }

    class ServiceHolder {
        @BindView(R.id.cont_tv)
        TextView cont_tv;
        @BindView(R.id.title_tv)
        TextView title_tv;
        Context context;

        ServiceHolder(View view) {
            ButterKnife.bind(this, view);
            context = view.getContext();
        }
    }
}
