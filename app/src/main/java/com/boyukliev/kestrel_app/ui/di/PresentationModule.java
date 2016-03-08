package com.boyukliev.kestrel_app.ui.di;

import com.boyukliev.kestrel_app.ui.bluetooth.BluetoothConnection;
import com.boyukliev.kestrel_app.ui.presenter.MainPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Admin on 3/8/2016.
 */
@Module
public class PresentationModule {
    public PresentationModule() {}

    @Provides
    public MainPresenter provideMainPresenter(BluetoothConnection bluetoothConnection) {
        return new MainPresenter(bluetoothConnection);
    }
}
