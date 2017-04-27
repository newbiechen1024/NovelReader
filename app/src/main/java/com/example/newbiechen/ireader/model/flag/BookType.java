package com.example.newbiechen.ireader.model.flag;

/**
 * Created by newbiechen on 17-4-24.
 *
 */

public enum BookType implements BookConvert{
    ALL("全部类型","all"),
    XHQH("玄幻奇幻","xhqh"),
    WXXX("武侠仙侠","wxxx"),
    DSYN("都市异能","dsyn"),
    LSJS("历史军事","lsjs"),
    YXJJ("游戏竞技","yxjj"),
    KHLY("科幻灵异","khly"),
    CYJK("穿越架空","cyjk"),
    HMZC("豪门总裁","hmzc"),
    XDYQ("现代言情","xdyq"),
    GDYQ("古代言情","gdyq"),
    HXYQ("幻想言情","hxyq"),
    DMTR("耽美同人","dmtr");

    String typeName;
    String netName;

    BookType(String typeName, String netName){
      this.typeName = typeName;
        this.netName = netName;
    }

    @Override
    public String getTypeName(){
      return typeName;
    }

    @Override
    public String getNetName() {
        return netName;
    }
}

