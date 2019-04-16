package com.meituan.android.tax_calculator.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by duoshilin on 2019/3/4.
 */
public class City {
    private String id;
    private String name;
    private String rank;
    private String pinyin;
    private String lng;
    private String lat;
    private String divisionStr;
    private boolean open;
    private boolean isOpen;
    private boolean weather;


    @Override
    public String toString() {
        return "City{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", rank='" + rank + '\'' +
                ", pinyin='" + pinyin + '\'' +
                ", lng='" + lng + '\'' +
                ", lat='" + lat + '\'' +
                ", divisionStr='" + divisionStr + '\'' +
                ", open=" + open +
                ", isOpen=" + isOpen +
                ", weather=" + weather +
                '}';
    }

    public static Map<String,City> initCities(){
        Map<String,City> cities = new HashMap<>();
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader("app/src/main/assets/cities.json");
            JsonArray cutArray = new JsonParser().parse(reader).getAsJsonArray();
            for (JsonElement element : cutArray){
                City city = gson.fromJson(element,City.class);
                cities.put(city.id, city);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return cities;
    }

}
