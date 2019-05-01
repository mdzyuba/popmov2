package com.mdzyuba.popularmovies.service;

import com.mdzyuba.popularmovies.database.MovieDatabase;
import com.mdzyuba.popularmovies.model.Movie;
import com.mdzyuba.popularmovies.model.TopMovie;

import java.net.URL;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class TopRatedMoviesProvider extends BaseMoviesProvider {

    public TopRatedMoviesProvider(@NonNull NetworkDataProvider networkDataProvider, MovieDatabase db) {
        super(networkDataProvider, db);
    }

    @Override
    public URL getRequestUri(int page) {
        URL topRatedMoviesURL = new MovieApiClient().buildGetTopRatedMoviesUrl(page);
        return topRatedMoviesURL;
    }

    @Override
    protected LiveData<List<Movie>> loadMoviesFromDb() {
        return getDb().topMovieDao().loadMovies();
    }

    @Override
    protected void deleteAllMovies() {
        getDb().topMovieDao().deleteAll();
    }

    @Override
    protected void saveMovie(Movie movie) {
        super.saveMovie(movie);
        getDb().topMovieDao().insert(new TopMovie(movie.getId()));
    }
}
