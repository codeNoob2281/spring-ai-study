package org.example.ai.controller;

import jakarta.annotation.Resource;
import org.example.ai.entity.AirTicket;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.StructuredOutputValidationAdvisor;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author floyd
 */
@RestController
@RequestMapping("/advisor-demo")
public class AdvisorDemoController {

    @Resource
    ChatModel chatModel;

    @Resource
    ChatMemory chatMemory;

    @GetMapping("/send-message-with-chat-memory")
    public String sendMessageWithChatMemory(String message) {
        MessageChatMemoryAdvisor chatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory)
                .conversationId("with-chat-memory-test")
                .order(BaseAdvisor.HIGHEST_PRECEDENCE + 100)
                .build();

        ChatClient chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(chatMemoryAdvisor)
                .build();

        return chatClient.prompt()
                .advisors(new SimpleLoggerAdvisor())
                .user(message)
                .call()
                .content();
    }

    @GetMapping("/check-and-send-message")
    public String checkAndSendMessage(String message) {
        SafeGuardAdvisor safeGuardAdvisor = SafeGuardAdvisor.builder()
                .sensitiveWords(List.of("敏感词1", "敏感词2"))
                .failureResponse("请勿输入敏感词")
                .order(BaseAdvisor.HIGHEST_PRECEDENCE + 100)
                .build();

        ChatClient chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(safeGuardAdvisor)
                .build();

        return chatClient.prompt()
                .advisors(new SimpleLoggerAdvisor())
                .user(message)
                .call()
                .content();
    }

    @GetMapping("/send-message-and-format-answer")
    public AirTicket sendMessageAndFormatAnswer(String message) {
        StructuredOutputValidationAdvisor structuredOutputValidationAdvisor = StructuredOutputValidationAdvisor.builder()
                .outputType(AirTicket.class)
                .maxRepeatAttempts(3)
                .advisorOrder(BaseAdvisor.HIGHEST_PRECEDENCE + 100)
                .build();

        ChatClient chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(structuredOutputValidationAdvisor)
                .build();

        return chatClient.prompt()
                .advisors(new SimpleLoggerAdvisor())
                .user(message)
                .call()
                .entity(AirTicket.class);
    }

}
