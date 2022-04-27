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

import android.util.Log;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testRandom() {
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

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

//    @Test
//    public void getDistance(){
//        double EARTH_RADIUS = 6378137.0;
//        double radLat1 = rad(24.92769063086905);
//        double radLat2 = rad(121.25424407422543);
//        double a = radLat1 - radLat2;
//        double b = rad(24.9278101) - rad(121.2540865);
//        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
//                + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
//        s = s * EARTH_RADIUS;
//        s = Math.round(s * 10000)/10000 ;
//        System.err.println(s);
//    }

    @Test
    public void getDistance() {
//        final int R = 6371; // Radius of the earth
//
//        double latDistance = Math.toRadians(24.9281099 - 24.927937512681435);
//        double lonDistance = Math.toRadians(121.2544731 - 121.25437952578066);
//        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
//                + Math.cos(Math.toRadians(24.927937512681435)) * Math.cos(Math.toRadians(24.9281099))
//                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
//        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//        double distance = R * c * 1000; // convert to meters
//        distance = Math.pow(distance, 2);

//        System.err.println(Math.sqrt(distance));
        System.err.println(String.valueOf(distance(24.928187640919155,121.25424191333384,24.928184070242647,121.25425781147958)));
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515 *1.603 *100;
        return (dist);
    }

}