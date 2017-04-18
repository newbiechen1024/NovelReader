package com.example.newbiechen.ireader;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by newbiechen on 17-4-18.
 * 原理:PublishSubject本身作为转发者和接受者
 */

public class RxBus{
    private static volatile RxBus sInstance;
    private final PublishSubject<Object> mEventBus = PublishSubject.create();

    public static RxBus getInstance(){
        if (sInstance == null){
            synchronized (RxBus.class){
                if (sInstance == null){
                    sInstance = new RxBus();
                }
            }
        }
        return sInstance;
    }

    public void post(Object target){
        mEventBus.onNext(target);
    }

    public void post(int code,Object target){
        Message msg = new Message(code,target);
        mEventBus.onNext(msg);
    }

    public Observable toObservable(){
        return mEventBus;
    }

    public <T> Observable<T> toObservable(Class<T> cls){
        //ofType起到过滤的作用
        return mEventBus.ofType(cls);
    }

    public <T> Observable<T> toObservable(int code,Class<T> cls){
        return mEventBus.ofType(Message.class)
                .filter(msg -> msg.code == code && cls.isInstance(msg.value))
                .map( msg -> (T)msg.value);

    }

    class Message{
        int code;
        Object value;

        public Message(int code,Object value){
            this.code = code;
            this.value = value;
        }
    }
}
