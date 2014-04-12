package com.pabloogc.bequ.app.modules.dropbox.request;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.google.gson.reflect.TypeToken;
import com.pabloogc.playa.handlers.ErrorHandler;
import com.pabloogc.playa.toolbox.GsonRequest;

/**
 * Created by Pablo Orgaz - 4/12/14 - pabloogc@gmail.com - https://github.com/pabloogc
 */
public class DropboxRequest<T> extends GsonRequest<T> {

    public DropboxRequest(ErrorHandler errorHandler, TypeToken<T> token) {
        super(errorHandler, token);
    }

    public DropboxRequest(ErrorHandler errorHandler, Class<T> clazz) {
        super(errorHandler, clazz);
    }

    @Override public boolean responseIsOk(NetworkResponse response, T data) {
        Log.d("DropboxRequest", "Completed requests, status: " + (response != null ? response.statusCode : -1));
        return response != null;
    }
}
