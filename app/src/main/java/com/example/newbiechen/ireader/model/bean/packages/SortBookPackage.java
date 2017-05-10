package com.example.newbiechen.ireader.model.bean.packages;

import com.example.newbiechen.ireader.model.bean.BaseBean;
import com.example.newbiechen.ireader.model.bean.SortBookBean;

import java.util.List;

/**
 * Created by newbiechen on 17-5-3.
 */

public class SortBookPackage extends BaseBean {

    /**
     * total : 222998
     * books : [{"_id":"53855a750ac0b3a41e00c7e6","title":"择天记","author":"猫腻","shortIntro":"太始元年，有神石自太空飞来，分散落在人间，其中落在东土大陆的神石，上面镌刻着奇怪的图腾，人因观其图腾而悟道，后立国教。 数千年后，十四岁的少年孤儿陈长生，为治病...","cover":"/agent/http://image.cmfu.com/books/3347595/3347595.jpg","site":"zhuishuvip","majorCate":"玄幻","banned":0,"latelyFollower":265651,"retentionRatio":53.39,"lastChapter":"第1183章 天凉好个秋","tags":["玄幻","架空","杀伐决断","天才","升级练功","东方玄幻"]},{"_id":"5816b415b06d1d32157790b1","title":"圣墟","author":"辰东","shortIntro":"在破败中崛起，在寂灭中复苏。 沧海成尘，雷电枯竭，那一缕幽雾又一次临近大地，世间的枷锁被打开了，一个全新的世界就此揭开神秘的一角\u2026\u2026","cover":"/agent/http://qidian.qpic.cn/qdbimg/349573/1004608738/180","site":"zhuishuvip","majorCate":"玄幻","banned":0,"latelyFollower":240455,"retentionRatio":74.71,"lastChapter":"第373章 暴力美学","tags":["玄幻","东方玄幻"]},{"_id":"53e56ee335f79bb626a496c9","title":"帝霸","author":"厌笔萧生","shortIntro":"千万年前，李七夜栽下一株翠竹。 八百万年前，李七夜养了一条鲤鱼。 五百万年前，李七夜收养一个小女孩。 今天，李七夜一觉醒来，翠竹修练成神灵，鲤鱼化作金龙，小女孩...","cover":"/agent/http://image.cmfu.com/books/3258971/3258971.jpg","site":"zhuishuvip","majorCate":"玄幻","banned":0,"latelyFollower":123207,"retentionRatio":71.35,"lastChapter":"第2422章 抱美入怀","tags":["玄幻","热血","架空","巅峰","传奇","东方玄幻"]},{"_id":"50bee5172033d09b2f00001b","author":"莫默","cover":"/cover/148308514973824","shortIntro":"武之巅峰，是孤独，是寂寞，是漫漫求索，是高处不胜寒 逆境中成长，绝地里求生，不屈不饶，才能堪破武之极道。 凌霄阁试炼弟子兼扫地小厮杨开偶获一本无字黑书，从此踏上...","title":"武炼巅峰","site":"zhuishuvip","majorCate":"玄幻","banned":0,"latelyFollower":89103,"retentionRatio":72.97,"lastChapter":"第3612章 解决之法","tags":["玄幻","热血","架空","巅峰","奇遇","升级练功","东方玄幻"]},{"_id":"516531015a29ee6a5e0000e1","author":"善良的蜜蜂","cover":"/agent/http://wfqqreader.3g.qq.com/cover/742/229742/t7_229742.jpg","shortIntro":"论潜力，不算天才，可玄功武技，皆可无师自通。论实力，任凭你有万千至宝，但定不敌我界灵大军。我是谁？天下众生视我为修罗，却不知，我以修罗成武神。等级：灵武，元武，...","title":"修罗武神","site":"zhuishuvip","majorCate":"玄幻","banned":0,"latelyFollower":82590,"retentionRatio":57.89,"lastChapter":"第两千九百五十三章 楚枫的回答（1）","tags":["玄幻","热血","武神","架空","巅峰","天才","修罗","修炼","东方玄幻"]}]
     */

    private int total;
    private List<SortBookBean> books;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<SortBookBean> getBooks() {
        return books;
    }

    public void setBooks(List<SortBookBean> books) {
        this.books = books;
    }
}
