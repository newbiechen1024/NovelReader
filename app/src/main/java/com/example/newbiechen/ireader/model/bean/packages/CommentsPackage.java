package com.example.newbiechen.ireader.model.bean.packages;

import com.example.newbiechen.ireader.model.bean.BaseBean;
import com.example.newbiechen.ireader.model.bean.CommentBean;

import java.util.List;

/**
 * Created by newbiechen on 17-4-29.
 */

public class CommentsPackage extends BaseBean {

    /**
     * comments : [{"_id":"57b8fbc4b679b59a66472be5","content":"我都把都市文当做小黄文看","author":{"_id":"571eea02d411f7867cfd6899","avatar":"/avatar/28/4a/284a438dc8594731ba1255551aae4b6e","nickname":"请情清","activityAvatar":"","type":"normal","lv":8,"gender":"male"},"floor":10,"likeCount":79,"created":"2016-08-21T00:54:28.087Z","replyTo":null},{"_id":"57ba682ada477d63034cfd68","content":"必有一个身材火辣的女警先和主角处处作对，后又钦佩无比拜倒在主角牛仔裤下;必有一冷艳女总裁/女董事长/女高管(最关键年龄还不大)和主角发生暧昧;必有一个女大学生;非常有可能有女高管的女秘书;极有可能出现空姐。 都市书总是少不了这几类女角色和主角感情纠葛。而且女警察往往前一两章就出现。现在翻一本都市小说 一出现女警 简直都要倒胃口。某些书男主都同时有了十几个女人了，某个女人突然要和他保持距离分手，男主简直痛不欲生泪飞顿做倾盆雨,死活都要找出来原因挽回这个女人。看到这种描写简直他妈恶心。对了还有个抓住女总裁心的桥段 那就是男主往往做菜手段高超 时不时给女总裁做几餐饭 女总裁就感动的眼泪汪汪了。","author":{"_id":"563761a0a31c0f0363f4d4cf","avatar":"/avatar/df/29/df291b5ef63cbcda6ebd6e47e655d670","nickname":"策马绝尘","activityAvatar":"/activities/20170120/2.jpg","type":"normal","lv":9,"gender":"male"},"floor":41,"likeCount":39,"created":"2016-08-22T02:49:14.911Z","replyTo":null},{"_id":"57b8e366933b5d5d6842b947","content":"无脑yy只会引起读者反感＋1","author":{"_id":"56d8c4a0a61c2f0f19f1edbb","avatar":"/avatar/1a/3d/1a3df7d2df21ba537a85f621736d3723","nickname":"forever、\u201c浅笑\u201d。","activityAvatar":"","type":"normal","lv":7,"gender":"male"},"floor":9,"likeCount":31,"created":"2016-08-20T23:10:30.914Z","replyTo":null},{"_id":"57ba5aaca9dbcc687529b16e","content":"不要狗血，就像电视剧里的各种车祸，失忆。\n        要有逻辑，不要出个门就被拉过去当挡箭牌，不要动不动就去当冒牌男盆友，然后各种富二代，大老板，二奶轮流找猪脚打脸。还不要无脑收老婆，还有各种遇见一个女人就：这是我遇见的最美的女人，比***还美。最后，别看见美女就走不动路好吗？被美女利用都不知道，还傻乎乎的凑上去。\n          今日酒吧必有一夜情。还打蛇不死，必受其害，然后买杀手弄死猪脚，最后才下狠手\u2026\u2026\u2026\u2026","author":{"_id":"556fcea7f116d52278184d9d","avatar":"/avatar/13/66/1366e2ca6d206dc0e621f81596c7907b","nickname":"炸天帮\u2014\u2014擒屎皇","activityAvatar":"/activities/20170120/1.jpg","type":"normal","lv":9,"gender":"female"},"floor":40,"likeCount":21,"created":"2016-08-22T01:51:40.275Z","replyTo":null},{"_id":"57b9c532b0d0557b5805c872","content":"个人及其不喜种马.......tm还不如看片去呢","author":{"_id":"55096df97033e035267cff13","avatar":"/avatar/37/f5/37f57ae21f83baea3b2df9ac9498e778","nickname":"邪恶的微笑","activityAvatar":"","type":"normal","lv":8,"gender":"male"},"floor":31,"likeCount":17,"created":"2016-08-21T15:13:54.693Z","replyTo":null}]
     */
    private List<CommentBean> comments;

    public List<CommentBean> getComments() {
        return comments;
    }

    public void setComments(List<CommentBean> comments) {
        this.comments = comments;
    }
}
