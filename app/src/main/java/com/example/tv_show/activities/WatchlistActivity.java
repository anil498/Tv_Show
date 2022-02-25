package com.example.tv_show.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.tv_show.Listners.WatchlistListner;
import com.example.tv_show.R;
import com.example.tv_show.adapters.WatchlistAdapter;
import com.example.tv_show.databinding.ActivityWatchlistBinding;
import com.example.tv_show.models.TVShow;
import com.example.tv_show.utilies.TempDataHolder;
import com.example.tv_show.viewmodels.WatchListviewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class WatchlistActivity extends AppCompatActivity implements WatchlistListner {

    private ActivityWatchlistBinding activityWatchlistBinding;
    private WatchListviewModel viewmodel;
    private WatchlistAdapter watchlistAdapter;
    private List<TVShow> watchlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       activityWatchlistBinding = DataBindingUtil.setContentView(this,R.layout.activity_watchlist);
       doInitilization();
       loadWatchList();
    }//on create close

    private void doInitilization() {
        viewmodel = new ViewModelProvider(this).get(WatchListviewModel.class);
        activityWatchlistBinding.imageBack.setOnClickListener(view -> onBackPressed());
        watchlist=new ArrayList<>();
    }//fun close

    private void loadWatchList()
    {
        activityWatchlistBinding.setIsLoading(true);
        CompositeDisposable compositeDisposable =new CompositeDisposable();
        compositeDisposable.add(viewmodel.loadWatchList().subscribeOn(Schedulers.computation()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(tvShows -> {
                    activityWatchlistBinding.setIsLoading(false);
            Toast.makeText(getApplicationContext(), "Watchlist: " +tvShows.size(), Toast.LENGTH_SHORT).show();
            if(watchlist.size()>0)
            {
                watchlist.clear();
            }//if close
            watchlist.addAll(tvShows);
            watchlistAdapter = new WatchlistAdapter(watchlist,this);
            activityWatchlistBinding.watcjlistRecyclerview.setAdapter(watchlistAdapter);
            activityWatchlistBinding.watcjlistRecyclerview.setVisibility(View.VISIBLE);
            compositeDisposable.dispose();

                }));

    }//fun close

    @Override
    protected void onResume() {
        super.onResume();
        if(TempDataHolder.IS_WATCJLIST_UPDATED)
        {
            loadWatchList();
            TempDataHolder.IS_WATCJLIST_UPDATED =false;
        }
        //loadWatchList();
    }

    @Override
    public void onTVShowClicked(TVShow tvShow) {
        Intent intent = new Intent(getApplicationContext(),TVShowDetailsActivity.class);
        intent.putExtra("tvShow",tvShow);
        startActivity(intent);

    }

    @Override
    public void removeTVShowFromWatchlist(TVShow tvShow, int position) {
               CompositeDisposable compositeDisposableforDelete = new CompositeDisposable();
               compositeDisposableforDelete.add(viewmodel.removeTVShowFromWatchlist(tvShow)
                       .subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
               .subscribe(()->{
                   watchlist.remove(position);
                   watchlistAdapter.notifyItemRemoved(position);
                   watchlistAdapter.notifyItemRangeChanged(position,watchlistAdapter.getItemCount());
                   compositeDisposableforDelete.dispose();
               }));


    }
}//class close