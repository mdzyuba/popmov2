package com.mdzyuba.popularmovies.service;

import android.os.AsyncTask;
import android.util.Log;

import com.mdzyuba.popularmovies.database.MovieDao;
import com.mdzyuba.popularmovies.database.MovieDatabase;
import com.mdzyuba.popularmovies.model.Movie;
import com.mdzyuba.popularmovies.model.MovieCollection;
import com.mdzyuba.popularmovies.service.json.MovieParser;

import java.io.IOException;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public abstract class BaseMoviesProvider implements MoviesProvider {
    private static final String TAG = BaseMoviesProvider.class.getSimpleName();
    private final NetworkDataProvider networkDataProvider;
    private final MovieCollection movieCollection;
    private final MovieDatabase db;

    BaseMoviesProvider(@NonNull NetworkDataProvider networkDataProvider, MovieDatabase db) {
        this.networkDataProvider = networkDataProvider;
        movieCollection = new MovieCollection();
        this.db = db;
    }

    @Override
    public void getMovies(final MutableLiveData<List<Movie>> movies,
                          final MutableLiveData<Boolean> moviesLoadingProgress,
                          final MutableLiveData<Exception> dataLoadException) {
        AsyncTask<Void, Void, MovieCollection> task = new AsyncTask<Void, Void, MovieCollection>() {
            private Exception exception;

            @Override
            protected void onPreExecute() {
                moviesLoadingProgress.setValue(true);
            }

            @Override
            protected MovieCollection doInBackground(Void... voids) {
                return getMovieCollectionOnline();
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

            private MovieCollection getMovieCollectionOnline() {
                try {
                    int page = movieCollection.getFurthestPage() + 1;
                    if (movieCollection.getTotalPages() > 0 && page >= movieCollection.getTotalPages()) {
                        Log.d(TAG, "Reached max number of pages.");
                        return movieCollection;
                    }
                    URL getMoviesURL = getRequestUri(page);
                    if (getMoviesURL == null) {
                        throw new InvalidParameterException("The url should not be null");
                    }
                    String json = networkDataProvider.getResponseFromHttpUrl(getMoviesURL);
                    MovieParser movieParser = new MovieParser();
                    MovieCollection partialCollection = movieParser.parseMovieCollection(json);
                    List<Movie> movies = partialCollection.getMovieList();
                    if (page == 1) {
                        deleteAllMovies();
                    }
                    saveMoviesToDb(movies);
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

        };

        if (networkDataProvider.isNetworkAvailable()) {
            task.execute();
        } else {
            getMovieCollectionOffline(movies, moviesLoadingProgress);
        }
    }

    private void getMovieCollectionOffline(MutableLiveData<List<Movie>> movies,
                                  MutableLiveData<Boolean> moviesLoadingProgress) {
        moviesLoadingProgress.setValue(true);
        LiveData<List<Movie>> moviesLiveData = loadMoviesFromDb();
        moviesLiveData.observeForever(new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> dbMovies) {
                moviesLiveData.removeObserver(this);
                movieCollection.getMovieList().clear();
                movieCollection.getMovieList().addAll(dbMovies);
                movieCollection.setFurthestPage(1);
                movieCollection.setTotalPages(100); // enable updating if the device is online.
                movies.postValue(dbMovies);
                moviesLoadingProgress.setValue(false);
            }
        });
    }

    protected abstract LiveData<List<Movie>> loadMoviesFromDb();

    private void saveMoviesToDb(List<Movie> movies) {
        for (Movie movie: movies) {
            saveMovie(movie);
        }
    }

    /**
     * This method will clean the list of popular or top movies before updating them.
     */
    protected abstract void deleteAllMovies();

    /**
     * This method could be extended by a specific class to update foreign keys.
     * @param movie a movie to be saved.
     */
    protected void saveMovie(Movie movie) {
        MovieDao movieDao = db.movieDao();
        movieDao.insert(movie);
    }

    @Override
    public boolean canLoadMoreMovies() {
        return movieCollection.getFurthestPage() < movieCollection.getTotalPages();
    }

    public MovieDatabase getDb() {
        return db;
    }
}
