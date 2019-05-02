package com.mdzyuba.popularmovies.view;

import android.app.Application;

import com.mdzyuba.popularmovies.model.Movie;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


public class MovieDetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final Movie movie;
    private final Application application;

    public MovieDetailsViewModelFactory(Application application, Movie movie) {
        this.application = application;
        this.movie = movie;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        MovieDetailsViewModel viewModel = new MovieDetailsViewModel(application, movie);
        //noinspection unchecked
        return (T) viewModel;
    }
}
