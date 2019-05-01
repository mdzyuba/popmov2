package com.mdzyuba.popularmovies.database;

import com.mdzyuba.popularmovies.model.Movie;
import com.mdzyuba.popularmovies.model.TopMovie;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface TopMovieDao {
    @Query("SELECT movie.* FROM top_movie inner join movie on top_movie.movie_id = movie.id")
    LiveData<List<Movie>> loadMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TopMovie movie);

    @Update
    void update(TopMovie movie);

    @Delete
    void delete(TopMovie movie);

    @Query("DELETE FROM top_movie")
    void deleteAll();
}
