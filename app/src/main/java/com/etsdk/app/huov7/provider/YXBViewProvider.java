package com.etsdk.app.huov7.provider;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.YXBBean;
import com.etsdk.app.huov7.ui.GameDetailV2Activity;
import com.game.sdk.SdkConstant;
import com.liang530.views.imageview.roundedimageview.RoundedImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2016/12/21.
 */
public class YXBViewProvider
        extends ItemViewProvider<YXBBean, YXBViewProvider.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_game_yxb_item, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final YXBBean yxbBean) {
        if (yxbBean == null) {
            return;
        }
        Glide.with(holder.context).load(SdkConstant.BASE_URL + yxbBean.getIcon()).placeholder(R.mipmap.icon_load).into(holder.ivGameImg);
        holder.tvGameName.setText(yxbBean.getName());
        holder.tvOneword.setText("游戏币：" + yxbBean.getRemain());
        holder.gameListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameDetailV2Activity.start(holder.context, yxbBean.getApp_id());
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.game_list_item)
        RelativeLayout gameListItem;
        @BindView(R.id.iv_game_img)
        RoundedImageView ivGameImg;
        @BindView(R.id.tv_game_name)
        TextView tvGameName;
        @BindView(R.id.tv_oneword)
        TextView tvOneword;
        Context context;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }
    }
}