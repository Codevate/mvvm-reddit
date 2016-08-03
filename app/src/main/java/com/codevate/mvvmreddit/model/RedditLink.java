package com.codevate.mvvmreddit.model;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class RedditLink implements RedditObject
{
    private String id;
    private String title;
    private String domain;
    private String subreddit;
    private String subredditId;
    private String linkFlairText;
    private String author;
    private String thumbnail;
    private String permalink;
    private String url;
    private int score;
    private int ups;
    private int downs;
    private int numComments;
    private boolean over18;
    private boolean hideScore;
    private Date created;
    private Date createdUtc;
}
