package com.pabloogc.bequ.app.screens.meta;

import android.app.Fragment;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.pabloogc.bequ.app.modules.dropbox.request.DropboxRestHelper;

import javax.inject.Inject;

import dagger.ObjectGraph;

/**
 * Created by Pablo Orgaz - 4/12/14 - pabloogc@gmail.com - https://github.com/pabloogc
 * <p/>
 * Base class for all activities to avoid rewriting the injection.
 */
public abstract class BaseFragment extends Fragment {

    protected @Inject DropboxAPI<AndroidAuthSession> api;
    protected @Inject RequestQueue queue;
    protected @Inject DropboxRestHelper dropboxRestHelper;

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivityGraph().inject(this);
    }

    protected ObjectGraph getActivityGraph() {
        if (getActivity() == null)
            throw new IllegalStateException("Activity not created yet, there is no graph");
        return ((BaseActivity) getActivity()).getGraph();
    }

    protected DropboxAPI<AndroidAuthSession> getApi() {
        return api;
    }

    protected DropboxRestHelper getApiHelper() {
        return dropboxRestHelper;
    }

    @Override public void onDestroy() {
        super.onDestroy();
        if (queue != null) {
            queue.cancelAll(new RequestQueue.RequestFilter() {
                @Override public boolean apply(Request<?> request) {
                    return true;
                }
            });
        }
    }
}
