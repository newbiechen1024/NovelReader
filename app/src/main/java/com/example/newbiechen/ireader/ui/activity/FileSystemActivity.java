package com.example.newbiechen.ireader.ui.activity;

import static com.example.newbiechen.ireader.ui.fragment.BaseFileFragment.*;
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
import com.example.newbiechen.ireader.ui.fragment.BaseFileFragment;
import com.example.newbiechen.ireader.ui.fragment.FileCategoryFragment;
import com.example.newbiechen.ireader.ui.fragment.LocalBookFragment;
import com.example.newbiechen.ireader.utils.Constant;
import com.example.newbiechen.ireader.utils.MD5Utils;
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
    private BaseFileFragment mCurFragment;

    private OnFileCheckedListener mListener = new OnFileCheckedListener() {
        @Override
        public void onItemCheckedChange(boolean isChecked) {
            changeMenuStatus();
        }

        @Override
        public void onCategoryChanged() {
            //状态归零
            mCurFragment.setCheckedAll(false);
            //改变菜单
            changeMenuStatus();
            //改变是否能够全选
            changeCheckedAllStatus();
        }
    };

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
    protected void initClick() {
        super.initClick();
        mCbSelectAll.setOnClickListener(
                (view) -> {
                    //设置全选状态
                    boolean isChecked = mCbSelectAll.isChecked();
                    mCurFragment.setCheckedAll(isChecked);
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
                if (position == 0){
                    mCurFragment = mLocalFragment;
                }
                else {
                    mCurFragment = mCategoryFragment;
                }
                //改变菜单状态
                changeMenuStatus();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mBtnAddBook.setOnClickListener(
                (v) -> {
                    //获取选中的文件
                    List<File> files = mCurFragment.getCheckedFiles();
                    //转换成CollBook,并存储
                    List<CollBookBean> collBooks = convertCollBook(files);
                    BookRepository.getInstance()
                            .saveCollBooks(collBooks);
                    //设置HashMap为false
                    mCurFragment.setCheckedAll(false);
                    //改变菜单状态
                    changeMenuStatus();
                    //改变是否可以全选
                    changeCheckedAllStatus();
                    //提示加入书架成功
                    ToastUtils.show(getResources().getString(R.string.nb_file_add_succeed,collBooks.size()));

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
                                    //删除选中的文件
                                    mCurFragment.deleteCheckedFiles();
                                    //提示删除文件成功
                                    ToastUtils.show("删除文件成功");
                                }
                            })
                            .setNegativeButton(getResources().getString(R.string.nb_common_cancel), null)
                            .show();
                }
        );

        mLocalFragment.setOnFileCheckedListener(mListener);
        mCategoryFragment.setOnFileCheckedListener(mListener);
    }

    @Override
    protected void processLogic() {
        super.processLogic();
        mCurFragment = mLocalFragment;
    }

    /**
     * 将文件转换成CollBook
     * @param files:需要加载的文件列表
     * @return
     */
    private List<CollBookBean> convertCollBook(List<File> files){
        List<CollBookBean> collBooks = new ArrayList<>(files.size());
        for(File file : files){
            //判断文件是否存在
            if (!file.exists()) continue;

            CollBookBean collBook = new CollBookBean();
            collBook.set_id(MD5Utils.strToMd5By16(file.getAbsolutePath()));
            collBook.setTitle(file.getName().replace(".txt",""));
            collBook.setAuthor("");
            collBook.setShortIntro("无");
            collBook.setCover(file.getAbsolutePath());
            collBook.setLocal(true);
            collBook.setLastChapter("开始阅读");
            collBook.setUpdated(StringUtils.dateConvert(file.lastModified(), Constant.FORMAT_BOOK_DATE));
            collBook.setLastRead(StringUtils.
                    dateConvert(System.currentTimeMillis(), Constant.FORMAT_BOOK_DATE));
            collBooks.add(collBook);
        }
        return collBooks;
    }

    /**
     * 改变底部选择栏的状态
     */
    private void changeMenuStatus(){

        //点击、删除状态的设置
        if (mCurFragment.getCheckedCount() == 0){
            mBtnAddBook.setText(getString(R.string.nb_file_add_shelf));
            //设置某些按钮的是否可点击
            setMenuClickable(false);

            if (mCbSelectAll.isChecked()){
                mCurFragment.setChecked(false);
                mCbSelectAll.setChecked(mCurFragment.isCheckedAll());
            }

        }
        else {
            mBtnAddBook.setText(getString(R.string.nb_file_add_shelves,mCurFragment.getCheckedCount()));
            setMenuClickable(true);

            //全选状态的设置

            //如果选中的全部的数据，则判断为全选
            if (mCurFragment.getCheckedCount() == mCurFragment.getCheckableCount()){
                //设置为全选
                mCurFragment.setChecked(true);
                mCbSelectAll.setChecked(mCurFragment.isCheckedAll());
            }
            //如果曾今是全选则替换
            else if (mCurFragment.isCheckedAll()){
                mCurFragment.setChecked(false);
                mCbSelectAll.setChecked(mCurFragment.isCheckedAll());
            }
        }

        //重置全选的文字
        if (mCurFragment.isCheckedAll()){
            mCbSelectAll.setText("取消");
        }
        else {
            mCbSelectAll.setText("全选");
        }

    }

    private void setMenuClickable(boolean isClickable){

        //设置是否可删除
        mBtnDelete.setEnabled(isClickable);
        mBtnDelete.setClickable(isClickable);

        //设置是否可添加书籍
        mBtnAddBook.setEnabled(isClickable);
        mBtnAddBook.setClickable(isClickable);
    }

    /**
     * 改变全选按钮的状态
     */
    private void changeCheckedAllStatus(){
        //获取可选择的文件数量
        int count = mCurFragment.getCheckableCount();

        //设置是否能够全选
        if (count > 0){
            mCbSelectAll.setClickable(true);
            mCbSelectAll.setEnabled(true);
        }
        else {
            mCbSelectAll.setClickable(false);
            mCbSelectAll.setEnabled(false);
        }
    }
}
