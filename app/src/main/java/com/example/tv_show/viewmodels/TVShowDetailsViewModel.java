package com.example.tv_show.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.tv_show.Database.TVShowsDatabase;
import com.example.tv_show.models.TVShow;
import com.example.tv_show.repositories.TVShowDetailsRepository;
import com.example.tv_show.responses.TVShowDetailsResponses;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;


public class TVShowDetailsViewModel extends AndroidViewModel {

    private TVShowDetailsRepository tvShowDetailsRepository;
    private TVShowsDatabase tvShowsDatabase;


    public TVShowDetailsViewModel(@NonNull Application application) {
        super(application);
        tvShowDetailsRepository = new TVShowDetailsRepository();
         tvShowsDatabase =TVShowsDatabase.getTvShowsDatabase(application);
    }

    public LiveData<TVShowDetailsResponses> getTVShowDetails(String tvShowId) {
        return tvShowDetailsRepository.getTVShowDetails(tvShowId);

    }//fun close

    public Completable addToWatchlist(TVShow tvShow)
    {
        return  tvShowsDatabase.tvShowDao().addToWatchlist(tvShow);
    }

    public Flowable<TVShow> getTVShowFromWatchlist(String tvShowId)
    {
        return tvShowsDatabase.tvShowDao().getTVShowFromWatchlist(tvShowId);
    }

    public Completable removeTVShowFromwatchlist(TVShow tvShow)
    {
        return tvShowsDatabase.tvShowDao().removeFromwatchlist(tvShow);
    }
}//class close
