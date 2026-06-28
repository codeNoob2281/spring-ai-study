package org.example.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.mongo.MongoChatMemoryRepository;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author floyd
 */
@Configuration
public class OpenAiConfig {

    @Bean
    public ChatClient defaultChatClient(ChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .build();
    }

    @Bean
    public ChatClient tellJokeChatClient(ChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultSystem("你是一个擅长讲笑话的聊天机器人，使用{language}语言将你的笑话分享给用户")
                .build();
    }

    @Bean
    public ChatMemory chatMemory(MongoChatMemoryRepository mongoChatMemoryRepository) {
        return MessageWindowChatMemory.builder()
                .maxMessages(5)
                .chatMemoryRepository(mongoChatMemoryRepository)
                .build();
    }

    @Bean
    public ChatClient withMemoryChatClient(ChatModel chatModel, ChatMemory chatMemory) {
        return ChatClient.builder(chatModel)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory)
                        .build())
                .build();
    }

}
