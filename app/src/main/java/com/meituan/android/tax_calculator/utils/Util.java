package com.meituan.android.tax_calculator.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.ColorDrawable;
import android.widget.NumberPicker;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.meituan.android.tax_calculator.model.Cut;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by duoshilin on 2019/3/4.
 */
public class Util {

    public static String getJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        //获得assets资源管理器
        AssetManager assetManager = context.getAssets();
        //使用IO流读取json文件内容
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName), "utf-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static List<Cut> initCutList(Context context) {
        List<Cut> cutList = new ArrayList<>();
        Gson gson = new Gson();
        JsonArray cutArray = new JsonParser().parse(getJson(context, "cutlist.json")).getAsJsonArray();
        for (JsonElement element : cutArray) {
            Cut cut = gson.fromJson(element, Cut.class);
            cutList.add(cut);
        }
        return cutList;
    }
    public static List<Map<String,Object>> initCities(Context context) {
        List<Map<String,Object>> list = new ArrayList<>();
        Gson gson = new Gson();
        JsonArray cutArray = new JsonParser().parse(getJson(context, "cities.json")).getAsJsonArray();
        for (JsonElement element : cutArray) {
            HashMap<String,Object> map = gson.fromJson(element, HashMap.class);
            list.add(map);
        }
        return list;
    }

    public static String format(String format ,Object object){
        if (object==null || format==null){
            throw new IllegalArgumentException("参数不能为空");
        }else {
            if (object instanceof String){
                object = Float.parseFloat(object.toString());
            }
            DecimalFormat df = new DecimalFormat(format);
            return df.format(object);

        }
    }
    public static void setDividerColor(NumberPicker picker, int color) {
        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    ColorDrawable colorDrawable = new ColorDrawable(color);
                    pf.set(picker, colorDrawable);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}
