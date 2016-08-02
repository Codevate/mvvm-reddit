package com.codevate.mvvmreddit.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.codevate.mvvmreddit.MvvmRedditApplication;
import com.codevate.mvvmreddit.R;
import com.codevate.mvvmreddit.viewmodel.FeedViewModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

@EActivity(R.layout.activity_feed)
@OptionsMenu(R.menu.feed)
public class FeedActivity extends AppCompatActivity
{
    @ViewById(R.id.post_list)
    RecyclerView postList;

    @OptionsMenuItem(R.id.progress)
    MenuItem loadingMenuItem;

    @Inject
    FeedViewModel viewModel;

    private PostAdapter postAdapter;
    private LinearLayoutManager postListLayoutManager;

    /** Hold active loading observable subscriptions, so that they can be unsubscribed from when the activity is destroyed */
    private CompositeSubscription subscriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        ((MvvmRedditApplication) getApplication()).component().inject(this);

        subscriptions = new CompositeSubscription();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        subscriptions.unsubscribe();
    }

    @AfterViews
    void init()
    {
        initViews();
        initBindings();

        // Initial page load
        loadNextPage();
    }

    private void initViews()
    {
        postListLayoutManager = new LinearLayoutManager(this);
        postList.setLayoutManager(postListLayoutManager);

        postAdapter = new PostAdapter();
        postList.setAdapter(postAdapter);
    }

    private void initBindings()
    {
        // Observable that emits when the RecyclerView is scrolled to the bottom
        Observable<Void> infiniteScrollObservable = Observable.create(subscriber -> {
            postList.addOnScrollListener(new RecyclerView.OnScrollListener()
            {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy)
                {
                    int totalItemCount = postListLayoutManager.getItemCount();
                    int visibleItemCount = postListLayoutManager.getChildCount();
                    int firstVisibleItem = postListLayoutManager.findFirstVisibleItemPosition();

                    if ((visibleItemCount + firstVisibleItem) >= totalItemCount)
                    {
                        subscriber.onNext(null);
                    }
                }
            });
        });

        subscriptions.addAll(
            // Bind list of posts to the RecyclerView
            viewModel.postsObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(postAdapter::setItems),

            // Bind loading status to show/hide loading spinner
            viewModel.isLoadingObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(this::setIsLoading),

            // Trigger next page load when RecyclerView is scrolled to the bottom
            infiniteScrollObservable.subscribe(x -> loadNextPage())
        );
    }

    private void loadNextPage()
    {
        subscriptions.add(
            viewModel.loadMorePosts().subscribe()
        );
    }

    private void setIsLoading(boolean isLoading)
    {
        if (loadingMenuItem != null)
        {
            loadingMenuItem.setVisible(isLoading);
        }
    }
}
