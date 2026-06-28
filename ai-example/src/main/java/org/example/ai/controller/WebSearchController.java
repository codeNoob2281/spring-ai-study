package org.example.ai.controller;

import jakarta.annotation.Resource;
import org.example.ai.tool.FetchWebService;
import org.example.ai.tool.WebSearchService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Web搜索控制器
 * @author floyd
 */
@RestController
@RequestMapping("/web-search-demo")
public class WebSearchController {

    @Resource
    WebSearchService webSearchService;

    @Resource
    FetchWebService fetchWebService;

    @Resource
    ChatClient defaultChatClient;

    /**
     * 直接搜索
     */
    @RequestMapping("/search")
    public String search(@RequestParam String query) {
        return webSearchService.searchWeb(query).toString();
    }

    /**
     * 抓取网页内容
     */
    @RequestMapping("/fetch")
    public String fetch(@RequestParam String url) {
        return fetchWebService.fetchWeb(url).toString();
    }

    /**
     * 通过LLM调用搜索和网页抓取
     */
    @RequestMapping("/chat-with-search")
    public String chatWithSearch(@RequestParam String message) {
        return defaultChatClient.prompt()
                .tools(webSearchService, fetchWebService)
                .advisors(new SimpleLoggerAdvisor())
                .user(message)
                .call()
                .content();
    }
}
