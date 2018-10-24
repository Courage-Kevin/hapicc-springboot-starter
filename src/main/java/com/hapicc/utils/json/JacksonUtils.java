package com.hapicc.utils.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hapicc.common.json.JsonHelper;

import java.io.IOException;

public class JacksonUtils {

    /**
     * 将对象转换为 json 字符串
     */
    public static String obj2Json(Object obj) {
        try {
            return JsonHelper.jackson().format(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将 json 字符串转换为对象
     */
    public static <T> T json2Obj(String json, Class<T> clz) {
        try {
            return JsonHelper.jackson().parse(json, clz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将 json 字符串转换为复杂对象
     */
    public static <T> T json2Obj(String json, TypeReference<T> typeRef) {
        try {
            return JsonHelper.jackson().parse(json, typeRef);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
