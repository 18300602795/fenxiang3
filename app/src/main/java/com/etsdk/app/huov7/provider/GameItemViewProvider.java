package com.etsdk.app.huov7.provider;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.etsdk.app.huov7.model.GameBean;
import com.etsdk.app.huov7.view.NewListGameItem;

import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2016/12/21.
 */
public class GameItemViewProvider
        extends ItemViewProvider<GameBean, GameItemViewProvider.ViewHolder> {
    boolean showRank;
    int category;
    public GameItemViewProvider(boolean showRank) {
        this.showRank = showRank;
        category = 0;
    }

    public GameItemViewProvider(boolean showRank, int category) {
        this.showRank = showRank;
        this.category = category;
    }

    public GameItemViewProvider(int category) {
        this.category = category;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(new NewListGameItem(parent.getContext()));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull GameBean gameBean) {
        holder.listGameItem.showLine(true);
        holder.listGameItem.setGameBean(gameBean, category);
        if(showRank){
            holder.listGameItem.setIsHotRank(showRank,holder.getAdapterPosition());
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        NewListGameItem listGameItem;
        ViewHolder(NewListGameItem itemView) {
            super(itemView);
            listGameItem=itemView;
        }
    }
}