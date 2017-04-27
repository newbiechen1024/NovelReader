package com.example.newbiechen.ireader.model.flag;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.example.newbiechen.ireader.App;
import com.example.newbiechen.ireader.R;

/**
 * Created by newbiechen on 17-4-24.
 */

public enum CommunityType {

    COMMENT(R.string.nb_fragment_community_comment, R.drawable.ic_section_comment),
    REVIEW(R.string.nb_fragment_community_discussion, R.drawable.ic_section_discuss),
    HELP(R.string.nb_fragment_community_help,R.drawable.ic_section_help),
    GIRL(R.string.nb_fragment_community_girl,R.drawable.ic_section_girl),
    COMPOSE(R.string.nb_fragment_community_compose,R.drawable.ic_section_compose);

    private String typeName;
    private int iconId;
    CommunityType(@StringRes int typeId, @DrawableRes int iconId){
        this.typeName = App.getContext().getResources().getString(typeId);
        this.iconId = iconId;
    }

    public String getTypeName(){
        return typeName;
    }

    public int getIconId(){
        return iconId;
    }
}
