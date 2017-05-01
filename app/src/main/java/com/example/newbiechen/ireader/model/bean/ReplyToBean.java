package com.example.newbiechen.ireader.model.bean;

/**
 * Created by newbiechen on 17-4-29.
 */

public class ReplyToBean {
    /**
     * _id : 57caec937a142c2277757f2d
     * floor : 7038
     * author : {"_id":"576a96dd4cb19fa249303369","nickname":"刘"}
     */

    private String _id;
    private int floor;
    private ReplyAuthorBean author;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public ReplyAuthorBean getAuthor() {
        return author;
    }

    public void setAuthor(ReplyAuthorBean author) {
        this.author = author;
    }

    public static class ReplyAuthorBean {
        /**
         * _id : 576a96dd4cb19fa249303369
         * nickname : 刘
         */

        private String _id;
        private String nickname;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }
}
