package com.hapicc.common.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.function.Consumer;

import static java.nio.charset.StandardCharsets.UTF_8;

public class GsonHelper implements MessageFormatter, MessageParser {

    private static final Gson gson = new Gson();

    @Override
    public String format(Object object) throws IOException {
        return gson.toJson(object);
    }

    @Override
    public byte[] formatAsBytes(Object object) throws IOException {
        return gson.toJson(object).getBytes(UTF_8);
    }

    @Override
    public <T> T parse(String json) throws Exception {
        return gson.fromJson(json, new TypeToken<T>() {}.getType());
    }

    @Override
    public <T> T parse(String json, Class<T> clz) throws Exception {
        return gson.fromJson(json, clz);
    }

    @Override
    public <T> T parseBytes(byte[] bytes) throws Exception {
        return gson.fromJson(new String(bytes, UTF_8), new TypeToken<T>() {}.getType());
    }

    @Override
    public <T> T parseBytes(byte[] bytes, Class<T> clz) throws Exception {
        return gson.fromJson(new String(bytes, UTF_8), clz);
    }

    public <T> T parse(String json, TypeToken<T> typeToken) throws Exception {
        return gson.fromJson(json, typeToken.getType());
    }

    public <T> T parseBytes(byte[] bytes, TypeToken<T> typeToken) throws Exception {
        return gson.fromJson(new String(bytes, UTF_8), typeToken.getType());
    }

    public void with(Consumer<Gson> consumer) {
        consumer.accept(gson);
    }
}
