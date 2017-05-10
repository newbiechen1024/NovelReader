package com.example.newbiechen.ireader.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by newbiechen on 17-4-20.
 * 作者
 */
@Entity
public class AuthorBean {
    /**
     * _id : 553136ba70feaa764a096f6f
     * avatar : /avatar/26/eb/26ebf8ede76d7f52cd377960bd66383b
     * nickname : 九歌
     * activityAvatar :
     * type : normal
     * lv : 8
     * gender : female
     */
    @Id
    private String _id;

    private String avatar;
    private String nickname;
    private String activityAvatar;
    private String type;
    private int lv;
    private String gender;

    @Generated(hash = 1152582024)
    public AuthorBean(String _id, String avatar, String nickname,
            String activityAvatar, String type, int lv, String gender) {
        this._id = _id;
        this.avatar = avatar;
        this.nickname = nickname;
        this.activityAvatar = activityAvatar;
        this.type = type;
        this.lv = lv;
        this.gender = gender;
    }

    @Generated(hash = 1694633584)
    public AuthorBean() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getActivityAvatar() {
        return activityAvatar;
    }

    public void setActivityAvatar(String activityAvatar) {
        this.activityAvatar = activityAvatar;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLv() {
        return lv;
    }

    public void setLv(int lv) {
        this.lv = lv;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "AuthorBean{" +
                "_id='" + _id + '\'' +
                ", avatar='" + avatar + '\'' +
                ", nickname='" + nickname + '\'' +
                ", activityAvatar='" + activityAvatar + '\'' +
                ", type='" + type + '\'' +
                ", lv=" + lv +
                ", gender='" + gender + '\'' +
                '}';
    }
}