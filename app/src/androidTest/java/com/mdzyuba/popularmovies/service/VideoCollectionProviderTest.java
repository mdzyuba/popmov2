package com.mdzyuba.popularmovies.service;

import android.content.Context;

import com.mdzyuba.popularmovies.model.Video;
import com.mdzyuba.popularmovies.model.VideosCollection;

import org.junit.Test;

import java.util.List;

import androidx.test.platform.app.InstrumentationRegistry;

import static org.junit.Assert.*;

public class VideoCollectionProviderTest {

    @Test
    public void getVideos() throws Exception {
        Context appContext = InstrumentationRegistry.getInstrumentation().getContext();
        NetworkDataProvider networkDataProvider = new NetworkDataProvider(appContext);
        VideoCollectionProvider provider = new VideoCollectionProvider(networkDataProvider);
        VideosCollection videosCollection = provider.getVideos(399579);
        assertSampleVideoCollection(videosCollection);
    }

    public static void assertSampleVideoCollection(VideosCollection videosCollection) {
        assertNotNull(videosCollection);
        assertEquals(399579L, (long) videosCollection.getId());

        List<Video> videoList = videosCollection.getVideos();
        assertEquals(15, videoList.size());

        Video video = videoList.get(0);
        assertEquals("5a37e67d0e0a264cd01ede50", video.id);
        assertEquals("en", video.iso_639_1);
        assertEquals("US", video.iso_3166_1);
        assertEquals("QvHv-99VfcU", video.key);
        assertEquals("Behind the Scenes with James Cameron and Robert Rodriguez", video.name);
        assertEquals("YouTube", video.site);
        assertEquals(1080, video.size);
        assertEquals("Featurette", video.type);
    }
}