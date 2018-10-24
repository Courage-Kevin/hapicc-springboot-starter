package com.hapicc.common.json;

public class JsonHelper {

    private static final GsonHelper gsonHelper;

    private static final JacksonHelper jacksonHelper;

    private static final FastjsonHelper fastjsonHelper;

    static {
        gsonHelper = new GsonHelper();
        jacksonHelper = new JacksonHelper();
        fastjsonHelper = new FastjsonHelper();
    }

    public static MessageFormatter serializer() {
        return jacksonHelper;
    }

    public static MessageParser deserializer() {
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
