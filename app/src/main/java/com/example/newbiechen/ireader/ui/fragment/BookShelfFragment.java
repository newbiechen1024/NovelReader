package com.example.newbiechen.ireader.ui.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.RxBus;
import com.example.newbiechen.ireader.event.DeleteResponseEvent;
import com.example.newbiechen.ireader.event.DeleteTaskEvent;
import com.example.newbiechen.ireader.event.DownloadMessage;
import com.example.newbiechen.ireader.event.RecommendBookEvent;
import com.example.newbiechen.ireader.model.bean.CollBookBean;
import com.example.newbiechen.ireader.model.local.BookRepository;
import com.example.newbiechen.ireader.presenter.BookShelfPresenter;
import com.example.newbiechen.ireader.presenter.contract.BookShelfContract;
import com.example.newbiechen.ireader.ui.activity.ReadActivity;
import com.example.newbiechen.ireader.ui.adapter.CollBookAdapter;
import com.example.newbiechen.ireader.ui.base.BaseMVPFragment;
import com.example.newbiechen.ireader.utils.RxUtils;
import com.example.newbiechen.ireader.utils.ToastUtils;
import com.example.newbiechen.ireader.widget.adapter.WholeAdapter;
import com.example.newbiechen.ireader.widget.itemdecoration.DividerItemDecoration;
import com.example.newbiechen.ireader.widget.refresh.ScrollRefreshRecyclerView;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by newbiechen on 17-4-15.
 */

public class BookShelfFragment extends BaseMVPFragment<BookShelfContract.Presenter>
        implements BookShelfContract.View{
    private static final String TAG = "BookShelfFragment";
    @BindView(R.id.book_shelf_rv_content)
    ScrollRefreshRecyclerView mRvContent;

    /************************************/
    private CollBookAdapter mCollBookAdapter;
    private FooterItemView mFooterItem;

    //是否是第一次进入
    private boolean isInit = true;

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
        mRvContent.addItemDecoration(new DividerItemDecoration(getContext()));
        mRvContent.setAdapter(mCollBookAdapter);
    }

    @Override
    protected void initClick() {
        super.initClick();
        //推荐书籍
        Disposable recommendDisp = RxBus.getInstance()
                .toObservable(RecommendBookEvent.class)
                .subscribe(
                        event ->  {
                            mRvContent.startRefresh();
                            mPresenter.loadRecommendBooks(event.sex);
                        }
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

        //删除书籍 (写的丑了点)
        Disposable deleteDisp = RxBus.getInstance()
                .toObservable(DeleteResponseEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        event -> {
                            if (event.isDelete){
                                ProgressDialog progressDialog = new ProgressDialog(getContext());
                                progressDialog.setMessage("正在删除中");
                                progressDialog.show();
                                BookRepository.getInstance().deleteCollBookInRx(event.collBook)
                                        .compose(RxUtils::toSimpleSingle)
                                        .subscribe(
                                                (Void) -> {
                                                    mCollBookAdapter.removeItem(event.collBook);
                                                    progressDialog.dismiss();
                                                }
                                        );
                            }
                            else {
                                //弹出一个Dialog
                                AlertDialog tipDialog = new AlertDialog.Builder(getContext())
                                        .setTitle("您的任务正在加载")
                                        .setMessage("先请暂停任务再进行删除")
                                        .setPositiveButton("确定", (dialog, which) -> {
                                            dialog.dismiss();
                                        }).create();
                                tipDialog.show();
                            }
                        }
                );
        addDisposable(deleteDisp);

        mRvContent.setOnRefreshListener(
                () ->   mPresenter.updateCollBooks(mCollBookAdapter.getItems())
        );

        mCollBookAdapter.setOnItemClickListener(
                (view, pos) -> {
                    //如果是本地文件，首先判断这个文件是否存在
                    CollBookBean collBook = mCollBookAdapter.getItem(pos);
                    if (collBook.isLocal()){
                        //id表示本地文件的路径
                        String path = collBook.getCover();
                        File file = new File(path);
                        //判断这个本地文件是否存在
                        if (file.exists()){
                            ReadActivity.startActivity(getContext(),
                                    mCollBookAdapter.getItem(pos), true);
                        }
                        else {
                            //提示(从目录中移除这个文件)
                            new AlertDialog.Builder(getContext())
                                    .setTitle(getResources().getString(R.string.nb_common_tip))
                                    .setMessage("文件不存在,是否删除")
                                    .setPositiveButton(getResources().getString(R.string.nb_common_sure),
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    deleteBook(collBook);
                                                }
                                            })
                                    .setNegativeButton(getResources().getString(R.string.nb_common_cancel), null)
                                    .show();
                        }
                    }
                    else {
                        ReadActivity.startActivity(getContext(),
                                mCollBookAdapter.getItem(pos), true);
                    }
                }
        );

        mCollBookAdapter.setOnItemLongClickListener(
                (v,pos) -> {
                    //开启Dialog,最方便的Dialog,就是AlterDialog
                    openItemDialog(mCollBookAdapter.getItem(pos));
                    return true;
                }
        );
    }

    @Override
    protected void processLogic() {
        super.processLogic();
        mRvContent.startRefresh();
    }

    private void openItemDialog(CollBookBean collBook){
        String[] menus;
        if (collBook.isLocal()){
            menus = getResources().getStringArray(R.array.nb_menu_local_book);
        }
        else {
            menus = getResources().getStringArray(R.array.nb_menu_net_book);
        }
        AlertDialog collBookDialog = new AlertDialog.Builder(getContext())
                .setTitle(collBook.getTitle())
                .setAdapter(new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_list_item_1, menus),
                            (dialog,which) -> onItemMenuClick(menus[which],collBook))
                .setNegativeButton(null,null)
                .setPositiveButton(null,null)
                .create();

        collBookDialog.show();
    }

    private void onItemMenuClick(String which,CollBookBean collBook){
        switch (which){
            //置顶
            case "置顶":
                break;
            //缓存
            case "缓存":
                //2. 进行判断，如果CollBean中状态为未更新。那么就创建Task，加入到Service中去。
                //3. 如果状态为finish，并且isUpdate为true，那么就根据chapter创建状态
                //4. 如果状态为finish，并且isUpdate为false。
                downloadBook(collBook);
                break;
            //删除
            case "删除":
                deleteBook(collBook);
                break;
            //批量管理
            case "批量管理":
                break;
            default:
                break;
        }
    }

    private void downloadBook(CollBookBean collBook){
        //创建任务
        mPresenter.createDownloadTask(collBook);
    }

    /**
     * 默认删除本地文件
     * @param collBook
     */
    private void deleteBook(CollBookBean collBook){
        if (collBook.isLocal()){
            View view = LayoutInflater.from(getContext())
                    .inflate(R.layout.dialog_delete, null);
            CheckBox cb = (CheckBox) view.findViewById(R.id.delete_cb_select);
            new AlertDialog.Builder(getContext())
                    .setTitle("删除文件")
                    .setView(view)
                    .setPositiveButton(getResources().getString(R.string.nb_common_sure), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            boolean isSelected = cb.isSelected();
                            if (isSelected) {
                                ProgressDialog progressDialog = new ProgressDialog(getContext());
                                progressDialog.setMessage("正在删除中");
                                progressDialog.show();
                                //删除
                                File file = new File(collBook.getCover());
                                if (file.exists()) file.delete();
                                BookRepository.getInstance().deleteCollBook(collBook);
                                BookRepository.getInstance().deleteBookRecord(collBook.get_id());

                                //从Adapter中删除
                                mCollBookAdapter.removeItem(collBook);
                                progressDialog.dismiss();
                            } else {
                                BookRepository.getInstance().deleteCollBook(collBook);
                                BookRepository.getInstance().deleteBookRecord(collBook.get_id());
                                //从Adapter中删除
                                mCollBookAdapter.removeItem(collBook);
                            }
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.nb_common_cancel),null)
                    .show();
        }
        else {
            RxBus.getInstance().post(new DeleteTaskEvent(collBook));
        }
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
        //如果是初次进入，则更新书籍信息
        if (isInit){
            isInit = false;
            mRvContent.post(
                    () -> mPresenter.updateCollBooks(mCollBookAdapter.getItems())
            );
        }
    }

    @Override
    public void finishUpdate() {
        //重新从数据库中获取数据
        mCollBookAdapter.refreshItems(BookRepository
                .getInstance().getCollBooks());
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
