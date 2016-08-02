package com.codevate.mvvmreddit.viewmodel;

import com.codevate.mvvmreddit.client.RedditClient;
import com.codevate.mvvmreddit.model.RedditLink;
import com.codevate.mvvmreddit.model.RedditListing;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.BehaviorSubject;

public class FeedViewModel
{
    private RedditClient redditClient;

    private int pageLimit;
    private String afterToken;
    private BehaviorSubject<List<PostViewModel>> postSubject = BehaviorSubject.create(new ArrayList<>());
    private BehaviorSubject<Boolean> isLoadingSubject = BehaviorSubject.create(false);

    @Inject
    public FeedViewModel(RedditClient redditClient)
    {
        this.redditClient = redditClient;
        this.pageLimit = 25;
    }

    public Observable<List<PostViewModel>> loadMorePosts()
    {
        // Don't try and load if we're already loading
        if (isLoadingSubject.getValue())
        {
            return Observable.empty();
        }

        isLoadingSubject.onNext(true);

        return redditClient
            .getTop(afterToken, pageLimit)
            // Safe to cast to RedditListing, as this is always returned from top posts
            .cast(RedditListing.class)
            // Store the after token, so we can use it to get the next page of posts is a subsequent load
            .doOnNext(listing -> afterToken = listing.getAfter())
            // Flatten into observable of RedditLinks
            .map(RedditListing::getChildren)
            .flatMapIterable(list -> list)
            .filter(object -> object instanceof RedditLink)
            // Transform model to viewmodel
            .map(link -> new PostViewModel((RedditLink) link))
            // Merge viewmodels into a single list to be emitted
            .toList()
            // Concatenate the new posts to the current posts list, then emit it via the post subject
            .doOnNext(list -> {
                List<PostViewModel> fullList = new ArrayList<>(postSubject.getValue());
                fullList.addAll(list);
                postSubject.onNext(fullList);
            })
            .doOnTerminate(() -> isLoadingSubject.onNext(false));
    }

    public Observable<List<PostViewModel>> postsObservable()
    {
        return postSubject.asObservable();
    }

    public Observable<Boolean> isLoadingObservable()
    {
        return isLoadingSubject.asObservable();
    }
}
