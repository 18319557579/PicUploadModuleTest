package com.example.uploadpic.network;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public enum ServiceCreator {
    INSTANCE;

    private final String BASE_URL = "http://192.168.10.214:80/v1/";

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

    public <T> T create(Class<T> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
