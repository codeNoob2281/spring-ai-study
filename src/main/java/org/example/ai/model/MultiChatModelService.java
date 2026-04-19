package org.example.ai.model;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 多llm模型service
 *
 * @author floyd
 */
@Service
public class MultiChatModelService implements InitializingBean {

    @Resource
    private OpenAiChatModel openAiChatModel;

    @Resource
    private OpenAiApi openAiApi;

    public final Map<String, String> innerModelMap = new ConcurrentHashMap<>(8);

    public ChatModel getChatModel(String modelId) {
        return openAiChatModel.mutate()
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(getModeNameFromModelId(modelId))
                        .build())
                .build();
    }

    private String getModeNameFromModelId(String modelId) {
        String modelName = innerModelMap.get(modelId);
        if (modelName == null || modelName.isBlank()) {
            throw new RuntimeException("modelId:" + modelId + " not found");
        }
        return modelName;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        innerModelMap.put("1", "doubao-seed-2-0-pro-260215");
        innerModelMap.put("2", "glm-4-7-251222");
        innerModelMap.put("3", "deepseek-v3-2-251201");
    }
}
