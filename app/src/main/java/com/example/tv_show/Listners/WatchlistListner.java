package com.example.tv_show.Listners;

import com.example.tv_show.models.TVShow;

public interface WatchlistListner {

void onTVShowClicked(TVShow tvShow);

void removeTVShowFromWatchlist(TVShow tvShow,int position);


}//class clsoe
