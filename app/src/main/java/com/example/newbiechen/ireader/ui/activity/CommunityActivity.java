package com.example.newbiechen.ireader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.ui.base.BaseActivity;
import com.example.newbiechen.ireader.ui.fragment.CommentFragment;
import com.example.newbiechen.ireader.widget.SelectorView;

import butterknife.BindView;

/**
 * Created by newbiechen on 17-4-17.
 */

public class CommunityActivity extends BaseActivity {
    private static final String EXTRA_COMMUNITY = "extra_community";

    private String mSection = "";

    @BindView(R.id.section_sv_selector)
    SelectorView mSvSelector;

    @Override
    protected int getContentId() {
        return R.layout.activity_section;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mSection = getIntent().getStringExtra(EXTRA_COMMUNITY);
    }

    @Override
    protected void setUpToolbar(Toolbar toolbar) {
        toolbar.setTitle(mSection);
    }

    @Override
    protected void processLogic() {
        String [] sections = getResources().getStringArray(R.array.nb_fragment_section);
        Fragment fragment = null;
        if (sections[0].equals(mSection)){
           fragment = new CommentFragment();
        }
        else if (sections[1].equals(mSection)){

        }
        else if (sections[2].equals(mSection)){

        }
        else if (sections[3].equals(mSection)){

        }
        else if (sections[4].equals(mSection)){

        }

        if (fragment != null){
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            transaction.replace(R.id.section_fl,fragment);
        }
    }

    public static void startActivity(Context context, String community){
        Intent intent = new Intent(context,CommunityActivity.class);
        intent.putExtra(EXTRA_COMMUNITY,community);
        context.startActivity(intent);
    }

}
