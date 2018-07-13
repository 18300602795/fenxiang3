package com.etsdk.app.huov7.provider;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.ApplyGoodsRequestBean;
import com.etsdk.app.huov7.model.GiftCard;
import com.etsdk.app.huov7.model.Goods;
import com.etsdk.app.huov7.ui.EntityDetailActivity;
import com.etsdk.app.huov7.ui.dialog.EntityExchangeDialogUtil;
import com.game.sdk.http.HttpCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.util.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.liang530.utils.GlideDisplay;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2017/1/12.
 */
public class GiftCardViewProvider
        extends ItemViewProvider<GiftCard, GiftCardViewProvider.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.adapter_gift_card, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final GiftCard goods) {
        holder.tvExchangeScore.setText(goods.getIntegral() + "积分");
        holder.tvMoney.setText(goods.getMarket_price());
        holder.tvMoneyText.setText(goods.getMarket_price());
        GlideDisplay.dispalyWithFitCenterDef(holder.ivGiftCard, goods.getOriginal_img(), R.mipmap.ic_launcher);
        int total = goods.getTotal();
        int remain = goods.getRemain();
        int progress = 100;
        if (total != 0) {
            progress = (int) (remain * 100. / total);
        }
        holder.pbProgressBar.setProgress(progress);
        holder.tvProgress.setText(progress + "%");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                EntityDetailActivity.start(v.getContext(), GsonUtil.getGson().toJson(goods));
                applyCoupon(goods, holder);
            }
        });
        holder.btnExchange.setClickable(false);
        if (isViewTypeStart(goods)) {
            holder.vLine.setVisibility(View.GONE);
        } else {
            holder.vLine.setVisibility(View.VISIBLE);
        }
    }

    public void applyCoupon(final GiftCard goods, final ViewHolder holder) {
        final ApplyGoodsRequestBean applyGoodsRequestBean = new ApplyGoodsRequestBean();
        applyGoodsRequestBean.setGoodsid(goods.getGoodsid());
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(applyGoodsRequestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<Goods>(holder.context, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(Goods data) {
                if (data != null) {
                    new EntityExchangeDialogUtil().showExchangeDialog(holder.context, "兑换成功");
//                    if ("1".equals(goods.getIs_real())) {
//                        new EntityExchangeDialogUtil().showExchangeDialog(holder.context, "兑换成功，请在我的已领取礼品卡中查看卡密！");
//                    } else {
//                        new EntityExchangeDialogUtil().showExchangeDialog(holder.context, "兑换成功，请在我的已领取实物中查看！");
//                    }
                }
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(true);
        RxVolley.post(AppApi.getUrl(AppApi.userGoodsAddApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_gift_card)
        ImageView ivGiftCard;
        @BindView(R.id.tv_money_text)
        TextView tvMoneyText;
        @BindView(R.id.tv_money)
        TextView tvMoney;
        @BindView(R.id.tv_exchangeScore)
        TextView tvExchangeScore;
        @BindView(R.id.btn_exchange)
        Button btnExchange;
        @BindView(R.id.tv_progress)
        TextView tvProgress;
        @BindView(R.id.pb_progressBar)
        ProgressBar pbProgressBar;
        @BindView(R.id.game_list_item)
        LinearLayout gameListItem;
        @BindView(R.id.v_line)
        View vLine;
        Context context;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }
    }
}