package com.mdzyuba.popularmovies.view;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * This factory provides a single instance of SharedViewModel class to be shared among
 * activities.
 */
public class SharedViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private static SharedViewModel sharedViewModel;
    private static final Object LOCK = new Object();

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (sharedViewModel == null) {
            synchronized (LOCK) {
                sharedViewModel = new SharedViewModel();
            }
        }
        //noinspection unchecked
        return (T) sharedViewModel;
    }
}
