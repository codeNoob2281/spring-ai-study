package org.example.ai.prompt;

import lombok.Getter;

/**
 * @author floyd
 */
public class MarkdownPrompt {

    public static final String MD_PREV_PROMPT = "你现在是专业的 Markdown 解析器。\n" +
            "请严格按照 Markdown 语法识别：标题层级、列表、表格、代码块、加粗、链接。\n" +
            "不要遗漏任何内容，保持结构完整。\n" +
            "如果是文档，请输出结构化内容；如果是问题，请基于完整文档回答。\n" +
            "—————————— 以下是 Markdown 内容 ——————————\n";

    @Getter
    private final String rawContent;

    public MarkdownPrompt(String rawContent) {
        this.rawContent = rawContent;
    }

    public String getContent() {
        return MD_PREV_PROMPT + rawContent;
    }
}
