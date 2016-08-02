package com.codevate.mvvmreddit.model;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RedditComment extends RedditObject
{
    private String id;
    private String body;
    private String bodyHtml;
    private String author;
    private String subredditId;
    private String linkId;
    private String parentId;
    private String score;
    private String ups;
    private String downs;
    private Date created;
    private Date createdUtc;
}
