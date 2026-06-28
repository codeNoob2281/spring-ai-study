package org.example.ai.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Tavily搜索结果项
 * @author floyd
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record TavilySearchResult(
        String url,
        String title,
        String content,
        double score,
        String rawContent
) {

}
