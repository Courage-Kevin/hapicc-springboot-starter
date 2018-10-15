package com.hapicc.utils.common;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

    // 定义 jackson 对象
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 将对象转换为 json 字符串
     */
    public static String obj2Json(Object obj) {
        try {
            String jsonStr = MAPPER.writeValueAsString(obj);
            return jsonStr;
        } catch (final JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将 json 字符串转换为对象
     */
    public static <T> T json2Obj(String jsonStr, Class<T> clazz) {
        try {
            T t = MAPPER.readValue(jsonStr, clazz);
            return t;
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将 json 字符串转换为对象列表
     */
    public static <T> List<T> json2List(String jsonStr, Class<T> clazz) {
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, clazz);
        try {
            List<T> list = MAPPER.readValue(jsonStr, javaType);
            return list;
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
