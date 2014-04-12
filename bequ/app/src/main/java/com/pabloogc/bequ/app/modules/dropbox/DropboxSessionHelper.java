package com.pabloogc.bequ.app.modules.dropbox;

import android.content.Context;
import android.content.SharedPreferences;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;

import javax.inject.Singleton;

/**
 * Created by Pablo Orgaz - 4/12/14 - pabloogc@gmail.com - https://github.com/pabloogc
 */
@Singleton
public class DropboxSessionHelper {

    private static final String PREF_FILE = "prefs";
    private static final String TOKEN_KEY = "token";
    private final DropboxAPI<AndroidAuthSession> api;
    private final SharedPreferences preferences;

    DropboxSessionHelper(Context context, DropboxAPI<AndroidAuthSession> api) {
        this.api = api;
        preferences = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
    }

    private void saveToken(AndroidAuthSession session) {
        preferences.edit().putString(TOKEN_KEY, session.getOAuth2AccessToken()).apply();
    }

    public boolean restoreSession() {
        if (!isSessionStored()) return false;
        api.getSession().setOAuth2AccessToken(preferences.getString(TOKEN_KEY, null));
        return true;
    }

    private boolean isSessionStored() {
        return preferences.getString(TOKEN_KEY, null) != null;
    }


    public boolean saveSession() {
        if (api.getSession().authenticationSuccessful()) {
            api.getSession().finishAuthentication();
            saveToken(api.getSession());
            return true;
        } else {
            clearSession();
            return false;
        }
    }

    public void clearSession() {
        preferences.edit().remove(TOKEN_KEY).apply();
    }


}
