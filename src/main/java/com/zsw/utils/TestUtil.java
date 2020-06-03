package com.zsw.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Created by zhangshaowei on 2020/6/3.
 */
public class TestUtil {
    public static JsonSerializer<Date>  gsonJsonDate() {
        JsonSerializer<Date> numberJsonSerializer = new JsonSerializer<Date>() {
            @Override
            public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
                SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
                return new JsonPrimitive(String.valueOf(ft.format(src)));

            }
        };
        return numberJsonSerializer;
    }

    public static JsonSerializer<Timestamp> gsonJsonTimestamp() {
        JsonSerializer<Timestamp> numberJsonSerializer = new JsonSerializer<Timestamp>() {
            @Override
            public JsonElement serialize(Timestamp src, Type typeOfSrc, JsonSerializationContext context) {
                SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                return new JsonPrimitive(String.valueOf(ft.format(src)));
            }
        };
        return numberJsonSerializer;
    }


}
