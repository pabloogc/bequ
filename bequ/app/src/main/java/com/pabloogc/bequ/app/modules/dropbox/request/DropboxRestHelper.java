package com.pabloogc.bequ.app.modules.dropbox.request;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.RESTUtility;
import com.dropbox.client2.android.AndroidAuthSession;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by Pablo Orgaz - 4/12/14 - pabloogc@gmail.com - https://github.com/pabloogc
 */
public class DropboxRestHelper {

    AndroidAuthSession session;
    DropboxAPI<AndroidAuthSession> api;
    RequestQueue queue;

    public DropboxRestHelper(RequestQueue queue, DropboxAPI<AndroidAuthSession> api) {
        session = api.getSession();
        this.api = api;
        this.queue = queue;
    }


    /**
     * @see com.dropbox.client2.DropboxAPI#metadata(String, int, String, boolean, String)
     */
    public DropboxRequestBuilder<DropboxAPI.Entry>
    metadata(String path, int fileLimit, String hash, boolean list, String rev) {

        String url_path = "/metadata/" + session.getAccessType() + path;
        String url = RESTUtility.buildURL(session.getAPIServer(), 1, url_path, null);

        return new DropboxRequestBuilder<DropboxAPI.Entry>(queue, api.getSession())
                .token(DropboxAPI.Entry.class)
                .method(Request.Method.GET)
                .url(url)
                .query("file_limit", fileLimit > 0 ? fileLimit : 25000)
                .query("hash", hash)
                .query("list", list)
                .query("rev", rev)
                .query("locale", session.getLocale());
    }

    /**
     * @see com.dropbox.client2.DropboxAPI#search(String, String, int, boolean)
     */
    public DropboxRequestBuilder<List<DropboxAPI.Entry>>
    search(String path, String query, int fileLimit, boolean includeDeleted) {

        String url_path = "/search/" + session.getAccessType() + path;
        String url = RESTUtility.buildURL(session.getAPIServer(), 1, url_path, null);


        return new DropboxRequestBuilder<List<DropboxAPI.Entry>>(queue, api.getSession())
                .token(new TypeToken<List<DropboxAPI.Entry>>() {
                })
                .method(Request.Method.GET)
                .url(url)
                .query("query", query)
                .query("file_limit", fileLimit > 0 ? fileLimit : 1000)
                .query("include_deleted", includeDeleted)
                .query("locale", session.getLocale());
    }

    //TODO: Add the all the REST endpoints (not the file ones, they must use another HttpStack)
}
