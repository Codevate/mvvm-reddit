package com.codevate.mvvmreddit.viewmodel;

import com.codevate.mvvmreddit.client.RedditClient;
import com.codevate.mvvmreddit.deserializer.RedditDateDeserializer;
import com.codevate.mvvmreddit.deserializer.RedditObjectJsonDeserializer;
import com.codevate.mvvmreddit.model.RedditObject;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FeedViewModelTest
{
    @Mock
    RedditClient redditClient;

    private Gson gson;

    @Before
    public void setup() throws Exception
    {
        gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(RedditObject.class, new RedditObjectJsonDeserializer())
            .registerTypeAdapter(Date.class, new RedditDateDeserializer())
            .create();

        when(redditClient.getTop(null, 25)).thenReturn(Observable.just(topPage1()));
        when(redditClient.getTop("t3_4tsqgv", 25)).thenReturn(Observable.just(topPage2()));
    }

    @Test
    public void loadNextPage_loadsPage1() throws Exception
    {
        FeedViewModel viewModel = new FeedViewModel(redditClient);

        TestSubscriber<List<PostViewModel>> testSubscriber = new TestSubscriber<>();
        viewModel.loadMorePosts().subscribe(testSubscriber);
        testSubscriber.assertCompleted();
        testSubscriber.assertValueCount(1);

        verify(redditClient, times(1)).getTop(anyString(), anyInt());
        verify(redditClient).getTop(null, 25);
    }

    @Test
    public void loadNextPage_loadsPage2() throws Exception
    {
        FeedViewModel viewModel = new FeedViewModel(redditClient);

        // Load initial page
        TestSubscriber<List<PostViewModel>> testSubscriber = new TestSubscriber<>();
        viewModel.loadMorePosts().subscribe(testSubscriber);
        testSubscriber.assertCompleted();
        testSubscriber.assertValueCount(1);

        verify(redditClient).getTop(null, 25);
        verify(redditClient, times(1)).getTop(null, 25);

        // Load next page
        testSubscriber = new TestSubscriber<>();
        viewModel.loadMorePosts().subscribe(testSubscriber);
        testSubscriber.assertCompleted();
        testSubscriber.assertValueCount(1);

        verify(redditClient, times(2)).getTop(anyString(), anyInt());
        verify(redditClient, times(1)).getTop("t3_4tsqgv", 25);
        verify(redditClient).getTop("t3_4tsqgv", 25);
    }

    @Test
    public void isLoading_toggled() throws Exception
    {
        FeedViewModel viewModel = new FeedViewModel(redditClient);

        // Initial state - not loading
        TestSubscriber<Boolean> testSubscriber = new TestSubscriber<>();
        viewModel.isLoadingObservable().subscribe(testSubscriber);
        testSubscriber.assertValue(false);

        // Start loading page
        Observable<List<PostViewModel>> postsObservable = viewModel.loadMorePosts();

        testSubscriber = new TestSubscriber<>();
        viewModel.isLoadingObservable().subscribe(testSubscriber);
        testSubscriber.assertValue(true);

        // Finish loading page
        postsObservable.subscribe();

        testSubscriber = new TestSubscriber<>();
        viewModel.isLoadingObservable().subscribe(testSubscriber);
        testSubscriber.assertValue(false);
    }

    private RedditObject topPage1() throws Exception
    {
        return gson.fromJson(IOUtils.toString(getClass().getResourceAsStream("/top.json")), RedditObject.class);
    }

    private RedditObject topPage2() throws Exception
    {
        return gson.fromJson(IOUtils.toString(getClass().getResourceAsStream("/top.json")), RedditObject.class);
    }
}