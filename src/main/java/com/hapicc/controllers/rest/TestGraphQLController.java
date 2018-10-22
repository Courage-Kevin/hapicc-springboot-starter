package com.hapicc.controllers.rest;

import com.google.gson.Gson;
import com.hapicc.common.rest.services.github.GitHubEntry;
import com.hapicc.common.rest.services.github.GraphQLService;
import com.hapicc.pojo.HapiccJSONResult;
import com.hapicc.utils.common.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("graphQL")
public class TestGraphQLController {

    @Autowired
    private GraphQLService graphQLService;

    @PostMapping("testListEntriesInfo")
    public HapiccJSONResult testListEntriesInfo(@RequestBody Map<String, String> data) {
        try {
            List<GitHubEntry> entries = graphQLService.listEntriesInfo(
                    data.get("expression"), data.get("repoName"), data.get("repoOwner")
            );
            return HapiccJSONResult.ok(Collections.singletonMap("entries", entries));
        } catch (Exception e) {
            log.warn("Error occurred when list entries info with data: " + JsonUtils.obj2Json(data), e);
            return HapiccJSONResult.build(400, "Invalid data!");
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
            log.warn("Error occurred when load file content with params: " + JsonUtils.obj2Json(params), e);
            return HapiccJSONResult.build(400, "Invalid parameters!");
        }
    }
}
