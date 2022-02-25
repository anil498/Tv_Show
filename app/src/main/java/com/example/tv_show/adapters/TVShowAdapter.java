package com.example.tv_show.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tv_show.Listners.TVShowListner;
import com.example.tv_show.R;
import com.example.tv_show.databinding.ItemContainerTvShowBinding;
import com.example.tv_show.models.TVShow;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TVShowAdapter extends RecyclerView.Adapter<TVShowAdapter.TVShowViewHolder> {
    private List<TVShow> tvShows;
    private LayoutInflater layoutInflater;
    private TVShowListner tvShowListner;

    public TVShowAdapter(List<TVShow> tvShows,TVShowListner tvshowListner)
    {
        this.tvShows = tvShows;
        this.tvShowListner=tvshowListner;
    }

    @NonNull
    @NotNull
    @Override
    public TVShowViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        if(layoutInflater == null)
        {
            layoutInflater= LayoutInflater.from(parent.getContext());
        }
        ItemContainerTvShowBinding tvShowBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_container_tv_show,parent,false);
        return new TVShowViewHolder(tvShowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TVShowViewHolder holder, int position) {

        holder.bindTVShow(tvShows.get(position));

    }

    @Override
    public int getItemCount() {
        return tvShows.size();
    }

     class TVShowViewHolder extends RecyclerView.ViewHolder
    {
        private ItemContainerTvShowBinding itemContainerTvShowBinding;

        public TVShowViewHolder(ItemContainerTvShowBinding itemContainerTvShowBinding) {
            super(itemContainerTvShowBinding.getRoot());
            this.itemContainerTvShowBinding = itemContainerTvShowBinding;
        }
        public void bindTVShow(TVShow tvShow)
        {
            itemContainerTvShowBinding.setTvShow(tvShow);
            itemContainerTvShowBinding.executePendingBindings();
            itemContainerTvShowBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tvShowListner.onTVShowClicked(tvShow);
                }
            });
        }
    }//viewjolder lcose


}//class clsoe
