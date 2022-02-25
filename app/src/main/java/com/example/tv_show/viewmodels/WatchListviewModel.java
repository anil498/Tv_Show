package com.example.tv_show.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.tv_show.Database.TVShowsDatabase;
import com.example.tv_show.models.TVShow;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public class WatchListviewModel extends AndroidViewModel {

private TVShowsDatabase tvShowsDatabase;

    public WatchListviewModel( @NotNull Application application) {
        super(application);
        tvShowsDatabase =TVShowsDatabase.getTvShowsDatabase(application);
    }

    public Flowable<List<TVShow>> loadWatchList()
    {
        return tvShowsDatabase.tvShowDao().getwatchlist();//rxjava 3 vala
    }

    public Completable removeTVShowFromWatchlist(TVShow tvShow)
    {
        return tvShowsDatabase.tvShowDao().removeFromwatchlist(tvShow);
    }
}//clas clsoe

