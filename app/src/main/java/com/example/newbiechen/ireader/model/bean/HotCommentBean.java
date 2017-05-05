package com.example.newbiechen.ireader.model.bean;

/**
 * Created by newbiechen on 17-5-4.
 */

public class HotCommentBean {
    /**
     * _id : 53d6461674e1445c4bff32c5
     * rating : 5
     * content : 我看的第一本网文大部书叫什么大法师，穿越剧，记忆深刻的桥段，在地球吃的红薯在异世居然是增加法力的神物，如此主角在异世如鱼得水。（那时玄幻刚萌芽，都是西方异幻），之后初中了，全校都在讨论谢文东（那会以为他是我们学校哪位老大呢，后来才知道东哥东北的，是六道写的一大哥）那时就觉得六道真tmnb。这TM写的真像我，谁少年没有一个做大哥的梦，都感觉谢文东有自己的影子（东哥是写神了的人物）。那会儿零花钱基本全投给我们学校门口那书店了，10块钱可以包月看书（10块钱真就是我一月的零花钱了，我们这经济落后，本人也没怎么去过大城市，不知道乃们都是什么情况），那会开始看了《邪风曲》《僵尸医生》《金鳞》（嘿嘿，别喷，小心封你，你懂的）唐三的只看过《死神》，说实话看书我不能说这书好不好，我只能说看书真的需要根据书龄和个人的文化素养来分（首先申明:分级不是歧视，只是想让大家更轻松的选择适合大家阅读的网文，不然如同嚼蜡），大致可分:初读级（刚入门或文化程度低一些的），痴迷级（这个就是你在看了一些网文后确实感兴趣，并牺牲日常生活时间的），随身级（这个就比较入迷了，咱走着吃着还看着，严重的咱相亲时也捧着手机看的津津有味，有时哥几个聚着也不能阻挡看书的节奏，让一切基情面临危机）。再往后高中，高一买不起手机就买个MP4.（这才是神器，有了它咱上课看书从没被老师抓过，当然成绩也直线下降）可以说网文的崛起它功不可没，没有mP4多少学生党看不了TXT.飞卢，黑龙这些网站可是我们的天下！那时才开始网文追书时代，《星辰变》可以说是那时最受同学们喜爱的，快餐文崛起了，番茄功不可没（那时番茄是独尊，别说文笔，那一年我记得他一本书是200万吧，独一无二，笑傲群雄，那时的网评还都是肯定的）到现在再来看番茄，我觉得他带了个坏头，更多的作者开始在乎月票，点击率，推荐榜。对于书评，对于情节开始走入了一个奇怪的圈子————情节月底爆发圈，开始了拖延症，为了收入最大化管他情节铺垫什么的，高潮一律拖在月底打月票战，还捆绑读者，搞成什么什么帮派团伙之类的，务必多多结交有钱大佬土豪，为其助阵，咱拼的不是作品，拼的是谁背后站的土豪多！当然咱也不能全怪作者，他们也没有办法，这种环境下，除了那些有真材实料的大神敢一心一意凭喜好写作，写自己喜欢写的情节，一般的小作者为了生活谁敢这么写，立马网站封杀你，全网拉平论拉喷子喷死你，没了网站的支持你在多的报复都会付之东流。所以网站攥着编辑的脖子，编辑攥着作者的脖子，作者只能捆绑着读者喽！所以错不在作者，或者说不全在。要喷喷网站！现在看了这么久的书，感觉还在坚持写故事给大家看的也就烽火，猫腻，柳下惠，月关之流（还有好多，大家可以补充）平心而论那些土豆之流站得住脚吗？前天还议论唐三呢，昨天还记得番茄，今天就换成土豆！我敢这么说，烽火，猫腻这些作者只要我还看网文，我就还记得他们，不论20年还是30年，呵呵，或许这就是魅力跟炒作力的对比！
     * title : 本人10年网文我也来喷，不服来辩
     * author : {"_id":"539504dc241e0ddd1e5b10fb","avatar":"/avatar/0e/6d/0e6df891ea523f36f57304fa5db3cbb0","nickname":"Cx丶 ㄑ","activityAvatar":"","type":"normal","lv":8,"gender":"male"}
     * helpful : {"no":1796,"total":8767,"yes":10563}
     * likeCount : 1094
     * state : normal
     * updated : 2017-05-04T07:41:15.224Z
     * created : 2014-07-28T12:46:14.596Z
     * commentCount : 4904
     */

    private String _id;
    private int rating;
    private String content;
    private String title;
    private AuthorBean author;
    private BookHelpfulBean helpful;
    private int likeCount;
    private String state;
    private String updated;
    private String created;
    private int commentCount;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public AuthorBean getAuthor() {
        return author;
    }

    public void setAuthor(AuthorBean author) {
        this.author = author;
    }

    public BookHelpfulBean getHelpful() {
        return helpful;
    }

    public void setHelpful(BookHelpfulBean helpful) {
        this.helpful = helpful;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
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
}
