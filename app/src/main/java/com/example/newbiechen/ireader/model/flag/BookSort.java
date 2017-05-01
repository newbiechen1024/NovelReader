package com.example.newbiechen.ireader.model.flag;

/**
 * Created by newbiechen on 17-4-24.
 * ("默认排序","最新发布","最多评论"),
 */

public enum BookSort implements BookConvert{
    DEFAULT("默认排序","updated","Updated"),
    CREATED("最新发布","created","Created"),
    HELPFUL("最多推荐","helpful","LikeCount"),
    COMMENT_COUNT("最多评论","comment-count","CommentCount");

    String typeName;
    String netName;
    String dbName;
    BookSort(String typeName, String netName,String dbName){
        this.typeName = typeName;
        this.netName = netName;
        this.dbName = dbName;
    }

    @Override
    public String getTypeName(){
        return typeName;
    }

    @Override
    public String getNetName(){
        return netName;
    }

    public String getDbName(){
        return dbName;
    }
}
