package com.pabloogc.playa.handlers.impl;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
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

    public DialogLoadingHandler(Context context) {
        this.context = context;
    }

    public Request<?>[] getRequest() {
        return request;
    }

    public void setRequest(Request<?>... request) {
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
            dialog.cancel();
            dialog = null;
        }
        dialog = ProgressDialog.show(context, null, message != null && !message.isEmpty() ? message : null, true, true);
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.setContentView(new ProgressBar(context));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override public void onDismiss(DialogInterface dialog) {
                for (Request<?> r : request) {
                    Log.d("DialogLoadingHandler", "cancelling requests");
                    if (r != null)
                        r.cancel();
                }
            }
        });
    }

    @Override public void hideLoading(String message, boolean success) {
        if (dialog != null && dialog.isShowing())
            dialog.cancel();
        if (message != null && !message.isEmpty())
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public ProgressDialog getDialog() {
        return dialog;
    }
}
