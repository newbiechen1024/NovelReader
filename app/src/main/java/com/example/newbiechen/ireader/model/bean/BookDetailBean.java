package com.example.newbiechen.ireader.model.bean;

import com.example.newbiechen.ireader.model.bean.BaseBean;

import java.util.List;

/**
 * Created by newbiechen on 17-5-4.
 */

public class BookDetailBean{
    /**
     * _id : 525253d094336b3155000dd8       (Collect)
     * author : w风雪                       (Collect)
     * cover : /agent/http://image.cmfu.com/books/2797907/2797907.jpg  (Collect)
     * creater : iPhone 5 (GSM+CDMA)
     * longIntro : 一死今生了却凡尘！         (Collect)
     重生洪荒造化苍生！
     天道之下尽皆蝼蚁！
     唯有异数勘破万法！
     且看主角这个穿入洪荒世界的异数如何：
     造化福泽苍生
     道法纵横天地
     挣脱天道束缚
     一剑破空而去
     自此逍遥无束...
     书友群：209425550
     * title : 洪荒造化
     * cat : 洪荒封神
     * majorCate : 仙侠
     * minorCate : 洪荒封神
     * _le : false
     * allowMonthly : false
     * allowVoucher : true
     * allowBeanVoucher : false
     * hasCp : true              (Collect)
     * postCount : 121
     * latelyFollower : 1233      (Collect)
     * followerCount : 35
     * wordCount : 5947980
     * serializeWordCount : 4614
     * retentionRatio : 18.04     (Collect)
     * updated : 2016-04-03T13:48:05.907Z  (Collect)
     * isSerial : true
     * chaptersCount : 1294         (Collect)
     * lastChapter : 完本感言！     (Collect)
     * gender : ["male"]
     * tags : ["热血","洪荒封神","洪荒","架空","修炼","仙侠"]
     * donate : false
     * copyright : 阅文集团正版授权
     */

    private String _id;
    private String author;
    private String cover;
    private String creater;
    private String longIntro;
    private String title;
    private String cat;
    private String majorCate;
    private String minorCate;
    private boolean _le;
    private boolean allowMonthly;
    private boolean allowVoucher;
    private boolean allowBeanVoucher;
    private boolean hasCp;
    private int postCount;
    private int latelyFollower;
    private int followerCount;
    private int wordCount;
    private int serializeWordCount;
    private String retentionRatio;
    private String updated;
    private boolean isSerial;
    private int chaptersCount;
    private String lastChapter;
    private boolean donate;
    private String copyright;
    private List<String> gender;
    private List<String> tags;


    private CollBookBean collBookBean;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getLongIntro() {
        return longIntro;
    }

    public void setLongIntro(String longIntro) {
        this.longIntro = longIntro;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public boolean is_le() {
        return _le;
    }

    public void set_le(boolean _le) {
        this._le = _le;
    }

    public boolean isAllowMonthly() {
        return allowMonthly;
    }

    public void setAllowMonthly(boolean allowMonthly) {
        this.allowMonthly = allowMonthly;
    }

    public boolean isAllowVoucher() {
        return allowVoucher;
    }

    public void setAllowVoucher(boolean allowVoucher) {
        this.allowVoucher = allowVoucher;
    }

    public boolean isAllowBeanVoucher() {
        return allowBeanVoucher;
    }

    public void setAllowBeanVoucher(boolean allowBeanVoucher) {
        this.allowBeanVoucher = allowBeanVoucher;
    }

    public boolean isHasCp() {
        return hasCp;
    }

    public void setHasCp(boolean hasCp) {
        this.hasCp = hasCp;
    }

    public int getPostCount() {
        return postCount;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }

    public int getLatelyFollower() {
        return latelyFollower;
    }

    public void setLatelyFollower(int latelyFollower) {
        this.latelyFollower = latelyFollower;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    public int getSerializeWordCount() {
        return serializeWordCount;
    }

    public void setSerializeWordCount(int serializeWordCount) {
        this.serializeWordCount = serializeWordCount;
    }

    public String getRetentionRatio() {
        return retentionRatio;
    }

    public void setRetentionRatio(String retentionRatio) {
        this.retentionRatio = retentionRatio;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public boolean isIsSerial() {
        return isSerial;
    }

    public void setIsSerial(boolean isSerial) {
        this.isSerial = isSerial;
    }

    public int getChaptersCount() {
        return chaptersCount;
    }

    public void setChaptersCount(int chaptersCount) {
        this.chaptersCount = chaptersCount;
    }

    public String getLastChapter() {
        return lastChapter;
    }

    public void setLastChapter(String lastChapter) {
        this.lastChapter = lastChapter;
    }

    public boolean isDonate() {
        return donate;
    }

    public void setDonate(boolean donate) {
        this.donate = donate;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public List<String> getGender() {
        return gender;
    }

    public void setGender(List<String> gender) {
        this.gender = gender;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public CollBookBean getCollBookBean(){
        if (collBookBean == null){
            collBookBean = createCollBookBean();
        }
        return collBookBean;
    }

    public CollBookBean createCollBookBean(){
        CollBookBean bean = new CollBookBean();
        bean.set_id(get_id());
        bean.setTitle(getTitle());
        bean.setAuthor(getAuthor());
        bean.setShortIntro(getLongIntro());
        bean.setCover(getCover());
        bean.setHasCp(isHasCp());
        bean.setLatelyFollower(getLatelyFollower());
        bean.setRetentionRatio(Double.parseDouble(getRetentionRatio()));
        bean.setUpdated(getUpdated());
        bean.setChaptersCount(getChaptersCount());
        bean.setLastChapter(getLastChapter());
        return bean;
    }
}
