package com.mdzyuba.popularmovies.view;

import android.os.AsyncTask;
import android.util.Log;

import com.mdzyuba.popularmovies.model.Movie;
import com.mdzyuba.popularmovies.model.Reviews;
import com.mdzyuba.popularmovies.model.VideosCollection;
import com.mdzyuba.popularmovies.service.NetworkDataProvider;
import com.mdzyuba.popularmovies.service.ReviewsProvider;
import com.mdzyuba.popularmovies.service.VideoCollectionProvider;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MovieDetailsViewModel extends ViewModel {

    private static final String TAG = MovieDetailsViewModel.class.getSimpleName();

    public final MutableLiveData<VideosCollection> videosCollection;

    public final MutableLiveData<Reviews> reviews;

    private final NetworkDataProvider networkDataProvider;

    public MovieDetailsViewModel() {
        this.networkDataProvider = new NetworkDataProvider();
        this.videosCollection = new MutableLiveData<>();
        this.reviews = new MutableLiveData<>();
    }

    public void loadVideos(final Movie movie) {
        AsyncTask<Movie, Void, VideosCollection> task = new AsyncTask<Movie, Void, VideosCollection>() {
            @Override
            protected VideosCollection doInBackground(Movie... movies) {
                Movie mv = movies[0];
                VideoCollectionProvider provider = new VideoCollectionProvider(networkDataProvider);
                try {
                    VideosCollection videosCollection = provider.getVideos(mv.getId());
                    return videosCollection;
                } catch (Exception e) {
                    Log.e(TAG, "Error: " + e.getMessage(), e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(VideosCollection videos) {
                videosCollection.setValue(videos);
            }
        };
        task.execute(movie);
    }

    public void loadReviews(final Movie movie) {
        AsyncTask<Movie, Void, Reviews> task = new AsyncTask<Movie, Void, Reviews>() {
            @Override
            protected Reviews doInBackground(Movie... movies) {
                Movie mv = movies[0];
                ReviewsProvider provider = new ReviewsProvider(networkDataProvider);
                try {
                    Reviews reviews = provider.getReviews(mv.getId(), 1);
                    return reviews;
                } catch (Exception e) {
                    Log.e(TAG, "Error: " + e.getMessage(), e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Reviews revs) {
                reviews.setValue(revs);
            }
        };
        task.execute(movie);
    }
}
