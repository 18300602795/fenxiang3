package com.etsdk.app.huov7.provider;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.HomePage1Data;
import com.etsdk.app.huov7.view.GameItemView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

import static android.R.attr.category;

/**
 * Created by liu hong liang on 2016/12/21.
 */
public class NewGameProvider
        extends ItemViewProvider<HomePage1Data.DataBean, NewGameProvider.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_new_game, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final HomePage1Data.DataBean dataBean) {
        holder.game_ll.removeAllViews();
        if (dataBean.getNewgame().getList().size() <= 10) {
            for (int i = 0; i < dataBean.getNewgame().getList().size(); i++) {
                GameItemView gameItemView = new GameItemView(holder.mContext);
                gameItemView.setData(dataBean.getNewgame().getList().get(i));
                holder.game_ll.addView(gameItemView);
            }
//            game_ll.addView(new MoreItemView(mContext));
        } else {
            for (int i = 0; i < 10; i++) {
                GameItemView gameItemView = new GameItemView(holder.mContext);
                gameItemView.setData(dataBean.getNewgame().getList().get(i));
                holder.game_ll.addView(gameItemView);
            }
//            game_ll.addView(new MoreItemView(mContext));
        }
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.game_ll)
        LinearLayout game_ll;
        Context mContext;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
        }
    }
}