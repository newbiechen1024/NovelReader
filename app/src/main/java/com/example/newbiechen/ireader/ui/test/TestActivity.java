package com.example.newbiechen.ireader.ui.test;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.ui.adapter.HorizonTagAdapter;
import com.example.newbiechen.ireader.ui.base.BaseListAdapter;
import com.example.newbiechen.ireader.utils.LogUtils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by newbiechen on 17-5-5.
 */

public class TestActivity extends Activity {
    private static final String TAG = "TestActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }


}
