package org.example.ai.controller;


import lombok.extern.slf4j.Slf4j;
import org.example.ai.skill.SkillManager;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author floyd
 */
@RestController
@RequestMapping("/ai-bot")
@Slf4j
public class AiBotController {

    @Value("classpath:prompt/ai-bot-skills-prompt.st")
    Resource aiBotSkillsPrompt;

    @Autowired
    private SkillManager skillManager;

    @Autowired
    private ChatClient withMemoryChatClient;


    @PostMapping("/chat")
    public String chat(String message, String conversationId) {
        PromptTemplate aiBotSkillsPromptTemp = PromptTemplate.builder()
                .resource(aiBotSkillsPrompt)
                .build();

        return withMemoryChatClient.prompt()
                .system(aiBotSkillsPromptTemp.render())
                .user(message)
                .toolCallbacks(skillManager.getToolCallback())
                .advisors(p -> p.param(ChatMemory.CONVERSATION_ID, conversationId))
                .advisors(new SimpleLoggerAdvisor())
                .call()
                .content();
    }


}
