package com.codevate.mvvmreddit.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codevate.mvvmreddit.R;
import com.codevate.mvvmreddit.viewmodel.PostViewModel;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostViewHolder>
{
    private List<PostViewModel> items = new ArrayList<>();

    public PostAdapter()
    {
        setHasStableIds(true);
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position)
    {
        holder.bind(getItem(position));
    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }

    @Override
    public long getItemId(int position)
    {
        return getItem(position).getId().hashCode();
    }

    public PostViewModel getItem(int position)
    {
        return items.get(position);
    }

    public void setItems(List<PostViewModel> items)
    {
        if (items == null)
        {
            return;
        }

        this.items = new ArrayList<>(items);
        notifyDataSetChanged();
    }
}
