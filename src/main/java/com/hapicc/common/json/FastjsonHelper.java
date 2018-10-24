package com.hapicc.common.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.io.IOException;

public class FastjsonHelper implements MessageFormatter, MessageParser {

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
    public <T> T parse(String json, Class<T> clz) throws IOException {
        return JSON.parseObject(json, clz);
    }

    @Override
    public <T> T parseBytes(byte[] bytes) throws IOException {
        return JSON.parseObject(bytes, new TypeReference<T>() {}.getType());
    }

    @Override
    public <T> T parseBytes(byte[] bytes, Class<T> clz) throws IOException {
        return JSON.parseObject(bytes, clz);
    }

    public <T> T parse(String json, TypeReference<T> typeRef) {
        return JSON.parseObject(json, typeRef);
    }

    public <T> T parseBytes(byte[] bytes, TypeReference<T> typeRef) {
        return JSON.parseObject(bytes, typeRef.getType());
    }
}
