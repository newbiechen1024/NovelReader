package com.example.newbiechen.ireader.model.bean.packages;

import com.example.newbiechen.ireader.model.bean.BaseBean;
import com.example.newbiechen.ireader.model.bean.TagBookBean;

import java.util.List;

/**
 * Created by newbiechen on 17-5-4.
 * 通过Tag检索获取的数据
 */

public class TagSearchPackage extends BaseBean {

    /**
     * books : [{"_id":"57206c3539a913ad65d35c7b","title":"一念永恒","author":"耳根","shortIntro":"一念成沧海，一念化桑田。一念斩千魔，一念诛万仙。唯我念\u2026\u2026永恒","cover":"/agent/http://image.cmfu.com/books/1003354631/1003354631.jpg","cat":"仙侠","majorCate":"仙侠","minorCate":"幻想修仙","latelyFollower":221865,"retentionRatio":75.17,"lastChapter":"第760章 监察底蕴！","tags":["仙侠"]},{"_id":"559b29837a0f8b65521464a7","title":"申公豹传承","author":"第九天命","shortIntro":"本书主角玉独秀获得应灾劫大道而生的申公豹传承，然后又在无意间融合了一丝诸天劫难本源，有了执掌、引动大劫之力量，为众生带来劫难，可以借助大劫，来加快自己的修炼速，...","cover":"/agent/http://image.cmfu.com/books/3533952/3533952.jpg","cat":"仙侠","majorCate":"仙侠","minorCate":"洪荒封神","latelyFollower":19833,"retentionRatio":74.24,"lastChapter":"完本时间","tags":["洪荒封神","仙侠"]},{"_id":"589c8d60271465ca18fffb96","title":"洪荒之冥河问道","author":"神仙爱凡尘","shortIntro":"盘古开天，无极洪荒，道祖鸿钧、三清、十二祖巫、西方二圣、女娲、帝俊太一·····，他们都有着属于自己的传奇，而身为血海之祖的冥河却没留下什么真正的传奇，一个来自...","cover":"/cover/149092490875342","cat":"仙侠","majorCate":"仙侠","minorCate":"古典仙侠","latelyFollower":15353,"retentionRatio":70.9,"lastChapter":"第164章 变故","tags":["仙侠"]},{"_id":"58ad1de6fb9803d1085c8178","title":"洪荒火榕道","author":"大猪神","shortIntro":"叶天不由的感叹，我怎么会成为一颗大榕树了！你就是穿越也好、重生也罢，最起码是个会动的啊！叶天吹着清风静静的，看着自己全身如火一般颜色的树叶跟躯干，非常无语。","cover":"/agent/http://qidian.qpic.cn/qdbimg/349573/1005317161/180","cat":"仙侠","majorCate":"其它","minorCate":"","latelyFollower":10674,"retentionRatio":69.63,"lastChapter":"第一百四十章龙族出四海 凤族现世间（求订阅）","tags":["仙侠"]},{"_id":"58c815525eabc15402fb537f","title":"洪荒之吾名元始","author":"红尘天魔","shortIntro":"吾名元始，吾就是一元复始； 吾名元始，吾就是诸果之因； 吾名元始，吾就是众道之源； 吾名元始，吾就是万象之始。 末法时代大魔头元始梦魔渡劫失败，携带鸿蒙至宝元始...","cover":"/agent/https://img1.write.qq.com/upload/defaultcover/2017-03-12/cb_default_58c542e2aaf4a.png","cat":"仙侠","majorCate":"其它","minorCate":"","latelyFollower":4555,"retentionRatio":69.46,"lastChapter":"第一百零三章 二讲结束，成道三法","tags":["西游","洪荒流","孤儿","仙侠"]}]
     * ok : true
     */
    private List<TagBookBean> books;

    public List<TagBookBean> getBooks() {
        return books;
    }

    public void setBooks(List<TagBookBean> books) {
        this.books = books;
    }
}
