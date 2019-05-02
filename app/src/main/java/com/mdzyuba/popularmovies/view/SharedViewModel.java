package com.mdzyuba.popularmovies.view;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * This view model is used to communicate data changes between MovieDetailsActivity and MainActivity.
 * In the Favorite movie selection, when the user clears a favorite flag for a movie in
 * MovieDetailsActivity and goes back to the list of movies, the MainActivity will refresh the list.
 */
public class SharedViewModel extends ViewModel {

    public final MutableLiveData<Boolean> refreshFavoriteMovies;

    public SharedViewModel() {
        this.refreshFavoriteMovies = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> getRefreshFavoriteMovies() {
        return refreshFavoriteMovies;
    }
}
