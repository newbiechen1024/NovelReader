package com.example.newbiechen.ireader.ui.activity;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.CollBookBean;
import com.example.newbiechen.ireader.model.local.BookRepository;
import com.example.newbiechen.ireader.ui.base.BaseTabActivity;
import com.example.newbiechen.ireader.ui.fragment.FileCategoryFragment;
import com.example.newbiechen.ireader.ui.fragment.LocalBookFragment;
import com.example.newbiechen.ireader.ui.fragment.OnFileCheckedListener;
import com.example.newbiechen.ireader.utils.Constant;
import com.example.newbiechen.ireader.utils.StringUtils;
import com.example.newbiechen.ireader.utils.ToastUtils;

import java.io.File;
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
        public void onItemCheckedChange(boolean isChecked) {
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
        public void onCategoryChanged() {
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
                            menuStatus.checkedCount = mLocalFragment.getCheckedFiles().size();
                        }
                        else {
                            menuStatus.checkedCount = mCategoryFragment.getCheckedFiles().size();
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

        mBtnAddBook.setOnClickListener(
                (v) -> {
                    List<File> files = null;
                    //0表示:local
                    if (mCurFragment == 0){
                        files = mLocalFragment.getCheckedFiles();
                        mLocalFragment.setSelectedAll(false);
                    }
                    else {
                        files = mCategoryFragment.getCheckedFiles();
                        mCategoryFragment.setSelectedAll(false);
                    }

                    //转换成CollBook,并存储
                    List<CollBookBean> collBooks = convertCollBook(files);
                    BookRepository.getInstance()
                            .saveCollBooks(collBooks);

                    //提示加入书架成功
                    ToastUtils.show(getResources().getString(R.string.nb_file_add_succeed,collBooks.size()));

                    //刷新Adapter的显示，将HasMap为false，将加入书架的数量归零
                    MenuStatus menuStatus = mMenuStatusList.get(mCurFragment);
                    menuStatus.checkedCount = 0;
                    menuStatus.isCheckedAll = false;
                    //改变菜单状态
                    changeMenuStatus();
                }
        );

        mBtnDelete.setOnClickListener(
                (v) -> {
                    //弹出，确定删除文件吗。
                    new AlertDialog.Builder(this)
                            .setTitle("删除文件")
                            .setMessage("确定删除文件吗?")
                            .setPositiveButton(getResources().getString(R.string.nb_common_sure), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //这个功能还没有想好。。。
/*                                    List<File> files;
                                    //删除文件
                                    if(mCurFragment == 0){
                                        files = mLocalFragment.getCheckedFiles();
                                    }
                                    else {
                                        files = mCategoryFragment.getCheckedFiles();
                                    }

                                    for(File file : files){
                                        file.delete();
                                    }
                                    //提示删除文件成功*/

                                }
                            })
                            .setNegativeButton(getResources().getString(R.string.nb_common_cancel), null)
                            .show();
                }
        );

        mLocalFragment.setOnFileCheckedListener(mListener);
        mCategoryFragment.setOnFileCheckedListener(mListener);
    }

    private List<CollBookBean> convertCollBook(List<File> files){
        List<CollBookBean> collBooks = new ArrayList<>(files.size());
        for(File file : files){
            CollBookBean collBook = new CollBookBean();
            collBook.setLocal(true);
            collBook.set_id(file.getAbsolutePath());
            collBook.setTitle(file.getName().replace(".txt",""));
            collBook.setLastChapter("开始阅读");
            collBook.setLastRead(StringUtils.
                    dateConvert(System.currentTimeMillis(), Constant.FORMAT_BOOK_DATE));
            collBooks.add(collBook);
        }
        return collBooks;
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
            count = mLocalFragment.getFileCount();
        }
        else {
            count = mCategoryFragment.getFileCount();
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
