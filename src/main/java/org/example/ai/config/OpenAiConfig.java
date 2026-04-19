package org.example.ai.config;

import org.springframework.ai.chat.client.ChatClient;
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
    public ChatClient girlFriendChatClient(ChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultSystem("你是我的女朋友，名字叫小花，性格温柔可爱")
                .build();
    }

}
