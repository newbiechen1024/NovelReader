package com.example.newbiechen.ireader.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;
import com.example.newbiechen.ireader.model.gen.DaoSession;
import com.example.newbiechen.ireader.model.gen.AuthorBeanDao;
import com.example.newbiechen.ireader.model.gen.BookHelpsBeanDao;

/**
 * Created by newbiechen on 17-4-20.
 */
@Entity
public class BookHelpsBean {
    /**
     * _id : 58f7590223c128231d6fc3ec
     * author : {"_id":"575558500082adf41f499536","avatar":"/avatar/19/40/1940310d1277e583b07369dce21ca701","nickname":"秋名山丶公子","activityAvatar":"/activities/20170120/1.jpg","type":"commentator","lv":8,"gender":"female"}
     * title : 【读书会-送给喜欢末世的你】末世流精选小说（第三期）
     * likeCount : 19
     * haveImage : false
     * state : normal
     * updated : 2017-04-20T05:45:19.976Z
     * created : 2017-04-19T12:33:06.285Z
     * commentCount : 46
     */
    @Id
    private String _id;
    private String authorId;
    @ToOne(joinProperty = "authorId")
    private AuthorBean author;
    private String title;
    private int likeCount;
    private boolean haveImage;
    @Index
    private String state;
    private String updated;
    private String created;
    private int commentCount;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 899589534)
    private transient BookHelpsBeanDao myDao;
    @Generated(hash = 1349326057)
    private transient String author__resolvedKey;

    @Generated(hash = 2002227547)
    public BookHelpsBean(String _id, String authorId, String title, int likeCount, boolean haveImage, String state, String updated, String created, int commentCount) {
        this._id = _id;
        this.authorId = authorId;
        this.title = title;
        this.likeCount = likeCount;
        this.haveImage = haveImage;
        this.state = state;
        this.updated = updated;
        this.created = created;
        this.commentCount = commentCount;
    }

    @Generated(hash = 1556001284)
    public BookHelpsBean() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public boolean getHaveImage() {
        return this.haveImage;
    }

    public String getAuthorId() {
        return this.authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
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
    @Generated(hash = 1431996007)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getBookHelpsBeanDao() : null;
    }

    public String getTitle() {
        return this.title;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 625279819)
    public AuthorBean getAuthor() {
        String __key = this.authorId;
        if (author__resolvedKey == null || author__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AuthorBeanDao targetDao = daoSession.getAuthorBeanDao();
            AuthorBean authorNew = targetDao.load(__key);
            synchronized (this) {
                author = authorNew;
                author__resolvedKey = __key;
            }
        }
        return author;
    }
    @Keep
    public AuthorBean getAuthorBean(){
        if (authorId == null){
            setAuthor(author);
        }
        if (daoSession == null){
            return author;
        }
        else {
            return getAuthor();
        }
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1823011606)
    public void setAuthor(AuthorBean author) {
        synchronized (this) {
            this.author = author;
            authorId = author == null ? null : author.get_id();
            author__resolvedKey = authorId;
        }
    }
}