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
        int indexStart = 0;
        int indexEnd = 0;
        String value = "《Son of Bitch》it's a good book《empty》";
        indexStart = value.indexOf("《",indexStart);
        indexEnd = value.indexOf("》",indexEnd);

        while(indexStart != -1 || indexEnd != -1){
            System.out.println(indexStart+"  "+indexEnd);
            System.out.println(value.substring(indexStart+1, indexEnd));
            indexStart = value.indexOf("《",indexStart+1);
            indexEnd = value.indexOf("》",indexEnd+1);
        }
    }
}