package com.example.newbiechen.ireader.ui.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import butterknife.ButterKnife;

/**
 * Created by newbiechen on 17-5-12.
 */

public abstract class AdapterImpl<T> extends RelativeLayout implements IAdapter<T> {

    private View view;

    public AdapterImpl(Context context) {
        super(context);

        int id = getContentId();
        view = LayoutInflater.from(context)
                .inflate(id, this, false);
        addView(view);

        initView();
    }

    protected abstract int getContentId();

    protected void initView(){
    }

    protected <V> V findById(int id){
        return (V)ButterKnife.findById(view, id);
    }
}
