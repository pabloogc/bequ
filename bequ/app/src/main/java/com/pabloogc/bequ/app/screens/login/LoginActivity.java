package com.pabloogc.bequ.app.screens.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.pabloogc.bequ.app.R;
import com.pabloogc.bequ.app.modules.dropbox.DropboxSessionHelper;
import com.pabloogc.bequ.app.screens.meta.BaseActivity;
import com.pabloogc.bequ.app.screens.meta.RootActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.Icicle;

/**
 * Created by Pablo Orgaz - 4/12/14 - pabloogc@gmail.com - https://github.com/pabloogc
 * <p/>
 * Login activity, simply connect with dropbox provided login activity since OAUTH2.0 login
 * is already implemented there. The login is not launched automatically since launching this app
 * and immediately see dropbox is confusing.
 */
public class LoginActivity extends BaseActivity {

    protected @Inject DropboxAPI<AndroidAuthSession> api;
    protected @Inject DropboxSessionHelper sessionHelper;
    //saving this because low ram devices may kill the activity when dropbox one is launched
    protected @Icicle boolean authProcessStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.login_activity);
        getApplicationGraph().inject(this);
        ButterKnife.inject(this);
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @OnClick(R.id.action_login)
    protected void onLoginClick() {
        authProcessStarted = true;
        api.getSession().startOAuth2Authentication(this);
    }

    @Override protected void onResume() {
        super.onResume();

        if (!authProcessStarted)
            return;

        authProcessStarted = false;
        if (sessionHelper.saveSession()) {
            startActivity(new Intent(this, RootActivity.class));
            finish(); //Root is the new top
        } else {
            Toast.makeText(this, getString(R.string.auth_failure), Toast.LENGTH_LONG).show();
        }

    }
}
