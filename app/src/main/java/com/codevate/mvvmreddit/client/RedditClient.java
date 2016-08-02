package com.codevate.mvvmreddit.client;

import com.codevate.mvvmreddit.model.RedditObject;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface RedditClient
{
    @GET("/top.json")
    Observable<RedditObject> getTop(@Query("after") String after, @Query("limit") int limit);
}
