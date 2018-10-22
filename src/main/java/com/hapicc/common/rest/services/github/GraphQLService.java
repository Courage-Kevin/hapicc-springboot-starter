package com.hapicc.common.rest.services.github;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.hapicc.common.rest.client.HttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GraphQLService {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Value("${github.graphql.server}")
    private String apiServer;

    @Value("${github.accessToken}")
    private String accessToken;

    /**
     * List entries info in the expression. The info contains oid, name and type.
     *
     * @param expression A string. Format like "${branch}:${path}".
     * @param repoName   A string. The name of repository.
     * @param repoOwner  A string. The owner of repository.
     * @return A list. Entries info list.
     */
    public List<GitHubEntry> listEntriesInfo(String expression, String repoName, String repoOwner) {
        return batchListEntriesInfo(Collections.singletonMap("obj", expression), repoName, repoOwner).get("obj");
    }

    /**
     * Batch list entries info in multiple expressions. The info contains oid, name and type.
     *
     * @param expressionMap A map. The key will be used in the result, the value like "${branch}:${path}".
     * @param repoName      A string. The name of repository.
     * @param repoOwner     A string. The owner of repository.
     * @return A map. The key use the key in expressionMap, the value is the corresponding entries info list.
     */
    public Map<String, List<GitHubEntry>> batchListEntriesInfo(Map<String, String> expressionMap, String repoName, String repoOwner) {
        Assert.notEmpty(expressionMap, "The expressionMap cannot be empty!");
        Assert.hasLength(repoName, "The repoName cannot be empty!");
        Assert.hasLength(repoOwner, "The repoOwner cannot be empty!");

        StringBuilder sb = new StringBuilder("{ repository(name: \"");
        sb.append(repoName).append("\", owner: \"").append(repoOwner).append("\") { ");
        expressionMap.forEach((objAlias, objExpression) -> sb.append("_").append(objAlias).append(": object(expression: \"").append(objExpression).append("\") { ... on Tree { entries { oid name type } } } "));
        sb.append("} }");

        Map<String, List<GitHubEntry>> result = new HashMap<>();

        String resp = request(sb.toString().replaceAll("\\s+", " "));
        try {
            JsonNode node = mapper.readTree(resp);
            JsonNode repoNode = node.get("data").get("repository");
            if (repoNode.isNull()) {
                log.info("Null repository, resp: " + resp);
                return result;
            }
            Iterator<String> fieldNames = repoNode.fieldNames();
            while (fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                JsonNode objNode = repoNode.get(fieldName);
                if (!objNode.isNull()) {
                    String entriesStr = objNode.get("entries").toString();
                    JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, GitHubEntry.class);
                    result.put(fieldName.substring(1), mapper.readValue(entriesStr, javaType));
                }
            }
        } catch (IOException e) {
            log.warn("Error occurred when parse resp: " + resp, e);
        }

        return result;
    }

    /**
     * Load file content as string.
     *
     * @param oid       A string. The Git object ID of entry file.
     * @param repoName  A string. The name of repository.
     * @param repoOwner A string. The owner of repository.
     * @return A string. Content string or null.
     */
    public String loadFileContent(String oid, String repoName, String repoOwner) {
        return batchLoadFilesContent(Collections.singletonList(oid), repoName, repoOwner).get(oid);
    }

    /**
     * Batch load files content as string.
     *
     * @param oids      A string list. The Git object ID list of entry files.
     * @param repoName  A string. The name of repository.
     * @param repoOwner A string. The owner of repository.
     * @return A map. The key use oid, the value is the corresponding entry file content string or null.
     */
    public Map<String, String> batchLoadFilesContent(List<String> oids, String repoName, String repoOwner) {
        Assert.notEmpty(oids, "The oids cannot be empty!");
        Assert.hasLength(repoName, "The repoName cannot be empty!");
        Assert.hasLength(repoOwner, "The repoOwner cannot be empty!");

        StringBuilder sb = new StringBuilder("{ repository(name: \"");
        sb.append(repoName).append("\", owner: \"").append(repoOwner).append("\") { ");
        oids = oids.stream().distinct().collect(Collectors.toList());
        int count = oids.size();
        for (int i = 0; i < count; i++) {
            sb.append("_").append(i).append(": object(oid: \"").append(oids.get(i)).append("\") { ... on Blob { text } } ");
        }
        sb.append("} }");

        Map<String, String> result = new HashMap<>();

        String resp = request(sb.toString().replaceAll("\\s+", " "));
        try {
            JsonNode node = mapper.readTree(resp);
            JsonNode repoNode = node.get("data").get("repository");
            if (repoNode.isNull()) {
                log.info("Null repository, resp: " + resp);
                return result;
            }
            Iterator<String> fieldNames = repoNode.fieldNames();
            while (fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                JsonNode objNode = repoNode.get(fieldName);
                if (!objNode.isNull()) {
                    String content = objNode.get("text").asText();
                    result.put(oids.get(Integer.valueOf(fieldName.substring(1))), content);
                }
            }
        } catch (IOException e) {
            log.warn("Error occurred when parse resp: " + resp, e);
        }

        return result;
    }

    private String request(String queryString) {
        Assert.hasLength(queryString, "The query string cannot be empty!");
        log.info("Execute GitHub GraphQL request with query string: {}", queryString);

        Map<String, String> data = new HashMap<>();
        data.put("query", queryString);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(new Gson().toJson(data), headers);
        try {
            ResponseEntity<String> response = HttpClient.getRestTemplate().exchange(apiServer, HttpMethod.POST, entity, String.class);
            if (response != null && response.hasBody()) {
                return response.getBody();
            }
        } catch (Exception e) {
            log.warn("Error occurred when execute GitHub GraphQL request!", e);
        }
        return null;
    }
}
