package com.example.newbiechen.ireader.ui.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.local.Void;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by newbiechen on 17-4-14.
 */

public class SplashActivity extends AppCompatActivity {
    private static final int WAIT_TIME = 3000;

    @BindView(R.id.splash_tv_skip)
    TextView mTvSkip;

    private Unbinder unbinder;
    private Runnable skipRunnable;

    private boolean isSkip = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        unbinder = ButterKnife.bind(this);
        skipRunnable = () -> skipToMain();
        //自动跳过
        mTvSkip.postDelayed(skipRunnable,WAIT_TIME);
        //点击跳过
        mTvSkip.setOnClickListener((view) -> skipToMain());
    }

    private synchronized void skipToMain(){
        if (!isSkip){
            isSkip = true;
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isSkip = true;
        mTvSkip.removeCallbacks(skipRunnable);
        unbinder.unbind();
    }
}
