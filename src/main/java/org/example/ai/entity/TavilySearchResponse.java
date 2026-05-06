package org.example.ai.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Tavily搜索API响应实体
 *
 * @author floyd
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record TavilySearchResponse(
        String query,
        Object followUpQuestions,
        String answer,
        List<Object> images,
        List<TavilySearchResult> results,
        double responseTime,
        String requestId
) {

}
