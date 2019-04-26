package com.mdzyuba.popularmovies.service;

import com.mdzyuba.popularmovies.model.VideosCollection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
public class VideosParserTest {

    @Test
    public void parseVideoCollection() throws Exception {
        TestDataUtils resourceUtils = new TestDataUtils();
        String json = resourceUtils.readMovieVideosJsonResponse();
        assertNotNull("The json is null", json);

        VideosParser videosParser = new VideosParser();
        VideosCollection videosCollection = videosParser.parseVideoCollection(json);

        TestDataUtils.assertSampleVideoCollection(videosCollection);
    }

}