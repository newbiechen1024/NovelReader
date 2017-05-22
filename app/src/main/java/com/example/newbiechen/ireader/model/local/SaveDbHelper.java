package com.example.newbiechen.ireader.model.local;

import com.example.newbiechen.ireader.model.bean.AuthorBean;
import com.example.newbiechen.ireader.model.bean.DownloadTaskBean;
import com.example.newbiechen.ireader.model.bean.packages.BillboardPackage;
import com.example.newbiechen.ireader.model.bean.ReviewBookBean;
import com.example.newbiechen.ireader.model.bean.BookCommentBean;
import com.example.newbiechen.ireader.model.bean.BookHelpfulBean;
import com.example.newbiechen.ireader.model.bean.BookHelpsBean;
import com.example.newbiechen.ireader.model.bean.BookReviewBean;
import com.example.newbiechen.ireader.model.bean.packages.BookSortPackage;

import java.util.List;

/**
 * Created by newbiechen on 17-4-28.
 */

public interface SaveDbHelper {
    void saveBookComments(List<BookCommentBean> beans);
    void saveBookHelps(List<BookHelpsBean> beans);
    void saveBookReviews(List<BookReviewBean> beans);
    void saveAuthors(List<AuthorBean> beans);
    void saveBooks(List<ReviewBookBean> beans);
    void saveBookHelpfuls(List<BookHelpfulBean> beans);

    void saveBookSortPackage(BookSortPackage bean);
    void saveBillboardPackage(BillboardPackage bean);
    /*************DownloadTask*********************/
    void saveDownloadTask(DownloadTaskBean bean);
}
