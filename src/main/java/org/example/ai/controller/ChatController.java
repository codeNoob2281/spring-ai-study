package org.example.ai.controller;

import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.ai.tool.AirTicketService;
import org.example.ai.tool.WeatherService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author floyd
 */
@RestController
@RequestMapping("/chat")
@Slf4j
public class ChatController {

    @Resource
    ChatClient defaultChatClient;

    @Resource
    WeatherService weatherService;

    @Resource
    AirTicketService airTicketService;

    @Resource
    ChatMemory chatMemory;

    @PostMapping("/sendMessage")
    public String sendMessage(@RequestBody String message) {
        return defaultChatClient.prompt()
                .tools(weatherService, airTicketService)
                .user(message)
                .call()
                .content();
    }

    @SneakyThrows
    private String readBootstrapPrompt() {
        ClassPathResource classPathResource = new ClassPathResource("assistant/bootstrap.md");
        String content = new String(classPathResource.getInputStream().readAllBytes());
        return content;
    }

}
