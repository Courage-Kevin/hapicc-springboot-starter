package com.hapicc.utils.json;

import com.alibaba.fastjson.TypeReference;
import com.hapicc.common.json.JsonHelper;

public class FastjsonUtils {

    /**
     * 将对象转换为 json 字符串
     */
    public static String obj2Json(Object obj) {
        try {
            return JsonHelper.fastjson().format(obj);
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
            return JsonHelper.fastjson().parse(json, clz);
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
            return JsonHelper.fastjson().parse(json, typeRef);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
