package com.example.newbiechen.ireader.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.BookChapterBean;
import com.example.newbiechen.ireader.model.bean.CollBookBean;
import com.example.newbiechen.ireader.model.local.CollBookManager;
import com.example.newbiechen.ireader.model.local.ReadSettingManager;
import com.example.newbiechen.ireader.presenter.ReadPresenter;
import com.example.newbiechen.ireader.presenter.contract.ReadContract;
import com.example.newbiechen.ireader.ui.adapter.CategoryAdapter;
import com.example.newbiechen.ireader.ui.base.BaseRxActivity;
import com.example.newbiechen.ireader.ui.dialog.ReadSettingDialog;
import com.example.newbiechen.ireader.utils.LogUtils;
import com.example.newbiechen.ireader.utils.PageFactory;
import com.example.newbiechen.ireader.utils.RxUtils;
import com.example.newbiechen.ireader.utils.StatusBarCompat;
import com.example.newbiechen.ireader.utils.SystemBarUtils;
import com.example.newbiechen.ireader.widget.PageView;

import java.util.List;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by newbiechen on 17-5-16.
 */

public class ReadActivity extends BaseRxActivity<ReadContract.Presenter>
 implements ReadContract.View{
    private static final String TAG = "ReadActivity";

    private static final String EXTRA_IS_FROM_SHELF = "extra_is_from_shelf";
    private static final String EXTRA_BOOK_ID = "extra_book_id";
    @BindView(R.id.read_dl_slide)
    DrawerLayout mDlSlide;
    /*************top_menu_view*******************/
    @BindView(R.id.read_abl_top_menu)
    AppBarLayout mAblTopMenu;
    @BindView(R.id.read_tv_community)
    TextView mTvCommunity;
    @BindView(R.id.read_tv_brief)
    TextView mTvBrief;
    /***************content_view******************/
    @BindView(R.id.read_pv_page)
    PageView mPvPage;
    /***************bottom_menu_view***************************/
    @BindView(R.id.read_tv_download_tip)
    TextView mTvDownloadTip;

    @BindView(R.id.read_ll_bottom_menu)
    LinearLayout mLlBottomMenu;
    @BindView(R.id.read_tv_pre_chapter)
    TextView mTvPreChapter;
    @BindView(R.id.read_sb_chapter_progress)
    SeekBar mSbChapterProgress;
    @BindView(R.id.read_tv_next_chapter)
    TextView mTvNextChapter;
    @BindView(R.id.read_tv_category)
    TextView mTvCategory;
    @BindView(R.id.read_tv_mode)
    TextView mTvMode;
    @BindView(R.id.read_tv_download)
    TextView mTvDownload;
    @BindView(R.id.read_tv_setting)
    TextView mTvSetting;

    /***************left slide*******************************/
    @BindView(R.id.read_rv_category)
    RecyclerView mRvCategory;

    /*****************view******************/
    private ReadSettingDialog mSettingDialog;
    private PageFactory mPageFactory;
    private Animation mTopInAnim;
    private Animation mTopOutAnim;
    private Animation mBottomInAnim;
    private Animation mBottomOutAnim;
    private CategoryAdapter mCategoryAdapter;
    /***************params*****************/
    private boolean isFromBookShelf = false;
    private String mBookId;

    //书架页进入
    public static void startActivity(Context context, String bookId,boolean fromShelf){
        context.startActivity(new Intent(context,ReadActivity.class)
        .putExtra(EXTRA_BOOK_ID,bookId)
        .putExtra(EXTRA_IS_FROM_SHELF,fromShelf));
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_read;
    }

    @Override
    protected ReadContract.Presenter bindPresenter() {
        return new ReadPresenter();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        mBookId = getIntent().getStringExtra(EXTRA_BOOK_ID);
        isFromBookShelf = getIntent().getBooleanExtra(EXTRA_IS_FROM_SHELF,false);
    }

    @Override
    protected void setUpToolbar(Toolbar toolbar) {
        super.setUpToolbar(toolbar);
        //半透明化StatusBar
        SystemBarUtils.transparentStatusBar(this);
        getSupportActionBar().setTitle("");
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        //禁止滑动展示DrawerLayout
        mDlSlide.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        setUpAdapter();
        mPvPage.setBgColor(getResources().getColor(R.color.nb_read_bg_1));
        mPageFactory = PageFactory.getInstance(mPvPage);
        mSettingDialog = new ReadSettingDialog(this,mPageFactory);
    }

    private void setUpAdapter(){
        mCategoryAdapter = new CategoryAdapter();
        mRvCategory.setLayoutManager(new LinearLayoutManager(this));
        mRvCategory.setAdapter(mCategoryAdapter);
    }

    @Override
    protected void initClick() {
        super.initClick();

        mPvPage.setTouchListener(new PageView.TouchListener() {
            @Override
            public void center() {
                toggleMenu(true);
            }

            @Override
            public Boolean prePage() {
                return false;
            }

            @Override
            public Boolean nextPage() {
                if (mAblTopMenu.getVisibility() == VISIBLE){
                    toggleMenu(true);
                    return false;
                }
                else if (mSettingDialog.isShowing()){
                    mSettingDialog.dismiss();
                    return false;
                }
                return true;
            }

            @Override
            public void cancel() {

            }
        });

        mTvCategory.setOnClickListener(
                (v) -> {
                    toggleMenu(true);
                    //打开侧滑动栏
                    mDlSlide.openDrawer(Gravity.START);
                }
        );
        mTvSetting.setOnClickListener(
                (v) -> {
                    toggleMenu(false);
                    mSettingDialog.show();
                }
        );
    }

    //切换StatusBar显示状态
    private void toggleStatusBar(){
        if (SystemBarUtils.isFlagUsed(this,View.SYSTEM_UI_FLAG_FULLSCREEN)){
            //显示
            SystemBarUtils.clearFlag(this,View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
        else{
            //隐藏
            SystemBarUtils.setFlag(this,View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

    private void toggleNavBar(){
        //有三种状态，设置中是否使用、是否有虚拟按键，该版本是否支持NavBar。
    }

    /**
     * 切换菜单栏的可视状态
     * 默认是隐藏的
     */
    private void toggleMenu(boolean hideStatusBar){
        initMenuAnim();

        if(mAblTopMenu.getVisibility() == View.VISIBLE){
            //关闭
            mAblTopMenu.startAnimation(mTopOutAnim);
            mLlBottomMenu.startAnimation(mBottomOutAnim);
            mAblTopMenu.setVisibility(GONE);
            mLlBottomMenu.setVisibility(GONE);
        }
        else {
            mAblTopMenu.setVisibility(View.VISIBLE);
            mLlBottomMenu.setVisibility(View.VISIBLE);
            mAblTopMenu.startAnimation(mTopInAnim);
            mLlBottomMenu.startAnimation(mBottomInAnim);
        }

        if (hideStatusBar){
            toggleStatusBar();
        }
    }

    //初始化菜单动画
    private void initMenuAnim(){
        if (mTopInAnim != null) return;

        mTopInAnim = AnimationUtils.loadAnimation(this, R.anim.slide_top_in);
        mTopOutAnim = AnimationUtils.loadAnimation(this, R.anim.slide_top_out);
        mBottomInAnim = AnimationUtils.loadAnimation(this, R.anim.slide_bottom_in);
        mBottomOutAnim = AnimationUtils.loadAnimation(this, R.anim.slide_bottom_out);
        //退出的速度要快
        mTopOutAnim.setDuration(200);
        mBottomOutAnim.setDuration(200);
    }

    @Override
    protected void processLogic() {
        super.processLogic();
        //初始化目录
        if (isFromBookShelf){
            Disposable disposable =CollBookManager.getInstance()
                    .getBookChapters(mBookId)
                    .compose(RxUtils::toSimpleSingle)
                    .subscribe(
                            (bookChapterBeen, throwable) -> {
                                mCategoryAdapter.refreshItems(bookChapterBeen);
                                LogUtils.e(throwable);
                            }
                    );
            addDisposable(disposable);
        }
        else{
            //从网络中加载
            mPresenter.loadCategory(mBookId);
        }
    }
/***************************view************************************/
    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }

    @Override
    public void showCategory(List<BookChapterBean> bookChapterList) {
        //显示
        mCategoryAdapter.refreshItems(bookChapterList);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            toggleStatusBar();
        }
    }
}
