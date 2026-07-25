package org.example.alibaba.hooks;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.RunnableConfig;
import com.alibaba.cloud.ai.graph.agent.hook.AgentHook;
import com.alibaba.cloud.ai.graph.agent.hook.HookPosition;
import com.alibaba.cloud.ai.graph.agent.hook.HookPositions;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 *
 *
 * @author <a href="mailto:huangzf1778@fingard.com">huang zifan</a>
 * @version 1.0
 * @projectName: spring-ai-study
 * @date 2026/7/25
 */
@Slf4j
@HookPositions({HookPosition.BEFORE_AGENT, HookPosition.AFTER_AGENT})
public class LoggingHook extends AgentHook {

    @Override
    public CompletableFuture<Map<String, Object>> beforeAgent(OverAllState state, RunnableConfig config) {
        log.info("Agent开始执行....");
        return CompletableFuture.completedFuture(Map.of());
    }

    @Override
    public CompletableFuture<Map<String, Object>> afterAgent(OverAllState state, RunnableConfig config) {
        log.info("Agent结束执行....");
        return CompletableFuture.completedFuture(Map.of());
    }

    @Override
    public String getName() {
        return "LoggingHook";
    }
}
