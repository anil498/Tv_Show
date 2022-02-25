package com.example.tv_show.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.tv_show.repositories.MostPopularTVShowRespository;
import com.example.tv_show.responses.TVShowsResponse;

public class MostPopularTVShowViewModel extends ViewModel {

private MostPopularTVShowRespository mostPopularTVShowRespository;

    public MostPopularTVShowViewModel() {
        mostPopularTVShowRespository = new MostPopularTVShowRespository();
    }

    public LiveData<TVShowsResponse> getMostPopularTVShows(int page)
    {
        return mostPopularTVShowRespository.getMostPopularTVShow(page);
    }
}//class clsoe
