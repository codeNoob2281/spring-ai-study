package org.example.alibaba.controller;

import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.example.alibaba.config.LlmAgentManager;
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
    private LlmAgentManager llmAgentManager;

    @GetMapping("/chat")
    @SneakyThrows
    public String chat(@RequestParam String message) {
        ReactAgent weatherQueryAgent = llmAgentManager.weatherQueryAgent();
        return weatherQueryAgent.call(message).getText();
    }
}
