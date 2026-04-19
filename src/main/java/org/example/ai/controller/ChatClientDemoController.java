package org.example.ai.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.ai.entity.AirTicket;
import org.example.ai.model.MultiChatModelService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * @author floyd
 */
@RestController
@RequestMapping("/chat-client-demo")
@Slf4j
public class ChatClientDemoController {

    @Resource
    ChatClient defaultChatClient;

    @Resource
    MultiChatModelService multiChatModelService;

    @GetMapping("/send-message")
    public String sendMessage(String message) {
        return defaultChatClient.prompt()
                .user(message)
                .call()
                .content();
    }

    @GetMapping("/choose-model-and-send-message")
    public String chooseModelAndSendMessage(String modelId, String message) {
        ChatModel chatModel = multiChatModelService.getChatModel(modelId);
        ChatClient chatClient = ChatClient.builder(chatModel).build();
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

    @GetMapping("/send-message-2")
    public ChatResponse getAirTicket(String message) {
        ChatResponse chatResponse = defaultChatClient.prompt()
                .user(message)
                .call()
                .chatResponse();

        Usage usage = chatResponse.getMetadata().getUsage();
        log.info("本次对话消耗token：输入{}，输出{}，一共{}", usage.getPromptTokens(), usage.getCompletionTokens(), usage.getTotalTokens());
        return chatResponse;
    }

    @GetMapping("/get-air-ticket")
    public AirTicket getAirTicket(String fromCity, String toCity, String orderDate) {
        return defaultChatClient.prompt()
                .system("请根据用户输入的出发城市、目的地城市和订票日期，生成一张机票，注意日期格式为yyyy-MM-dd HH:mm:ss")
                .user(u -> u.text("我想在{orderDate}订一张从{fromCity}到{toCity}的机票")
                        .param("orderDate", orderDate)
                        .param("fromCity", fromCity)
                        .param("toCity", toCity))
                .call()
                .entity(AirTicket.class);
    }

    @GetMapping("/get-air-tickets")
    public List<AirTicket> getAirTickets(String fromCity, String toCity, String orderDate) {
        return defaultChatClient.prompt()
                .system("请根据用户输入的出发城市、目的地城市和订票日期，模拟生成机票，注意日期格式为yyyy-MM-dd HH:mm:ss")
                .user(u -> u.text("帮我查一下{orderDate}后，从{fromCity}到{toCity}可预订的机票")
                        .param("orderDate", orderDate)
                        .param("fromCity", fromCity)
                        .param("toCity", toCity))
                .call()
                .entity(new ParameterizedTypeReference<List<AirTicket>>() {
                });
    }


    @GetMapping(value = "/stream-send-message", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamSendMessage(String message) {
        return defaultChatClient.prompt()
                .user(message)
                .stream()
                .content();
    }
}
