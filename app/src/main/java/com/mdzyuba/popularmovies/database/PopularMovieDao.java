package com.mdzyuba.popularmovies.database;

import com.mdzyuba.popularmovies.model.Movie;
import com.mdzyuba.popularmovies.model.PopularMovie;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface PopularMovieDao {
    @Query("SELECT movie.* FROM popular_movie inner join movie on popular_movie.movie_id = movie.id")
    LiveData<List<Movie>> loadMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PopularMovie movie);

    @Update
    void update(PopularMovie movie);

    @Delete
    void delete(PopularMovie movie);

    @Query("DELETE FROM popular_movie")
    void deleteAll();
}
