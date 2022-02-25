package com.example.tv_show.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tv_show.network.ApiClient;
import com.example.tv_show.network.ApiService;
import com.example.tv_show.responses.TVShowDetailsResponses;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TVShowDetailsRepository {
    private ApiService apiService;

    public TVShowDetailsRepository()
    {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }
    public LiveData<TVShowDetailsResponses> getTVShowDetails(String tvShowId)
    {
        MutableLiveData<TVShowDetailsResponses> data = new MutableLiveData<>();
        // String id="35624";//the splash
        //tvShowId="29560";
        apiService.getTVShowDetails(tvShowId).enqueue(new Callback<TVShowDetailsResponses>() {
            @Override
            public void onResponse(Call<TVShowDetailsResponses> call, Response<TVShowDetailsResponses> response) {
                data.setValue(response.body());
                Log.d("anil", "TVShowDetailsRespority onResponse: run");
            }

            @Override
            public void onFailure(Call<TVShowDetailsResponses> call, Throwable t) {
           data.setValue(null);
                Log.d("anil", "TVShowDetailsRespority onfailure: run");
            }
        });
        return data;
    }//fun close
}//class clsoe
