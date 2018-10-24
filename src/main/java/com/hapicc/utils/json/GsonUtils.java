package com.hapicc.utils.json;

import com.google.gson.reflect.TypeToken;
import com.hapicc.common.json.JsonHelper;

public class GsonUtils {

    /**
     * 将对象转换为 json 字符串
     */
    public static String obj2Json(Object obj) {
        try {
            return JsonHelper.gson().format(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将 json 字符串转换为对象
     */
    public static <T> T json2Obj(String json, Class<T> clz) {
        try {
            return JsonHelper.gson().parse(json, clz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将 json 字符串转换为复杂对象
     */
    public static <T> T json2Obj(String json, TypeToken<T> typeToken) {
        try {
            return JsonHelper.gson().parse(json, typeToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
