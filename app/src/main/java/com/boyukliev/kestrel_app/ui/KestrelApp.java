package com.boyukliev.kestrel_app.ui;

import android.app.Application;
import android.content.Context;

import com.boyukliev.kestrel_app.ui.di.ApplicationComponent;
import com.boyukliev.kestrel_app.ui.di.ApplicationModule;
import com.boyukliev.kestrel_app.ui.di.DaggerApplicationComponent;

/**
 * Created by Admin on 3/8/2016.
 */
public class KestrelApp extends Application {

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getComponent() {
        return component;
    }

    public static KestrelApp get(Context context) {
        return (KestrelApp) context.getApplicationContext();
    }
}
