package org.example.alibaba.hooks;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.RunnableConfig;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.agent.hook.HookPosition;
import com.alibaba.cloud.ai.graph.agent.hook.HookPositions;
import com.alibaba.cloud.ai.graph.agent.hook.ModelHook;
import com.alibaba.cloud.ai.graph.agent.hook.messages.AgentCommand;
import com.alibaba.cloud.ai.graph.agent.hook.messages.MessagesAgentHook;
import com.alibaba.cloud.ai.graph.agent.hook.messages.MessagesModelHook;
import com.alibaba.cloud.ai.graph.agent.hook.messages.UpdatePolicy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.Message;

import java.util.List;
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
@HookPositions(HookPosition.BEFORE_MODEL)
public class LimitMessageModelHook extends MessagesModelHook {

    public static final int MAX_MESSAGES = 20;

    @Override
    public AgentCommand beforeModel(List<Message> previousMessages, RunnableConfig config) {
        if (previousMessages.size() > MAX_MESSAGES) {
            log.warn("当前会话上下文超过最大消息数[{}]，丢弃最早的消息", MAX_MESSAGES);
            List<Message> subMessages =
                    previousMessages.subList(previousMessages.size() - MAX_MESSAGES, previousMessages.size());
            return new AgentCommand(subMessages, UpdatePolicy.REPLACE);
        }
        return new AgentCommand(previousMessages);
    }

    @Override
    public String getName() {
        return "";
    }
}
