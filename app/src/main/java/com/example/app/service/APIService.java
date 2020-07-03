package com.example.app.service;

import com.hyeoksin.admanager.data.Ad;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface APIService {
    @GET("{baseUrl}")
    Call<List<Ad>> getAds(@Path(value = "baseUrl", encoded = true) String endUrl);
}
