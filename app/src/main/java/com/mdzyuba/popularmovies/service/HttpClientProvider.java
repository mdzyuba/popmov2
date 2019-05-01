package com.mdzyuba.popularmovies.service;

import android.content.Context;

import java.io.File;

import androidx.annotation.NonNull;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Provides a singleton instance of the OkHttpClient.
 *
 * According to https://square.github.io/okhttp/3.x/okhttp/,
 * "OkHttp performs best when you create a single OkHttpClient instance and reuse it for all of
 * your HTTP calls."
 */
public class HttpClientProvider {
    private static final int CACHE_SIZE_BYTES = 200 * 1024 * 1024; // 200 Mb
    private static final String HTTP_CACHE = "http-cache";
    private static volatile OkHttpClient client;

    private HttpClientProvider() {
    }

    @NonNull
    public static synchronized OkHttpClient getClient(@NonNull Context context) {
        if (client == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            File cacheDir = new File(context.getCacheDir(), HTTP_CACHE);
            Cache cache = new Cache(cacheDir, CACHE_SIZE_BYTES);
            builder.cache(cache);
            builder.addInterceptor(new HttpLoggingInterceptor());
            client = builder.build();
        }
        return client;
    }
}
