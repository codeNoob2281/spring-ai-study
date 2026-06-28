package org.example.ai.skill;

import java.io.IOException;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springaicommunity.agent.tools.SkillsTool;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

/**
 * 技能管理器 - 启动时从类路径加载所有 SKILL.md 文件并缓存
 *
 * @author floyd
 */
@Slf4j
@Component
public class SkillManager {

    private static final String SKILL_PATTERN = "classpath*:skills/*";

    @Resource
    private ResourcePatternResolver resourcePatternResolver;

    @Getter
    private ToolCallback toolCallback;


    @PostConstruct
    public void init() {
        loadSkills();
    }

    /**
     * 从类路径扫描并加载所有技能
     */
    private void loadSkills() {
        try {
            org.springframework.core.io.Resource[] resources = resourcePatternResolver.getResources(SKILL_PATTERN);
            SkillsTool.Builder builder = SkillsTool.builder();
            for (org.springframework.core.io.Resource resource : resources) {
                builder.addSkillsResource(resource);
            }
            this.toolCallback = builder.build();
            log.info("成功加载 {} 个技能: {}", resources.length, this.toolCallback);
        } catch (IOException e) {
            log.error("扫描技能资源失败", e);
        }
    }

}
