package com.example.tv_show.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tv_show.R;
import com.example.tv_show.adapters.EpisodeAdapter;
import com.example.tv_show.adapters.ImageSliderAdapter;
import com.example.tv_show.databinding.ActivityTvshowDetailsBinding;
import com.example.tv_show.databinding.LayoutEpisodesBottomSheetBinding;
import com.example.tv_show.models.TVShow;
import com.example.tv_show.responses.TVShowDetailsResponses;
import com.example.tv_show.utilies.TempDataHolder;
import com.example.tv_show.viewmodels.TVShowDetailsViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Locale;


import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TVShowDetailsActivity extends AppCompatActivity {

    private ActivityTvshowDetailsBinding activityTvshowDetailsBinding;
    private TVShowDetailsViewModel tvShowDetailsViewModel;
    private BottomSheetDialog episodesBottomSheetDialog;
    private LayoutEpisodesBottomSheetBinding layoutEpisodesBottomSheetBinding;
    private TVShow tvShow;
    private Boolean isTVShowAvailableInWatchlist =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTvshowDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_tvshow_details);
        doInitialization();

        //getTVShowDetails();
    }//on create close

    private void doInitialization() {
        tvShowDetailsViewModel = new ViewModelProvider(this).get(TVShowDetailsViewModel.class);
        activityTvshowDetailsBinding.imageBack.setOnClickListener(view -> onBackPressed());

       tvShow= (TVShow) getIntent().getSerializableExtra("tvShow");
        checkTVShowInWatchlist();
       getTVShowDetails();
    }

    private  void  checkTVShowInWatchlist()
    {
        CompositeDisposable compositeDisposable =new CompositeDisposable();
        compositeDisposable.add(tvShowDetailsViewModel.getTVShowFromWatchlist(String.valueOf(tvShow.getId()))
        .observeOn(AndroidSchedulers .mainThread())
        .subscribe(tvShow1 -> {
            isTVShowAvailableInWatchlist =true;
            activityTvshowDetailsBinding.imagewatchlist.setImageResource(R.drawable.ic_added);
            compositeDisposable.dispose();
        }));

    }

    private void getTVShowDetails() {
        activityTvshowDetailsBinding.setIsLoading(true);
        String tvshowId = String.valueOf(tvShow.getId());
        Log.d("anil", "getTVShowDetails: id of click sshow" + tvshowId);
        tvShowDetailsViewModel.getTVShowDetails(tvshowId).observe(this, new Observer<TVShowDetailsResponses>() {
            @Override
            public void onChanged(TVShowDetailsResponses tvShowDetailsResponses) {

                /////////
                Log.d("anil", "TVShowDetailsActivity onChanged: try run start");

                activityTvshowDetailsBinding.setIsLoading(false);
                Toast.makeText(getApplicationContext(), "te" + tvShowDetailsResponses.getTvShowDetails().getUrl(), Toast.LENGTH_LONG).show();
                ////////////
                try {
                    Log.d("anil", "TVShowDetailsActivity onChanged: try run start");

                    activityTvshowDetailsBinding.setIsLoading(false);
                    Toast.makeText(getApplicationContext(), "te" + tvShowDetailsResponses.getTvShowDetails().getUrl(), Toast.LENGTH_LONG).show();
                    if (tvShowDetailsResponses.getTvShowDetails() != null) {
                        if (tvShowDetailsResponses.getTvShowDetails().getPictures() != null) {
                            loadImageSlider(tvShowDetailsResponses.getTvShowDetails().getPictures());
                        }//inner if close
                        activityTvshowDetailsBinding.setTvShowImageURL(tvShowDetailsResponses.getTvShowDetails()
                                .getImagePath());
                        activityTvshowDetailsBinding.imageTVShow.setVisibility(View.VISIBLE);
                        activityTvshowDetailsBinding.setDescription(String.valueOf(HtmlCompat.fromHtml(tvShowDetailsResponses.getTvShowDetails().getDescription(), HtmlCompat.FROM_HTML_MODE_LEGACY)));
                        activityTvshowDetailsBinding.textDescription.setVisibility(View.VISIBLE);
                        activityTvshowDetailsBinding.textreadmore.setVisibility(View.VISIBLE);
                        activityTvshowDetailsBinding.textreadmore.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (activityTvshowDetailsBinding.textreadmore.getText().toString().equals("Read More")) {
                                    activityTvshowDetailsBinding.textDescription.setMaxLines(Integer.MAX_VALUE);
                                    activityTvshowDetailsBinding.textDescription.setEllipsize(null);
                                    activityTvshowDetailsBinding.textreadmore.setText("Read Less");
                                } else {
                                    activityTvshowDetailsBinding.textDescription.setMaxLines(4);
                                    activityTvshowDetailsBinding.textDescription.setEllipsize(TextUtils.TruncateAt.END);
                                    activityTvshowDetailsBinding.textreadmore.setText("Read More");
                                }
                            }//on click close
                        });

                        activityTvshowDetailsBinding.setRating(String.format(Locale.getDefault(), "%.2f",
                                Double.parseDouble(tvShowDetailsResponses.getTvShowDetails().getRating())));
                        if (tvShowDetailsResponses.getTvShowDetails().getGenres() != null) {
                            activityTvshowDetailsBinding.setGenre(tvShowDetailsResponses.getTvShowDetails().getGenres()[0]);
                        }//if close
                        else {
                            activityTvshowDetailsBinding.setGenre("N/A");
                        }
                        activityTvshowDetailsBinding.setRuntime(tvShowDetailsResponses.getTvShowDetails().getRuntime() + " min");
                        activityTvshowDetailsBinding.viewDivider1.setVisibility(View.VISIBLE);
                        activityTvshowDetailsBinding.layoutmisc.setVisibility(View.VISIBLE);
                        activityTvshowDetailsBinding.viewDivider2.setVisibility(View.VISIBLE);

                        activityTvshowDetailsBinding.buttonwebsite.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(tvShowDetailsResponses.getTvShowDetails().getUrl()));
                                startActivity(intent);
                            }
                        });
                        activityTvshowDetailsBinding.buttonwebsite.setVisibility(View.VISIBLE);
                        activityTvshowDetailsBinding.buttonEpisodes.setVisibility(View.VISIBLE);
                        activityTvshowDetailsBinding.buttonEpisodes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (episodesBottomSheetDialog == null) {
                                    episodesBottomSheetDialog = new BottomSheetDialog(TVShowDetailsActivity.this);
                                    layoutEpisodesBottomSheetBinding = DataBindingUtil.inflate(
                                            LayoutInflater.from(TVShowDetailsActivity.this), R.layout.layout_episodes_bottom_sheet,
                                            findViewById(R.id.episodecontainer), false
                                    );
                                    episodesBottomSheetDialog.setContentView(layoutEpisodesBottomSheetBinding.getRoot());
                                    layoutEpisodesBottomSheetBinding.episoderecyclerview.setAdapter(new EpisodeAdapter(tvShowDetailsResponses.getTvShowDetails().getEpisodes()));

                                    layoutEpisodesBottomSheetBinding.texttitle.setText(String.format("Episode | %s",
                                         tvShow.getName()));

                                    layoutEpisodesBottomSheetBinding.imageclose.setOnClickListener(view1 -> episodesBottomSheetDialog.dismiss());

                                }//if close
                                ///----------------optional
                                FrameLayout frameLayout =episodesBottomSheetDialog.findViewById(
                                        com.google.android.material.R.id.design_bottom_sheet);
                                if(frameLayout !=null)
                                {
                                    BottomSheetBehavior<View> bottomSheetBehavior =BottomSheetBehavior.from(frameLayout);
                                    bottomSheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
                                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                }

                                //------------------END 0PTIONAl
                                episodesBottomSheetDialog.show();
                            }//on click close
                        });
                       ////////////=----for watchlist
                        //onclick close
                        activityTvshowDetailsBinding.imagewatchlist.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                ////part 11
                                CompositeDisposable compositeDisposable = new CompositeDisposable();
                                    if (isTVShowAvailableInWatchlist) {
                                        compositeDisposable.add(tvShowDetailsViewModel.removeTVShowFromwatchlist(tvShow)
                                        .subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(() ->{
                                            isTVShowAvailableInWatchlist =false;
                                            TempDataHolder.IS_WATCJLIST_UPDATED =true;
                                            activityTvshowDetailsBinding.imagewatchlist.setImageResource(R.drawable.ic_watchlist);
                                            Toast.makeText(getApplicationContext(), "Removed to Watchlist", Toast.LENGTH_SHORT).show();
                                            compositeDisposable.dispose();

                                        }));


                                    } else {
                                       compositeDisposable.add(tvShowDetailsViewModel.addToWatchlist(tvShow)
                                                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
                                                    TempDataHolder.IS_WATCJLIST_UPDATED=true;
                                                    activityTvshowDetailsBinding.imagewatchlist.setImageResource(R.drawable.ic_added);
                                                    Toast.makeText(getApplicationContext(), "Added To WatchList", Toast.LENGTH_LONG).show();
                                                    compositeDisposable.dispose();
                                                })//lamda subcrib close
                                        );//add

                                    }//else close


                                /////part 11 end

                                /*new CompositeDisposable().add(tvShowDetailsViewModel.addToWatchlist(tvShow)
                                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
                                            activityTvshowDetailsBinding.imagewatchlist.setImageResource(R.drawable.ic_added);
                                            Toast.makeText(getApplicationContext(), "Added To WatchList", Toast.LENGTH_LONG).show();
                                        })//lamda subcrib close
                                );//add*/
                            }//on click close
                        });
                        ///////-----end watchlist
                        activityTvshowDetailsBinding.imagewatchlist.setVisibility(View.VISIBLE);
                        loadBasicTVShowDetails();
                    }//if close
                    Log.d("anil", "TVShowDetailsActivity onChanged: try run end");

                } catch (Exception e) {
                    Log.d("anil", "TVShowDetailsActivity onChanged: catch run");
                }


            }//on changed close
        });

    }//fun close

    private void loadImageSlider(String[] sliderImages) {
        activityTvshowDetailsBinding.sliderviewpager.setOffscreenPageLimit(1);
        activityTvshowDetailsBinding.sliderviewpager.setAdapter(new ImageSliderAdapter(sliderImages));
        activityTvshowDetailsBinding.sliderviewpager.setVisibility(View.VISIBLE);
        activityTvshowDetailsBinding.viewfadingedge.setVisibility(View.VISIBLE);

        setupImageSliderIndicator(sliderImages.length);
        activityTvshowDetailsBinding.sliderviewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentSliderIndicator(position);
            }
        });
    }//func coloe

    private void setupImageSliderIndicator(int count) {
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(8, 0, 8, 0);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_slider_indicator_inactive));

            indicators[i].setLayoutParams(layoutParams);
            activityTvshowDetailsBinding.layoutSliderindicator.addView(indicators[i]);
        }//for lcose
        activityTvshowDetailsBinding.layoutSliderindicator.setVisibility(View.VISIBLE);
        setCurrentSliderIndicator(0);
    }//fun close

    private void setCurrentSliderIndicator(int position) {
        int childcount = activityTvshowDetailsBinding.layoutSliderindicator.getChildCount();
        for (int i = 0; i < childcount; i++) {
            ImageView imageView = (ImageView) activityTvshowDetailsBinding.layoutSliderindicator.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_slider_indicator_active));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_slider_indicator_inactive));

            }
        }//for close
    }//fun close


    private void loadBasicTVShowDetails() {
        activityTvshowDetailsBinding.setTvShowName(tvShow.getName());
        activityTvshowDetailsBinding.setNetworkCountry(tvShow.getNetwork() + "(" + tvShow.getCountry() + ")");
        activityTvshowDetailsBinding.setStatus(tvShow.getStatus());
        activityTvshowDetailsBinding.setStartedDate(tvShow.getStartDate());
        activityTvshowDetailsBinding.textName.setVisibility(View.VISIBLE);
        activityTvshowDetailsBinding.textNetworkCountry.setVisibility(View.VISIBLE);
        activityTvshowDetailsBinding.textstatus.setVisibility(View.VISIBLE);
        activityTvshowDetailsBinding.textstarted.setVisibility(View.VISIBLE);
    }//fnt close
}//class close