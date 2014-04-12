package com.pabloogc.bequ.app.screens.meta;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxUnlinkedException;
import com.pabloogc.bequ.app.R;
import com.pabloogc.bequ.app.modules.ApplicationModule;
import com.pabloogc.bequ.app.modules.dropbox.DropboxSessionHelper;
import com.pabloogc.bequ.app.screens.home.HomeFragment;
import com.pabloogc.bequ.app.screens.login.LoginActivity;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import butterknife.ButterKnife;
import icepick.Icepick;

/**
 * Created by Pablo Orgaz - 4/12/14 - pabloogc@gmail.com - https://github.com/pabloogc
 */
public class RootActivity extends BaseActivity {

    protected @Inject DropboxAPI<AndroidAuthSession> api;
    protected @Inject DropboxSessionHelper sessionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.root_activity);

        Icepick.restoreInstanceState(this, savedInstanceState);
        getGraph().inject(this);
        ButterKnife.inject(this);

        if (!sessionHelper.restoreSession()) {
            gotoLogin();
        } else if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new HomeFragment())
                    .commit();
        }

    }

    @Override protected void onStart() {
        super.onStart();
        ApplicationModule.getBus().register(this);
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override protected void onStop() {
        super.onStop();
        ApplicationModule.getBus().unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                gotoLogin();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Subscribe public void onAuthError(DropboxException event) {
        if (event instanceof DropboxUnlinkedException) {
            sessionHelper.clearSession();
            gotoLogin();
        }
    }

    private void gotoLogin() {
        sessionHelper.clearSession();
        startActivity(new Intent(this, LoginActivity.class));
        finish(); //LoginActivity is the new top
    }

}
