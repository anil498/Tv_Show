package com.example.tv_show.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tv_show.network.ApiClient;
import com.example.tv_show.network.ApiService;
import com.example.tv_show.responses.TVShowsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MostPopularTVShowRespository {

    private ApiService apiService;

    public MostPopularTVShowRespository() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }//cost close

    public LiveData<TVShowsResponse> getMostPopularTVShow(int page) {
        MutableLiveData<TVShowsResponse> data = new MutableLiveData<>();
        apiService.getMostPopularTVShows(page).enqueue(new Callback<TVShowsResponse>() {
            @Override
            public void onResponse(@NonNull Call<TVShowsResponse> call, @NonNull Response<TVShowsResponse> response) {
                data.setValue(response.body());
                Log.d("anil", "mostpopulartvshow_repostory onResponse: "+"run");
            }

            @Override
            public void onFailure(@NonNull Call<TVShowsResponse> call, @NonNull Throwable t) {
                data.setValue(null);
                Log.d("anil", "mostpopulartvshow_repostory onFailure: "+"run");
            }
        });
        Log.d("anil", "getMostPopularTVShow: "+"data");
        return data;
    }//fun close
}//class close
