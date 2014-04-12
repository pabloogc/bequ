package com.pabloogc.playa.handlers;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.lang.ref.WeakReference;


public abstract class ErrorHandler implements Response.ErrorListener {

    private WeakReference<LoadingHandler> loadingHandler = new WeakReference<LoadingHandler>(null);
    private WeakReference<Context> context = new WeakReference<Context>(null);
    private Visitor visitor;

    public Visitor getVisitor() {
        return visitor;
    }

    public void setVisitor(Visitor visitor) {
        this.visitor = visitor;

    }

    public LoadingHandler getLoadingHandler() {
        return loadingHandler.get();
    }

    public void setLoadingHandler(LoadingHandler loadingHandler) {
        this.loadingHandler = new WeakReference<LoadingHandler>(loadingHandler);
    }

    public Context getContext() {
        return context.get();
    }

    public void setContext(Context context) {
        this.context = new WeakReference<Context>(context);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (visitor != null)
            visitor.beforeError(error);
        handleError(error);
        if (visitor != null)
            visitor.afterError(error);

        hideLoadingHandler(getErrorMessage(error));
    }

    public abstract void handleError(VolleyError error);

    public void hideLoadingHandler(String message) {
        LoadingHandler loadingHandler = getLoadingHandler();
        if (loadingHandler != null)
            loadingHandler.hideLoading(message, false);
    }

    public String getErrorMessage(VolleyError error) {
        return null;
    }

    public interface Visitor {
        void beforeError(VolleyError response);

        void afterError(VolleyError response);
    }


}
