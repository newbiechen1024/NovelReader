package com.example.newbiechen.ireader.model.bean;

import java.util.List;

/**
 * Created by newbiechen on 17-5-1.
 */

public class BookTagBean {
    /**
     * name : 时空
     * tags : ["都市","古代","科幻","架空","重生","未来","穿越","历史","快穿","末世","异界位面"]
     */

    private String name;
    private List<String> tags;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
