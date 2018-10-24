package com.hapicc.common.rest.client;

import com.hapicc.common.constants.LogConstants;
import com.hapicc.common.context.ApplicationContextHelper;
import com.hapicc.common.json.MessageFormatter;
import com.hapicc.common.json.JsonHelper;
import com.hapicc.common.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.MDC;
import org.springframework.http.*;
import org.springframework.http.client.*;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
public class HttpClient {

    private static final RestProperties restProperties;

    private static final MessageFormatter serializer;

    public static String service;

    static {
        serializer = JsonHelper.serializer();
        restProperties = ApplicationContextHelper.getBean("restProperties");
    }

    private static RestTemplate restTemplate = createRestTemplate();

    private static RestTemplate allTrustRestTemplate = createRestTemplate(true);

    private static RetryTemplate retryTemplate = createRetryTemplate();

    public static RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public static RestTemplate getAllTrustRestTemplate() {
        return allTrustRestTemplate;
    }

    public static RestTemplate createNewRestTemplate() {
        return createRestTemplate();
    }

    public static RestTemplate createNewAllTrustRestTemplate() {
        return createRestTemplate(true);
    }

    public static String getForObject(String url, Object... urlVariables) {
        urlVariables = fixVariables(urlVariables);
        return restTemplate.getForObject(url, String.class, urlVariables);
    }

    public static String postForObject(String url, Object request, Object... urlVariables) {
        request = handleRequest(request);
        urlVariables = fixVariables(urlVariables);
        try {
            return restTemplate.postForObject(url, request, String.class, urlVariables);
        } catch (RestClientException e) {
            log.info("request body: {}", request);
            throw e;
        }
    }

    public static byte[] postForBinary(String url, Object request, Object... urlVariables) {
        urlVariables = fixVariables(urlVariables);
        return restTemplate.postForObject(url, handleRequest(request), byte[].class, urlVariables);
    }

    public static String putForObject(String url, Object request, Object... urlVariables) {
        request = handleRequest(request);
        urlVariables = fixVariables(urlVariables);
        HttpEntity entity = new HttpEntity<>(request, createHeaders());
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class, urlVariables);
            return response.hasBody() ? response.getBody() : null;
        } catch (RestClientException e) {
            log.info("request body: {}", request);
            throw e;
        }
    }

    public static void delete(String url, Object... urlVariables) {
        restTemplate.delete(url, fixVariables(urlVariables));
    }

    public static String deleteForObject(String url, Object... urlVariables) {
        urlVariables = fixVariables(urlVariables);
        HttpEntity<String> entity = new HttpEntity<>(createHeaders());
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class, urlVariables);
        return response.hasBody() ? response.getBody() : null;
    }

    private static HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private static String handleRequest(Object request) {
        if (request instanceof Map) {
            CommonUtils.preProcessMap((Map) request);
        }
        if (request != null) {
            try {
                return serializer.format(request);
            } catch (IOException e) {
                log.warn("Error occurred when format request to json!", e);
                return request.toString();
            }
        }
        return "";
    }

    private static Object[] fixVariables(Object... urlVariables) {
        return urlVariables == null || urlVariables.length == 0 ? new Object[]{ "" } : urlVariables;
    }

    private static RestTemplate createRestTemplate() {
        return createRestTemplate(false);
    }

    private static RestTemplate createRestTemplate(boolean allTrust) {
        RestTemplate restTemplate = new RestTemplate(httpRequestFactory(allTrust));

        List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
        converters.set(1, new StringHttpMessageConverter(UTF_8));
        restTemplate.setMessageConverters(converters);

        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>(restTemplate.getInterceptors());
        interceptors.add(new HeaderEnhancer());
        restTemplate.setInterceptors(interceptors);

        return restTemplate;
    }

    private static RetryTemplate createRetryTemplate() {
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(restProperties.getRetry().getMaxAttempts());

        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(restProperties.getRetry().getBackOffPeriod().toMillis());

        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(retryPolicy);
        retryTemplate.setBackOffPolicy(backOffPolicy);

        return retryTemplate;
    }

    private static class HeaderEnhancer implements ClientHttpRequestInterceptor {

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            HttpHeaders headers = request.getHeaders();
            if (headers.get(LogConstants.X_REQUEST_ID) == null && CommonUtils.isMDCValueNotNull(MDC.get(LogConstants.X_REQUEST_ID))) {
                headers.add(LogConstants.X_REQUEST_ID, MDC.get(LogConstants.X_REQUEST_ID));
            }
            if (!StringUtils.isEmpty(service)) {
                headers.add(LogConstants.X_UPSTREAM, service);
            }

            if (restProperties.getRetry().getHosts().contains(request.getURI().getHost())) {
                return retryTemplate.execute(retryContext -> {
                    log.info("Accessing {} with retry, retry count: {}", request.getURI(), retryContext.getRetryCount());
                    return execution.execute(new HttpRequestWrapper(request), body);
                });

            } else {
                log.info("Access url: {}", request.getURI());
                return execution.execute(new HttpRequestWrapper(request), body);
            }
        }
    }

    private static ClientHttpRequestFactory httpRequestFactory(boolean allTrust) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient(allTrust));
        factory.setConnectTimeout((int) restProperties.getConnectTimeout().toMillis());
        factory.setConnectionRequestTimeout((int) restProperties.getRequestTimeout().toMillis());
        factory.setReadTimeout((int) restProperties.getReadTimeout().toMillis());
        return factory;
    }

    private static org.apache.http.client.HttpClient httpClient(boolean allTrust) {
        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(getSSLContext(allTrust), getHostnameVerifier());

        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", socketFactory)
                .build();

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        connectionManager.setMaxTotal(restProperties.getConnection().getPool().getMaxTotal());
        connectionManager.setDefaultMaxPerRoute(restProperties.getConnection().getPool().getDefaultMaxPerRoute());

        return HttpClients.custom().setConnectionManager(connectionManager).build();
    }

    private static HostnameVerifier getHostnameVerifier() {
        return (s, sslSession) -> true;
    }

    private static SSLContext getSSLContext(boolean allTrust) {
        if (allTrust) {
            return SSLContexts.createDefault();
        } else {
            try {
                return SSLContexts.custom()
                        .loadTrustMaterial(null, (TrustStrategy) (chain, authType) -> true)
                        .build();
            } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
                log.error("Load trust material failed!");
            }
            return null;
        }
    }
}
