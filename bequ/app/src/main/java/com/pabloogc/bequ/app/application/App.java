package com.pabloogc.bequ.app.application;

import android.app.Application;

import com.pabloogc.bequ.app.modules.ApplicationModule;

import dagger.ObjectGraph;

/**
 * Created by Pablo Orgaz - 4/12/14 - pabloogc@gmail.com - https://github.com/pabloogc
 */
public class App extends Application {

    ObjectGraph applicationGraph;

    @Override public void onCreate() {
        super.onCreate();
        applicationGraph = ObjectGraph.create(
                new ApplicationModule(this));


    }

    public ObjectGraph getApplicationGraph() {
        return applicationGraph;
    }
}
