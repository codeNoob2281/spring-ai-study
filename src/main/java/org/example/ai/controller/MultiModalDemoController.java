package org.example.ai.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.content.Media;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author floyd
 */
@RestController
@RequestMapping("/multimodal")
public class MultiModalDemoController {

    @Resource
    private ChatClient defaultChatClient;

    @GetMapping("explain-pictures")
    public String explainPictures() {
        ClassPathResource imgResource = new ClassPathResource("images/mc-demo1.png");
        return defaultChatClient.prompt()
                .options(ChatOptions.builder()
                        .temperature(0d)
                        .topK(5)
                        .topP(1d)
                        .build())
                .user(u -> u.text("说说你在这张图片看到了什么")
                        .media(new Media(MimeTypeUtils.IMAGE_PNG, imgResource)))
                .call()
                .content();
    }
}
