package com.hapicc.common.json;

import java.io.IOException;

public interface JsonParser {

    <T> T parse(String json) throws IOException;

    <T> T parseBytes(byte[] bytes) throws IOException;
}
