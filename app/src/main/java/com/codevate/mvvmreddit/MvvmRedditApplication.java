package com.codevate.mvvmreddit;

import android.app.Application;

import com.codevate.mvvmreddit.dependencyinjection.ApplicationComponent;
import com.codevate.mvvmreddit.dependencyinjection.DaggerApplicationComponent;
import com.codevate.mvvmreddit.dependencyinjection.module.ApplicationModule;

import org.androidannotations.annotations.EApplication;

@EApplication
public class MvvmRedditApplication extends Application
{
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate()
    {
        super.onCreate();

        applicationComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(new ApplicationModule(this))
            .build();
    }

    public ApplicationComponent component()
    {
        return applicationComponent;
    }
}
