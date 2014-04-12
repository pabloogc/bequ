package com.pabloogc.bequ.app.screens.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.dropbox.client2.DropboxAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pabloogc.bequ.app.screens.meta.BaseFragment;
import com.pabloogc.bequ.app.util.Time;
import com.pabloogc.playa.handlers.SuccessHandler;
import com.pabloogc.playa.handlers.impl.DialogLoadingHandler;

import java.util.List;

/**
 * Created by Pablo Orgaz - 4/12/14 - pabloogc@gmail.com - https://github.com/pabloogc
 */
public class HomeFragment extends BaseFragment {

    protected TextView text;
    protected DialogLoadingHandler loadingHandler;

    @Override public void onDetach() {
        if (loadingHandler != null && loadingHandler.getDialog() != null)
            loadingHandler.getDialog().cancel();
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ScrollView scrollView = new ScrollView(inflater.getContext());
        text = new TextView(inflater.getContext());
        text.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                doSearch();
            }
        });
        text.setText("Loading . . . \n\n\n\n\n\n\n\n . . .");
        //TODO: Use the helper
        scrollView.addView(text);
        return scrollView;
    }

    public void doSearch() {
        if (loadingHandler == null)
            loadingHandler = new DialogLoadingHandler(getActivity());

        Request<?> request = getApiHelper().search("/", ".pdf", 0, false)
                .success(new SuccessHandler<List<DropboxAPI.Entry>>() {
                    @Override public void onSuccess(List<DropboxAPI.Entry> result) {
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        text.setText(gson.toJson(result));
                    }

                    @Override public String getSuccessMessage(List<DropboxAPI.Entry> response) {
                        return String.format("Found %d", response.size());
                    }
                })
                .loading(loadingHandler)
                .cacheTime(Time.seconds(30), Time.seconds(60))
                .execute();

        loadingHandler.setRequest(request);
    }
}
