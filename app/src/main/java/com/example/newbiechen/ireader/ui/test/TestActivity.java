package com.example.newbiechen.ireader.ui.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.service.DownloadService;

/**
 * Created by newbiechen on 17-5-5.
 */

public class TestActivity extends Activity {
    private static final String TAG = "TestActivity";
/*    private List<DownloadTask> mTaskList = new ArrayList<>();*/
    private int count = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        startService(new Intent(this, DownloadService.class));
    }

    public void onClick(View view){
    }
}
