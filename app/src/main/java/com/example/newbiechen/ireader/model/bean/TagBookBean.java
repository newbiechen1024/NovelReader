package com.example.newbiechen.ireader.model.bean;

import java.util.List;

/**
 * Created by newbiechen on 17-5-4.
 */

public  class TagBookBean {
    /**
     * _id : 57206c3539a913ad65d35c7b
     * title : 一念永恒
     * author : 耳根
     * shortIntro : 一念成沧海，一念化桑田。一念斩千魔，一念诛万仙。唯我念……永恒
     * cover : /agent/http://image.cmfu.com/books/1003354631/1003354631.jpg
     * cat : 仙侠
     * majorCate : 仙侠
     * minorCate : 幻想修仙
     * latelyFollower : 221865
     * retentionRatio : 75.17
     * lastChapter : 第760章 监察底蕴！
     * tags : ["仙侠"]
     */

    private String _id;
    private String title;
    private String author;
    private String shortIntro;
    private String cover;
    private String cat;
    private String majorCate;
    private String minorCate;
    private int latelyFollower;
    private double retentionRatio;
    private String lastChapter;
    private List<String> tags;

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

    public String getMajorCate() {
        return majorCate;
    }

    public void setMajorCate(String majorCate) {
        this.majorCate = majorCate;
    }

    public String getMinorCate() {
        return minorCate;
    }

    public void setMinorCate(String minorCate) {
        this.minorCate = minorCate;
    }

    public int getLatelyFollower() {
        return latelyFollower;
    }

    public void setLatelyFollower(int latelyFollower) {
        this.latelyFollower = latelyFollower;
    }

    public double getRetentionRatio() {
        return retentionRatio;
    }

    public void setRetentionRatio(double retentionRatio) {
        this.retentionRatio = retentionRatio;
    }

    public String getLastChapter() {
        return lastChapter;
    }

    public void setLastChapter(String lastChapter) {
        this.lastChapter = lastChapter;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}