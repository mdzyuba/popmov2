package com.mdzyuba.popularmovies;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mdzyuba.popularmovies.model.Movie;
import com.mdzyuba.popularmovies.model.Reviews;
import com.mdzyuba.popularmovies.model.Video;
import com.mdzyuba.popularmovies.model.VideosCollection;
import com.mdzyuba.popularmovies.view.MovieDetailsViewModel;
import com.mdzyuba.popularmovies.view.MovieDetailsViewModelFactory;
import com.mdzyuba.popularmovies.view.MoviePosterImageUtil;
import com.mdzyuba.popularmovies.view.PicassoProvider;
import com.mdzyuba.popularmovies.view.SharedViewModel;
import com.mdzyuba.popularmovies.view.SharedViewModelFactory;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

/**
 * Displays a Movie details.
 */
public class MovieDetailsActivity extends AppCompatActivity {
    private static final String TAG = MovieDetailsActivity.class.getSimpleName();
    private static final String KEY_MOVIE = "MOVIE";
    private static final String YOUTUBE = "https://www.youtube.com/watch?v=%s";

    private Movie movie;
    private Picasso picasso;
    private MovieDetailsViewModel viewModel;
    private SharedViewModel sharedViewModel;
    private CheckBox favoriteCb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (movie == null) {
            Intent intent = getIntent();
            if (intent.hasExtra(KEY_MOVIE)) {
                movie = intent.getParcelableExtra(KEY_MOVIE);
            }
        }

        if (movie == null) {
            Log.e(TAG, "Missing movie parameter");
            return;
        }

        setContentView(R.layout.activity_movie_details);
        MovieDetailsViewModelFactory factory = new MovieDetailsViewModelFactory(getApplication(), movie);
        viewModel = ViewModelProviders.of(this, factory).get(MovieDetailsViewModel.class);

        SharedViewModelFactory sharedViewModelFactory = new SharedViewModelFactory();
        sharedViewModel = ViewModelProviders.of(this, sharedViewModelFactory).get(SharedViewModel.class);

        if (picasso == null) {
            picasso = PicassoProvider.getPicasso(this.getApplicationContext());
        }

        viewModel.videosCollection.observe(this, new Observer<VideosCollection>() {
            @Override
            public void onChanged(VideosCollection videosCollection) {
                Log.d(TAG, "videos are ready: " + videosCollection);
                initTrailers(videosCollection);
            }
        });

        viewModel.reviews.observe(this, new Observer<Reviews>() {
            @Override
            public void onChanged(Reviews reviews) {
                Log.d(TAG, "reviews are ready: " + reviews);
                initReviews(reviews);
            }
        });

        if (movie != null) {
            initView();
        }
    }

    private void initTrailers(VideosCollection videosCollection) {
        if (videosCollection == null || videosCollection.getVideos().isEmpty()) {
            // no trailers available
            TextView noTrailers = findViewById(R.id.tv_no_trailers);
            noTrailers.setVisibility(View.VISIBLE);
            return;
        }
        LayoutInflater layoutInflater = LayoutInflater.from(MovieDetailsActivity.this);
        ViewGroup trailersView = findViewById(R.id.trailers);
        int i = 1;
        for (Video video: videosCollection.getVideos()) {
            View trailerView = layoutInflater.inflate(R.layout.trailer, trailersView, false);
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Video trailer = (Video) view.getTag();
                    Uri uri = Uri.parse(String.format(YOUTUBE, trailer.key));
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    MovieDetailsActivity.this.startActivity(intent);
                }
            };

            TextView text = trailerView.findViewById(R.id.text);
            if (!TextUtils.isEmpty(video.name)) {
                text.setText(video.name);
            } else {
                text.setText(getString(R.string.trailer, String.valueOf(i++)));
            }
            text.setTag(video);
            text.setOnClickListener(clickListener);

            Button playButton = trailerView.findViewById(R.id.play);
            playButton.setTag(video);
            playButton.setOnClickListener(clickListener);
            trailersView.addView(trailerView);
        }
    }

    private void initReviews(Reviews reviews) {
        if (reviews == null || reviews.results.isEmpty()) {
            // no reviews available
            TextView noReviews = findViewById(R.id.tv_no_reviews);
            noReviews.setVisibility(View.VISIBLE);
            return;
        }
        LayoutInflater layoutInflater = LayoutInflater.from(MovieDetailsActivity.this);
        ViewGroup reviewsContainer = findViewById(R.id.reviews);
        for (Reviews.Review review: reviews.results) {
            View reviewView = layoutInflater.inflate(R.layout.review_item_card, reviewsContainer, false);

            TextView authorTv = reviewView.findViewById(R.id.tv_author);
            authorTv.setText(review.author);
            TextView contentView = reviewView.findViewById(R.id.tv_content);
            contentView.setText(review.content);

            reviewsContainer.addView(reviewView);
        }
        if (reviews.canGetMorePages()) {
            View showMoreView = layoutInflater.inflate(R.layout.load_more_reviews, reviewsContainer, false);
            reviewsContainer.addView(showMoreView);
            Button showMoreReviewsButton = showMoreView.findViewById(R.id.show_more_reviews_button);
            showMoreReviewsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewModel.loadOrUpdateReviews(movie);
                }
            });
        }
    }

    @SuppressLint("DefaultLocale")
    private void initView() {
        initTextView(R.id.tv_title, movie.getTitle());
        initReleaseYear();
        if (movie.getVoteAverage() != null) {
            initTextView(R.id.tv_rating, String.format("%1.1f/10", movie.getVoteAverage()));
        }
        initPosterImage();
        initTextView(R.id.tv_overview, movie.getOverview());
        initFavoriteCheckBox();
    }

    private void initFavoriteCheckBox() {
        favoriteCb = findViewById(R.id.cb_favorite);
        viewModel.favorite.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean favorite) {
                favoriteCb.setChecked(favorite);
                // If a user cleared the movie favorite flag,
                // refresh the list of favorite movies once we navigate back
                // to the main activity.
                if (!favorite) {
                    sharedViewModel.getRefreshFavoriteMovies().postValue(true);
                }
            }
        });
        favoriteCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                if (checked) {
                    viewModel.markMovieAsFavorite(movie);
                } else {
                    viewModel.clearMovieFavoriteFlag(movie);
                }
            }
        });
    }

    private void initTextView(int resourceId, String text) {
        TextView overviewTextView = findViewById(resourceId);
        if (text != null) {
            overviewTextView.setText(text);
        } else {
            overviewTextView.setText(R.string.no_data);
        }
    }

    private void initPosterImage() {
        ViewGroup parentView = findViewById(R.id.page_layout);
        MoviePosterImageUtil.loadImage(picasso, movie, parentView);
    }

    private void initReleaseYear() {
        TextView releaseYearTextView = findViewById(R.id.tv_release_year);
        Date releaseDate = movie.getReleaseDate();
        if (releaseDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(releaseDate);
            String text = Integer.toString(calendar.get(Calendar.YEAR));
            releaseYearTextView.setText(text);
        } else {
            releaseYearTextView.setText(R.string.no_data);
        }
    }

    public static Intent createIntent(Context context, Movie movie) {
        Intent intent = new Intent(context, MovieDetailsActivity.class);
        intent.putExtra(KEY_MOVIE, movie);
        return intent;
    }
}
