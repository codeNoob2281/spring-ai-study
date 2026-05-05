package org.example.ai.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author floyd
 */
@RestController
@RequestMapping("/chat-memory")
public class ChatMemoryDemoController {

    @Resource
    ChatClient withMemoryChatClient;

    @Resource
    ChatMemory chatMemory;


    @RequestMapping("/test-mongo")
    public String testMongo(String message, String conversationId) {
        return withMemoryChatClient.prompt()
                .advisors(p -> p.param(ChatMemory.CONVERSATION_ID, conversationId))
                .advisors(new SimpleLoggerAdvisor())
                .user(message)
                .call()
                .content();
    }

    @RequestMapping("/get-chat-history")
    public List<Message> getChatHistory(String conversationId) {
        return chatMemory.get(conversationId);
    }
}
