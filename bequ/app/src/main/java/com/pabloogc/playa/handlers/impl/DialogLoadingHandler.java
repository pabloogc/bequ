package com.pabloogc.playa.handlers.impl;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.pabloogc.playa.handlers.LoadingHandler;

/**
 * Created by pablo on 10/15/13.
 * welvi-android
 */
public class DialogLoadingHandler implements LoadingHandler {

    private Context context;
    private Request<?>[] request;
    private ProgressDialog dialog;

    public DialogLoadingHandler(Context context, Request<?>... request) {
        this.context = context;
        this.request = request;
    }

    public Request<?>[] getRequest() {
        return request;
    }

    public void setRequest(Request<?>[] request) {
        this.request = request;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override public void showLoading(String message) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
        dialog = ProgressDialog.show(context, null, message != null && !message.isEmpty() ? message : null);
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.setContentView(new ProgressBar(context));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override public void onCancel(DialogInterface dialog) {
                for (int i = 0; i < request.length; i++) {
                    if (request[i] != null)
                        request[i].cancel();
                }
            }
        });
    }

    @Override public void hideLoading(String message, boolean success) {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
        if (message != null && !message.isEmpty())
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
