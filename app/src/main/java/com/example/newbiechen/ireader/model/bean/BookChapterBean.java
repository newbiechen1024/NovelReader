package com.example.newbiechen.ireader.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by newbiechen on 17-5-10.
 * 书的章节链接(作为下载的进度数据)
 */
@Entity
public class BookChapterBean {
    /**
     * title : 第一章 他叫public class BookChapterBean {

}
白小纯
     * link : http://read.qidian.com/chapter/rJgN8tJ_cVdRGoWu-UQg7Q2/6jr-buLIUJSaGfXRMrUjdw2
     * unreadble : false
     */

    //链接是唯一的
    @Id
    private String link;

    private String title;
    @Index
    private String taskName;

    private boolean unreadble;

    @Generated(hash = 850344662)
    public BookChapterBean(String link, String title, String taskName, boolean unreadble) {
        this.link = link;
        this.title = title;
        this.taskName = taskName;
        this.unreadble = unreadble;
    }

    @Generated(hash = 853839616)
    public BookChapterBean() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isUnreadble() {
        return unreadble;
    }

    public void setUnreadble(boolean unreadble) {
        this.unreadble = unreadble;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public boolean getUnreadble() {
        return this.unreadble;
    }
}