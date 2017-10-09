package com.example.newbiechen.ireader.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.local.Void;
import com.example.newbiechen.ireader.utils.PermissionsChecker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by newbiechen on 17-4-14.
 */

public class SplashActivity extends AppCompatActivity {
    private static final int WAIT_TIME = 3000;
    private static final int PERMISSIONS_REQUEST_STORAGE = 0;

    static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @BindView(R.id.splash_tv_skip)
    TextView mTvSkip;

    private Unbinder unbinder;
    private Runnable skipRunnable;
    private PermissionsChecker mPermissionsChecker;

    private boolean isSkip = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        unbinder = ButterKnife.bind(this);

        skipRunnable = () -> skipToMain();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            mPermissionsChecker = new PermissionsChecker(this);
            requestPermission();
        }
        else {
            //自动跳过
            mTvSkip.postDelayed(skipRunnable,WAIT_TIME);
            //点击跳过
            mTvSkip.setOnClickListener((view) -> skipToMain());
        }
    }

    private void requestPermission(){
        //获取读取和写入SD卡的权限
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)){
            //是否应该展示详细信息
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                //请求权限
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSIONS_REQUEST_STORAGE);
            }
        }
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_STORAGE: {
/*                // 如果取消权限，则返回的值为0
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //申请权限成功

                } else {
                    //申请权限失败
                }*/

                //自动跳过
                mTvSkip.postDelayed(skipRunnable,WAIT_TIME);
                //点击跳过
                mTvSkip.setOnClickListener((view) -> skipToMain());
                return;
            }
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
