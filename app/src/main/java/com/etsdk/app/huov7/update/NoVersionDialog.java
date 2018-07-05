package com.etsdk.app.huov7.update;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.etsdk.app.huov7.R;
import com.liang530.utils.BaseAppUtil;


public class NoVersionDialog {
    private Dialog updateDialog;

    public void showDialog(Context context) {
        dismiss();

        View dialogview = LayoutInflater.from(context).inflate(R.layout.dialog_no_version, null);
        updateDialog = new Dialog(context, R.style.dialog_bg_style);
        //设置view
        updateDialog.setContentView(dialogview);
        updateDialog.setCanceledOnTouchOutside(false);
        //dialog默认是环绕内容的
        //通过window来设置位置、高宽
        Window window = updateDialog.getWindow();
        WindowManager.LayoutParams windowparams = window.getAttributes();
////        window.setGravity(Gravity.BOTTOM);
//        windowparams.height = ;
        windowparams.width = BaseAppUtil.getDeviceWidth(context) - 2 * BaseAppUtil.dip2px(context, 16);
//        设置背景透明,但是那个标题头还是在的，只是看不见了
        //注意：不设置背景，就不能全屏
//        window.setBackgroundDrawableResource(android.R.color.transparent);

        TextView btok = (TextView) dialogview.findViewById(R.id.confirm_tv);
        btok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        updateDialog.show();
    }

    public void dismiss() {
        if (updateDialog != null) {
            updateDialog.dismiss();
        }
    }
}
