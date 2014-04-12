package com.pabloogc.bequ.app.modules;

import android.app.Application;

import com.pabloogc.bequ.app.application.App;
import com.pabloogc.bequ.app.modules.dropbox.DropboxModule;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Pablo Orgaz - 4/12/14 - pabloogc@gmail.com - https://github.com/pabloogc
 */
@Module(
        library = true,
        includes = {
                DropboxModule.class,
        }
)
public class ApplicationModule {

    private static final Bus bus = new Bus();
    private final App app;

    public ApplicationModule(App app) {
        this.app = app;
    }

    /**
     * Utility method to avoid creating a context independent module
     */
    public static Bus getBus() {
        return bus;
    }

    @Provides
    @Singleton public Application provideApplication() {
        return app;
    }

    @Provides
    public Bus provideBus() {
        return getBus();
    }

}
