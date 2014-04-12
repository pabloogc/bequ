package com.pabloogc.bequ.app.modules.dropbox.request;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
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
        Log.d("DropboxRequest", String.valueOf(response.data.length));
        return response.statusCode == 200;
    }

    @Override public VolleyError generateErrorResponse(NetworkResponse response, T data) {
        return new VolleyError(response);
    }
}
