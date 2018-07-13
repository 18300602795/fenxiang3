package com.etsdk.app.huov7.provider;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.etsdk.app.huov7.model.GameBean;
import com.etsdk.app.huov7.model.TryGameBean;
import com.etsdk.app.huov7.view.NewListGameItem;
import com.etsdk.app.huov7.view.NewListTryItem;

import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2016/12/21.
 */
public class TryItemViewProvider
        extends ItemViewProvider<TryGameBean, TryItemViewProvider.ViewHolder> {
    boolean showRank;
    public TryItemViewProvider(boolean showRank) {
        this.showRank = showRank;
    }
    public TryItemViewProvider() {
    }


    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(new NewListTryItem(parent.getContext()));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull TryGameBean gameBean) {
        holder.listGameItem.showLine(true);
        holder.listGameItem.setGameBean(gameBean);
        if(showRank){
            holder.listGameItem.setIsHotRank(showRank,holder.getAdapterPosition());
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        NewListTryItem listGameItem;
        ViewHolder(NewListTryItem itemView) {
            super(itemView);
            listGameItem=itemView;
        }
    }
}