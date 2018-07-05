package com.etsdk.app.huov7.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.GameBean;
import com.etsdk.app.huov7.model.GamelikeBean;
import com.etsdk.app.huov7.ui.GameDetailV2Activity;
import com.etsdk.app.huov7.util.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.category;
import static com.etsdk.app.huov7.R.id.discount_tv;

/**
 * 首页猜你喜欢
 * Created by Administrator on 2017/4/27 0027.
 */

public class GamelikeItemAdapter extends RecyclerView.Adapter {
    GamelikeBean gamelikeBean;

    public GamelikeItemAdapter(GamelikeBean gamelikeBean) {
        this.gamelikeBean = gamelikeBean;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game_like_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final GameBean gameBean = gamelikeBean.getGameBeanList().get(position);
        ((ViewHolder)holder).tvName.setText(gameBean.getGamename());
        if (gameBean.getCategory().equals("4")) {
            ((ViewHolder)holder).discount_tv.setVisibility(View.VISIBLE);
            String name;
            if (StringUtils.isEmpty(gameBean.getMem_rate()) || gameBean.getMem_rate().equals("0")) {
                name = "10";
            } else {
                double dis = Double.valueOf(gameBean.getMem_rate()) * 10;
                if (dis >= 10) {
                    name = "10";
                } else {
                    name = dis + "";
                }
            }
            String uname = "折";
            String str = name + uname;
            final SpannableStringBuilder sp = new  SpannableStringBuilder(str);
            sp.setSpan(new AbsoluteSizeSpan(10, true), name.length(), str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //字体大小
            ((ViewHolder)holder).discount_tv.setText(sp);
        } else {
            ((ViewHolder)holder).discount_tv.setVisibility(View.GONE);
        }
//        GlideDisplay.display(((ViewHolder)holder).ivGameIcon, gameBean.getIcon(), R.mipmap.icon_load);
        Glide.with(((ViewHolder)holder).context).load(gameBean.getIcon()).placeholder(R.mipmap.icon_load).into(((ViewHolder)holder).ivGameIcon);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameDetailV2Activity.start(v.getContext(),gameBean.getGameid());
            }
        });
    }

    @Override
    public int getItemCount() {
        return gamelikeBean.getGameBeanList().size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_game_icon)
        ImageView ivGameIcon;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.discount_tv)
        TextView discount_tv;
        Context context;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }
    }

}
