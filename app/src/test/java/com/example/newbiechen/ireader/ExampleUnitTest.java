package com.example.newbiechen.ireader;

import com.example.newbiechen.ireader.utils.StringUtils;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() throws Exception {
        String date = "2017-04-22";
        String pattern = "yyyy-MM-dd";
        String value = StringUtils.dateConvert(date,pattern);
        System.out.print(value);
    }
}