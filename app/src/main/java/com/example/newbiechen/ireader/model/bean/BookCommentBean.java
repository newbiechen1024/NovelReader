package com.example.newbiechen.ireader.model.bean;


import com.example.newbiechen.ireader.model.gen.AuthorBeanDao;
import com.example.newbiechen.ireader.model.gen.BookCommentBeanDao;
import com.example.newbiechen.ireader.model.gen.DaoSession;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.ToOne;

/**
 * Created by newbiechen on 17-4-20.
 */
@Entity(
        indexes = {
                @Index(value = "block"),
                @Index(value = "type"),
                @Index(value = "state")
        }
)
public class BookCommentBean{
    /**
     * _id : 58f805798c8c193a414c6853
     * title : 女朋友生气的奇葩理由我知道这里是女生区可我觉得好玩还是很想转
     * author : {"_id":"553136ba70feaa764a096f6f","avatar":"/avatar/26/eb/26ebf8ede76d7f52cd377960bd66383b","nickname":"九歌","activityAvatar":"","type":"normal","lv":8,"gender":"female"}
     * type : normal
     * likeCount : 6
     * block : girl
     * haveImage : false
     * state : normal
     * updated : 2017-04-20T05:34:17.931Z
     * created : 2017-04-20T00:48:57.085Z
     * commentCount : 35
     * voteCount : 0
     */
    @Id
    private String _id;
    private String authorId;
    private String title;
    @ToOne(joinProperty = "authorId")
    private AuthorBean author;
    private String type;
    private int likeCount;
    private String block;
    private boolean haveImage;
    private String state;
    private String updated;
    private String created;
    private int commentCount;
    private int voteCount;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 2053172650)
    private transient BookCommentBeanDao myDao;
    @Generated(hash = 1349326057)
    private transient String author__resolvedKey;

    @Generated(hash = 189360274)
    public BookCommentBean(String _id, String authorId, String title, String type, int likeCount, String block, boolean haveImage, String state, String updated, String created, int commentCount,
            int voteCount) {
        this._id = _id;
        this.authorId = authorId;
        this.title = title;
        this.type = type;
        this.likeCount = likeCount;
        this.block = block;
        this.haveImage = haveImage;
        this.state = state;
        this.updated = updated;
        this.created = created;
        this.commentCount = commentCount;
        this.voteCount = voteCount;
    }

    @Generated(hash = 677452091)
    public BookCommentBean() {
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
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

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
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
    @Generated(hash = 1667482087)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getBookCommentBeanDao() : null;
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