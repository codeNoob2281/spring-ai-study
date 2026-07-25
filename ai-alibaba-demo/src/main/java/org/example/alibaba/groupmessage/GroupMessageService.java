package org.example.alibaba.groupmessage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 *
 * @author <a href="mailto:huangzf1778@fingard.com">huang zifan</a>
 * @version 1.0
 * @projectName: spring-ai-study
 * @date 2026/7/25
 */
@Slf4j
@Service
public class GroupMessageService {

    public static final List<String> ATTACK_WORDS = List.of("sb", "250", "fw");
    public static final List<String> POINT_TO_ADMIN_WORDS = List.of("群主", "管理", "admin");

    public static final String NO_REPLY_MESSAGE = "NO_REPLY";

    @Tool
    public boolean judgeAsAttackAdmin(String message) {
        boolean hasAttachWord = ATTACK_WORDS.stream().anyMatch(message::contains);
        boolean pointToAdmin = POINT_TO_ADMIN_WORDS.stream().anyMatch(message::contains);
        log.info("判断消息[{}]是否具备攻击性质，判定结果：hasAttackWord={},pointToAdmin={}", message, hasAttachWord, pointToAdmin);
        return hasAttachWord && pointToAdmin;
    }

    @Tool
    public void sendGroupMessage(@ToolParam(description = "你想说的话，如果没有想说的，就输入NO_REPLY") String replyMsg) {
        if (replyMsg == null || replyMsg.isBlank() || NO_REPLY_MESSAGE.equals(replyMsg)) {
            log.info("消息[{}]为空或NO_REPLY，跳过发送", NO_REPLY_MESSAGE);
            return;
        }
        log.info("模拟发送群消息：{}", replyMsg);
    }

}
