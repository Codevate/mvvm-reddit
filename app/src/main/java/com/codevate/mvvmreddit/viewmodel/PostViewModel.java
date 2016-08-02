package com.codevate.mvvmreddit.viewmodel;

import com.codevate.mvvmreddit.model.RedditLink;

import org.ocpsoft.prettytime.PrettyTime;

import lombok.Data;

@Data
public class PostViewModel
{
    private String id;
    private String title;
    private String author;
    private String thumbnailUrl;
    private String createdOn;
    private String subreddit;
    private String domain;
    private int numComments;
    private int score;

    public PostViewModel(RedditLink redditLink)
    {
        this.id = redditLink.getId();
        this.title = redditLink.getTitle();
        this.author = redditLink.getAuthor();
        this.thumbnailUrl = redditLink.getThumbnail();
        this.subreddit = redditLink.getSubreddit();
        this.domain = redditLink.getDomain();
        this.numComments = redditLink.getNumComments();
        this.score = redditLink.getScore();

        PrettyTime pt = new PrettyTime();
        this.createdOn = pt.format(redditLink.getCreated());
    }
}
