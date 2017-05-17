package com.example.newbiechen.ireader.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.RxBus;
import com.example.newbiechen.ireader.event.DownloadMessage;
import com.example.newbiechen.ireader.event.RecommendBookEvent;
import com.example.newbiechen.ireader.model.bean.BookChapterBean;
import com.example.newbiechen.ireader.model.bean.CollBookBean;
import com.example.newbiechen.ireader.model.bean.DownloadTaskBean;
import com.example.newbiechen.ireader.model.local.CollBookManager;
import com.example.newbiechen.ireader.presenter.BookShelfPresenter;
import com.example.newbiechen.ireader.presenter.contract.BookShelfContract;
import com.example.newbiechen.ireader.ui.activity.ReadActivity;
import com.example.newbiechen.ireader.ui.adapter.CollBookAdapter;
import com.example.newbiechen.ireader.ui.base.BaseRxFragment;
import com.example.newbiechen.ireader.utils.ToastUtils;
import com.example.newbiechen.ireader.widget.adapter.WholeAdapter;
import com.example.newbiechen.ireader.widget.refresh.ScrollRefreshRecyclerView;

import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by newbiechen on 17-4-15.
 */

public class BookShelfFragment extends BaseRxFragment<BookShelfContract.Presenter>
        implements BookShelfContract.View{
    private static final String TAG = "BookShelfFragment";
    @BindView(R.id.book_shelf_rv_content)
    ScrollRefreshRecyclerView mRvContent;

    /************************************/
    private CollBookAdapter mCollBookAdapter;
    private FooterItemView mFooterItem;

    @Override
    protected int getContentId() {
        return R.layout.fragment_bookshelf;
    }

    @Override
    protected BookShelfContract.Presenter bindPresenter() {
        return new BookShelfPresenter();
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        super.initWidget(savedInstanceState);
        setUpAdapter();
    }

    private void setUpAdapter(){
        //添加Footer
        mCollBookAdapter = new CollBookAdapter();
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvContent.setAdapter(mCollBookAdapter);
    }

    @Override
    protected void initClick() {
        super.initClick();
        //推荐书籍
        Disposable recommendDisp = RxBus.getInstance()
                .toObservable(RecommendBookEvent.class)
                .subscribe(
                        event ->  mPresenter.loadRecommendBooks(event.sex)
                );
        addDisposable(recommendDisp);

        Disposable donwloadDisp = RxBus.getInstance()
                .toObservable(DownloadMessage.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        event -> {
                            //使用Toast提示
                            ToastUtils.show(event.message);
                        }
                );
        addDisposable(donwloadDisp);

        mRvContent.setOnRefreshListener(
                () ->   mPresenter.updateCollBooks(mCollBookAdapter.getItems())
        );

        mCollBookAdapter.setOnItemClickListener(
                (view, pos) -> ReadActivity.startActivity(getContext(),
                        mCollBookAdapter.getItem(pos).get_id(),true)
        );

        mCollBookAdapter.setOnItemLongClickListener(
                (v,pos) -> {
                    //开启Dialog,最方便的Dialog,就是AlterDialog
                    openItemDialog(pos);
                    return true;
                }
        );
    }

    private void openItemDialog(int itemPos){
        String[] menus = getResources().getStringArray(R.array.nb_menu_coll_book);
        AlertDialog collBookDialog = new AlertDialog.Builder(getContext())
                .setTitle(mCollBookAdapter.getItem(itemPos).getTitle())
                .setAdapter(new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_list_item_1, menus),
                            (dialog,which) -> onItemMenuClick(which,itemPos))
                .setNegativeButton(null,null)
                .setPositiveButton(null,null)
                .create();

        collBookDialog.show();
    }

    private void onItemMenuClick(int which,int itemPos){
        switch (which){
            //置顶
            case 0:
                break;
            //缓存
            case 1:
                //2. 进行判断，如果CollBean中状态为未更新。那么就创建Task，加入到Service中去。
                //3. 如果状态为finish，并且isUpdate为true，那么就根据chapter创建状态
                //4. 如果状态为finish，并且isUpdate为false。
                cacheClick(itemPos);
                break;
            //删除
            case 2:
                break;
            //批量管理
            case 3:
                break;
            default:
                break;
        }
    }

    private void cacheClick(int itemPos){
        CollBookBean bean = mCollBookAdapter.getItem(itemPos);
        //创建任务
        mPresenter.createDownloadTask(bean);
    }

    /*******************************************************************8*/

    @Override
    public void showError() {

    }

    @Override
    public void complete() {
        if (mCollBookAdapter.getItemCount() > 0 && mFooterItem == null) {
            mFooterItem = new FooterItemView();
            mCollBookAdapter.addFooterView(mFooterItem);
        }

        if (mRvContent.isRefreshing()){
            mRvContent.finishRefresh();
        }
    }

    @Override
    public void finishRefresh(List<CollBookBean> collBookBeans){
        mCollBookAdapter.refreshItems(collBookBeans);

    }


    @Override
    public void showErrorTip(String error) {
        mRvContent.setTip(error);
        mRvContent.showTip();
    }
    /*****************************************************************/
    class FooterItemView implements WholeAdapter.ItemView{
        @Override
        public View onCreateView(ViewGroup parent) {
            View view = LayoutInflater.from(getContext())
                    .inflate(R.layout.footer_book_shelf, parent, false);
            view.setOnClickListener(
                    (v) -> {
                        //设置RxBus回调
                    }
            );
            return view;
        }

        @Override
        public void onBindView(View view) {
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mPresenter.refreshCollBooks();
    }
}
