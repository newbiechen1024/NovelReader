package com.example.newbiechen.ireader.ui.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.newbiechen.ireader.ui.base.BaseMVPActivity;
import com.example.newbiechen.ireader.ui.dialog.ReadSettingDialog;
import com.example.newbiechen.ireader.utils.BrightnessUtils;
import com.example.newbiechen.ireader.utils.Constant;
import com.example.newbiechen.ireader.utils.LogUtils;
import com.example.newbiechen.ireader.utils.RxUtils;
import com.example.newbiechen.ireader.utils.ScreenUtils;
import com.example.newbiechen.ireader.widget.page.NetPageLoader;
import com.example.newbiechen.ireader.utils.StringUtils;
import com.example.newbiechen.ireader.utils.SystemBarUtils;
import com.example.newbiechen.ireader.widget.page.PageLoader;
import com.example.newbiechen.ireader.widget.page.PageView;
import com.example.newbiechen.ireader.widget.page.TxtChapter;

import java.util.List;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by newbiechen on 17-5-16.
 */

public class ReadActivity extends BaseMVPActivity<ReadContract.Presenter>
 implements ReadContract.View{
    private static final String TAG = "ReadActivity";
    public static final int REQUEST_MORE_SETTING = 1;
    public static final String EXTRA_COLL_BOOK = "extra_coll_book";
    public static final String EXTRA_IS_COLLECTED = "extra_is_collected";

    //注册 Brightness 的 uri
    private final Uri BRIGHTNESS_MODE_URI =
            Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS_MODE);
    private final Uri BRIGHTNESS_URI =
            Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
    private final Uri BRIGHTNESS_ADJ_URI =
            Settings.System.getUriFor("screen_auto_brightness_adj");

    private boolean isRegistered = false;

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
    private PageLoader mPageLoader;
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
                mPageLoader.updateBattery(level);
            }
            //监听分钟的变化
            else if (intent.getAction().equals(Intent.ACTION_TIME_TICK)){
                mPageLoader.updateTime();
            }
        }
    };

    //亮度调节监听
    //由于亮度调节没有 Broadcast 而是直接修改 ContentProvider 的。所以需要创建一个 Observer 来监听 ContentProvider 的变化情况。
    private ContentObserver mBrightObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange);

            //判断当前是否跟随屏幕亮度，如果不是则返回
            if (selfChange || !mSettingDialog.isBrightFollowSystem()) return;

            //如果系统亮度改变，则修改当前 Activity 亮度
            if (BRIGHTNESS_MODE_URI.equals(uri)) {
                Log.d(TAG, "亮度模式改变");
            } else if (BRIGHTNESS_URI.equals(uri) && !BrightnessUtils.isAutoBrightness(ReadActivity.this)) {
                Log.d(TAG, "亮度模式为手动模式 值改变");
                BrightnessUtils.setBrightness(ReadActivity.this,BrightnessUtils.getScreenBrightness(ReadActivity.this));
            } else if (BRIGHTNESS_ADJ_URI.equals(uri) && BrightnessUtils.isAutoBrightness(ReadActivity.this)) {
                Log.d(TAG, "亮度模式为自动模式 值改变");
                BrightnessUtils.setBrightness(ReadActivity.this,BrightnessUtils.getScreenBrightness(ReadActivity.this));
            } else {
                Log.d(TAG, "亮度调整 其他");
            }
        }
    };

    /***************params*****************/
    private boolean isCollected = false; //isFromSDCard
    private boolean isNightMode = false;
    private boolean isFullScreen = false;
    private String mBookId;

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
        mCollBook = getIntent().getParcelableExtra(EXTRA_COLL_BOOK);
        isCollected = getIntent().getBooleanExtra(EXTRA_IS_COLLECTED,false);
        isNightMode = ReadSettingManager.getInstance().isNightMode();
        isFullScreen = ReadSettingManager.getInstance().isFullScreen();

        mBookId = mCollBook.get_id();
    }

    @Override
    protected void setUpToolbar(Toolbar toolbar) {
        super.setUpToolbar(toolbar);
        //设置标题
        toolbar.setTitle(mCollBook.getTitle());
        //半透明化StatusBar
        SystemBarUtils.transparentStatusBar(this);
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        //获取页面加载器
        mPageLoader = mPvPage.getPageLoader(mCollBook.isLocal());
        //禁止滑动展示DrawerLayout
        mDlSlide.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mSettingDialog = new ReadSettingDialog(this, mPageLoader);

        setUpAdapter();

        //夜间模式按钮的状态
        toggleNightMode();

        //注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(mReceiver, intentFilter);

        //设置当前Activity的Brightness
        if (ReadSettingManager.getInstance().isBrightnessAuto()){
            BrightnessUtils.setBrightness(this,BrightnessUtils.getScreenBrightness(this));
        }
        else {
            BrightnessUtils.setBrightness(this,ReadSettingManager.getInstance().getBrightness());
        }

        //初始化屏幕常亮类
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "keep bright");

        //隐藏StatusBar
        mPvPage.post(
                () -> hideSystemBar()
        );

        //初始化TopMenu
        initTopMenu();

        //初始化BottomMenu
        initBottomMenu();
    }

    private void initTopMenu(){
        if (Build.VERSION.SDK_INT >= 19){
            mAblTopMenu.setPadding(0,ScreenUtils.getStatusBarHeight(),0,0);
        }
    }

    private void initBottomMenu(){
        //判断是否全屏
        if (ReadSettingManager.getInstance().isFullScreen()){
            //还需要设置mBottomMenu的底部高度
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mLlBottomMenu.getLayoutParams();
            params.bottomMargin = ScreenUtils.getNavigationBarHeight();
            mLlBottomMenu.setLayoutParams(params);
        }
        else{
            //设置mBottomMenu的底部距离
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mLlBottomMenu.getLayoutParams();
            params.bottomMargin = 0;
            mLlBottomMenu.setLayoutParams(params);
        }
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

    //注册亮度观察者
    private void registerBrightObserver() {
        try {
            if (mBrightObserver != null) {
                if (!isRegistered) {
                    final ContentResolver cr = getContentResolver();
                    cr.unregisterContentObserver(mBrightObserver);
                    cr.registerContentObserver(BRIGHTNESS_MODE_URI, false, mBrightObserver);
                    cr.registerContentObserver(BRIGHTNESS_URI, false, mBrightObserver);
                    cr.registerContentObserver(BRIGHTNESS_ADJ_URI, false, mBrightObserver);
                    isRegistered = true;
                }
            }
        } catch (Throwable throwable) {
            Log.e(TAG, "[ouyangyj] register mBrightObserver error! " + throwable);
        }
    }

    //解注册
    private void unregisterBrightObserver() {
        try {
            if (mBrightObserver != null) {
                if (isRegistered) {
                    getContentResolver().unregisterContentObserver(mBrightObserver);
                    isRegistered = false;
                }
            }
        } catch (Throwable throwable) {
            Log.e(TAG, "unregister BrightnessObserver error! " + throwable);
        }
    }

    @Override
    protected void initClick() {
        super.initClick();

        mPageLoader.setOnPageChangeListener(
                new PageLoader.OnPageChangeListener() {

                    @Override
                    public void onChapterChange(int pos) {
                        mCategoryAdapter.setChapter(pos);
                    }

                    @Override
                    public void onLoadChapter(List<TxtChapter> chapters, int pos){
                        mPresenter.loadChapter(mBookId, chapters);
                        mLvCategory.post(
                                () -> mLvCategory.setSelection(mPageLoader.getChapterPos())
                        );
                        if (mPageLoader.getPageStatus() == NetPageLoader.STATUS_LOADING
                                || mPageLoader.getPageStatus() == NetPageLoader.STATUS_ERROR){
                            //冻结使用
                            mSbChapterProgress.setEnabled(false);
                        }
                        //隐藏提示
                        mTvPageTip.setVisibility(GONE);
                        mSbChapterProgress.setProgress(0);
                    }

                    @Override
                    public void onCategoryFinish(List<TxtChapter> chapters) {
                        mCategoryAdapter.refreshItems(chapters);
                    }

                    @Override
                    public void onPageCountChange(int count) {
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
                        int pagePos = mSbChapterProgress.getProgress();
                        if (pagePos != mPageLoader.getPagePos()){
                            mPageLoader.skipToPage(pagePos);
                        }
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
            public boolean onTouch() {
                return !hideReadMenu();
            }

            @Override
            public boolean prePage(){
                return true;
            }

            @Override
            public boolean nextPage() {
                return true;
            }

            @Override
            public void cancel() {
            }
        });

        mLvCategory.setOnItemClickListener(
                (parent, view, position, id) -> {
                    mDlSlide.closeDrawer(Gravity.START);
                    mPageLoader.skipToChapter(position);
                }
        );

        mTvCategory.setOnClickListener(
                (v) -> {
                    //移动到指定位置
                    mLvCategory.setSelection(mPageLoader.getChapterPos());
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
                (v) ->  mCategoryAdapter.setChapter(mPageLoader.skipPreChapter())
        );

        mTvNextChapter.setOnClickListener(
                (v) ->  mCategoryAdapter.setChapter(mPageLoader.skipNextChapter())
        );

        mTvNightMode.setOnClickListener(
                (v) -> {
                    if (isNightMode){
                        isNightMode = false;
                    }
                    else {
                        isNightMode = true;
                    }
                    mPageLoader.setNightMode(isNightMode);
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

        mSettingDialog.setOnDismissListener(
                dialog ->  hideSystemBar()
        );
    }

    /**
     * 隐藏阅读界面的菜单显示
     * @return 是否隐藏成功
     */
    private boolean hideReadMenu(){
        hideSystemBar();
        if (mAblTopMenu.getVisibility() == VISIBLE){
            toggleMenu(true);
            return true;
        }
        else if (mSettingDialog.isShowing()){
            mSettingDialog.dismiss();
            return true;
        }
        return false;
    }

    private void showSystemBar(){
        //显示
        SystemBarUtils.showUnStableStatusBar(this);
        if (isFullScreen){
            SystemBarUtils.showUnStableNavBar(this);
        }
    }

    private void hideSystemBar(){
        //隐藏
        SystemBarUtils.hideStableStatusBar(this);
        if (isFullScreen){
            SystemBarUtils.hideStableNavBar(this);
        }
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
            mTvPageTip.setVisibility(GONE);

            if (hideStatusBar){
                hideSystemBar();
            }
        }
        else {
            mAblTopMenu.setVisibility(View.VISIBLE);
            mLlBottomMenu.setVisibility(View.VISIBLE);
            mAblTopMenu.startAnimation(mTopInAnim);
            mLlBottomMenu.startAnimation(mBottomInAnim);

            showSystemBar();
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
        //如果是已经收藏的，那么就从数据库中获取目录
        if (isCollected){
            Disposable disposable = BookRepository.getInstance()
                    .getBookChaptersInRx(mBookId)
                    .compose(RxUtils::toSimpleSingle)
                    .subscribe(
                            (bookChapterBeen, throwable) -> {
                                mCollBook.setBookChapters(bookChapterBeen);
                                mPageLoader.openBook(mCollBook);
                                //如果是网络小说并被标记更新的，则从网络下载目录
                                if (mCollBook.isUpdate() && !mCollBook.isLocal()){
                                    mPresenter.loadCategory(mBookId);
                                }
                                LogUtils.e(throwable);
                            }
                    );
            addDisposable(disposable);
        }
        else{
            //从网络中获取目录
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
        mCollBook.setBookChapters(bookChapters);
        //如果是更新加载，那么重置PageLoader的Chapter
        if (mCollBook.isUpdate() && isCollected){
            mPageLoader.setChapterList(bookChapters);
            BookRepository.getInstance()
                    .saveBookChaptersWithAsync(bookChapters);
        }
        else {
            mPageLoader.openBook(mCollBook);
        }
    }

    @Override
    public void finishChapter() {
        if (mPageLoader.getPageStatus() == PageLoader.STATUS_LOADING){
            mPvPage.post(
                    () -> mPageLoader.openChapter()
            );
        }
        //当完成章节的时候，刷新列表
        mCategoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void errorChapter() {
        if (mPageLoader.getPageStatus() == PageLoader.STATUS_LOADING){
            mPageLoader.chapterError();
        }
    }

    @Override
    public void onBackPressed(){
        if(mAblTopMenu.getVisibility() == View.VISIBLE){
            //非全屏下才收缩，全屏下直接退出
            if (!ReadSettingManager.getInstance().isFullScreen()){
                toggleMenu(true);
                return;
            }
        }
        else if (mSettingDialog.isShowing()){
            mSettingDialog.dismiss();
            return;
        }
        else if (mDlSlide.isDrawerOpen(Gravity.START)){
            mDlSlide.closeDrawer(Gravity.START);
            return;
        }

        if (!mCollBook.isLocal() && !isCollected){
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("加入书架")
                    .setMessage("喜欢本书就加入书架吧")
                    .setPositiveButton("确定",(dialog, which) -> {
                        //设置为已收藏
                        isCollected = true;
                        //设置BookChapter
                        mCollBook.setBookChapters(mCollBook.getBookChapters());
                        //设置阅读时间
                        mCollBook.setLastRead(StringUtils.
                                dateConvert(System.currentTimeMillis(), Constant.FORMAT_BOOK_DATE));

                        BookRepository.getInstance()
                                .saveCollBookWithAsync(mCollBook);

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
    protected void onStart() {
        super.onStart();
        registerBrightObserver();
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
        if (isCollected){
            mPageLoader.saveRecord();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterBrightObserver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        mPageLoader.closeBook();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean isVolumeTurnPage = ReadSettingManager
                .getInstance().isVolumeTurnPage();
        switch (keyCode){
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (isVolumeTurnPage){
                    return mPageLoader.autoPrevPage();
                }
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (isVolumeTurnPage){
                    return mPageLoader.autoNextPage();
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SystemBarUtils.hideStableStatusBar(this);
        if (requestCode == REQUEST_MORE_SETTING){
            boolean fullScreen = ReadSettingManager.getInstance().isFullScreen();
            if (isFullScreen != fullScreen){
                isFullScreen = fullScreen;
                //刷新BottomMenu
                initBottomMenu();
            }

            //设置显示状态
            if (isFullScreen){
                SystemBarUtils.hideStableNavBar(this);
            }
            else {
                SystemBarUtils.showStableNavBar(this);
            }
        }
    }
}
