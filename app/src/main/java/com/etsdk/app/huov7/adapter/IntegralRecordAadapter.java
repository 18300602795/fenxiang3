package com.etsdk.app.huov7.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.ChargeRecordListBean;
import com.etsdk.app.huov7.model.IntegralList;
import com.etsdk.app.huov7.model.IntegralModel;
import com.etsdk.app.huov7.ui.fragment.ChargeRecordFragment;
import com.liang530.views.refresh.mvc.IDataAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.type;

/**
 * Created by liu hong liang on 2016/9/26.
 */

public class IntegralRecordAadapter extends RecyclerView.Adapter implements IDataAdapter<List<IntegralModel>> {
    List<IntegralModel> listBeen = new ArrayList<>();
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GameListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_integral_record, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ((GameListViewHolder) holder).tvCostYuan.setText(listBeen.get(position).getGive_integral() + "");
        ((GameListViewHolder) holder).tvPayType.setText(listBeen.get(position).getName());
        ((GameListViewHolder) holder).tvTime.setText(format.format(new Date((Long.valueOf(listBeen.get(position).getCreate_time())) * 1000)));
    }

    @Override
    public int getItemCount() {
        return listBeen.size();
    }

    @Override
    public void notifyDataChanged(List<IntegralModel> listBeen, boolean isRefresh) {
        if (isRefresh) {
            this.listBeen.clear();
        }
        this.listBeen.addAll(listBeen);
        notifyDataSetChanged();
    }

    @Override
    public List<IntegralModel> getData() {
        return listBeen;
    }

    @Override
    public boolean isEmpty() {
        return listBeen.isEmpty();
    }

    static class GameListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_cost_yuan)
        TextView tvCostYuan;
        @BindView(R.id.tv_pay_type)
        TextView tvPayType;
        @BindView(R.id.tv_time)
        TextView tvTime;
        GameListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
