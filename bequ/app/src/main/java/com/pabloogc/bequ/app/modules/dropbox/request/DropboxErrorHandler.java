package com.pabloogc.bequ.app.modules.dropbox.request;

import com.android.volley.NetworkError;
import com.android.volley.VolleyError;
import com.dropbox.client2.exception.DropboxServerException;
import com.dropbox.client2.exception.DropboxUnlinkedException;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.pabloogc.bequ.app.modules.ApplicationModule;
import com.pabloogc.playa.handlers.ErrorHandler;

/**
 * Created by Pablo Orgaz - 4/12/14 - pabloogc@gmail.com - https://github.com/pabloogc
 */
public class DropboxErrorHandler extends ErrorHandler {

    @Override public final void handleError(VolleyError error) {
        if (error.networkResponse != null
                && error.networkResponse.statusCode == DropboxServerException._401_UNAUTHORIZED) {
            //Access revocation is the only error that needs special actions, the others are either
            //user problems or connectivity problems. Also there is no context to handle de event, so
            //simply post it.
            ApplicationModule.getBus().post(new DropboxUnlinkedException());
        }
    }

    @Override public String getErrorMessage(VolleyError error) {
        if (error.networkResponse != null) {
            try {
                JsonElement jsonElement = new JsonParser().parse(new String(error.networkResponse.data));
                return jsonElement.getAsJsonObject().get("error").getAsString();
            } catch (Exception ex) {
                return new String(error.networkResponse.data);
            }
        } else {
            return "Error: " + error.getMessage();
        }
    }
}
