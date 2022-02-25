package com.example.tv_show.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.tv_show.Listners.TVShowListner;
import com.example.tv_show.R;
import com.example.tv_show.adapters.TVShowAdapter;
import com.example.tv_show.databinding.ActivitySearchBinding;
import com.example.tv_show.models.TVShow;
import com.example.tv_show.viewmodels.SearchViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SearchActivity extends AppCompatActivity implements TVShowListner {

    private ActivitySearchBinding activitySearchBinding;
    private SearchViewModel viewModel;
    private List<TVShow> tvShows =new ArrayList<>();
    private TVShowAdapter tvShowAdapter;
    private int currentpage=1;
    private int totalavialablepage=1;
    private Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySearchBinding = DataBindingUtil.setContentView(this,R.layout.activity_search);
        doInitialization();
    }//on create clsoe

    private void doInitialization()
    {
        activitySearchBinding.imageBack.setOnClickListener(view -> onBackPressed());

        activitySearchBinding.tvshowsRecyclerview.setHasFixedSize(true);
        viewModel=new ViewModelProvider(this).get(SearchViewModel.class);
        tvShowAdapter = new TVShowAdapter(tvShows,this);
        activitySearchBinding.tvshowsRecyclerview.setAdapter(tvShowAdapter);

        activitySearchBinding.inputsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if(timer!= null)
                    {
                        timer.cancel();
                    }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                     if(!editable.toString().trim().isEmpty())
                     {
                         timer= new Timer();
                         timer.schedule(new TimerTask() {
                             @Override
                             public void run() {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                   currentpage=1;
                                   totalavialablepage=1;
                                   searchTVShow(editable.toString());
                                    }
                                });

                             }
                         },800);
                     }//if close
                else
                    {
                        tvShows.clear();
                        tvShowAdapter.notifyDataSetChanged();
                    }
            }
        });

        activitySearchBinding.tvshowsRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!activitySearchBinding.tvshowsRecyclerview.canScrollVertically(1))
                {
                    if(!activitySearchBinding.inputsearch.getText().toString().isEmpty())
                    {
                        if(currentpage< totalavialablepage)
                        {
                            currentpage+=1;
                            searchTVShow(activitySearchBinding.inputsearch.getText().toString());
                        }
                    }//if close
                }//if clsoe

            }//on scrooleed close
        });
        activitySearchBinding.inputsearch.requestFocus();
    }//fun close

    private void searchTVShow(String query)
    {
        toggleLoading();
        viewModel.searchTVShow(query,currentpage).observe(this,tvShowsResponse ->{
            toggleLoading();
            if(tvShowsResponse !=null)
            {
                totalavialablepage =tvShowsResponse.getTotalPages();
                if(tvShowsResponse.getTvShows()!=null)
                {
                 int oldcount =tvShows.size();
                 tvShows.addAll(tvShowsResponse.getTvShows());
                 tvShowAdapter.notifyItemRangeInserted(oldcount,tvShows.size());
                }
            }//if close
        } );
    }//fun close

    private void toggleLoading()
    {
        if(currentpage==1)
        {
            if(activitySearchBinding.getIsLoading() !=null && activitySearchBinding.getIsLoading())
            {
                activitySearchBinding.setIsLoading(false);
            }//inner if close
            else
            {
                activitySearchBinding.setIsLoading(true);
            }

        }//if close
        else
        {
            if(activitySearchBinding.getIsLoadingMore() !=null && activitySearchBinding.getIsLoadingMore())
            {
                activitySearchBinding.setIsLoadingMore(false);
            }//inner if close
            else
            {
                activitySearchBinding.setIsLoadingMore(true);
            }
        }//else close
    }//fun close

    @Override
    public void onTVShowClicked(TVShow tvShow) {

        Intent intent =new Intent(getApplicationContext(),TVShowDetailsActivity.class);
        intent.putExtra("tvShow",tvShow);
        startActivity(intent);

    }//fun close
}//class close