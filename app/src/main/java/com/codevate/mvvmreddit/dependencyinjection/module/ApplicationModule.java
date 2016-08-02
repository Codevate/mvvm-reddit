package com.codevate.mvvmreddit.dependencyinjection.module;

import com.codevate.mvvmreddit.MvvmRedditApplication;

import dagger.Module;

@Module
public class ApplicationModule
{
    private MvvmRedditApplication application;

    public ApplicationModule(MvvmRedditApplication application)
    {
        this.application = application;
    }
}
