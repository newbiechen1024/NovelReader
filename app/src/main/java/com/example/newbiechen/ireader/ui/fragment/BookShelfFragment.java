package com.example.newbiechen.ireader.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.RxBus;
import com.example.newbiechen.ireader.event.RecommendBookEvent;
import com.example.newbiechen.ireader.model.bean.CollBookBean;
import com.example.newbiechen.ireader.model.local.CollBookManager;
import com.example.newbiechen.ireader.presenter.BookShelfPresenter;
import com.example.newbiechen.ireader.presenter.contract.BookShlefContract;
import com.example.newbiechen.ireader.ui.adapter.CollBookAdapter;
import com.example.newbiechen.ireader.ui.base.BaseFragment;
import com.example.newbiechen.ireader.ui.base.BaseListAdapter;
import com.example.newbiechen.ireader.ui.base.BaseRxFragment;
import com.example.newbiechen.ireader.utils.RxUtils;
import com.example.newbiechen.ireader.widget.adapter.WholeAdapter;
import com.example.newbiechen.ireader.widget.refresh.ScrollRefreshRecyclerView;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;

/**
 * Created by newbiechen on 17-4-15.
 */

public class BookShelfFragment extends BaseRxFragment<BookShlefContract.Presenter>
        implements BookShlefContract.View{
    private static final String TAG = "BookShelfFragment";
    @BindView(R.id.book_shelf_rv_content)
    ScrollRefreshRecyclerView mRvContent;

    /************************************/
    private CollBookAdapter mCollBookAdapter;
    private FooterItemView mFooterItem;

    private List<CollBookBean> mCollBookList;
    @Override
    protected int getContentId() {
        return R.layout.fragment_bookshelf;
    }

    @Override
    protected BookShlefContract.Presenter bindPresenter() {
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
        Disposable disposable = RxBus.getInstance()
                .toObservable(RecommendBookEvent.class)
                .subscribe(
                        event ->  mPresenter.loadRecommendBooks(event.sex)
                );
        addDisposable(disposable);

        mRvContent.setOnRefreshListener(
                () ->   mPresenter.updateCollBooks(mCollBookList)
        );
        mCollBookAdapter.setOnItemLongClickListener(
                (v,pos) -> {
                    //开启Dialog,最方便的Dialog,就是AlterDialog
                    openItemDialog(mCollBookAdapter.getItem(pos).getTitle());
                    return true;
                }
        );
    }

    private void openItemDialog(String title){
        String[] menus = getResources().getStringArray(R.array.nb_menu_coll_book);
        final AlertDialog collBookDialog = new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setAdapter(new ArrayAdapter<String>(getContext(),
                                android.R.layout.simple_list_item_1, menus),
                        (dialog,which) ->{
                            //点击事件
                        })
                .setNegativeButton(null,null)
                .setPositiveButton(null,null)
                .create();
        collBookDialog.show();
    }

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
        mCollBookList = collBookBeans;
        mCollBookAdapter.refreshItems(collBookBeans);
        //存储到数据库中
        CollBookManager.getInstance()
                .saveCollBooks(collBookBeans);
    }

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
