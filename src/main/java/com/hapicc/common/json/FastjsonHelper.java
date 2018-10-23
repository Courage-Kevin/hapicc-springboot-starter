package com.hapicc.common.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

public class FastjsonHelper implements JsonFormatter, JsonParser {

    @Override
    public String format(Object object) throws IOException {
        return JSON.toJSONString(object);
    }

    @Override
    public byte[] formatAsBytes(Object object) throws IOException {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T parse(String json) throws IOException {
        return JSON.parseObject(json, new TypeReference<T>() {});
    }

    @Override
    public <T> T parseBytes(byte[] bytes) throws IOException {
        return JSON.parseObject(bytes, new TypeToken<T>() {}.getType());
    }
}
