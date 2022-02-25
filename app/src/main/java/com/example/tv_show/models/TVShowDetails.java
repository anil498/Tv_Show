package com.example.tv_show.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TVShowDetails {

    ///yha vo vo varible bnao jo jo chahoye but unka type and serializesname exact same hona chahiyhe


    @SerializedName("url")
    private String url;

    @SerializedName("description")
    private String description;
    @SerializedName("runtime")
    private String runtime;
    @SerializedName("image_path")
    private String imagePath;



    @SerializedName("pictures")
    private String[] pictures;

    @SerializedName("episodes")
    private List<Episode> episodes;



    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getImagePath() {
        return imagePath;
    }



    public String[] getPictures() {
        return pictures;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }


     @SerializedName("rating")
    private String rating;

    @SerializedName("genres")
    private String[] genres;

    public String getRating() {
        return rating;
    }

    public String[] getGenres() {
        return genres;
    }
    //////
    /*@SerializedName("rating")
    private String rating;

    @SerializedName("genres")
    private String genres;

    public String getRating() {
        return rating;
    }

    public String getGenres() {
        return genres;
    }*/

}//class close
