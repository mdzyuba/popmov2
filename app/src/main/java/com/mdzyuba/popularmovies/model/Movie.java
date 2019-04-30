package com.mdzyuba.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "movie")
public class Movie implements Parcelable {

    private static final String TAG = Movie.class.getSimpleName();

    @PrimaryKey
    @NonNull
    private Integer id;
    private String title;
    private String posterPath;
    private Boolean adult;
    private String overview;
    private Date releaseDate;
    @Ignore
    private Integer[] genreIDs;
    private String originalTitle;
    private String originalLanguage;
    private String backdropPath;
    private Integer popularity;
    private Integer voteCount;
    private Boolean video;
    private Float voteAverage;

    private Boolean favorite;

    @Ignore
    private VideosCollection videosCollection;

    public Movie(@NonNull Integer id, String title, String posterPath, Boolean adult,
                 String overview, Date releaseDate, String originalTitle,
                 String originalLanguage, String backdropPath, Integer popularity,
                 Integer voteCount, Boolean video, Float voteAverage, Boolean favorite) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.adult = adult;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.originalTitle = originalTitle;
        this.originalLanguage = originalLanguage;
        this.backdropPath = backdropPath;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.video = video;
        this.voteAverage = voteAverage;
        this.favorite = favorite;
    }

    private Movie(@NonNull Integer id, String title, String posterPath, Boolean adult, String overview, Date releaseDate,
                  Integer[] genreIDs, String originalTitle, String originalLanguage,
                  String backdropPath, Integer popularity, Integer voteCount, Boolean video,
                  Float voteAverage) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.adult = adult;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.genreIDs = genreIDs;
        this.originalTitle = originalTitle;
        this.originalLanguage = originalLanguage;
        this.backdropPath = backdropPath;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.video = video;
        this.voteAverage = voteAverage;
        this.favorite = false;
    }

    @Nullable
    public String getPosterPath() {
        return posterPath;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    @Nullable
    public Boolean getAdult() {
        return adult;
    }

    @Nullable
    public String getOverview() {
        return overview;
    }

    @Nullable
    public Date getReleaseDate() {
        return releaseDate;
    }

    @Nullable
    public Integer[] getGenreIDs() {
        return genreIDs;
    }

    @NonNull
    public Integer getId() {
        return id;
    }

    @Nullable
    public String getOriginalTitle() {
        return originalTitle;
    }

    @Nullable
    public String getOriginalLanguage() {
        return originalLanguage;
    }

    @Nullable
    public String getBackdropPath() {
        return backdropPath;
    }

    @Nullable
    public Integer getPopularity() {
        return popularity;
    }

    @Nullable
    public Integer getVoteCount() {
        return voteCount;
    }

    @Nullable
    public Boolean getVideo() {
        return video;
    }

    @Nullable
    public Float getVoteAverage() {
        return voteAverage;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public VideosCollection getVideosCollection() {
        return videosCollection;
    }

    public void setVideosCollection(VideosCollection videosCollection) {
        this.videosCollection = videosCollection;
    }

    public static class Builder {
        private String title;
        private String posterPath;
        private Boolean adult;
        private String overview;
        private Date releaseDate;
        private Integer[] genreIDs;
        private Integer id;
        private String originalTitle;
        private String originalLanguage;
        private String backdropPath;
        private Integer popularity;
        private Integer voteCount;
        private Boolean video;
        private Float voteAverage;

        public Builder withId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withPosterPath(String posterPath) {
            this.posterPath = posterPath;
            return this;
        }

        public Builder withOverview(String overview) {
            this.overview = overview;
            return this;
        }

        public Builder withReleaseDate(Date releaseDate) {
            this.releaseDate = releaseDate;
            return this;
        }

        public Builder withVoteAverage(Float voteAverage) {
            this.voteAverage = voteAverage;
            return this;
        }

        public Movie build() {
            return new Movie(id, title, posterPath, adult, overview, releaseDate, genreIDs,
                             originalTitle, originalLanguage, backdropPath, popularity,
                             voteCount, video, voteAverage);
        }
    }

    @Override
    public String toString() {
        return "Movie{" + "title='" + title + '\'' + ", posterPath='" + posterPath + '\'' +
               ", adult=" + adult + ", overview='" + overview + '\'' + ", releaseDate=" +
               releaseDate + ", genreIDs=" + Arrays.toString(genreIDs) + ", id=" + id +
               ", originalTitle='" + originalTitle + '\'' + ", originalLanguage='" +
               originalLanguage + '\'' + ", backdropPath='" + backdropPath + '\'' +
               ", popularity=" + popularity + ", voteCount=" + voteCount + ", video=" + video +
               ", voteAverage=" + voteAverage + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;
        Movie movie = (Movie) o;
        return Objects.equals(id, movie.id) && Objects.equals(title, movie.title) &&
               Objects.equals(posterPath, movie.posterPath) && Objects.equals(adult, movie.adult) &&
               Objects.equals(overview, movie.overview) &&
               Objects.equals(releaseDate, movie.releaseDate) &&
               Arrays.equals(genreIDs, movie.genreIDs) &&
               Objects.equals(originalTitle, movie.originalTitle) &&
               Objects.equals(originalLanguage, movie.originalLanguage) &&
               Objects.equals(backdropPath, movie.backdropPath) &&
               Objects.equals(popularity, movie.popularity) &&
               Objects.equals(voteCount, movie.voteCount) && Objects.equals(video, movie.video) &&
               Objects.equals(voteAverage, movie.voteAverage);
    }

    @Override
    public int hashCode() {
        int result =
                Objects.hash(id, title, posterPath, adult, overview, releaseDate, originalTitle,
                             originalLanguage, backdropPath, popularity, voteCount, video,
                             voteAverage);
        result = 31 * result + Arrays.hashCode(genreIDs);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.title);
        dest.writeString(this.posterPath);
        dest.writeValue(this.adult);
        dest.writeString(this.overview);
        dest.writeLong(this.releaseDate != null ? this.releaseDate.getTime() : -1);
        dest.writeArray(this.genreIDs);
        dest.writeString(this.originalTitle);
        dest.writeString(this.originalLanguage);
        dest.writeString(this.backdropPath);
        dest.writeValue(this.popularity);
        dest.writeValue(this.voteCount);
        dest.writeValue(this.video);
        dest.writeValue(this.voteAverage);
    }

    protected Movie(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.title = in.readString();
        this.posterPath = in.readString();
        this.adult = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.overview = in.readString();
        long tmpReleaseDate = in.readLong();
        this.releaseDate = tmpReleaseDate == -1 ? null : new Date(tmpReleaseDate);
        this.genreIDs = (Integer[]) in.readArray(Integer[].class.getClassLoader());
        this.originalTitle = in.readString();
        this.originalLanguage = in.readString();
        this.backdropPath = in.readString();
        this.popularity = (Integer) in.readValue(Integer.class.getClassLoader());
        this.voteCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.video = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.voteAverage = (Float) in.readValue(Float.class.getClassLoader());
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
