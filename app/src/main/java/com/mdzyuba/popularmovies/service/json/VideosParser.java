package com.mdzyuba.popularmovies.service.json;

import com.mdzyuba.popularmovies.model.Video;
import com.mdzyuba.popularmovies.model.VideosCollection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.Nullable;

public class VideosParser {

    private static final String ID = "id";
    private static final String RESULTS = "results";
    private static final String ISO_639_1 = "iso_639_1";
    private static final String ISO_3166_1 = "iso_3166_1";
    private static final String KEY = "key";
    private static final String NAME = "name";
    private static final String SITE = "site";
    private static final String SIZE = "size";
    private static final String TYPE = "type";

    @Nullable
    public VideosCollection parseVideoCollection(@Nullable String json) throws JSONException {
        if (json == null) {
            return null;
        }

        JSONObject jsonObject = new JSONObject(json);

        Integer id = jsonObject.optInt(ID, 0);
        VideosCollection videosCollection = new VideosCollection(id);

        JSONArray results = jsonObject.optJSONArray(RESULTS);
        for (int i = 0; i < results.length(); i++) {
            Video video = parseVideo(results.optJSONObject(i));
            if (video != null) {
                videosCollection.addVideo(video);
            }
        }
        return videosCollection;
    }

    private Video parseVideo(@Nullable JSONObject jsonVideo) {
        if (jsonVideo == null) {
            return null;
        }
        String id = jsonVideo.optString(ID);
        String iso_639_1 = jsonVideo.optString(ISO_639_1);
        String iso_3166_1 = jsonVideo.optString(ISO_3166_1);
        String key = jsonVideo.optString(KEY);
        String name = jsonVideo.optString(NAME);
        String site = jsonVideo.optString(SITE);
        int size = jsonVideo.optInt(SIZE);
        String type = jsonVideo.optString(TYPE);
        Video video = new Video(id, iso_639_1, iso_3166_1, key, name, site, size, type);
        return video;
    }

}
