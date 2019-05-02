package com.mdzyuba.popularmovies.view;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mdzyuba.popularmovies.R;
import com.mdzyuba.popularmovies.model.Movie;
import com.mdzyuba.popularmovies.service.MovieApiClient;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.net.URL;

import androidx.annotation.NonNull;


public class MoviePosterImageUtil {

    private static final String TAG = MoviePosterImageUtil.class.getSimpleName();

    public static void loadImage(@NonNull Picasso picasso,
                                 @NonNull Movie movie,
                                 @NonNull View itemView) {
        ImageView imageView = itemView.findViewById(R.id.iv_poster);
        TextView title = itemView.findViewById(R.id.poster_title);
        String posterPath = movie.getPosterPath();
        if (posterPath == null) {
            Log.w(TAG, "The poster path is null");
            showPlaceholder(imageView, title, movie);
            return;
        }
        MovieApiClient movieApiClient = new MovieApiClient();
        URL imageUrl = movieApiClient.getImageUri(posterPath);
        if (imageUrl == null) {
            showPlaceholder(imageView, title, movie);
            return;
        }
        String path = imageUrl.toString();
        ProgressBar progressBar = itemView.findViewById(R.id.loading_progress);
        picasso.load(path)
               .placeholder(R.drawable.image_placeholder)
               .into(imageView, new Callback() {
                   @Override
                   public void onSuccess() {
                       progressBar.setVisibility(View.GONE);
                   }

                   @Override
                   public void onError(Exception e) {
                       progressBar.setVisibility(View.GONE);
                       showPlaceholder(imageView, title, movie);
                   }
               });
        imageView.setContentDescription(movie.getTitle());
    }

    private static void showPlaceholder(@NonNull ImageView imageView,
                                        @NonNull TextView title,
                                        @NonNull Movie movie) {
        imageView.setImageResource(R.drawable.image_placeholder);
        updateMoviePosterPlaceholder(title, movie);
    }

    private static void updateMoviePosterPlaceholder(@NonNull TextView title, @NonNull Movie movie) {
        String movieTitle = movie.getTitle();
        if (movie.getPosterPath() == null && movieTitle != null) {
            title.setText(movieTitle.trim());
            title.setVisibility(View.VISIBLE);
        } else {
            title.setText("");
            title.setVisibility(View.GONE);
        }
    }
}
