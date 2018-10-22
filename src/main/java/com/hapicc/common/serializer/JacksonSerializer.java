package com.hapicc.common.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JacksonSerializer implements MessageSerializer {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public byte[] serialize(Object object) throws IOException {
        return mapper.writeValueAsBytes(object);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws IOException {
        return mapper.readValue(bytes, clazz);
    }

    @Override
    public String format(Object object) throws IOException {
        return mapper.writeValueAsString(object);
    }

    @Override
    public <T> T parse(String json, Class<T> clazz) throws IOException {
        return mapper.readValue(json, clazz);
    }
}
