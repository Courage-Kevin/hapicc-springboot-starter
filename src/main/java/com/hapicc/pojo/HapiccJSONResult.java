package com.hapicc.pojo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 自定义响应数据结构
 * 1. 200：表示成功
 * 2. 500：表示错误，错误信息在 msg 字段中
 * 3. 501：bean 验证错误，不管多少个错误都以 map 形式返回
 * 4. 502：拦截器拦截到用户 token 非法
 * 5. 555：异常抛出信息
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HapiccJSONResult {

    // 定义 jackson 对象
    private static final ObjectMapper MAPPER = new ObjectMapper();

    // 响应状态
    private Integer status;

    // 响应消息
    private String msg;

    // 响应数据
    private Object data;

    public HapiccJSONResult() {
    }

    public HapiccJSONResult(Integer status, String msg, Object data) {
        this.status = status;
        this.setMsg(msg);
        this.setData(data);
    }

    public HapiccJSONResult(Object data) {
        this.status = 200;
        this.setMsg("OK");
        this.setData(data);
    }

    public static HapiccJSONResult build(Integer status, String msg) {
        return new HapiccJSONResult(status, msg, null);
    }

    public static HapiccJSONResult build(Integer status, String msg, Object data) {
        return new HapiccJSONResult(status, msg, data);
    }

    public static HapiccJSONResult ok(Object data) {
        return new HapiccJSONResult(data);
    }

    public static HapiccJSONResult ok() {
        return new HapiccJSONResult(null);
    }

    public static HapiccJSONResult errorMsg(String msg) {
        return new HapiccJSONResult(500, msg, null);
    }

    public static HapiccJSONResult errorMap(Object data) {
        return new HapiccJSONResult(501, "error", data);
    }

    public static HapiccJSONResult errorTokenMsg(String msg) {
        return new HapiccJSONResult(502, msg, null);
    }

    public static HapiccJSONResult errorException(String msg) {
        return new HapiccJSONResult(555, msg, null);
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static HapiccJSONResult formatToPojo(String jsonData, Class<?> clazz) {
        try {
            if (clazz == null) {
                return MAPPER.readValue(jsonData, HapiccJSONResult.class);
            }
            JsonNode node = MAPPER.readTree(jsonData);
            JsonNode data = node.get("data");
            Object obj = null;
            if (data.isObject()) {
                obj = MAPPER.readValue(data.traverse(), clazz);
            } else if (data.isTextual()) {
                obj = MAPPER.readValue(data.asText(), clazz);
            }
            return build(node.get("status").intValue(), node.get("msg").asText(), obj);
        } catch (Exception e) {
            return null;
        }
    }

    public static HapiccJSONResult format(String jsonData) {
        try {
            return MAPPER.readValue(jsonData, HapiccJSONResult.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static HapiccJSONResult formatToList(String jsonData, Class<?> clazz) {
        try {
            JsonNode node = MAPPER.readTree(jsonData);
            JsonNode data = node.get("data");
            Object obj = null;
            if (data.isArray() && data.size() > 0) {
                obj = MAPPER.readValue(data.traverse(),
                        MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
            }
            return build(node.get("status").intValue(), node.get("msg").asText(), obj);
        } catch (Exception e) {
            return null;
        }
    }
}
