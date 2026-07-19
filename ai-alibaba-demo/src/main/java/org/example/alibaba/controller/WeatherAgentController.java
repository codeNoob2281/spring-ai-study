package org.example.alibaba.controller;

import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.example.alibaba.service.WeatherService;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather-agent")
public class WeatherAgentController {

    @Resource
    private ChatModel chatModel;

    @Resource
    private WeatherService weatherService;

    @GetMapping("/chat")
    @SneakyThrows
    public String chat(@RequestParam String message) {
        //return weatherChatClient.prompt()
        //        .tools(weatherService)
        //        .user(message)
        //        .call()
        //        .content();
        ReactAgent weatherAgent = ReactAgent.builder()
                .name("weather_agent")
                .model(chatModel)
                .systemPrompt("你是一个专业的天气助手，能够根据用户的问题查询天气信息。" +
                        "当用户询问天气时，请使用提供的天气查询工具获取准确的天气数据，" +
                        "并用友好、简洁的语言回答用户。支持查询的城市有：北京、上海、广州、深圳、杭州、成都、武汉、西安、南京、重庆。")
                .methodTools(weatherService)
                .interceptors()
                .build();
        return weatherAgent.call(message).getText();
    }
}
