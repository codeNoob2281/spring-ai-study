package org.example.ai.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.content.Media;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.io.InputStream;
import java.net.URI;
import java.util.Map;

/**
 * @author floyd
 */
@RestController
@RequestMapping("/multimodal")
public class MultiModalDemoController {

    @Resource
    private ChatClient defaultChatClient;

    @Value("${spring.ai.openai.image.base-url}")
    private String imageBaseUrl;

    @Value("${spring.ai.openai.image.images-path}")
    private String imagesPath;

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    @Value("${spring.ai.openai.image.options.model}")
    private String imageModel;

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

    @SuppressWarnings("unchecked")
    @GetMapping("generate-pictures")
    public void generatePictures(String imageMsg, HttpServletResponse response) throws Exception {
        // 火山引擎豆包图片生成接口使用 content 字段，与 OpenAI 的 prompt 不兼容，需直接调用
        RestClient restClient = RestClient.builder()
                .baseUrl(imageBaseUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();

        // 1. 创建图片生成任务
        Map<String, Object> requestBody = Map.of(
                "model", imageModel,
                "content", imageMsg
        );
        Map<String, Object> taskResp = restClient.post()
                .uri(imagesPath)
                .body(requestBody)
                .retrieve()
                .body(Map.class);

        String taskId = ((Map<String, Object>) taskResp.get("data")).get("task_id").toString();

        // 2. 轮询任务状态
        String resultUrl = null;
        for (int i = 0; i < 30; i++) {
            Thread.sleep(2000);
            Map<String, Object> pollResp = restClient.get()
                    .uri(imagesPath + "/" + taskId)
                    .retrieve()
                    .body(Map.class);
            Map<String, Object> data = (Map<String, Object>) pollResp.get("data");
            String status = data.get("status").toString();
            if ("succeeded".equals(status)) {
                resultUrl = ((Map<String, Object>) ((java.util.List<?>) data.get("content")).get(0)).get("url").toString();
                break;
            }
            if ("failed".equals(status)) {
                response.sendError(500, "图片生成失败: " + data.get("error_message"));
                return;
            }
        }

        if (resultUrl == null) {
            response.sendError(504, "图片生成超时");
            return;
        }

        // 3. 下载图片并写入响应
        response.setContentType("image/png");
        response.setHeader("Content-Disposition", "attachment; filename=generated.png");
        try (InputStream is = URI.create(resultUrl).toURL().openStream()) {
            is.transferTo(response.getOutputStream());
        }
    }
}
