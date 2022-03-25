package com.jotangi.nickyen.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jotangi.nickyen.home.model.MyBonusBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * /\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/10/28
 * \/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
public class SharedPreferencesUtil
{
    private static SharedPreferencesUtil util;
    private static SharedPreferences sp;
    private static SharedPreferences.Editor editor;

    public SharedPreferencesUtil(Context context, String name)
    {
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    /**
     * 初始化SharedPreferencesUtil,只需要初始化一次，建議在Application中初始化
     *
     * @param context 上下文物件
     * @param name    SharedPreferences Name
     */
    public static void getInstance(Context context, String name)
    {
        if (util == null)
        {
            util = new SharedPreferencesUtil(context, name);
        }
    }

    /**
     * 儲存資料到SharedPreferences
     *
     * @param key   鍵
     * @param value 需要儲存的資料
     * @return 儲存結果
     */
    public static boolean putData(String key, Object value)
    {
        boolean result;
//        SharedPreferences.Editor editor = sp.edit();
        String type = value.getClass().getSimpleName();
        try
        {
            switch (type)
            {
                case "Boolean":
                    editor.putBoolean(key, (Boolean) value);
                    break;
                case "Long":
                    editor.putLong(key, (Long) value);
                    break;
                case "Float":
                    editor.putFloat(key, (Float) value);
                    break;
                case "String":
                    editor.putString(key, (String) value);
                    break;
                case "Integer":
                    editor.putInt(key, (Integer) value);
                    break;
                default:
                    Gson gson = new Gson();
                    String json = gson.toJson(value);
                    editor.putString(key, json);
                    break;
            }
            result = true;
        } catch (Exception e)
        {
            result = false;
            e.printStackTrace();
        }
        editor.apply();
        return result;
    }

    /**
     * 獲取SharedPreferences中儲存的資料
     *
     * @param key          鍵
     * @param defaultValue 獲取失敗預設值
     * @return 從SharedPreferences讀取的資料
     */
    public static Object getData(String key, Object defaultValue)
    {
        Object result;
        String type = defaultValue.getClass().getSimpleName();
        try
        {
            switch (type)
            {
                case "Boolean":
                    result = sp.getBoolean(key, (Boolean) defaultValue);
                    break;
                case "Long":
                    result = sp.getLong(key, (Long) defaultValue);
                    break;
                case "Float":
                    result = sp.getFloat(key, (Float) defaultValue);
                    break;
                case "":
                    result = sp.getString(key, (String) defaultValue);
                    break;
                case "Integer":
                    result = sp.getInt(key, (Integer) defaultValue);
                    break;
                default:
                    Gson gson = new Gson();
                    String json = sp.getString(key, "");
                    if (!json.equals("") && json.length() > 0)
                    {
                        result = gson.fromJson(json, defaultValue.getClass());
                    } else
                    {
                        result = defaultValue;
                    }
                    break;
            }
        } catch (Exception e)
        {
            result = null;
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 用於儲存集合
     *
     * @param key  key
     * @param list 集合資料
     * @return 儲存結果
     */
//    public void demo(){
//        new SharedPreferencesUtil(getActivity(), "安安");
//        MyBonusBean bonusBean = new MyBonusBean("1", "2", "3");
//        MyBonusBean bonusBean2 = new MyBonusBean("1", "2", "3");
//        List<MyBonusBean> list = new ArrayList<>();
//        list.add(bonusBean);
//        list.add(bonusBean2);
//        SharedPreferencesUtil.putListData("1",list);
//        List<MyBonusBean> list2 = new ArrayList<>();
//        list2.addAll(SharedPreferencesUtil.getListData("1",MyBonusBean .class));
//    }
    public static <T> boolean putListData(String key, List<T> list)
    {
        boolean result;
        String type = list.get(0).getClass().getSimpleName();
//        SharedPreferences.Editor editor = sp.edit();
        JsonArray array = new JsonArray();
        try
        {
            switch (type)
            {
                case "Boolean":
                    for (int i = 0; i < list.size(); i++)
                    {
                        array.add((Boolean) list.get(i));
                    }
                    break;
                case "Long":
                    for (int i = 0; i < list.size(); i++)
                    {
                        array.add((Long) list.get(i));
                    }
                    break;
                case "Float":
                    for (int i = 0; i < list.size(); i++)
                    {
                        array.add((Float) list.get(i));
                    }
                    break;
                case "String":
                    for (int i = 0; i < list.size(); i++)
                    {
                        array.add((String) list.get(i));
                    }
                    break;
                case "Integer":
                    for (int i = 0; i < list.size(); i++)
                    {
                        array.add((Integer) list.get(i));
                    }
                    break;
                default:
                    Gson gson = new Gson();
                    for (int i = 0; i < list.size(); i++)
                    {
                        JsonElement obj = gson.toJsonTree(list.get(i));
                        array.add(obj);
                    }
                    break;
            }
            editor.putString(key, array.toString());
            result = true;
        } catch (Exception e)
        {
            result = false;
            e.printStackTrace();
        }
        editor.apply();
        return result;
    }

    /**
     * 獲取儲存的List
     *
     * @param key key
     * @return 對應的Lis集合
     */
    public static <T> List<T> getListData(String key, Class<T> cls)
    {
        List<T> list = new ArrayList<>();
        String json = sp.getString(key, "");
        if (!json.equals("") && json.length() > 0)
        {
            Gson gson = new Gson();
            JsonArray array = new JsonParser().parse(json).getAsJsonArray();
            for (JsonElement elem : array)
            {
                list.add(gson.fromJson(elem, cls));
            }
        }
        return list;
    }

    /**
     * 用於儲存集合
     *
     * @param key key
     * @param map map資料
     * @return 儲存結果
     */
//    public void demo(){
//        new SharedPreferencesUtil(getActivity(), "安安");
//        MyBonusBean bonusBean = new MyBonusBean("2", "2", "2");
//        MyBonusBean bonusBean2 = new MyBonusBean("3", "3", "3");
//        Map<String, MyBonusBean> map = new HashMap<>();
//        map.put("1", bonusBean);
//        map.put("2", bonusBean2);
//        SharedPreferencesUtil.putHashMapData("2", map);
//        Map<String, MyBonusBean> map2 = new HashMap<>();
//        map2.putAll(SharedPreferencesUtil.getHashMapData("2", MyBonusBean.class));
//    }
    public static <K, V> boolean putHashMapData(String key, Map<K, V> map)
    {
        boolean result;
//        SharedPreferences.Editor editor = sp.edit();
        try
        {
            Gson gson = new Gson();
            String json = gson.toJson(map);
            editor.putString(key, json);
            result = true;
        } catch (Exception e)
        {
            result = false;
            e.printStackTrace();
        }
        editor.apply();
        return result;
    }

    /**
     * 用於儲存集合
     *
     * @param key key
     * @return HashMap
     */
    public static <V> HashMap<String, V> getHashMapData(String key, Class<V> clsV)
    {
        String json = sp.getString(key, "");
        HashMap<String, V> map = new HashMap<>();
        Gson gson = new Gson();
        JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        Set<Map.Entry<String, JsonElement>> entrySet = obj.entrySet();
        for (Map.Entry<String, JsonElement> entry : entrySet)
        {
            String entryKey = entry.getKey();
            JsonObject value = (JsonObject) entry.getValue();
            map.put(entryKey, gson.fromJson(value, clsV));
        }
        Log.e("SharedPreferencesUtil", obj.toString());
        return map;
    }

    public static void clearData(Context context, String name)
    {
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor.clear();
        editor.apply();
    }
}
