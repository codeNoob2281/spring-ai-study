package org.example.ai.tool;

import lombok.extern.slf4j.Slf4j;
import org.example.ai.entity.FetchWebResult;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 网页内容抓取服务
 * 从URL解析HTML内容
 * @author floyd
 */
@Service
@Slf4j
public class FetchWebService {

    private static final int MAX_CONTENT_LENGTH = 500000;

    private final HttpClient httpClient;

    public FetchWebService() {
        this.httpClient = HttpClient.newHttpClient();
    }

    @Tool(description = "Fetch the full content of a webpage from a URL. Use this when you need to get more detailed information from a specific webpage.")
    public FetchWebResult fetchWeb(@ToolParam(description = "The URL to fetch content from") String url) {
        log.info("开始抓取网页内容：{}", url);
        String title = "";
        String content = "";

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                    .header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8")
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            String html = response.body();

            title = extractTitle(html);
            content = extractContent(html);

            log.info("网页抓取完成，标题：{}", title);
        } catch (IOException | InterruptedException e) {
            log.error("网页抓取失败：{}", e.getMessage());
            Thread.currentThread().interrupt();
        }

        return new FetchWebResult(url, title, content);
    }

    private String extractTitle(String html) {
        Pattern titlePattern = Pattern.compile("<title[^>]*>([^<]+)</title>", Pattern.CASE_INSENSITIVE);
        Matcher titleMatcher = titlePattern.matcher(html);
        if (titleMatcher.find()) {
            return cleanHtml(titleMatcher.group(1));
        }
        return "";
    }

    private String extractContent(String html) {
        // 移除script和style标签
        String cleaned = html.replaceAll("(?i)<script[^>]*>.*?</script>", "")
                             .replaceAll("(?i)<style[^>]*>.*?</style>", "")
                             .replaceAll("(?i)<noscript[^>]*>.*?</noscript>", "");

        // 提取<body>标签内的内容
        Pattern bodyPattern = Pattern.compile("(?i)<body[^>]*>(.*?)</body>", Pattern.DOTALL);
        Matcher bodyMatcher = bodyPattern.matcher(cleaned);
        if (bodyMatcher.find()) {
            cleaned = bodyMatcher.group(1);
        }

        // 移除所有HTML标签
        cleaned = cleaned.replaceAll("<[^>]+>", " ");

        // 解码HTML实体
        cleaned = decodeHtmlEntities(cleaned);

        // 清理空白字符
        cleaned = cleaned.replaceAll("\\s+", " ").trim();

        // 截断过长内容
        if (cleaned.length() > MAX_CONTENT_LENGTH) {
            cleaned = cleaned.substring(0, MAX_CONTENT_LENGTH) + "...";
        }

        return cleaned;
    }

    private String cleanHtml(String text) {
        if (text == null) return "";
        return text.replaceAll("\\s+", " ").trim();
    }

    private String decodeHtmlEntities(String text) {
        if (text == null) return "";
        return text.replace("&nbsp;", " ")
                   .replace("&lt;", "<")
                   .replace("&gt;", ">")
                   .replace("&amp;", "&")
                   .replace("&quot;", "\"")
                   .replace("&#39;", "'")
                   .replace("&apos;", "'");
    }
}
