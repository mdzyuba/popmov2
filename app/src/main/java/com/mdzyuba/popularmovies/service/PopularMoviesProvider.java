package com.mdzyuba.popularmovies.service;

import com.mdzyuba.popularmovies.database.MovieDatabase;
import com.mdzyuba.popularmovies.model.Movie;
import com.mdzyuba.popularmovies.model.PopularMovie;

import java.net.URL;
import java.util.List;

import androidx.lifecycle.LiveData;

public class PopularMoviesProvider extends BaseMoviesProvider {

    public PopularMoviesProvider(NetworkDataProvider networkDataProvider, MovieDatabase db) {
        super(networkDataProvider, db);
    }

    @Override
    public URL getRequestUri(int page) {
        URL popularMoviesUrl = new MovieApiClient().buildGetPopularMoviesUrl(page);
        return popularMoviesUrl;
    }

    @Override
    protected LiveData<List<Movie>> loadMoviesFromDb() {
        return getDb().popularMovieDao().loadMovies();
    }

    @Override
    protected void saveMovie(Movie movie) {
        super.saveMovie(movie);
        PopularMovie popularMovie = new PopularMovie(movie.getId());
        getDb().popularMovieDao().insert(popularMovie);
    }

    @Override
    protected void deleteAllMovies() {
        getDb().popularMovieDao().deleteAll();
    }
}
