package com.mdzyuba.popularmovies.view;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.mdzyuba.popularmovies.database.FavoriteMovieDao;
import com.mdzyuba.popularmovies.database.MovieDao;
import com.mdzyuba.popularmovies.database.MovieDatabase;
import com.mdzyuba.popularmovies.model.FavoriteMovie;
import com.mdzyuba.popularmovies.model.Movie;
import com.mdzyuba.popularmovies.model.Reviews;
import com.mdzyuba.popularmovies.model.VideosCollection;
import com.mdzyuba.popularmovies.service.NetworkDataProvider;
import com.mdzyuba.popularmovies.service.ReviewsProvider;
import com.mdzyuba.popularmovies.service.VideoCollectionProvider;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class MovieDetailsViewModel extends AndroidViewModel {

    private static final String TAG = MovieDetailsViewModel.class.getSimpleName();

    public final MutableLiveData<Boolean> favorite;

    public final MutableLiveData<VideosCollection> videosCollection;

    public final MutableLiveData<Reviews> reviews;

    private final NetworkDataProvider networkDataProvider;

    private final MovieDatabase db;

    public MovieDetailsViewModel(@NonNull Application application) {
        super(application);
        networkDataProvider = new NetworkDataProvider(getApplication());
        favorite = new MutableLiveData<>();
        videosCollection = new MutableLiveData<>();
        reviews = new MutableLiveData<>();
        db = MovieDatabase.getInstance(getApplication());
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
                Reviews movieReviews = mv.getReviews();
                if (movieReviews != null && !movieReviews.canGetMorePages()) {
                    return movieReviews;
                }
                try {
                    ReviewsProvider provider = new ReviewsProvider(networkDataProvider);
                    int page = getNextPage(mv);
                    Reviews reviews = provider.getReviews(mv.getId(), page);
                    mv.setOrUpdateReviews(reviews);
                    return reviews;
                } catch (Exception e) {
                    Log.e(TAG, "Error: " + e.getMessage(), e);
                }
                return null;
            }

            private int getNextPage(Movie mv) {
                int page = 1;
                if (mv.getReviews() != null) {
                    Reviews movieReviews = mv.getReviews();
                    if (movieReviews.getPage() < movieReviews.getTotalPages()) {
                        page = movieReviews.getPage() + 1;
                    }
                }
                return page;
            }

            @Override
            protected void onPostExecute(Reviews revs) {
                reviews.setValue(revs);
            }
        };
        task.execute(movie);
    }

    public void loadFavoriteFlag(final Movie movie) {
        FavoriteMovieDao favoriteMovieDao = db.favoriteMovieDao();
        LiveData<List<FavoriteMovie>> favMovie = favoriteMovieDao.loadFavoriteMovie(movie.getId());
        favMovie.observeForever(new Observer<List<FavoriteMovie>>() {
            @Override
            public void onChanged(List<FavoriteMovie> favoriteMovies) {
                if (favoriteMovies != null && !favoriteMovies.isEmpty()) {
                    favorite.postValue(true);
                } else {
                    favorite.postValue(false);
                }
            }
        });
    }

    public void clearMovieFavoriteFlag(final Movie movie) {
        AsyncTask<Movie, Void, Void> deleteFavoriteMovie = new AsyncTask<Movie, Void, Void>() {
            @Override
            protected Void doInBackground(Movie... movies) {
                Movie mv = movies[0];
                FavoriteMovieDao favoriteMovieDao = db.favoriteMovieDao();
                favoriteMovieDao.deleteByMovieId(mv.getId());
                return null;
            }
        };
        deleteFavoriteMovie.execute(movie);
    }

    public void markMovieAsFavorite(final Movie movie) {
        AsyncTask<Movie, Void, Void> addFavoriteMovie = new AsyncTask<Movie, Void, Void>() {
            @Override
            protected Void doInBackground(Movie... movies) {
                Movie mv = movies[0];
                MovieDao movieDao = db.movieDao();
                movieDao.insert(mv);
                FavoriteMovieDao favoriteMovieDao = db.favoriteMovieDao();
                FavoriteMovie favoriteMovie = new FavoriteMovie(mv.getId());
                favoriteMovieDao.insert(favoriteMovie);
                return null;
            }
        };
        addFavoriteMovie.execute(movie);
    }
}
