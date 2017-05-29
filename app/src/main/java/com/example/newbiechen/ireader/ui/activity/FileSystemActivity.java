package com.example.newbiechen.ireader.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.ui.base.BaseTabActivity;
import com.example.newbiechen.ireader.ui.fragment.FileCategoryFragment;
import com.example.newbiechen.ireader.ui.fragment.LocalBookFragment;
import com.example.newbiechen.ireader.ui.fragment.OnFileCheckedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * Created by newbiechen on 17-5-27.
 */

public class FileSystemActivity extends BaseTabActivity {
    private static final String TAG = "FileSystemActivity";

    @BindView(R.id.file_system_cb_selected_all)
    CheckBox mCbSelectAll;
    @BindView(R.id.file_system_btn_delete)
    Button mBtnDelete;
    @BindView(R.id.file_system_btn_add_book)
    Button mBtnAddBook;

    private LocalBookFragment mLocalFragment;
    private FileCategoryFragment mCategoryFragment;
    private List<MenuStatus> mMenuStatusList = new ArrayList<>(2);
    private OnFileCheckedListener mListener = new OnFileCheckedListener() {
        @Override
        public void fileChecked(boolean isChecked) {
            MenuStatus menuStatus = mMenuStatusList.get(mCurFragment);
            if (isChecked){
                menuStatus.checkedCount++;
            }
            else {
                menuStatus.checkedCount--;
                //减少就不算全选了
                if(menuStatus.isCheckedAll){
                    menuStatus.isCheckedAll = false;
                }
            }
            changeMenuStatus();
        }

        @Override
        public void fileCategoryChange() {
            //状态归零
            MenuStatus menuStatus = mMenuStatusList.get(mCurFragment);
            menuStatus.checkedCount = 0;
            menuStatus.isCheckedAll = false;
            //改变菜单
            changeMenuStatus();
        }
    };
    private int mCurFragment = 0;

    @Override
    protected List<Fragment> createTabFragments() {
        mLocalFragment = new LocalBookFragment();
        mCategoryFragment = new FileCategoryFragment();
        return Arrays.asList(mLocalFragment,mCategoryFragment);
    }

    @Override
    protected List<String> createTabTitles() {
        return Arrays.asList("智能导入","手机目录");
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_file_system;
    }

    @Override
    protected void setUpToolbar(Toolbar toolbar) {
        super.setUpToolbar(toolbar);
        getSupportActionBar().setTitle("本机导入");
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        //创建状态对象
        mMenuStatusList.add(new MenuStatus());
        mMenuStatusList.add(new MenuStatus());
    }

    @Override
    protected void initClick() {
        super.initClick();
        mCbSelectAll.setOnClickListener(
                (view) -> {
                    boolean isChecked = mCbSelectAll.isChecked();
                    MenuStatus menuStatus = mMenuStatusList.get(mCurFragment);
                    //当前选中状态
                    menuStatus.isCheckedAll = isChecked;
                    //调用全选按钮
                    if (mCurFragment == 0){
                        mLocalFragment.setSelectedAll(isChecked);
                    }
                    else {
                        mCategoryFragment.setSelectedAll(isChecked);
                    }
                    //获取全选的数量
                    if (isChecked){
                        if (mCurFragment == 0){
                            menuStatus.checkedCount = mLocalFragment.getBookCount();
                        }
                        else {
                            menuStatus.checkedCount = mCategoryFragment.getBookCount();
                        }
                    }
                    else {
                        menuStatus.checkedCount = 0;
                    }
                    //改变菜单状态
                    changeMenuStatus();
                }
        );
        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurFragment = position;
                //改变菜单状态
                changeMenuStatus();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mLocalFragment.setOnFileCheckedListener(mListener);
        mCategoryFragment.setOnFileCheckedListener(mListener);
    }

    private void changeMenuStatus(){
        MenuStatus menuStatus = mMenuStatusList.get(mCurFragment);
        if (menuStatus.checkedCount == 0){
            mBtnAddBook.setText(getString(R.string.nb_file_add_shelf));
            setMenuClickable(false);
        }
        else {
            mBtnAddBook.setText(getString(R.string.nb_file_add_shelves,menuStatus.checkedCount));
            setMenuClickable(true);
        }

        if (menuStatus.isCheckedAll){
            mCbSelectAll.setText("取消");
        }
        else {
            mCbSelectAll.setText("全选");
        }
        mCbSelectAll.setChecked(menuStatus.isCheckedAll);
    }

    private void setMenuClickable(boolean isClickable){
        mBtnDelete.setEnabled(isClickable);
        mBtnDelete.setClickable(isClickable);

        mBtnAddBook.setEnabled(isClickable);
        mBtnAddBook.setClickable(isClickable);

        setSelectedAllClickable();
    }

    private void setSelectedAllClickable(){
        int count = 0;

        if (mCurFragment == 0){
            count = mLocalFragment.getBookCount();
        }
        else {
            count = mCategoryFragment.getBookCount();
        }

        if (count > 0){
            mCbSelectAll.setClickable(true);
            mCbSelectAll.setEnabled(true);
        }
        else {
            mCbSelectAll.setClickable(false);
            mCbSelectAll.setEnabled(false);
        }
    }

    private class MenuStatus{
        boolean isCheckedAll;
        int checkedCount;
    }
}
