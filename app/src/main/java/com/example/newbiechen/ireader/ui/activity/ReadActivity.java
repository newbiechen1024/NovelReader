package com.example.newbiechen.ireader.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.example.newbiechen.ireader.presenter.ReadPresenter;
import com.example.newbiechen.ireader.presenter.contract.ReadContract;
import com.example.newbiechen.ireader.ui.adapter.CategoryAdapter;
import com.example.newbiechen.ireader.ui.base.BaseRxActivity;
import com.example.newbiechen.ireader.utils.StatusBarCompat;
import com.example.newbiechen.ireader.utils.SystemBarUtils;

import java.util.List;

import butterknife.BindView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by newbiechen on 17-5-16.
 */

public class ReadActivity extends BaseRxActivity<ReadContract.Presenter>
 implements ReadContract.View{
    private static final String EXTRA_COLL_BOOK = "extra_coll_book";
    private static final String EXTRA_BOOK_NAME = "extra_book_name";
    private static final String EXTRA_BOOK_ID = "extra_book_id";

    @BindView(R.id.read_dl_slide)
    DrawerLayout mDlSlide;
    /*************top_menu_view*******************/
    @BindView(R.id.read_rl_top_menu)
    RelativeLayout mRlTopMenu;
    @BindView(R.id.read_iv_back)
    ImageView mIvBack;
    @BindView(R.id.read_tv_community)
    TextView mTvCommunity;
    @BindView(R.id.read_tv_brief)
    TextView mTvBrief;
    /***************content_view******************/
    @BindView(R.id.read_fl_content)
    FrameLayout mFlContent;
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

    /***************setting_view*******************************/
    @BindView(R.id.read_setting_ll_menu)
    LinearLayout mLlSetting;
    @BindView(R.id.read_setting_tv_font_minus)
    TextView mTvFontMinus;
    @BindView(R.id.read_setting_tv_font)
    TextView mTvFont;
    @BindView(R.id.read_setting_tv_font_plus)
    TextView mTvPlus;
    @BindView(R.id.read_setting_iv_brightness_minus)
    ImageView mIvBrightnessMinus;
    @BindView(R.id.read_setting_sb_brightness)
    SeekBar mSbBrightness;
    @BindView(R.id.read_setting_iv_brightness_plus)
    ImageView mIvBrightnessPlus;
    @BindView(R.id.read_setting_tv_brightness_auto)
    TextView mTvBrightnessAuto;
    @BindView(R.id.read_setting_gv_theme)
    GridView mGvTheme;
    @BindView(R.id.read_rv_category)
    RecyclerView mRvCategory;

    /*****************view******************/
    private Animation mTopInAnim;
    private Animation mTopOutAnim;
    private Animation mBottomInAnim;
    private Animation mBottomOutAnim;

    private CollBookBean mCollBook;
    private CategoryAdapter mCategoryAdapter;
    /***************params*****************/
    private boolean isFromBookShelf = false;
    private String mBookName;
    private String mBookId;

    //书架页进入
    public static void startActivity(Context context, CollBookBean bean){
        context.startActivity(new Intent(context,ReadActivity.class)
        .putExtra(EXTRA_COLL_BOOK,bean));
    }

    public static void startActivity(Context context,String bookId,String bookName){
        context.startActivity(new Intent(context,ReadActivity.class)
        .putExtra(EXTRA_BOOK_ID,bookId)
        .putExtra(EXTRA_BOOK_NAME,bookName));
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
        mCollBook = (CollBookBean) getIntent().getSerializableExtra(EXTRA_COLL_BOOK);

        if (mCollBook != null){
            isFromBookShelf = true;
            mBookId = mCollBook.get_id();
            mBookName = mCollBook.getTitle();
        }
        else {
            isFromBookShelf = false;
            mBookId = getIntent().getStringExtra(EXTRA_BOOK_ID);
            mBookName = getIntent().getStringExtra(EXTRA_BOOK_NAME);
        }
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        //禁止滑动展示DrawerLayout
        mDlSlide.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        setUpAdapter();
    }

    private void setUpAdapter(){
        mCategoryAdapter = new CategoryAdapter();
        mRvCategory.setLayoutManager(new LinearLayoutManager(this));
        mRvCategory.setAdapter(mCategoryAdapter);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            setStatusBarColor(R.color.black);
            SystemBarUtils.toggleAllBar(this);
        }
    }

    @Override
    protected void initClick() {
        super.initClick();
        mFlContent.setOnClickListener(
                (v) -> toggleMenu()
        );
        mTvCategory.setOnClickListener(
                (v) -> {
                    toggleMenu();
                    //打开侧滑动栏
                    mDlSlide.openDrawer(Gravity.START);
                }
        );
        mTvSetting.setOnClickListener(
                (v) -> {
                    toggleMenu();
                    toggleSettingMenu();
                }
        );
        mFlContent.setOnTouchListener(
                (v, event) -> {
                    if (event.getAction() == MotionEvent.ACTION_DOWN){
                        if (mLlSetting.getVisibility() == VISIBLE){
                            toggleSettingMenu();
                            return true;
                        }
                    }
                    return false;
                }
        );
    }

    /**
     * 切换菜单栏的可视状态
     * 默认是隐藏的
     */
    private void toggleMenu(){
        initMenuAnim();
        if(mRlTopMenu.getVisibility() == View.VISIBLE){
            //关闭
            mRlTopMenu.startAnimation(mTopOutAnim);
            mLlBottomMenu.startAnimation(mBottomOutAnim);
            mRlTopMenu.setVisibility(GONE);
            mLlBottomMenu.setVisibility(GONE);
        }
        else {
            mRlTopMenu.setVisibility(View.VISIBLE);
            mLlBottomMenu.setVisibility(View.VISIBLE);

            mRlTopMenu.startAnimation(mTopInAnim);
            mLlBottomMenu.startAnimation(mBottomInAnim);
        }
        //执行进入动画的同时
        mTopInAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                SystemBarUtils.toggleAllBar(ReadActivity.this);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mTopOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                setStatusBarColor(R.color.nb_read_bar_translucent);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setStatusBarColor(R.color.black);
                SystemBarUtils.toggleAllBar(ReadActivity.this);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //初始化菜单动画
    private void initMenuAnim(){
        if (mTopInAnim == null){
            mTopInAnim = AnimationUtils.loadAnimation(this, R.anim.slide_top_in);
            mTopOutAnim = AnimationUtils.loadAnimation(this, R.anim.slide_top_out);
            mBottomInAnim = AnimationUtils.loadAnimation(this, R.anim.slide_bottom_in);
            mBottomOutAnim = AnimationUtils.loadAnimation(this, R.anim.slide_bottom_out);
            //退出的速度要快
            mTopOutAnim.setDuration(200);
            mBottomOutAnim.setDuration(200);
        }
    }

    /**
     * 设置栏的切换
     */
    private void toggleSettingMenu(){
        if (mLlSetting.getVisibility() == View.VISIBLE){
            mLlSetting.startAnimation(mBottomOutAnim);
            mLlSetting.setVisibility(GONE);
        }
        else {
            mLlSetting.setVisibility(VISIBLE);
            mLlSetting.startAnimation(mBottomInAnim);
        }
    }

    @Override
    protected void processLogic() {
        super.processLogic();
        //初始化目录
        if (mCollBook != null){
            mCategoryAdapter.refreshItems(mCollBook.getBookChapterList());
        }
        else{
            //从网络中加载
            mPresenter.loadCategory(mBookId);
        }
    }

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
}
