package com.example.newbiechen.ireader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.ui.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by newbiechen on 17-4-22.
 */

public class DiscussionDetailActivity extends BaseActivity {

    private static final String EXTRA_TYPE = "extra_type";
    private static final String EXTRA_DETAIL_ID = "extra_detail_id";
    private String typeName = "";
    public static void startActivity(Context context,String typeName,String detailId){
        Intent intent = new Intent(context,DiscussionDetailActivity.class);
        intent.putExtra(EXTRA_TYPE,typeName);
        intent.putExtra(EXTRA_DETAIL_ID,detailId);
        context.startActivity(intent);
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_discussion_detail;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        typeName = getIntent().getStringExtra(EXTRA_TYPE);
    }

    @Override
    protected void setUpToolbar(Toolbar toolbar) {
        super.setUpToolbar(toolbar);
        getSupportActionBar().setTitle("详情");
    }

    @Override
    protected void processLogic() {
        super.processLogic();
        String [] typeNames = getResources().getStringArray(R.array.nb_fragment_section);
        Fragment fragment = null;
        if (typeNames[0].equals(typeName)){
            //创建
        }
        else if (typeNames[2].equals(typeName)){
            //创建
        }
        else if (typeNames[3].equals(typeName)){
            //创建
        }
        else if (typeNames[4].equals(typeName)){
            //
        }
        getSupportFragmentManager().beginTransaction()
                .add(R.id.discussion_detail_fl,fragment)
                .commit();
    }
}
