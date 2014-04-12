package com.pabloogc.bequ.app.modules.dropbox;

import android.app.Application;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.Volley;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.RESTUtility;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;
import com.pabloogc.bequ.app.screens.home.HomeFragment;
import com.pabloogc.bequ.app.screens.login.LoginActivity;
import com.pabloogc.bequ.app.screens.meta.RootActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.IOException;
import java.util.Map;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Pablo Orgaz - 4/12/14 - pabloogc@gmail.com - https://github.com/pabloogc
 */
@Module(
        library = true,
        complete = false,
        injects = {
                RootActivity.class,
                LoginActivity.class,
                HomeFragment.class
        }
)
public class DropboxModule {

    private static final String APP_KEY = "6uwjcby9xuaqy4g";
    private static final String APP_SECRET = "rjz8iki7plvvibi";

    @Provides
    @Singleton
    public DropboxAPI<AndroidAuthSession> provideApiAccess() {
        AppKeyPair pair = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(pair);

        return new DropboxAPI<AndroidAuthSession>(session);
    }

    @Provides
    @Singleton
    public DropboxSessionHelper provideSessionHelper(Application context, DropboxAPI<AndroidAuthSession> api) {
        return new DropboxSessionHelper(context, api);
    }

    @Provides @Singleton
    public RequestQueue providesRequestQueue(Application context, final DropboxAPI<AndroidAuthSession> api) {
        HttpStack stack = new DropboxHttpStackWrapper(api);
        return Volley.newRequestQueue(context, stack);
    }

    /**
     * Wrapper for HttpStack that uses the authorized client provided by the Dropbox API.
     */
    private static class DropboxHttpStackWrapper implements HttpStack {
        DropboxAPI<AndroidAuthSession> api;

        private DropboxHttpStackWrapper(DropboxAPI<AndroidAuthSession> api) {
            this.api = api;
        }

        @Override
        public HttpResponse performRequest(Request<?> request, Map<String, String> additionalHeaders) throws IOException, AuthFailureError {
            final HttpClient client = RESTUtility.updatedHttpClient(api.getSession());
            HttpClientStack stack = new HttpClientStack(client) {
                @Override
                protected void onPrepareRequest(HttpUriRequest request) throws IOException {
                    api.getSession().sign(request);
                }
            };
            return stack.performRequest(request, additionalHeaders);
        }

    }
}
