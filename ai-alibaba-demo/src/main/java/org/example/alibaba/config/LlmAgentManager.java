package org.example.alibaba.config;

import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.agent.interceptor.todolist.TodoListInterceptor;
import com.alibaba.cloud.ai.graph.checkpoint.savers.MemorySaver;
import jakarta.annotation.Resource;
import org.example.alibaba.groupmessage.GroupMessageService;
import org.example.alibaba.hooks.LimitMessageModelHook;
import org.example.alibaba.hooks.LoggingHook;
import org.example.alibaba.service.WeatherService;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Component;

@Component
public class LlmAgentManager {

    @Resource
    ChatModel chatModel;

    @Resource
    private WeatherService weatherService;

    @Resource
    private GroupMessageService groupMessageService;


    public ReactAgent defaultAgent() {
        return ReactAgent.builder()
                .name("defaultAgent")
                .model(chatModel)
                .systemPrompt("你是一名可靠的AI助手。")
                .saver(new MemorySaver())
                .hooks(new LoggingHook(), new LimitMessageModelHook())
                .build();
    }

    public ReactAgent weatherQueryAgent() {
        return ReactAgent.builder()
                .name("weatherQueryAgent")
                .model(chatModel)
                .systemPrompt("你是一个专业的天气助手，能够根据用户的问题查询天气信息。" +
                        "当用户询问天气时，请使用提供的天气查询工具获取准确的天气数据，" +
                        "并用友好、简洁的语言回答用户。支持查询的城市有：北京、上海、广州、深圳、杭州、成都、武汉、西安、南京、重庆。")
                .methodTools(weatherService)
                .build();
    }


    public ReactAgent replyForAttackWordAgent() {
        return ReactAgent.builder()
                .name("replyForAttackWordAgent")
                .model(chatModel)
                .systemPrompt("""
                        你是一个群聊管家，积极维护群主和管理员的声誉。
                        ## 你的职责
                        1. 判断群友的消息是否有攻击性质，如果是，则使用阴阳怪气的语气进行回击，否则，你不应该说话
                        ## 规则约束
                        1. 你是一名群友，而不是人机，你的发言不能太有AI味；
                        2. 发言尽量简短，一两句话即可；
                        3. 不要做自主判断，你的所有决策都必须经过工具，如果没有相关工具，直接终止后续流程
                        """)
                .methodTools(groupMessageService)
                .saver(new MemorySaver())
                .build();
    }

    public ReactAgent planModeAgent() {
        return ReactAgent.builder()
                .name("planAgent")
                .model(chatModel)
                .systemPrompt("你是一名可靠的AI助手，负责为将复杂任务进行拆解，得到待办事项列表。")
                .saver(new MemorySaver())
                .interceptors(TodoListInterceptor.builder().build())
                .build();
    }
}
