package com.hapicc.common.json;

public interface MessageParser {

    <T> T parse(String json) throws Exception;

    <T> T parse(String json, Class<T> clz) throws Exception;

    <T> T parseBytes(byte[] bytes) throws Exception;

    <T> T parseBytes(byte[] bytes, Class<T> clz) throws Exception;
}
