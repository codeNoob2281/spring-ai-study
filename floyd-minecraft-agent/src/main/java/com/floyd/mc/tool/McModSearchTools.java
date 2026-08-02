package com.floyd.mc.tool;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 我的世界模组查找工具
 * 调用 UAPI 接口搜索 Minecraft模组/插件（聚合 Modrinth 与 SpigotMC）
 *
 * @author floyd
 */
@Component
public class McModSearchTools {

    private static final Logger log = LoggerFactory.getLogger(McModSearchTools.class);

    private static final String API_URL = "https://uapis.cn/api/v1/game/minecraft/mods";

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(300, TimeUnit.SECONDS)
            .readTimeout(300, TimeUnit.SECONDS)
            .writeTimeout(300, TimeUnit.SECONDS)
            .build();

    @Value("${minecraft.api.key:}")
    private String apiKey;

    /**
     * 搜索 Minecraft 模组/插件
     *
     * @param query 搜索关键词，例如 sodium、jei、optifine
     * @return 搜索结果摘要
     */
    @Tool(description = "搜索 Minecraft 模组(Mod)或插件(Plugin)。可以按关键词搜索，结果包含模组名称、简介、作者、下载量、项目页和下载地址。数据来源包括 Modrinth 和 SpigotMC。")
    public String searchMods(@ToolParam(description = "搜索关键词，例如 sodium、jei、optifine（注意必须使用英文搜索，且关键字中间不能有空格等）") String query) {
        log.info("进行模组检索，关键字：{}", query);
        if (query == null || query.isBlank()) {
            log.error("搜索关键词不能为空");
            return "搜索关键词不能为空";
        }

        if (apiKey == null || apiKey.isBlank()) {
            log.error("未配置 UAPI 密钥，请在 application.yml 中设置 minecraft.api.key 环境变量");
            return "检索失败，未配置 UAPI 密钥";
        }

        try {
            HttpUrl.Builder urlBuilder = HttpUrl.parse(API_URL).newBuilder()
                    .addQueryParameter("query", query.trim())
                    .addQueryParameter("source", "all")
                    .addQueryParameter("limit", "10")
                    .addQueryParameter("enrich", "true");

            HttpUrl url = urlBuilder.build();
            Request request = new Request.Builder()
                    .url(url)
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Accept", "application/json")
                    .get()
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                String searchResult = handleResponse(response);

                log.info("返回mod检索结果：\n{}", searchResult);
                return searchResult;
            }
        } catch (IOException e) {
            log.error("搜索MC模组网络异常: query={}", query, e);
            return "搜索服务暂时不可用，请稍后重试";
        } catch (Exception e) {
            log.error("搜索MC模组异常: query={}", query, e);
            return "搜索过程中发生错误: " + e.getMessage();
        }
    }

    private String handleResponse(Response response) throws IOException {
        int code = response.code();

        if (code == 200) {
            ResponseBody body = response.body();
            if (body == null) {
                return "搜索返回为空";
            }
            String json = body.string();
            return parseAndFormatResults(json);
        }

        // 处理文档中列出的错误码
        String message;
        switch (code) {
            case 400 -> message = "搜索关键词缺失，请提供有效的搜索词";
            case 429 -> message = "请求过于频繁，请稍后重试";
            case 502 -> message = "搜索服务暂时不可用，请稍后重试";
            default -> {
                log.error("搜索MC模组接口返回非预期状态码: {}", code);
                message = "搜索服务异常（状态码: " + code + "），请稍后重试";
            }
        }
        return message;
    }

    private String parseAndFormatResults(String json) {
        try {
            ModSearchResponse searchResponse = objectMapper.readValue(json, ModSearchResponse.class);
            List<ModResult> results = searchResponse.results();

            if (results == null || results.isEmpty()) {
                return "未找到与「" + searchResponse.query() + "」相关的模组";
            }

            StringBuilder sb = new StringBuilder();
            sb.append("共找到 ").append(results.size()).append(" 个相关模组：\n\n");

            for (int i = 0; i < results.size(); i++) {
                ModResult mod = results.get(i);
                sb.append("**").append(i + 1).append(". ").append(mod.name()).append("**\n");
                sb.append("- 来源：").append(mod.source() != null ? mod.source() : "未知").append("\n");
                sb.append("- 作者：").append(mod.author() != null ? mod.author() : "未知").append("\n");
                if (mod.downloads() != null) {
                    sb.append("- 下载量：").append(formatDownloads(mod.downloads())).append("\n");
                }
                if (mod.description() != null && !mod.description().isBlank()) {
                    sb.append("- 简介：").append(mod.description()).append("\n");
                }
                if (mod.gameVersions() != null) {
                    sb.append("- 游戏版本：").append(String.join(",", mod.gameVersions())).append("\n");
                }
                if (mod.url() != null) {
                    sb.append("- 项目页：").append(mod.url()).append("\n");
                }
                if (mod.downloadUrl() != null) {
                    sb.append("- 下载地址：").append(mod.downloadUrl()).append("\n");
                }
                sb.append("\n");
            }

            return sb.toString();
        } catch (Exception e) {
            log.error("解析搜索结果异常", e);
            return "搜索结果解析失败: " + e.getMessage();
        }
    }

    private String formatDownloads(long downloads) {
        if (downloads >= 1_000_000) {
            return String.format("%.1fM", downloads / 1_000_000.0);
        } else if (downloads >= 1_000) {
            return String.format("%.1fK", downloads / 1_000.0);
        }
        return String.valueOf(downloads);
    }

    // ---- 响应模型 ----

    private record ModSearchResponse(
            String query,
            String source,
            Integer count,
            List<ModResult> results
    ) {
    }

    private record ModResult(
            String source,
            String name,
            String description,
            String author,
            Long downloads,
            String url,
            @JsonProperty("download_url") String downloadUrl,
            @JsonProperty("game_versions") List<String> gameVersions
    ) {
    }
}
