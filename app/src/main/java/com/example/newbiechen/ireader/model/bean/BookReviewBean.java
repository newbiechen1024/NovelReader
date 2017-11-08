package com.example.newbiechen.ireader.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;
import com.example.newbiechen.ireader.model.gen.DaoSession;
import com.example.newbiechen.ireader.model.gen.BookHelpfulBeanDao;
import com.example.newbiechen.ireader.model.gen.BookReviewBeanDao;
import com.example.newbiechen.ireader.model.gen.ReviewBookBeanDao;

/**
 * Created by newbiechen on 17-4-21.
 * 书籍类别讨论
 */
@Entity
public class BookReviewBean {
    /**
     * _id : 58f8f3efedaa9fe3624a87bb
     * title : 为你写一个中肯的书评，我的访客
     * book : {"_id":"530f3912651881e60d04deb3","cover":"/agent/http://img.17k.com/images/bookcover/2014/3769/18/753884-1399818238000.jpg","title":"我的26岁女房客","site":"zhuishuvip","type":"dsyn","latelyFollower":null,"retentionRatio":null}
     * helpful : {"total":1,"no":5,"yes":6}
     * likeCount : 0
     * haveImage : false
     * state : distillate
     * updated : 2017-04-21T08:20:15.991Z
     * created : 2017-04-20T17:46:23.366Z
     */
    @Id
    private String _id;
    //获取Book的外键
    private String bookId;


    private String title;
    @ToOne(joinProperty = "bookId")
    private ReviewBookBean book;
    @ToOne(joinProperty = "_id")
    private BookHelpfulBean helpful;
    private int likeCount;
    private boolean haveImage;
    @Index
    private String state;
    private String updated;
    private String created;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1457560531)
    private transient BookReviewBeanDao myDao;

    @Generated(hash = 765371588)
    public BookReviewBean(String _id, String bookId, String title, int likeCount, boolean haveImage, String state, String updated, String created) {
        this._id = _id;
        this.bookId = bookId;
        this.title = title;
        this.likeCount = likeCount;
        this.haveImage = haveImage;
        this.state = state;
        this.updated = updated;
        this.created = created;
    }

    @Generated(hash = 356564766)
    public BookReviewBean() {
    }

    @Generated(hash = 168832412)
    private transient String helpful__resolvedKey;
    @Generated(hash = 1195315420)
    private transient String book__resolvedKey;
    
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public boolean isHaveImage() {
        return haveImage;
    }

    public void setHaveImage(boolean haveImage) {
        this.haveImage = haveImage;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public boolean getHaveImage() {
        return this.haveImage;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 582781380)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getBookReviewBeanDao() : null;
    }

    public String getBookId() {
        return this.bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1574749097)
    public ReviewBookBean getBook() {
        String __key = this.bookId;
        if (book__resolvedKey == null || book__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ReviewBookBeanDao targetDao = daoSession.getReviewBookBeanDao();
            ReviewBookBean bookNew = targetDao.load(__key);
            synchronized (this) {
                book = bookNew;
                book__resolvedKey = __key;
            }
        }
        return book;
    }

    @Keep
    public ReviewBookBean getBookBean(){
        if (bookId == null){
            setBook(book);
        }
        if (daoSession == null){
            return book;
        }
        else {
            return getBook();
        }
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1617516334)
    public void setBook(ReviewBookBean book) {
        synchronized (this) {
            this.book = book;
            bookId = book == null ? null : book.get_id();
            book__resolvedKey = bookId;
        }
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 2081731991)
    public BookHelpfulBean getHelpful() {
        String __key = this._id;
        if (helpful__resolvedKey == null || helpful__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            BookHelpfulBeanDao targetDao = daoSession.getBookHelpfulBeanDao();
            BookHelpfulBean helpfulNew = targetDao.load(__key);
            synchronized (this) {
                helpful = helpfulNew;
                helpful__resolvedKey = __key;
            }
        }
        return helpful;
    }

    @Keep
    public BookHelpfulBean getHelpfulBean(){
        if (helpful != null){
            helpful.set_id(get_id());
        }
        if (daoSession == null){
            return helpful;
        }
        else {
            return getHelpful();
        }
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 849553614)
    public void setHelpful(BookHelpfulBean helpful) {
        synchronized (this) {
            this.helpful = helpful;
            _id = helpful == null ? null : helpful.get_id();
            helpful__resolvedKey = _id;
        }
    }




}