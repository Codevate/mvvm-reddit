package com.codevate.mvvmreddit.dependencyinjection.module;

import com.codevate.mvvmreddit.BuildConfig;
import com.codevate.mvvmreddit.client.RedditClient;
import com.codevate.mvvmreddit.model.RedditObject;
import com.codevate.mvvmreddit.deserializer.RedditObjectJsonDeserializer;
import com.codevate.mvvmreddit.deserializer.RedditDateDeserializer;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

@Module
public class ClientModule
{
    @Provides
    public RedditClient provideRedditClient()
    {
        Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(RedditObject.class, new RedditObjectJsonDeserializer())
            .registerTypeAdapter(Date.class, new RedditDateDeserializer())
            .create();

        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG)
        {
            okHttpClientBuilder.addInterceptor(interceptor);
        }
        OkHttpClient client = okHttpClientBuilder.build();

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://reddit.com")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(rxAdapter)
            .build();

        return retrofit.create(RedditClient.class);
    }
}
