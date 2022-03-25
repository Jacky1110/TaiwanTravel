package com.jotangi.nickyen;

import org.junit.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest
{
    @Test
    public void addition_isCorrect()
    {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testRandom()
    {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("QUESTIONNAIRE_COUPON7");
        arrayList.add("QUESTIONNAIRE_COUPON8");
        arrayList.add("QUESTIONNAIRE_COUPON9");
        arrayList.add("QUESTIONNAIRE_COUPON10");
        int index = (int) (Math.random() * arrayList.size());
        String rand = arrayList.get(index);
//        return rand;
        System.out.println(rand);
    }
}