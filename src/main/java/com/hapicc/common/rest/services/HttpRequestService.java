package com.hapicc.common.rest.services;

import com.google.gson.Gson;
import com.hapicc.common.json.JsonHelper;
import com.hapicc.common.rest.client.HttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

@Slf4j
public abstract class HttpRequestService {

    private String resource;

    public HttpRequestService() {
    }

    public HttpRequestService(String resource) {
        this.resource = resource;
    }

    protected abstract String getServerUrl();

    public <T> T get() {
        return get(resource);
    }

    public <T> T get(Map<String, Object> urlParams) {
        return get(resource, urlParams);
    }

    public <T> T get(String url) {
        return get(url, null);
    }

    public <T> T get(String url, Map<String, Object> urlParams) {
        return request(HttpMethod.GET, url, null, urlParams);
    }

    public <T> T post(Object data) {
        return post(resource, data);
    }

    public <T> T post(Object data, Map<String, Object> urlParams) {
        return post(resource, data, urlParams);
    }

    public <T> T post(String url, Object data) {
        return post(url, data, null);
    }

    public <T> T post(String url, Object data, Map<String, Object> urlParams) {
        return request(HttpMethod.POST, url, data, urlParams);
    }

    public <T> T put(Object data) {
        return put(resource, data);
    }

    public <T> T put(Object data, Map<String, Object> urlParams) {
        return put(resource, data, urlParams);
    }

    public <T> T put(String url, Object data) {
        return put(url, data, null);
    }

    public <T> T put(String url, Object data, Map<String, Object> urlParams) {
        return request(HttpMethod.PUT, url, data, urlParams);
    }

    public <T> T delete() {
        return delete(resource);
    }

    public <T> T delete(Map<String, Object> urlParams) {
        return delete(resource, urlParams);
    }

    public <T> T delete(String url) {
        return delete(url, null);
    }

    public <T> T delete(String url, Map<String, Object> urlParams) {
        return request(HttpMethod.DELETE, url, null, urlParams);
    }

    private <T> T request(HttpMethod method, String url, Object data, Map<String, Object> urlParams) {
        try {
            String resp = null;
            switch (method) {
                case GET:
                    resp = HttpClient.getForObject(prepareUrl(url, urlParams));
                    break;
                case POST:
                    resp = HttpClient.postForObject(prepareUrl(url, urlParams), data);
                    break;
                case PUT:
                    resp = HttpClient.putForObject(prepareUrl(url, urlParams), data);
                    break;
                case DELETE:
                    resp = HttpClient.deleteForObject(prepareUrl(url, urlParams));
                    break;
                default:
                    log.warn("Unsupported request method: {}", method.toString());
                    break;
            }
            if (resp != null) {
                return JsonHelper.jackson().parse(resp);
            }
        } catch (Exception e) {
            log.warn(getWarnMsg(method.toString(), url, data, urlParams), e);
        }
        return null;
    }

    private String prepareUrl(String url, Map<String, Object> urlParams) {
        Assert.hasLength(url, "The url cannot be empty!");
        Assert.doesNotContain(url, " ", "The url cannot contain spaces!");
        StringBuilder sb = new StringBuilder(getServerUrl());
        sb.append("/").append(url);
        if (!url.contains("?")) {
            sb.append("?");
        }
        if (urlParams != null && !urlParams.isEmpty()) {
            urlParams.forEach((key, value) -> {
                try {
                    sb.append("&").append(key).append("=").append(URLEncoder.encode(value.toString(), "UTF-8"));
                } catch (UnsupportedEncodingException ignored) {
                }
            });
        }
        return sb.toString();
    }

    private String getWarnMsg(String method, String url, Object data, Map<String, Object> urlParams) {
        StringBuilder sb = new StringBuilder("Error occurred when ");
        sb.append(method).append(" with url: ").append(url);
        Gson gson = new Gson();
        if (data != null) {
            sb.append(", data: ").append(gson.toJson(data));
        }
        if (urlParams != null) {
            sb.append(", urlParams: ").append(gson.toJson(urlParams));
        }
        return sb.toString();
    }
}
