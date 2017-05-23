package com.example.newbiechen.ireader;

import com.example.newbiechen.ireader.utils.StringUtils;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    @Ignore
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

    @Test
    public void testList(){
        List<String> str = new ArrayList<>();
        str.add("asd");
        str.add("zxc");

        List<String> sub = str.subList(1, 2);
        for (String data : sub) {
            System.out.println(data);
        }
    }
}