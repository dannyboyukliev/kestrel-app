package com.boyukliev.kestrel_app.ui.di;

import com.boyukliev.kestrel_app.ui.ui.activity.MainActivity;
import com.boyukliev.kestrel_app.ui.ui.activity.base.BaseActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Admin on 3/8/2016.
 */
@Singleton
@Component(
        modules = {
                ApplicationModule.class,
                PresentationModule.class
        }
)
public interface ApplicationComponent {
    void inject(MainActivity baseActivity);
}
