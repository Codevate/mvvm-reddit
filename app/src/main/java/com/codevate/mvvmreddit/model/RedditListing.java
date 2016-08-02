package com.codevate.mvvmreddit.model;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RedditListing extends RedditObject
{
    private List<RedditObject> children;
    private String before;
    private String after;
}
