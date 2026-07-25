package org.example.alibaba;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.agent.interceptor.todolist.TodoListInterceptor;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.alibaba.config.LlmAgentManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 *
 *
 * @author <a href="mailto:huangzf1778@fingard.com">huang zifan</a>
 * @version 1.0
 * @projectName: spring-ai-study
 * @date 2026/7/25
 */
@Slf4j
@Component
public class TestRunner implements CommandLineRunner {

    @Resource
    LlmAgentManager llmAgentManager;

    @Override
    public void run(String... args) throws Exception {
        ReactAgent planModeAgent = llmAgentManager.planModeAgent();
        Optional<OverAllState> overAllState = planModeAgent.invoke("使用Spring AI Alibaba框架搭建：企业知识库问答助手系统");
        overAllState.ifPresent(s -> {
            if (s.value("todos").isPresent()) {
                List<TodoListInterceptor.Todo> todoList = (List<TodoListInterceptor.Todo>) s.value("todos").get();
                for (TodoListInterceptor.Todo todo : todoList) {
                    log.info("todo列表项：{}，状态：{}", todo.getContent(), todo.getStatus());
                }
            }
        });
    }
}
