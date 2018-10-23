package com.hapicc.common.json;

public class JsonUtils {

    private static final GsonHelper gsonHelper;

    private static final JacksonHelper jacksonHelper;

    private static final FastjsonHelper fastjsonHelper;

    static {
        gsonHelper = new GsonHelper();
        jacksonHelper = new JacksonHelper();
        fastjsonHelper = new FastjsonHelper();
    }

    public static JsonFormatter serializer() {
        return jacksonHelper;
    }

    public static JsonParser deserializer() {
        return jacksonHelper;
    }

    public static JacksonHelper jackson() {
        return jacksonHelper;
    }

    public static GsonHelper gson() {
        return gsonHelper;
    }

    public static FastjsonHelper fastjson() {
        return fastjsonHelper;
    }
}
