package com.mdzyuba.popularmovies.service;

import android.content.Context;

import com.mdzyuba.popularmovies.database.FavoriteMovieDao;
import com.mdzyuba.popularmovies.database.MovieDatabase;
import com.mdzyuba.popularmovies.model.Movie;

import java.net.URL;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;


public class FavoriteMoviesProvider implements MoviesProvider {

    private final Context context;

    public FavoriteMoviesProvider(Context context) {
        this.context = context;
    }

    @Override
    public URL getRequestUri(int page) {
        return null;
    }

    @Override
    public boolean canLoadMoreMovies() {
        return false;
    }

    @Override
    public void getMovies(MutableLiveData<List<Movie>> movies,
                          MutableLiveData<Boolean> moviesLoadingProgress,
                          MutableLiveData<Exception> dataLoadException) {
        MovieDatabase db = MovieDatabase.getInstance(context);
        FavoriteMovieDao favoriteMovieDao = db.favoriteMovieDao();
        LiveData<List<Movie>> favMovies = favoriteMovieDao.loadMovies();
        favMovies.observeForever(new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> mv) {
                favMovies.removeObserver(this);
                movies.postValue(mv);
            }
        });
    }
}
