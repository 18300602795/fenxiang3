package com.etsdk.app.huov7.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.BackRecordList;
import com.etsdk.app.huov7.model.FeedInfoModel;
import com.etsdk.app.huov7.ui.BackEditActivity;
import com.etsdk.app.huov7.ui.FeedDetailActivity;
import com.etsdk.app.huov7.util.StringUtils;
import com.game.sdk.SdkConstant;
import com.liang530.utils.GlideDisplay;
import com.liang530.views.imageview.roundedimageview.RoundedImageView;
import com.liang530.views.refresh.mvc.IDataAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liu hong liang on 2016/9/26.
 */

public class FeedRecordAadapter extends RecyclerView.Adapter implements IDataAdapter<List<FeedInfoModel>> {
    List<FeedInfoModel> listBeen = new ArrayList<>();
    Context context;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new GameListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed_record, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        if (listBeen.get(position).getApp_id().equals("0")) {
            ((GameListViewHolder) holder).tvGameName.setText("纷享游戏APP");
        } else {
            ((GameListViewHolder) holder).tvGameName.setText(listBeen.get(position).getName());
        }

        switch (listBeen.get(position).getStatus()) {
            case "1":
                ((GameListViewHolder) holder).tvPayType.setText("未处理");
                ((GameListViewHolder) holder).tvPayType.setBackgroundResource(R.drawable.back_state_loading);
                break;
            case "2":
                ((GameListViewHolder) holder).tvPayType.setText("已处理");
                ((GameListViewHolder) holder).tvPayType.setBackgroundResource(R.drawable.back_state_succed);
                ((GameListViewHolder) holder).game_list_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.putExtra("model", listBeen.get(position));
                        intent.setClass(context, FeedDetailActivity.class);
                        context.startActivity(intent);
                    }
                });
                break;
        }
        if (!StringUtils.isEmpty(listBeen.get(position).getIcon())){
            GlideDisplay.display(((GameListViewHolder) holder).iv_game_img, SdkConstant.BASE_URL + listBeen.get(position).getIcon(), R.mipmap.ic_launcher);
        }
//        Glide.with(context).load(listBeen.get(position).getGame_icon()).placeholder(R.mipmap.ic_launcher).into(((GameListViewHolder) holder).iv_game_img);
        ((GameListViewHolder) holder).tv_rate.setText(listBeen.get(position).getMsg());
        ((GameListViewHolder) holder).tvTime.setText("提交时间：" + format.format(new Date((Long.valueOf(listBeen.get(position).getCreate_time()) * 1000))));
    }

    @Override
    public int getItemCount() {
        return listBeen.size();
    }

    @Override
    public void notifyDataChanged(List<FeedInfoModel> listBeen, boolean isRefresh) {
        if (isRefresh) {
            this.listBeen.clear();
        }
        this.listBeen.addAll(listBeen);
        notifyDataSetChanged();
    }

    @Override
    public List<FeedInfoModel> getData() {
        return listBeen;
    }

    @Override
    public boolean isEmpty() {
        return listBeen.isEmpty();
    }

    static class GameListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_game_name)
        TextView tvGameName;
        @BindView(R.id.tv_rate)
        TextView tv_rate;
        @BindView(R.id.back_state)
        TextView tvPayType;
        @BindView(R.id.tv_oneword)
        TextView tvTime;
        @BindView(R.id.iv_game_img)
        RoundedImageView iv_game_img;
        @BindView(R.id.game_list_item)
        RelativeLayout game_list_item;

        GameListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
