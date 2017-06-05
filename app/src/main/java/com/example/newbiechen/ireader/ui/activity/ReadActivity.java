package com.example.newbiechen.ireader.ui.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.BookChapterBean;
import com.example.newbiechen.ireader.model.bean.CollBookBean;
import com.example.newbiechen.ireader.model.local.BookRepository;
import com.example.newbiechen.ireader.model.local.ReadSettingManager;
import com.example.newbiechen.ireader.presenter.ReadPresenter;
import com.example.newbiechen.ireader.presenter.contract.ReadContract;
import com.example.newbiechen.ireader.ui.adapter.CategoryAdapter;
import com.example.newbiechen.ireader.ui.base.BaseRxActivity;
import com.example.newbiechen.ireader.ui.dialog.ReadSettingDialog;
import com.example.newbiechen.ireader.utils.BrightnessUtils;
import com.example.newbiechen.ireader.utils.LogUtils;
import com.example.newbiechen.ireader.utils.PageFactory;
import com.example.newbiechen.ireader.utils.RxUtils;
import com.example.newbiechen.ireader.utils.SimplePageFactory;
import com.example.newbiechen.ireader.utils.StringUtils;
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

    public static final String EXTRA_COLL_BOOK = "extra_coll_book";
    public static final String EXTRA_IS_COLLECTED = "extra_is_collected";

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
    @BindView(R.id.read_tv_page_tip)
    TextView mTvPageTip;

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
    @BindView(R.id.read_tv_night_mode)
    TextView mTvNightMode;
/*    @BindView(R.id.read_tv_download)
    TextView mTvDownload;*/
    @BindView(R.id.read_tv_setting)
    TextView mTvSetting;

    /***************left slide*******************************/
    @BindView(R.id.read_iv_category)
    ListView mLvCategory;
    /*****************view******************/
    private ReadSettingDialog mSettingDialog;
    private SimplePageFactory mPageFactory;
    private Animation mTopInAnim;
    private Animation mTopOutAnim;
    private Animation mBottomInAnim;
    private Animation mBottomOutAnim;
    private CategoryAdapter mCategoryAdapter;
    private CollBookBean mCollBook;
    //控制屏幕常亮
    private PowerManager.WakeLock mWakeLock;

    // 接收电池信息和时间更新的广播
    private BroadcastReceiver mReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                int level = intent.getIntExtra("level", 0);
                mPageFactory.updateBattery(level);
            }
            //监听分钟的变化
            else if (intent.getAction().equals(Intent.ACTION_TIME_TICK)){
                mPageFactory.updateTime();
            }
        }
    };
    /***************params*****************/
    private boolean isCollected = false; //isFromSd
    private boolean isNightMode = false;
    private String mBookId;

    //书架页进入
    public static void startActivity(Context context, CollBookBean collBook, boolean isCollected){
        context.startActivity(new Intent(context,ReadActivity.class)
        .putExtra(EXTRA_IS_COLLECTED,isCollected)
        .putExtra(EXTRA_COLL_BOOK,collBook));
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
        isCollected = getIntent().getBooleanExtra(EXTRA_IS_COLLECTED,false);
        mBookId = mCollBook.get_id();

        isNightMode = ReadSettingManager.getInstance().isNightMode();
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

        //创建页面工厂
        mPageFactory = new SimplePageFactory();
        mPageFactory.setPageWidget(mPvPage);
        mSettingDialog = new ReadSettingDialog(this,mPageFactory);
        setUpAdapter();
        //夜间模式按钮的状态
        toggleNightMode();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        //注册广播
        registerReceiver(mReceiver, intentFilter);
        //设置当前Activity的Bright
        if (ReadSettingManager.getInstance().isBrightnessAuto()){
            BrightnessUtils.setBrightness(this,BrightnessUtils.getScreenBrightness(this));
        }
        else {
            BrightnessUtils.setBrightness(this,ReadSettingManager.getInstance().getBrightness());
        }
        //初始化屏幕常亮类
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "keep bright");
    }

    private void toggleNightMode(){
        if (isNightMode){
            mTvNightMode.setText(StringUtils.getString(R.string.nb_mode_morning));
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_read_menu_morning);
            mTvNightMode.setCompoundDrawablesWithIntrinsicBounds(null,drawable,null,null);
        }
        else {
            mTvNightMode.setText(StringUtils.getString(R.string.nb_mode_night));
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_read_menu_night);
            mTvNightMode.setCompoundDrawablesWithIntrinsicBounds(null,drawable,null,null);
        }
    }

    private void setUpAdapter(){
        mCategoryAdapter = new CategoryAdapter();
        mLvCategory.setAdapter(mCategoryAdapter);
        mLvCategory.setFastScrollEnabled(true);
    }

    @Override
    protected void initClick() {
        super.initClick();

        mPageFactory.setOnPageChangeListener(
                new SimplePageFactory.OnPageChangeListener() {
                    @Override
                    public void onChapterChange(List<BookChapterBean> beanList, int pos) {
                        mPresenter.loadChapter(mBookId, beanList);
                        mCategoryAdapter.setChapter(pos);
                        mLvCategory.post(
                                () -> mLvCategory.setSelection(mPageFactory.getPosition())
                        );

                        if (mPageFactory.getPageStatus() == SimplePageFactory.STATUS_LOADING
                                || mPageFactory.getPageStatus() == SimplePageFactory.STATUS_ERROR){
                            //冻结使用
                            mSbChapterProgress.setEnabled(false);
                        }
                        //隐藏提示
                        mTvPageTip.setVisibility(GONE);
                        mSbChapterProgress.setProgress(0);
                    }

                    @Override
                    public void onPageListChange(int count) {
                        mSbChapterProgress.setEnabled(true);
                        mSbChapterProgress.setMax(count-1);
                        mSbChapterProgress.setProgress(0);
                    }

                    @Override
                    public void onPageChange(int pos) {
                        mSbChapterProgress.post(
                                () -> mSbChapterProgress.setProgress(pos)
                        );
                    }
                }
        );
        mSbChapterProgress.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (mLlBottomMenu.getVisibility() == VISIBLE){
                            //显示标题
                            mTvPageTip.setText((progress+1)+"/"+(mSbChapterProgress.getMax()+1));
                            mTvPageTip.setVisibility(VISIBLE);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        //进行切换
                        mPageFactory.skipToPage(mSbChapterProgress.getProgress());
                        //隐藏提示
                        mTvPageTip.setVisibility(GONE);
                    }
                }
        );

        mPvPage.setTouchListener(new PageView.TouchListener() {
            @Override
            public void center() {
                toggleMenu(true);
            }

            @Override
            public Boolean prePage(){
                hideStatusBar();
                if (mAblTopMenu.getVisibility() == VISIBLE){
                    toggleMenu(true);
                    return false;
                }
                else if (mSettingDialog.isShowing()){
                    mSettingDialog.dismiss();
                    return false;
                }
                return mPageFactory.prev();
            }

            @Override
            public Boolean nextPage() {
                hideStatusBar();

                if (mAblTopMenu.getVisibility() == VISIBLE){
                    toggleMenu(true);
                    return false;
                }
                else if (mSettingDialog.isShowing()){
                    mSettingDialog.dismiss();
                    return false;
                }
                return mPageFactory.next();
            }

            @Override
            public void cancel() {
                mPageFactory.pageCancel();
            }
        });

        mLvCategory.setOnItemClickListener(
                (parent, view, position, id) -> {
                    mPageFactory.skipToChapter(position);
                    mDlSlide.closeDrawer(Gravity.START);
                }
        );

        mTvCategory.setOnClickListener(
                (v) -> {
                    //移动到指定位置
                    mLvCategory.setSelection(mPageFactory.getPosition());
                    //切换菜单
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

        mTvPreChapter.setOnClickListener(
                (v) ->  mCategoryAdapter.setChapter(mPageFactory.skipPreChapter())
        );

        mTvNextChapter.setOnClickListener(
                (v) ->  mCategoryAdapter.setChapter(mPageFactory.skipNextChapter())
        );

        mTvNightMode.setOnClickListener(
                (v) -> {
                    if (isNightMode){
                        isNightMode = false;
                    }
                    else {
                        isNightMode = true;
                    }
                    mPageFactory.setNightMode(isNightMode);
                    toggleNightMode();
                }
        );

        mTvBrief.setOnClickListener(
                (v) -> BookDetailActivity.startActivity(this,mBookId)
        );

        mTvCommunity.setOnClickListener(
                (v) -> {
                    Intent intent = new Intent(this, CommunityActivity.class);
                    startActivity(intent);
                }
        );
    }

    private void showStatusBar(){
        //显示
        SystemBarUtils.clearFlag(this,View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private void hideStatusBar(){
        //隐藏
        SystemBarUtils.setFlag(this,View.SYSTEM_UI_FLAG_FULLSCREEN);
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

            if (hideStatusBar){
                hideStatusBar();
            }
        }
        else {
            mAblTopMenu.setVisibility(View.VISIBLE);
            mLlBottomMenu.setVisibility(View.VISIBLE);
            mAblTopMenu.startAnimation(mTopInAnim);
            mLlBottomMenu.startAnimation(mBottomInAnim);

            showStatusBar();
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
        if (isCollected){
            Disposable disposable = BookRepository.getInstance()
                    .getBookChapters(mBookId)
                    .compose(RxUtils::toSimpleSingle)
                    .subscribe(
                            (bookChapterBeen, throwable) -> {
                                mCategoryAdapter.refreshItems(bookChapterBeen);
                                mPageFactory.openBook(mBookId,bookChapterBeen);
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
    public void showCategory(List<BookChapterBean> bookChapters){
        //进行设定BookChapter所属的书的id。
        for (BookChapterBean bookChapter : bookChapters){
            bookChapter.setBookId(mBookId);
        }
        //显示
        mCategoryAdapter.refreshItems(bookChapters);
        mPageFactory.openBook(mBookId,bookChapters);
    }

    @Override
    public void finishChapter() {
        if (mPageFactory.getPageStatus() == PageFactory.STATUS_LOADING){
            mPageFactory.openChapter();
        }
        mCategoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void errorChapter() {
        if (mPageFactory.getPageStatus() == PageFactory.STATUS_LOADING){
            mPageFactory.chapterError();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            hideStatusBar();
        }
    }

    @Override
    public void onBackPressed(){
        if(mAblTopMenu.getVisibility() == View.VISIBLE){
            toggleMenu(true);
            return;
        }
        else if (mSettingDialog.isShowing()){
            mSettingDialog.dismiss();
            return;
        }

        if (!isCollected){
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("加入书架")
                    .setMessage("喜欢本书就加入书架吧")
                    .setPositiveButton("确定",(dialog, which) -> {
                        //设置为已收藏
                        isCollected = true;

                        List<BookChapterBean> bookChapters = mCategoryAdapter.getItems();
                        mCollBook.setBookChapters(bookChapters);
                        BookRepository.getInstance()
                                .saveCollBook(mCollBook);

                        exit();
                    })
                    .setNegativeButton("取消",(dialog, which) -> {
                        exit();
                    }).create();
            alertDialog.show();
        }
        else {
            exit();
        }
    }

    //退出
    private void exit(){
        //返回给BookDetail。
        Intent result = new Intent();
        result.putExtra(BookDetailActivity.RESULT_IS_COLLECTED, isCollected);
        setResult(Activity.RESULT_OK,result);
        //退出
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWakeLock.acquire();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWakeLock.release();
        mPageFactory.saveRecord();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        mPageFactory.closeBook(isCollected);
    }
}
