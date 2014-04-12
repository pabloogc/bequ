package com.pabloogc.playa.toolbox;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pabloogc.playa.handlers.ErrorHandler;
import com.pabloogc.playa.models.PlayaRequest;

/**
 * Created by Pablo Orgaz - 10/30/13 - pabloogc@gmail.com - https://github.com/pabloogc
 */
public class GsonRequest<T> extends PlayaRequest<T> {

    private static final Gson DEFAULT_GSON = new Gson();
    private Gson gson = DEFAULT_GSON;
    private TypeToken<T> token;
    private Class<T> clazz;

    public GsonRequest(ErrorHandler errorHandler, TypeToken<T> token) {
        super(errorHandler);
        this.token = token;
    }

    public GsonRequest(ErrorHandler errorHandler, Class<T> clazz) {
        super(errorHandler);
        this.clazz = clazz;
    }

    @Override public T getParsedData(NetworkResponse response) {
        if (clazz != null)
            return gson.fromJson(new String(response.data), clazz);
        else if (token != null)
            return gson.fromJson(new String(response.data), token.getType());

        throw new IllegalStateException("No Class or TypeToken given for Gson");
    }

    @Override public VolleyError generateErrorResponse(NetworkResponse response, T data) {
        return new VolleyError("error parsing data");
    }

    @Override public boolean responseIsOk(NetworkResponse response, T data) {
        return true;
    }

    @Override public byte[] getBody() throws AuthFailureError {
        if (getBodyObject() != null)
            return gson.toJson(getBodyObject()).getBytes();
        return super.getBody();
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }

    public Gson getGson() {
        return gson;
    }
}
