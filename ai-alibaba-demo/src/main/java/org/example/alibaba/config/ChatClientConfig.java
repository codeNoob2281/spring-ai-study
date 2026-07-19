package org.example.alibaba.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    @Bean
    public ChatClient weatherChatClient(ChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultSystem("你是一个专业的天气助手，能够根据用户的问题查询天气信息。" +
                        "当用户询问天气时，请使用提供的天气查询工具获取准确的天气数据，" +
                        "并用友好、简洁的语言回答用户。支持查询的城市有：北京、上海、广州、深圳、杭州、成都、武汉、西安、南京、重庆。")
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }
}
