package com.example.tv_show.Database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.tv_show.dao.TVShowDao;
import com.example.tv_show.models.TVShow;

@Database(entities = TVShow.class, version = 1, exportSchema = false)
public abstract class TVShowsDatabase extends RoomDatabase {

    private static TVShowsDatabase tvShowsDatabase;

    public static synchronized TVShowsDatabase getTvShowsDatabase(Context context) {
        if (tvShowsDatabase == null) {
            tvShowsDatabase = Room.databaseBuilder(context, TVShowsDatabase.class, "tv_shows_db").build();
        }//if cloe
        return tvShowsDatabase;
    }//fun close

    public abstract TVShowDao tvShowDao();
}//class clsoe
