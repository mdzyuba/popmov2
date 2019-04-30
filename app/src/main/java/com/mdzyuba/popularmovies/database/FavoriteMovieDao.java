package com.mdzyuba.popularmovies.database;

import com.mdzyuba.popularmovies.model.FavoriteMovie;
import com.mdzyuba.popularmovies.model.Movie;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface FavoriteMovieDao {
    @Query("SELECT movie.* FROM favorite_movie inner join movie on favorite_movie.movie_id = movie.id")
    LiveData<List<Movie>> loadAllFavoriteMovies();

    @Query("SELECT * FROM favorite_movie WHERE movie_id = :movieId")
    LiveData<List<FavoriteMovie>> loadFavoriteMovie(int movieId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FavoriteMovie movie);

    @Update
    void update(FavoriteMovie movie);

    @Delete
    void delete(FavoriteMovie movie);

    @Query("DELETE FROM favorite_movie WHERE movie_id = :movieId")
    void deleteByMovieId(int movieId);

    @Query("DELETE FROM favorite_movie")
    void deleteAll();
}
