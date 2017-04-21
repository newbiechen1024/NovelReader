package com.example.newbiechen.ireader.model.bean;

/**
 * Created by newbiechen on 17-4-21.
 */

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

    private String _id;
    private String title;
    private BookBean book;
    private HelpfulBean helpful;
    private int likeCount;
    private boolean haveImage;
    private String state;
    private String updated;
    private String created;

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

    public BookBean getBook() {
        return book;
    }

    public void setBook(BookBean book) {
        this.book = book;
    }

    public HelpfulBean getHelpful() {
        return helpful;
    }

    public void setHelpful(HelpfulBean helpful) {
        this.helpful = helpful;
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

    public static class BookBean {
        /**
         * _id : 530f3912651881e60d04deb3
         * cover : /agent/http://img.17k.com/images/bookcover/2014/3769/18/753884-1399818238000.jpg
         * title : 我的26岁女房客
         * site : zhuishuvip
         * type : dsyn
         * latelyFollower : null
         * retentionRatio : null
         */

        private String _id;
        private String cover;
        private String title;
        private String site;
        private String type;
        private Object latelyFollower;
        private Object retentionRatio;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSite() {
            return site;
        }

        public void setSite(String site) {
            this.site = site;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Object getLatelyFollower() {
            return latelyFollower;
        }

        public void setLatelyFollower(Object latelyFollower) {
            this.latelyFollower = latelyFollower;
        }

        public Object getRetentionRatio() {
            return retentionRatio;
        }

        public void setRetentionRatio(Object retentionRatio) {
            this.retentionRatio = retentionRatio;
        }
    }

    public static class HelpfulBean {
        /**
         * total : 1
         * no : 5
         * yes : 6
         */

        private int total;
        private int no;
        private int yes;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getNo() {
            return no;
        }

        public void setNo(int no) {
            this.no = no;
        }

        public int getYes() {
            return yes;
        }

        public void setYes(int yes) {
            this.yes = yes;
        }
    }
}