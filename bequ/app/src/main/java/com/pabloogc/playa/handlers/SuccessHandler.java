package com.pabloogc.playa.handlers;

import com.android.volley.Response;

import java.lang.ref.WeakReference;

public abstract class SuccessHandler<T> implements Response.Listener<T> {

    private WeakReference<LoadingHandler> loadingHandler = new WeakReference<LoadingHandler>(null);
    private Visitor visitor;

    public Visitor getVisitor() {
        return visitor;
    }

    public void setVisitor(Visitor visitor) {
        this.visitor = visitor;
    }

    @Override public void onResponse(T response) {
        if (visitor != null)
            visitor.beforeResponse(response);
        onSuccess(response);
        if (visitor != null)
            visitor.afterResponse(response);

        hideLoadingHandler(getSuccessMessage(response));
    }

    public abstract void onSuccess(T result);

    public LoadingHandler getLoadingHandler() {
        return loadingHandler.get();
    }

    public void setLoadingHandler(LoadingHandler loadingHandler) {
        this.loadingHandler = new WeakReference<LoadingHandler>(loadingHandler);
    }

    public void hideLoadingHandler(String successMessage) {
        LoadingHandler loadingHandler = getLoadingHandler();
        if (loadingHandler != null)
            loadingHandler.hideLoading(successMessage, true);
    }

    public String getSuccessMessage(T response) {
        return null;
    }

    public interface Visitor {
        void beforeResponse(Object response);

        void afterResponse(Object response);
    }

}
