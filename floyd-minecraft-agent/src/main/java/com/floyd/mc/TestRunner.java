package com.floyd.mc;

import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.floyd.mc.agent.AgentRegistry;
import jakarta.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author floyd
 */
@Component
public class TestRunner implements CommandLineRunner {

    @Resource
    AgentRegistry agentRegistry;


    @Override
    public void run(String... args) throws Exception {
        ReactAgent modMasterAgent = agentRegistry.modMasterAgent();
        System.out.println(modMasterAgent.call("帮我找一下和魔法相关的模组，需要支持1.21.1版本"));

    }
}
