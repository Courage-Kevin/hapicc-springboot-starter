package com.hapicc.common.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.function.Consumer;

public class JacksonHelper implements MessageFormatter, MessageParser {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String format(Object object) throws IOException {
        return mapper.writeValueAsString(object);
    }

    @Override
    public byte[] formatAsBytes(Object object) throws IOException {
        return mapper.writeValueAsBytes(object);
    }

    @Override
    public <T> T parse(String json) throws IOException {
        return mapper.readValue(json, new TypeReference<T>() {});
    }

    @Override
    public <T> T parse(String json, Class<T> clz) throws IOException {
        return mapper.readValue(json, clz);
    }

    @Override
    public <T> T parseBytes(byte[] bytes) throws IOException {
        return mapper.readValue(bytes, new TypeReference<T>() {});
    }

    @Override
    public <T> T parseBytes(byte[] bytes, Class<T> clz) throws IOException {
        return mapper.readValue(bytes, clz);
    }

    public <T> T parse(String json, TypeReference<T> typeRef) throws IOException {
        return mapper.readValue(json, typeRef);
    }

    public <T> T parseBytes(byte[] bytes, TypeReference<T> typeRef) throws IOException {
        return mapper.readValue(bytes, typeRef);
    }

    public void with(Consumer<ObjectMapper> consumer) {
        consumer.accept(mapper);
    }
}
