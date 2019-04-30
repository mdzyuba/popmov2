package com.mdzyuba.popularmovies.service;

import android.os.AsyncTask;
import android.util.Log;

import com.mdzyuba.popularmovies.model.Movie;
import com.mdzyuba.popularmovies.model.MovieCollection;
import com.mdzyuba.popularmovies.service.json.MovieParser;

import java.io.IOException;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public abstract class BaseMoviesProvider implements MoviesProvider {
    private static final String TAG = BaseMoviesProvider.class.getSimpleName();
    private final NetworkDataProvider networkDataProvider;
    private final MovieCollection movieCollection;

    BaseMoviesProvider(@NonNull NetworkDataProvider networkDataProvider) {
        this.networkDataProvider = networkDataProvider;
        movieCollection = new MovieCollection();
    }

    @Override
    public void getMovies(MutableLiveData<List<Movie>> movies,
                          MutableLiveData<Boolean> moviesLoadingProgress,
                          MutableLiveData<Exception> dataLoadException) {
        AsyncTask<Void, Void, MovieCollection> task = new AsyncTask<Void, Void, MovieCollection>() {
            private Exception exception;

            @Override
            protected void onPreExecute() {
                moviesLoadingProgress.setValue(true);
            }

            @Override
            protected MovieCollection doInBackground(Void... voids) {
                try {
                    int page = movieCollection.getFurthestPage() + 1;
                    if (movieCollection.getTotalPages() > 0 && page >= movieCollection.getTotalPages()) {
                        Log.d(TAG, "Reached max number of pages.");
                        return null;
                    }
                    URL getMoviesURL = getRequestUri(page);
                    if (getMoviesURL == null) {
                        throw new InvalidParameterException("The url should not be null");
                    }
                    String json = networkDataProvider.getResponseFromHttpUrl(getMoviesURL);
                    MovieParser movieParser = new MovieParser();
                    MovieCollection partialCollection = movieParser.parseMovieCollection(json);
                    List<Movie> movies = partialCollection.getMovieList();
                    movieCollection.getMovieList().addAll(movies);
                    movieCollection.setFurthestPage(page);
                    movieCollection.setTotalPages(partialCollection.getTotalPages());
                } catch (IOException e) {
                    Log.e(TAG, "Error: " + e.getMessage(), e);
                    this.exception = e;
                    return null;
                }
                return movieCollection;
            }

            @Override
            protected void onPostExecute(MovieCollection mv) {
                moviesLoadingProgress.setValue(false);
                if (exception != null) {
                    dataLoadException.setValue(exception);
                } else {
                    movies.postValue(mv.getMovieList());
                }
            }
        };
        task.execute();
    }

    @Override
    public boolean canLoadMoreMovies() {
        return movieCollection.getFurthestPage() < movieCollection.getTotalPages();
    }
}
