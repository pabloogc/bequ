package com.pabloogc.bequ.app.modules.dropbox.request;

import com.android.volley.RequestQueue;
import com.dropbox.client2.android.AndroidAuthSession;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pabloogc.playa.builder.PlayaRequestBuilder;

/**
 * Created by Pablo Orgaz - 4/12/14 - pabloogc@gmail.com - https://github.com/pabloogc
 */
public class DropboxRequestBuilder<T> extends PlayaRequestBuilder<DropboxRequestBuilder<T>, DropboxRequest<T>, T, Object> {

    private TypeToken<T> token;
    private Class<T> clazz;
    private Gson gson;
    private AndroidAuthSession session;

    public DropboxRequestBuilder(RequestQueue requestQueue, AndroidAuthSession session) {
        super(requestQueue);
        this.session = session;
    }

    @Override public void configure(DropboxRequest<T> request) {
        if (gson != null)
            request.setGson(gson);
    }

    @Override public DropboxRequest<T> create() {
        if (clazz != null)
            return new DropboxRequest<T>(errorHandler, clazz);
        else if (token != null)
            return new DropboxRequest<T>(errorHandler, token);
        else
            throw new IllegalStateException("Class or TypeToken not set for request");
    }

    public DropboxRequestBuilder<T> token(TypeToken<T> token) {
        this.token = token;
        this.clazz = null;
        return this;
    }

    public DropboxRequestBuilder<T> token(Class<T> clazz) {
        this.clazz = clazz;
        this.token = null;
        return this;
    }

    public DropboxRequestBuilder<T> gson(Gson gson) {
        this.gson = gson;
        return this;
    }

    public DropboxRequestBuilder<T> session(AndroidAuthSession session) {
        this.session = session;
        return this;
    }

}
