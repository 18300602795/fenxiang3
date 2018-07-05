package com.etsdk.app.huov7.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.etsdk.app.huov7.R;


public class ProgressDialog {
    private Dialog dialog;

    public void show(final Context context, String msg) {
        dismiss();
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_progress, null);
        TextView progress_tv = (TextView) view.findViewById(R.id.progress_tv);
        progress_tv.setText(msg);
        dialog = new Dialog(context, R.style.dialog_bg_style);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
