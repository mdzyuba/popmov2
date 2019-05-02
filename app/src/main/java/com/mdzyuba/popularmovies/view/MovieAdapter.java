package com.mdzyuba.popularmovies.view;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mdzyuba.popularmovies.R;
import com.mdzyuba.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();

    private final List<Movie> movieList;

    private final MovieClickListener movieClickListener;

    private final Picasso picasso;

    private Context context;

    public interface MovieClickListener {
        void onMovieClick(Movie movie);
    }

    public MovieAdapter(Context context, @NonNull MovieClickListener movieClickListener) {
        this.context = context;
        this.movieList = new ArrayList<>();
        this.movieClickListener = movieClickListener;
        this.picasso = PicassoProvider.getPicasso(context.getApplicationContext());
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final boolean shouldAttachToParentImmediately = false;
        View view = layoutInflater
                .inflate(R.layout.movie_view_item, parent, shouldAttachToParentImmediately);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        if (movie != null) {
            holder.bind(movie);
        } else {
            Log.e(TAG, String.format("The movie in position %d is null", position));
        }
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public void updateMovies(List<Movie> movies) {
        movieList.clear();
        movieList.addAll(movies);
        notifyDataSetChanged();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView imageView;

        MovieViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            imageView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            movieClickListener.onMovieClick(movieList.get(getAdapterPosition()));
        }

        void bind(@NonNull Movie movie) {
            TextView title = itemView.findViewById(R.id.title);
            MoviePosterImageUtil.loadImage(picasso, movie, imageView, title);
        }
    }
}
