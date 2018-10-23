package com.hapicc.common.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.function.Consumer;

import static java.nio.charset.StandardCharsets.UTF_8;

public class GsonHelper implements JsonFormatter, JsonParser {

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
    public <T> T parse(String json) throws IOException {
        return gson.fromJson(json, new TypeToken<T>() {}.getType());
    }

    @Override
    public <T> T parseBytes(byte[] bytes) throws IOException {
        return gson.fromJson(new String(bytes, UTF_8), new TypeToken<T>() {}.getType());
    }

    public void with(Consumer<Gson> consumer) {
        consumer.accept(gson);
    }
}
