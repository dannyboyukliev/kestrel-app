package com.boyukliev.kestrel_app.ui.di;

import com.boyukliev.kestrel_app.ui.KestrelApp;
import com.boyukliev.kestrel_app.ui.bluetooth.BluetoothConnection;
import com.boyukliev.kestrel_app.ui.bluetooth.BluetoothConnectionImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Admin on 3/8/2016.
 */
@Module
public class ApplicationModule {

    private KestrelApp app;

    public ApplicationModule(KestrelApp app) {
        this.app = app;
    }

    @Singleton
    @Provides
    public BluetoothConnection provideBluetoothConnection() {
        return new BluetoothConnectionImpl(app);
    }
}
