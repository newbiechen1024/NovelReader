package com.example.newbiechen.ireader.model;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * Created by newbiechen on 17-4-17.
 */

public enum Selection {
    DISTILLATE("全部","精品"),
    SORT_TYPE("默认排序","最新发布","最多评论"),
    BOOK_TYPE("全部类型","玄幻奇幻","武侠仙侠","都市异能","历史军事",
            "游戏竞技","科幻灵异","穿越架空","豪门总裁","现代言情",
            "古代言情","幻想言情","耽美同人");

    private String [] params;

    Selection(String ...params){
        this.params = params;
    }

    public List<String> getParams(){
        return Arrays.asList(params);
    }
}
