package com.example.newbiechen.ireader.model.bean;

/**
 * Created by newbiechen on 17-5-3.
 * 排行榜的书籍 (不被公用的直接作为内部类)
 */

public class BillBookBean {
    /**
     * _id : 57206c3539a913ad65d35c7b
     * title : 一念永恒
     * author : 耳根
     * shortIntro : 一念成沧海，一念化桑田。一念斩千魔，一念诛万仙。唯我念……永恒
     * cover : /agent/http://image.cmfu.com/books/1003354631/1003354631.jpg
     * cat : 仙侠
     * site : zhuishuvip
     * banned : 0
     * latelyFollower : 219403
     * retentionRatio : 75.24
     */

    private String _id;
    private String title;
    private String author;
    private String shortIntro;
    private String cover;
    private String cat;
    private String site;
    private int banned;
    private int latelyFollower;
    private String retentionRatio;

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getShortIntro() {
        return shortIntro;
    }

    public void setShortIntro(String shortIntro) {
        this.shortIntro = shortIntro;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public int getBanned() {
        return banned;
    }

    public void setBanned(int banned) {
        this.banned = banned;
    }

    public int getLatelyFollower() {
        return latelyFollower;
    }

    public void setLatelyFollower(int latelyFollower) {
        this.latelyFollower = latelyFollower;
    }

    public String getRetentionRatio() {
        return retentionRatio;
    }

    public void setRetentionRatio(String retentionRatio) {
        this.retentionRatio = retentionRatio;
    }
}
