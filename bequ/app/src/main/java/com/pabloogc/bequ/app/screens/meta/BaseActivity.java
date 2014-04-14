package com.pabloogc.bequ.app.screens.meta;

import android.app.Activity;
import android.os.Bundle;

import com.pabloogc.bequ.app.application.App;

import dagger.ObjectGraph;

/**
 * Created by Pablo Orgaz - 4/12/14 - pabloogc@gmail.com - https://github.com/pabloogc
 *
 * Base class for all activities to avoid rewriting the injection line.
 */
public abstract class BaseActivity extends Activity {

    private ObjectGraph graph;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        graph = getApplicationGraph().plus(getActivityModules());
    }

    public ObjectGraph getApplicationGraph() {
        return ((App) (getApplication())).getApplicationGraph();
    }

    public ObjectGraph getGraph() {
        return graph;
    }

    public void setGraph(ObjectGraph graph) {
        this.graph = graph;
    }

    protected Object[] getActivityModules() {
        return new Object[0];
    }
}
