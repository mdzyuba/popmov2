package com.mdzyuba.popularmovies.model;

import java.util.ArrayList;
import java.util.List;

public class VideosCollection {
    private Integer id;

    private List<Video> videos;

    public VideosCollection(Integer id) {
        this.id = id;
        videos = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void addVideo(Video video) {
        videos.add(video);
    }
}
