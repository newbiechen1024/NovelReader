package com.example.newbiechen.ireader;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by newbiechen on 2018/2/8.
 */

public class ListTest {

    @Test
    public void testList(){
        List<String> strList = new ArrayList<>();
        strList.add("1");
        strList.add("2");
        strList.add("3");
        strList.add("4");
        strList.add("5");
        strList.add("6");
        List<String> subList = strList.subList(3,3);
        for (String str : subList) {
            System.out.println(str);
        }
    }
}
