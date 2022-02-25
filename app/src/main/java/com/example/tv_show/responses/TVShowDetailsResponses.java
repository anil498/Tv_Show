package com.example.tv_show.responses;

import com.example.tv_show.models.TVShowDetails;
import com.google.gson.annotations.SerializedName;

public class TVShowDetailsResponses {

    @SerializedName("tvShow")
    private TVShowDetails tvShowDetails;

    public TVShowDetails getTvShowDetails() {
        return tvShowDetails;
    }


}//class clsoe
