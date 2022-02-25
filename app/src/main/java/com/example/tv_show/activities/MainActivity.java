package com.example.tv_show.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.tv_show.Listners.TVShowListner;
import com.example.tv_show.R;
import com.example.tv_show.adapters.TVShowAdapter;
import com.example.tv_show.databinding.ActivityMainBinding;
import com.example.tv_show.models.TVShow;
import com.example.tv_show.responses.TVShowsResponse;
import com.example.tv_show.viewmodels.MostPopularTVShowViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TVShowListner {
    private ActivityMainBinding activityMainBinding;
    private MostPopularTVShowViewModel viewModel;
    private List<TVShow> tvShows = new ArrayList<>();
    private TVShowAdapter tvShowAdapter;
    private int currentpage=1;
    private int totalAvailablePage=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        doInitialization();
    }//on create close

    private void doInitialization() {
        activityMainBinding.tvshowRecyclerview.setHasFixedSize(true);
        viewModel = new ViewModelProvider(this).get(MostPopularTVShowViewModel.class);
        tvShowAdapter = new TVShowAdapter(tvShows,this);
        activityMainBinding.tvshowRecyclerview.setAdapter(tvShowAdapter);

        activityMainBinding.tvshowRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
         @Override
         public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
             super.onScrolled(recyclerView, dx, dy);
                  if(!activityMainBinding.tvshowRecyclerview.canScrollVertically(1))
                  {
                      if(currentpage <= totalAvailablePage)
                      {
                          currentpage+=1;
                          getMostPopularTVShow();
                      }
                  }//if close

         }//onscrollred close
     });//listnr close
       activityMainBinding.imagewatchlist.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(),WatchlistActivity.class)));
       activityMainBinding.imagesearch.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(),SearchActivity.class)));
       getMostPopularTVShow();
    }//fun close

    private void getMostPopularTVShow() {
        //activityMainBinding.setIsLoading(true);
        toggleLoading();
        Log.d("anil", "getMostPopularTVShow:pages" + "run");
        viewModel.getMostPopularTVShows(currentpage).observe(MainActivity.this, new Observer<TVShowsResponse>() {
                    @Override
                    public void onChanged(TVShowsResponse mostMostPopularTVShowsResponse) {
                       try {
                           //activityMainBinding.setIsLoading(false);
                           toggleLoading();
                           Log.d("anil", "main activity onChanged: try run");
                           Log.d("anil", "getMostPopularTVShow:pages" + mostMostPopularTVShowsResponse.getTotalPages());
                           Toast.makeText(MainActivity.this,"getMostPopularTVShow:pages" + mostMostPopularTVShowsResponse.getTotalPages(),Toast.LENGTH_LONG).show();
                           if (mostMostPopularTVShowsResponse != null) {
                               totalAvailablePage=mostMostPopularTVShowsResponse.getTotalPages();
                               if (mostMostPopularTVShowsResponse.getTvShows() != null) {
                                   int oldcount =tvShows.size();
                                   tvShows.addAll(mostMostPopularTVShowsResponse.getTvShows());
                                   tvShowAdapter.notifyItemRangeInserted(oldcount,tvShows.size());
                                   //tvShowAdapter.notifyDataSetChanged();
                               }//if lcose
                           }//if close


                       }catch (Exception e)
                       {
                           Log.d("anil", "mainactivity onChanged: catch run");
                           Toast.makeText(MainActivity.this,"error in mainactivity gettotal"+e.getMessage(),Toast.LENGTH_LONG).show();
                       }

                    }//on changed close
                }
        );
        //Log.d("anil", "getMostPopularTVShow:pages"+ most.getTotalPages());
    }//fun close

    private void toggleLoading()
    {
        if(currentpage==1)
        {
            if(activityMainBinding.getIsLoading() !=null && activityMainBinding.getIsLoading())
            {
                activityMainBinding.setIsLoading(false);
            }//inner if close
            else
            {
                activityMainBinding.setIsLoading(true);
            }

        }//if close
        else
            {
                if(activityMainBinding.getIsLoadingMore() !=null && activityMainBinding.getIsLoadingMore())
                {
                    activityMainBinding.setIsLoadingMore(false);
                }//inner if close
                else
                {
                    activityMainBinding.setIsLoadingMore(true);
                }
            }//else close
    }//fun close

    @Override
    public void onTVShowClicked(TVShow tvShow) {
        Intent intent =new Intent(getApplicationContext(),TVShowDetailsActivity.class);
        intent.putExtra("tvShow",tvShow);
                /*
                intent.putExtra("id",tvShow.getId());
        intent.putExtra("name",tvShow.getName());
        intent.putExtra("startDate",tvShow.getStartDate());
        intent.putExtra("country",tvShow.getCountry());
        intent.putExtra("network",tvShow.getNetwork());
        intent.putExtra("status",tvShow.getStatus());
                 */
    startActivity(intent);
    }

}//calssss close