package com.mdzyuba.popularmovies.service;

import com.mdzyuba.popularmovies.model.Movie;

import java.net.URL;
import java.util.List;

import androidx.lifecycle.MutableLiveData;


public interface MoviesProvider {

    URL getRequestUri(int page);

    boolean canLoadMoreMovies();

    void getMovies(MutableLiveData<List<Movie>> movies,
                   MutableLiveData<Boolean> moviesLoadingProgress,
                   MutableLiveData<Exception> dataLoadException);

}
