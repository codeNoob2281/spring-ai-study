package org.example.ai.mcp;

import lombok.extern.slf4j.Slf4j;


import org.springaicommunity.mcp.annotation.McpTool;
import org.springframework.stereotype.Service;

/**
 * @author floyd
 */
@Service
@Slf4j
public class MathDemoMcpService {

    @McpTool(description = "Add two numbers together")
    public Integer add(Integer a, Integer b) {
        log.info("add: {} + {} = {}", a, b, a + b);
        return a + b;
    }

    @McpTool(description = "Multiply two numbers together")
    public Integer multi(Integer a, Integer b) {
        log.info("multi: {} * {} = {}", a, b, a * b);
        return a * b;
    }

}
