package com.example.newbiechen.ireader.model.flag;

/**
 * Created by newbiechen on 17-4-24.
 *
 */

public enum BookDistillate implements BookConvert{
    ALL("全部",""),
    BOUTIQUES("精品","true");

    String typeName;
    String netName;
    BookDistillate(String typeName, String netName){
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
