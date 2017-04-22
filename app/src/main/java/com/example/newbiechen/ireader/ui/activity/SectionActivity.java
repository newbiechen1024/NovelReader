package com.example.newbiechen.ireader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.RxBus;
import com.example.newbiechen.ireader.event.SelectorEvent;
import com.example.newbiechen.ireader.model.Selection;
import com.example.newbiechen.ireader.ui.base.BaseActivity;
import com.example.newbiechen.ireader.ui.fragment.BookHelpsFragment;
import com.example.newbiechen.ireader.ui.fragment.BookReviewFragment;
import com.example.newbiechen.ireader.ui.fragment.DiscussionFragment;
import com.example.newbiechen.ireader.utils.Constant;
import com.example.newbiechen.ireader.widget.SelectorView;

import butterknife.BindView;

/**
 * Created by newbiechen on 17-4-17.
 */

public class SectionActivity extends BaseActivity implements SelectorView.OnItemSelectedListener {
    private static final String TAG = "SectionActivity";

    private static final String EXTRA_COMMUNITY = "extra_community";

    private String mSection = "";

    private String sortType = Constant.SortType.DEFAULT;
    private String distillate = Constant.Distillate.ALL;
    private String bookType = Constant.BookType.ALL;
    @BindView(R.id.section_sv_selector)
    SelectorView mSvSelector;

    @Override
    protected int getContentId(){
        return R.layout.activity_section;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mSection = getIntent().getStringExtra(EXTRA_COMMUNITY);
        if (savedInstanceState != null){
            mSection = getIntent().getStringExtra(EXTRA_COMMUNITY);
        }
    }

    @Override
    protected void setUpToolbar(Toolbar toolbar) {
        getSupportActionBar().setTitle(mSection);
    }

    @Override
    protected void initClick() {
        super.initClick();
        mSvSelector.setOnItemSelectedListener(this);
    }

    @Override
    protected void processLogic() {
        String [] sections = getResources().getStringArray(R.array.nb_fragment_section);
        Fragment fragment = null;
        if (sections[0].equals(mSection)){
            mSvSelector.setSelectData(Selection.DISTILLATE.getParams(),
                    Selection.SORT_TYPE.getParams());
            fragment = DiscussionFragment.newInstance(Constant.Block.COMMENT);
        }
        else if (sections[1].equals(mSection)){
            mSvSelector.setSelectData(Selection.DISTILLATE.getParams(),
                    Selection.BOOK_TYPE.getParams(),Selection.SORT_TYPE.getParams());
            fragment = new BookReviewFragment();
        }
        else if (sections[2].equals(mSection)){
            mSvSelector.setSelectData(Selection.DISTILLATE.getParams(),
                    Selection.SORT_TYPE.getParams());
            fragment = new BookHelpsFragment();
        }
        else if (sections[3].equals(mSection)){
            mSvSelector.setSelectData(Selection.DISTILLATE.getParams(),
                    Selection.SORT_TYPE.getParams());
            fragment = DiscussionFragment.newInstance(Constant.Block.GIRL);
        }
        else if (sections[4].equals(mSection)){
            mSvSelector.setSelectData(Selection.DISTILLATE.getParams(),
                    Selection.SORT_TYPE.getParams());
            fragment = DiscussionFragment.newInstance(Constant.Block.ORIGIN);
        }

        getSupportFragmentManager().beginTransaction()
                .add(R.id.section_fl,fragment)
                .commit();
    }

    public static void startActivity(Context context, String community){
        Intent intent = new Intent(context,SectionActivity.class);
        intent.putExtra(EXTRA_COMMUNITY,community);

        context.startActivity(intent);
    }

    @Override
    public void onItemSelected(int type, int pos) {
        //转换器
        switch (type) {
            case 0:
                switch (pos) {
                    case 0:
                        distillate = Constant.Distillate.ALL;
                        break;
                    case 1:
                        distillate = Constant.Distillate.DISTILLATE;
                        break;
                    default:
                        break;
                }
                break;
            case 1:
                if (mSvSelector.getChildCount() == 2) {
                    //当size = 2的时候，就会到Sort这里。
                    sortType = Constant.sortTypeList.get(pos);
                } else if (mSvSelector.getChildCount() == 3) {
                    bookType = Constant.bookTypeList.get(pos);
                }
                break;
            case 2:
                sortType = Constant.sortTypeList.get(pos);
                break;
            default:
                break;
        }
        //RxBus发挥的时候
        RxBus.getInstance()
                .post(Constant.MSG_SELECTOR,new SelectorEvent(distillate,bookType,sortType));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_COMMUNITY,mSection);
    }
}
