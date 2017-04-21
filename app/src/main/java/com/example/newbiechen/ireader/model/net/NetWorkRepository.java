package com.example.newbiechen.ireader.model.net;

import com.example.newbiechen.ireader.model.bean.BookHelpsBean;
import com.example.newbiechen.ireader.model.bean.BookReviewBean;
import com.example.newbiechen.ireader.model.bean.DiscussionBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Retrofit;

/**
 * Created by newbiechen on 17-4-20.
 */

public class NetWorkRepository{
    private static final String TAG = "NetWorkRepository";

    private static NetWorkRepository sInstance;
    private Retrofit mRetrofit;
    private BookApi mBookApi;

    private NetWorkRepository(){
        mRetrofit = NetWorkHelper.getInstance()
                .getRetrofit();

        mBookApi = mRetrofit.create(BookApi.class);
    }

    public static NetWorkRepository getInstance(){
        if (sInstance == null){
            synchronized (NetWorkHelper.class){
                if (sInstance == null){
                    sInstance = new NetWorkRepository();
                }
            }
        }
        return sInstance;
    }

    public Observable<List<DiscussionBean>> getDiscussionBeanList(String block, String sort, int start, int limit, String distillate){
        return mBookApi.getBookDisscussionList(block,"all",sort,"all",start+"",limit+"",distillate)
                .map((listBean)-> listBean.getPosts());
    }

    public Observable<List<BookHelpsBean>> getBookHelpsBeanList(String sort, int start, int limit, String distillate){
        return mBookApi.getBookHelpList("all",sort,start+"",limit+"",distillate)
                .map((listBean)-> listBean.getHelps());
    }

    public Observable<List<BookReviewBean>> getBookReviewBeanList(String sort,String bookType,int start,int limited,String distillate){
        return mBookApi.getBookReviewList("all",sort,bookType,start+"",limited+"",distillate)
                .map(listBean-> listBean.getReviews());
    }
}
