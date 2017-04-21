package com.example.newbiechen.ireader.model.net;

import com.example.newbiechen.ireader.utils.Constant;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by newbiechen on 17-4-20.
 */

public class NetWorkHelper {
    private static NetWorkHelper sInstance;
    private Retrofit mRetrofit;

    private NetWorkHelper(){
        mRetrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Constant.API_BASE_URL)
                .build();
    }

    public static NetWorkHelper getInstance(){
        if (sInstance == null){
            synchronized (NetWorkHelper.class){
                if (sInstance == null){
                    sInstance = new NetWorkHelper();
                }
            }
        }
        return sInstance;
    }

    public Retrofit getRetrofit(){
        return mRetrofit;
    }
}
