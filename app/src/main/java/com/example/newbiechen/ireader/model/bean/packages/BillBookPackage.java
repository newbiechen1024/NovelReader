package com.example.newbiechen.ireader.model.bean.packages;

import com.example.newbiechen.ireader.model.bean.BaseBean;
import com.example.newbiechen.ireader.model.bean.BillBookBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by newbiechen on 17-5-3.
 * BillboardBookPackage
 */

public class BillBookPackage extends BaseBean {

    private RankingBean ranking;

    public RankingBean getRanking() {
        return ranking;
    }

    public void setRanking(RankingBean ranking) {
        this.ranking = ranking;
    }

    public static class RankingBean {
        /**
         * _id : 564d820bc319238a644fb408
         * updated : 2015-11-20T10:06:08.571Z
         * title : 追书最热榜月榜男
         * tag : zhuishuMonthHotMale
         * cover : /ranking-cover/144792013856420
         * __v : 1
         * created : 2017-05-08T10:25:33.192Z
         * isSub : true
         * collapse : false
         * new : true
         * gender : male
         * priority : 1000
         * books : [{"_id":"5642be60f1b24c7a7468c5d7","title":"逆鳞","author":"柳下挥","shortIntro":"天生废材，遭遇龙神附体。继承了神龙的意念和能力，生鳞幻爪、御水龙息、行云降雨，肉身无敌。 在这个人人都想屠龙的时代，李牧羊一直生活的很有压力。","cover":"/agent/http://static.zongheng.com/upload/cover/2015/11/1447211572067.jpg"*/
        private String _id;
        private String updated;
        private String title;
        private String tag;
        private String cover;
        private int __v;
        private String created;
        private boolean isSub;
        private boolean collapse;
        @SerializedName("new")
        private boolean newX;
        private String gender;
        private int priority;
        private String id;
        private int total;
        private List<BillBookBean> books;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getUpdated() {
            return updated;
        }

        public void setUpdated(String updated) {
            this.updated = updated;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public int get__v() {
            return __v;
        }

        public void set__v(int __v) {
            this.__v = __v;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public boolean isIsSub() {
            return isSub;
        }

        public void setIsSub(boolean isSub) {
            this.isSub = isSub;
        }

        public boolean isCollapse() {
            return collapse;
        }

        public void setCollapse(boolean collapse) {
            this.collapse = collapse;
        }

        public boolean isNewX() {
            return newX;
        }

        public void setNewX(boolean newX) {
            this.newX = newX;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<BillBookBean> getBooks() {
            return books;
        }

        public void setBooks(List<BillBookBean> books) {
            this.books = books;
        }
    }
}
