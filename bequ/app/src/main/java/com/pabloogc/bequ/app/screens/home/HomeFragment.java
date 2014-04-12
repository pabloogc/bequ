package com.pabloogc.bequ.app.screens.home;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pabloogc.bequ.app.modules.ApplicationModule;
import com.pabloogc.bequ.app.screens.meta.BaseFragment;
import com.pabloogc.playa.handlers.ErrorHandler;
import com.pabloogc.playa.handlers.SuccessHandler;

import java.util.List;

/**
 * Created by Pablo Orgaz - 4/12/14 - pabloogc@gmail.com - https://github.com/pabloogc
 */
public class HomeFragment extends BaseFragment {

    TextView text;

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ScrollView scrollView = new ScrollView(inflater.getContext());
        text = new TextView(inflater.getContext());
        text.setText("Loading . . .");
        //TODO: Use the helper
        scrollView.addView(text);
        return scrollView;
    }
}
