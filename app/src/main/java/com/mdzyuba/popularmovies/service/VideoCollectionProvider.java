package com.mdzyuba.popularmovies.service;

import com.mdzyuba.popularmovies.model.VideosCollection;

import java.net.URL;

import androidx.annotation.NonNull;

public class VideoCollectionProvider {
    private final NetworkDataProvider networkDataProvider;

    public VideoCollectionProvider(@NonNull NetworkDataProvider networkDataProvider) {
        this.networkDataProvider = networkDataProvider;
    }

    public VideosCollection getVideos(int movieId) throws Exception {
        MovieApiClient client = new MovieApiClient();
        URL url = client.buildGetVideosUrl(movieId);
        String json = networkDataProvider.getResponseFromHttpUrl(url);
        VideosParser parser = new VideosParser();
        VideosCollection videosCollection = parser.parseVideoCollection(json);
        return videosCollection;
    }
}
