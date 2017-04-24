package com.example.newbiechen.ireader.model.bean;

import java.util.List;

/**
 * Created by newbiechen on 17-4-23.
 */

public class SortListBean extends BaseBean {

    private List<SortBean> male;
    private List<SortBean> female;

    public List<SortBean> getMale() {
        return male;
    }

    public void setMale(List<SortBean> male) {
        this.male = male;
    }

    public List<SortBean> getFemale() {
        return female;
    }

    public void setFemale(List<SortBean> female) {
        this.female = female;
    }
}
