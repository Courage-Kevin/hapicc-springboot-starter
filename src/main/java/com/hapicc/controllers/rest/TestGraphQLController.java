package com.hapicc.controllers.rest;

import com.google.gson.Gson;
import com.hapicc.common.rest.services.github.GitHubEntry;
import com.hapicc.common.rest.services.github.GraphQLService;
import com.hapicc.pojo.HapiccJSONResult;
import com.hapicc.utils.json.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("graphQL")
public class TestGraphQLController {

    @Autowired
    private GraphQLService graphQLService;

    @PostMapping("testListEntriesInfo")
    public HapiccJSONResult testListEntriesInfo(@RequestParam Map<String, String> params, @RequestBody Map<String, String> data) {
        try {
            List<GitHubEntry> entries = graphQLService.listEntriesInfo(
                    data.get("expression"), params.get("repoName"), params.get("repoOwner")
            );
            return HapiccJSONResult.ok(Collections.singletonMap("entries", entries));
        } catch (Exception e) {
            log.warn("Error occurred when list entries info with params: " + JacksonUtils.obj2Json(params) + ", data: " + JacksonUtils.obj2Json(data), e);
            return HapiccJSONResult.build(400, "Invalid data or parameters!");
        }
    }

    @PostMapping("testBatchListEntriesInfo")
    public HapiccJSONResult testBatchListEntriesInfo(@RequestParam Map<String, String> params, @RequestBody Map<String, String> data) {
        try {
            Map<String, List<GitHubEntry>> entriesMap = graphQLService.batchListEntriesInfo(
                    data, params.get("repoName"), params.get("repoOwner")
            );
            return HapiccJSONResult.ok(entriesMap);
        } catch (Exception e) {
            log.warn("Error occurred when batch list entries info with params: " + JacksonUtils.obj2Json(params) + ", data: " + JacksonUtils.obj2Json(data), e);
            return HapiccJSONResult.build(400, "Invalid data or parameters!");
        }
    }

    @GetMapping("testLoadFileContent")
    public HapiccJSONResult testLoadFileContent(@RequestParam Map<String, String> params) {
        try {
            String content = graphQLService.loadFileContent(
                    params.get("oid"), params.get("repoName"), params.get("repoOwner")
            );
            return HapiccJSONResult.ok(Collections.singletonMap("content", new Gson().fromJson(content, Object.class)));
        } catch (Exception e) {
            log.warn("Error occurred when load file content with params: " + JacksonUtils.obj2Json(params), e);
            return HapiccJSONResult.build(400, "Invalid parameters!");
        }
    }

    @PostMapping("testBatchLoadFilesContent")
    public HapiccJSONResult testBatchLoadFilesContent(@RequestParam Map<String, String> params, @RequestBody Map<String, List<String>> data) {
        try {
            Map<String, String> oidContentMap = graphQLService.batchLoadFilesContent(
                    data.get("oids"), params.get("repoName"), params.get("repoOwner")
            );
            if (oidContentMap != null && !oidContentMap.isEmpty()) {
                Gson gson = new Gson();
                return HapiccJSONResult.ok(oidContentMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> gson.fromJson(entry.getValue(), Object.class))));
            }
            return HapiccJSONResult.ok(oidContentMap);
        } catch (Exception e) {
            log.warn("Error occurred when batch load files content with params: " + JacksonUtils.obj2Json(params) + ", data: " + JacksonUtils.obj2Json(data), e);
            return HapiccJSONResult.build(400, "Invalid data or parameters!");
        }
    }
}
