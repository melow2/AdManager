package com.example.app.service;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private static Retrofit retrofit;
    private static final String TAG = RetrofitInstance.class.getSimpleName();

    private RetrofitInstance(String url) {
        retrofit = new Retrofit
                .Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();
    }

    public static Retrofit getClient(String url) {
        if (retrofit == null) {
            new RetrofitInstance(url);
        }
        return retrofit;
    }
}
