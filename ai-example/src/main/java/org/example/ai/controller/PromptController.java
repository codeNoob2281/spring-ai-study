package org.example.ai.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author floyd
 */
@RestController
@RequestMapping("/prompt")
@Slf4j
public class PromptController {

    @Resource
    ChatClient defaultChatClient;

    @Value("classpath:prompt/movie-prompt.st")
    org.springframework.core.io.Resource moviePromptTemplateResource;

    @GetMapping("/message-types")
    public String messageTypes(String message) {
        SystemMessage systemMessage = new SystemMessage("你是一个AI问答机器人，接下来不管用户问啥，你只能回答是和否。");
        UserMessage userMessage = new UserMessage("1+1=3吗");
        AssistantMessage assistantMessage = new AssistantMessage("否");
        Prompt prompt = Prompt.builder()
                .messages(systemMessage, userMessage, assistantMessage)
                .build();
        String content = defaultChatClient.prompt(prompt)
                .user(message)
                .call()
                .content();
        log.info(prompt.getSystemMessage().getText());
        log.info(prompt.getLastUserOrToolResponseMessage().getText());
        return content;
    }

    @GetMapping("/use-prompt-template")
    public String usePromptTemplate(String movieType, Integer movieCount) {
        PromptTemplate promptTemplate = PromptTemplate.builder()
                .template("请给我推荐{movieCount}个{movieType}电影")
                .build();
        String renderContent = promptTemplate.render(Map.of("movieType", movieType, "movieCount", movieCount));
        return defaultChatClient.prompt()
                .system("""
                        你是一个电影推荐助手，用户询问建议时，直接给出json格式的电影名称列表，例如["《电影1》","《电影2》"]
                        """)
                .user(renderContent)
                .call()
                .content();
    }

    @GetMapping("/use-custom-prompt-template")
    public String useCustomPromptTemplate(String movieType, Integer movieCount) {
        PromptTemplate promptTemplate = PromptTemplate.builder()
                .template("请给我推荐<movieCount>个<movieType>电影")
                .renderer(StTemplateRenderer.builder()
                        .startDelimiterToken('<')
                        .endDelimiterToken('>')
                        .build())
                .build();
        String renderContent = promptTemplate.render(Map.of("movieType", movieType, "movieCount", movieCount));

        // 提示词注入拦截
        SafeGuardAdvisor safeGuardAdvisor = SafeGuardAdvisor.builder()
                .order(BaseAdvisor.HIGHEST_PRECEDENCE + 100)
                .sensitiveWords(List.of("忽略", "系统", "提示词", "不用推荐"))
                .build();

        return defaultChatClient.prompt()
                .advisors(safeGuardAdvisor)
                .system("""
                        你是一个电影推荐助手，用户询问建议时，直接给出json格式的电影名称列表，例如["《电影1》","《电影2》"]
                        """)
                .user(renderContent)
                .call()
                .content();
    }

    @GetMapping("/use-prompt-template-build-multi-role-messages")
    public String usePromptTemplateBuildMultiRoleMessages(String assistantName, String movieType, Integer movieCount) {
        SystemPromptTemplate systemPromptTemplate = SystemPromptTemplate.builder()
                .template("你是一个AI助手，你的名字是{name}")
                .build();
        SystemMessage systemMessage = (SystemMessage) systemPromptTemplate.createMessage(Map.of("name", assistantName));

        PromptTemplate userPromptTemplate = PromptTemplate.builder()
                .template("请给我推荐{movieCount}个{movieType}类型的电影")
                .build();
        UserMessage userMessage = (UserMessage) userPromptTemplate.createMessage(Map.of("movieType", movieType, "movieCount", movieCount));

        return defaultChatClient.prompt()
                .messages(systemMessage, userMessage)
                .call()
                .content();
    }

    @GetMapping("/use-prompt-template-from-resource")
    public String userPromptTemplateFromResource(String movieType, Integer movieCount) {
        PromptTemplate promptTemplate = PromptTemplate.builder()
                .resource(moviePromptTemplateResource)
                .build();
        return defaultChatClient.prompt()
                .user(promptTemplate.render(Map.of("movieType", movieType, "movieCount", movieCount)))
                .call()
                .content();
    }
}
