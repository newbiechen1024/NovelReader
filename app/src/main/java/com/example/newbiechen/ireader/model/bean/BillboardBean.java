package com.example.newbiechen.ireader.model.bean;

/**
 * Created by newbiechen on 17-4-23.
 * 排行榜
 */

public class BillboardBean {
    /**
     *
     * _id : 54d42d92321052167dfb75e3
     * title : 追书最热榜 Top100
     * cover : /ranking-cover/142319144267827
     * collapse : false
     * monthRank : 564d820bc319238a644fb408
     * totalRank : 564d8494fe996c25652644d2
     */

    private String _id;
    private String title;
    private String cover;
    private boolean collapse;
    private String monthRank;
    private String totalRank;

    public BillboardBean(String title){
        this.title = title;
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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public boolean isCollapse() {
        return collapse;
    }

    public void setCollapse(boolean collapse) {
        this.collapse = collapse;
    }

    public String getMonthRank() {
        return monthRank;
    }

    public void setMonthRank(String monthRank) {
        this.monthRank = monthRank;
    }

    public String getTotalRank() {
        return totalRank;
    }

    public void setTotalRank(String totalRank) {
        this.totalRank = totalRank;
    }
}