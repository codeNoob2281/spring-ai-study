package org.example.ai.controller;

import jakarta.annotation.Resource;
import org.example.ai.tool.DateTimeService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author floyd
 */
@RestController
@RequestMapping("/tool-calling-demo")
public class ToolCallingDemoController {

    @Resource
    DateTimeService dateTimeService;

    @Resource
    ChatClient defaultChatClient;

    @RequestMapping("/get-time")
    public String getTime(String message) {
        return defaultChatClient.prompt()
                .tools(dateTimeService)
                .advisors(new SimpleLoggerAdvisor())
                .user(message)
                .call()
                .content();
    }

}
