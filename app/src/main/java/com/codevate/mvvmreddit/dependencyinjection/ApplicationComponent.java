package com.codevate.mvvmreddit.dependencyinjection;

import com.codevate.mvvmreddit.dependencyinjection.module.ApplicationModule;
import com.codevate.mvvmreddit.dependencyinjection.module.ClientModule;
import com.codevate.mvvmreddit.view.FeedActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, ClientModule.class})
public interface ApplicationComponent
{
    void inject(FeedActivity activity);
}
