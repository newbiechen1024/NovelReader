package com.example.newbiechen.ireader.model.remote;

import com.example.newbiechen.ireader.utils.Constant;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by newbiechen on 17-4-20.
 */

public class RemoteHelper {
    private static RemoteHelper sInstance;
    private Retrofit mRetrofit;

    private RemoteHelper(){
        mRetrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Constant.API_BASE_URL)
                .build();
    }

    public static RemoteHelper getInstance(){
        if (sInstance == null){
            synchronized (RemoteHelper.class){
                if (sInstance == null){
                    sInstance = new RemoteHelper();
                }
            }
        }
        return sInstance;
    }

    public Retrofit getRetrofit(){
        return mRetrofit;
    }
}
