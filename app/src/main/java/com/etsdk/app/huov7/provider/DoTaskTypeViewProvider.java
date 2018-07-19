package com.etsdk.app.huov7.provider;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.DoTaskType;
import com.etsdk.app.huov7.ui.EarnActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2017/2/8.
 */
public class DoTaskTypeViewProvider
        extends ItemViewProvider<DoTaskType, DoTaskTypeViewProvider.ViewHolder> {
    private String[] titleNames={"","推广员任务","系统任务","充值任务"};


    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_do_task_type, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull DoTaskType doTaskType) {
        if(TextUtils.isDigitsOnly(doTaskType.getTypeid())){
            holder.tvTitle.setText(titleNames[Integer.valueOf(doTaskType.getTypeid())]);
        }
        holder.task_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EarnActivity.start(holder.context);
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.task_ll)
        LinearLayout task_ll;
        Context context;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            context = itemView.getContext();
        }
    }
}