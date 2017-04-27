package com.example.newbiechen.ireader.model.flag;

/**
 * Created by newbiechen on 17-4-24.
 * ("默认排序","最新发布","最多评论"),
 */

public enum BookSort implements BookConvert{
    DEFAULT("默认排序","updated"),
    CREATED("最新发布","created"),
    HELPFUL("最多推荐","helpful"),
    COMMENT_COUNT("最多评论","comment-count");

    String typeName;
    String netName;

    BookSort(String typeName, String netName){
        this.typeName = typeName;
        this.netName = netName;
    }

    @Override
    public String getTypeName(){
        return typeName;
    }

    @Override
    public String getNetName(){
        return netName;
    }
}
