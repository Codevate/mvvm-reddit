package com.codevate.mvvmreddit.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codevate.mvvmreddit.R;
import com.codevate.mvvmreddit.viewmodel.PostViewModel;
import com.squareup.picasso.Picasso;

import org.apache.commons.validator.routines.UrlValidator;

public class PostViewHolder extends RecyclerView.ViewHolder
{
    private View view;
    private TextView titleTextView;
    private TextView subtitleTextView;
    private TextView subtitle2TextView;
    private ImageView thumbnailImageView;

    public PostViewHolder(View view)
    {
        super(view);

        this.view = view;
        this.titleTextView = (TextView) view.findViewById(R.id.title);
        this.subtitleTextView = (TextView) view.findViewById(R.id.subtitle);
        this.subtitle2TextView = (TextView) view.findViewById(R.id.subtitle2);
        this.thumbnailImageView = (ImageView)view.findViewById(R.id.thumbnail);
    }

    public void bind(PostViewModel viewModel)
    {
        titleTextView.setText(viewModel.getTitle());
        subtitleTextView.setText(String.format("%s - %s - %s (%s)", viewModel.getAuthor(), viewModel.getCreatedOn(), viewModel.getSubreddit(), viewModel.getDomain()));
        subtitle2TextView.setText(String.format("%d points - %d comments", viewModel.getScore(), viewModel.getNumComments()));

        UrlValidator urlValidator = new UrlValidator();
        boolean hasThumbnail = viewModel.getThumbnailUrl() != null && urlValidator.isValid(viewModel.getThumbnailUrl());

        // Show/hide the thumbnail if there is/isn't one
        thumbnailImageView.setVisibility(hasThumbnail ? View.VISIBLE : View.GONE);

        // Load the thumbnail if there is one
        if (hasThumbnail)
        {
            Picasso.with(view.getContext()).load(viewModel.getThumbnailUrl()).into(thumbnailImageView);
        }
    }
}
