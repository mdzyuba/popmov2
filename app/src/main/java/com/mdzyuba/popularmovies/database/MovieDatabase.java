package com.mdzyuba.popularmovies.database;

import android.content.Context;
import android.util.Log;

import com.mdzyuba.popularmovies.model.FavoriteMovie;
import com.mdzyuba.popularmovies.model.Movie;
import com.mdzyuba.popularmovies.model.PopularMovie;
import com.mdzyuba.popularmovies.model.TopMovie;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;


@Database(entities = {
                        Movie.class,
                        FavoriteMovie.class,
                        PopularMovie.class,
                        TopMovie.class
                     }, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class MovieDatabase extends RoomDatabase {
    private static final String TAG = MovieDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "movies";

    private static MovieDatabase sInstance;

    public static MovieDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                                                 MovieDatabase.class, MovieDatabase.DATABASE_NAME)
                                .build();
            }
        }
        Log.d(TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract MovieDao movieDao();

    public abstract FavoriteMovieDao favoriteMovieDao();

    public abstract PopularMovieDao popularMovieDao();

    public abstract TopMovieDao topMovieDao();
}
