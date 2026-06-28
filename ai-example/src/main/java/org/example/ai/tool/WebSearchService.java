package org.example.ai.tool;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.ai.entity.TavilySearchResponse;
import org.example.ai.entity.WebSearchResult;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Web搜索服务
 * 使用Tavily API进行搜索
 * @author floyd
 */
@Service
@Slf4j
public class WebSearchService {

    private static final String TAVILY_API_URL = "https://api.tavily.com/search";

    @Value("${spring.ai.tavily.api-key}")
    private String tavilyApiKey;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public WebSearchService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    @Tool(description = "Search the web for information. Use this when you need to find current or real-time information that you don't already know.")
    public List<WebSearchResult> searchWeb(@ToolParam(description = "The search query to look up") String query) {
        log.info("开始Web搜索，查询关键词：{}", query);
        List<WebSearchResult> results = new ArrayList<>();

        try {
            String requestBody = String.format("{\"query\":\"%s\"}", query);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(TAVILY_API_URL))
                    .header("Authorization", "Bearer " + tavilyApiKey)
                    .header("Content-Type", "application/json")
                    .header("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                    .header("Accept", "*/*")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();

            log.debug("Tavily API响应: {}", responseBody);

            TavilySearchResponse tavilyResponse = objectMapper.readValue(responseBody, TavilySearchResponse.class);

            if (tavilyResponse.results() != null) {
                for (var result : tavilyResponse.results()) {
                    results.add(new WebSearchResult(result.title(), result.url(), result.content()));
                }
            }

            log.info("搜索完成，找到 {} 条结果", results.size());
        } catch (IOException | InterruptedException e) {
            log.error("搜索失败：{}", e.getMessage());
            Thread.currentThread().interrupt();
        }

        return results;
    }
}
