package com.floyd.mc.agent;

import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.floyd.mc.contant.PromptConstants;
import com.floyd.mc.tool.McModSearchTools;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author floyd
 */
@Component
public class AgentRegistry {

    Map<String, ReactAgent> reactAgentMap = new ConcurrentHashMap<>();

    @Resource
    ChatModel chatModel;

    @Resource
    McModSearchTools mcModSearchTools;

    /**
     * 通用agent
     *
     * @return
     */
    public ReactAgent defaultAgent() {
        return reactAgentMap.computeIfAbsent("#default", (k) -> {
            return ReactAgent.builder()
                    .name("#default")
                    .model(chatModel)
                    .chatOptions(ToolCallingChatOptions.builder()
                            .temperature(0.5d)
                            .build())
                    .systemPrompt(PromptConstants.SYSTEM_PROMPT)
                    .tools()
                    .build();
        });
    }

    /**
     * 模组相关agent
     *
     * @return
     */
    public ReactAgent modMasterAgent() {
        return reactAgentMap.computeIfAbsent("modMasterAgent", (k) -> {
            return ReactAgent.builder()
                    .name("modMasterAgent")
                    .model(chatModel)
                    .chatOptions(ToolCallingChatOptions.builder()
                            .temperature(0.5d)
                            .maxTokens(10000)
                            .build())
                    .systemPrompt("""
                            你是一个Minecraft模组问答助手，了解各种模组，尤其是拔刀剑、IC2等经典模组。你的目标是解答玩家的各种问题。
                            
                            ## 核心能力
                            1. 搜索各种模组信息，提供模组清单
                            2. 提供模组相关的攻略
                            
                            ## 回答格式
                            使用口语化的表达，展现玩家的活跃性，不要让别人觉得你是个AI
                            
                            ## 注意事项
                            - 如果问题模糊不清，先询问玩家的具体情况（游戏版本、当前进度、具体需求等）
                            - 对于有争议的话题（如最优策略），提供多种方案并说明各自的优缺点
                            - 涉及模组(Mod)或数据包的问题，先确认玩家使用的具体模组名称和版本
                            - 始终保持友好、耐心的态度
                            """)
                    .methodTools(mcModSearchTools)
                    .build();
        });
    }
}
