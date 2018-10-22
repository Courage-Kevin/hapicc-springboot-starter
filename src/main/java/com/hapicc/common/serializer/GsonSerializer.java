package com.hapicc.common.serializer;

import com.google.gson.Gson;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

public class GsonSerializer implements MessageSerializer {

    private static final Gson gson = new Gson();

    @Override
    public byte[] serialize(Object object) throws IOException {
        return format(object).getBytes(UTF_8);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws IOException {
        return parse(new String(bytes, UTF_8), clazz);
    }

    @Override
    public String format(Object object) throws IOException {
        return gson.toJson(object);
    }

    @Override
    public <T> T parse(String json, Class<T> clazz) throws IOException {
        return gson.fromJson(json, clazz);
    }
}
