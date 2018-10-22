package com.hapicc.common.serializer;

import java.io.IOException;

public interface MessageSerializer {

    String SERIALIZER = ".JacksonSerializer";

    String DESERIALIZER = ".JacksonSerializer";

    byte[] serialize(Object object) throws IOException;

    <T> T deserialize(byte[] bytes, Class<T> clazz) throws IOException;

    default Object deserialize(byte[] bytes) throws IOException {
        return deserialize(bytes, Object.class);
    }

    String format(Object object) throws IOException;

    <T> T parse(String json, Class<T> clazz) throws IOException;

    default Object parse(String json) throws IOException {
        return parse(json, Object.class);
    }
}
