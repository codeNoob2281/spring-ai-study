# Spring AI Demo 项目

一个基于 Spring AI 和 Vue3 的全栈 AI 对话应用，支持流式输出功能。

## 项目简介

本项目演示了如何使用 Spring AI 集成 OpenAI 兼容的 API，并提供了一个现代化的前端界面来测试 AI 对话的流式输出功能。

### 主要特性

- 🤖 **AI 对话**：集成 OpenAI 兼容的聊天模型
- ⚡ **流式输出**：支持 Server-Sent Events (SSE) 实时流式响应
- 🛠️ **Function Calling**：支持工具调用（天气查询、机票预订等）
- 📝 **Prompt 模板**：支持 Markdown 格式的提示词管理
- 🎨 **现代化 UI**：基于 Vue3 + Vite 的响应式前端界面
- 🔄 **多模型支持**：可配置不同的 AI 模型

## 技术栈

### 后端
- **Spring Boot 3.4.13** - Java 后端框架
- **Spring AI 1.1.4** - Spring AI 集成框架
- **OpenAI API** - AI 模型接口（兼容火山引擎豆包）
- **Java 17** - 运行环境
- **Lombok** - 简化 Java 代码
- **Maven** - 项目构建工具

### 前端
- **Vue 3** - 渐进式 JavaScript 框架
- **Vite 5** - 下一代前端构建工具
- **EventSource** - SSE 流式数据接收

## 项目结构

```
spring-ai-demo/
├── src/main/java/org/example/ai/
│   ├── config/
│   │   └── OpenAiConfig.java          # OpenAI 配置类
│   ├── controller/
│   │   ├── ChatController.java        # 基础聊天控制器
│   │   └── ChatClientDemoController.java  # ChatClient 示例控制器
│   ├── entity/
│   │   ├── AirTicket.java             # 机票实体
│   │   ├── BookParam.java             # 预订参数
│   │   └── CityWeatherInfo.java       # 城市天气信息
│   ├── model/
│   │   └── MultiChatModelService.java # 多模型服务
│   ├── prompt/
│   │   └── MarkdownPrompt.java        # Markdown 提示词处理
│   ├── tool/
│   │   ├── AirTicketService.java      # 机票服务（Function Calling）
│   │   └── WeatherService.java        # 天气服务（Function Calling）
│   └── SpringAiDemoApplication.java   # 应用启动类
├── src/main/resources/
│   ├── assistant/
│   │   ├── bootstrap.md               # 助手引导提示词
│   │   └── soul.md                    # 灵魂提示词
│   ├── application.properties         # 主配置文件
│   └── application-dev.properties     # 开发环境配置
├── web/                                # Vue3 前端项目
│   ├── src/
│   │   ├── assets/
│   │   │   └── main.css               # 全局样式
│   │   ├── App.vue                    # 主组件（聊天界面）
│   │   ├── main.ts                    # 应用入口
│   │   └── env.d.ts                   # TypeScript 声明
│   ├── index.html                     # HTML 入口
│   ├── vite.config.ts                 # Vite 配置
│   ├── package.json                   # 前端依赖
│   └── tsconfig.json                  # TypeScript 配置
├── pom.xml                             # Maven 配置
└── README.md                           # 项目说明文档
```

## 前置要求

- **JDK 17+**
- **Maven 3.6+**
- **Node.js 16+**
- **npm 或 yarn**
- **OpenAI API Key**（或兼容的 API，如火山引擎豆包）

## 快速开始

### 1. 后端配置

#### 配置 API Key

编辑 `src/main/resources/application.properties` 文件：

```properties
# 替换为你的 API Key
spring.ai.openai.api-key=your-api-key-here

# 如果使用其他兼容 OpenAI 的 API，修改以下配置
spring.ai.openai.base-url=https://ark.cn-beijing.volces.com/api
spring.ai.openai.chat.options.model=doubao-seed-2-0-pro-260215
spring.ai.openai.chat.completions-path=/v3/chat/completions
```

#### 启动后端服务

```bash
# 进入项目根目录
cd spring-ai-demo

# 使用 Maven 启动
mvn spring-boot:run

# 或者先打包再运行
mvn clean package
java -jar target/spring-ai-demo-0.0.1-SNAPSHOT.jar
```

后端服务将在 `http://localhost:8080` 启动。

### 2. 前端配置

#### 安装依赖

```bash
# 进入前端目录
cd web

# 安装依赖
npm install
```

#### 启动前端开发服务器

```bash
npm run dev
```

前端开发服务器将在 `http://localhost:3000` 启动。

### 3. 访问应用

打开浏览器访问 `http://localhost:3000`，即可开始体验 AI 对话功能。

## API 接口

### 流式对话接口

```
GET /stream-send-message?message=你的问题
```

**响应类型**：`text/event-stream` (SSE)

**示例**：
```javascript
const eventSource = new EventSource('/stream-send-message?message=你好');

eventSource.onmessage = (event) => {
  console.log('收到流式数据:', event.data);
};
```

### 其他接口

项目中还包含其他演示接口，详见 `ChatController` 和 `ChatClientDemoController`。

## 功能演示

### Function Calling

项目实现了两个 Function Calling 示例：

1. **天气查询** (`WeatherService`)
   - 可以查询指定城市的天气信息
   
2. **机票预订** (`AirTicketService`)
   - 可以查询和预订机票

### Prompt 管理

支持从 Markdown 文件加载提示词模板：
- `bootstrap.md` - 助手初始化提示词
- `soul.md` - 助手角色设定

## 开发指南

### 后端开发

#### 添加新的 AI 功能

1. 在 `controller` 包中创建新的 Controller
2. 注入 `ChatClient` 或 `ChatModel`
3. 实现业务逻辑

示例：
```java
@RestController
public class MyController {
    
    private final ChatClient chatClient;
    
    public MyController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }
    
    @GetMapping("/chat")
    public String chat(String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }
}
```

#### 添加 Function Calling

1. 创建 Service 类
2. 使用 `@Description` 注解描述方法功能
3. 在 ChatClient 中注册函数

### 前端开发

#### 修改聊天界面

编辑 `web/src/App.vue` 文件，可以自定义：
- 界面样式
- 消息显示方式
- 输入框行为

#### 添加新功能

1. 在 `src` 目录下创建新的组件
2. 在 `App.vue` 中引入和使用
3. 通过 Vite 代理访问后端 API

## 配置说明

### 后端配置项

| 配置项 | 说明 | 默认值 |
|--------|------|--------|
| `spring.ai.openai.api-key` | API Key | 必填 |
| `spring.ai.openai.base-url` | API 基础 URL | - |
| `spring.ai.openai.chat.options.model` | 模型名称 | - |
| `spring.ai.openai.chat.completions-path` | 完成路径 | `/v1/chat/completions` |
| `spring.ai.retry.max-attempts` | 最大重试次数 | 3 |

### 前端配置

Vite 代理配置在 `web/vite.config.ts`：

```typescript
server: {
  port: 3000,
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true,
      rewrite: (path) => path.replace(/^\/api/, '')
    }
  }
}
```

## 常见问题

### 1. API Key 错误

确保在 `application.properties` 中配置了正确的 API Key。

### 2. 跨域问题

前端开发时通过 Vite 代理解决跨域问题。生产环境需要配置 CORS。

### 3. 流式输出不工作

- 检查后端接口是否返回 `text/event-stream`
- 确认前端使用 `EventSource` 接收数据
- 查看浏览器控制台是否有错误

### 4. 依赖下载失败

配置 Maven 镜像或使用项目中的 Spring Snapshots 仓库。

## 许可证

本项目仅用于学习和演示目的。

## 参考资料

- [Spring AI 官方文档](https://spring.io/projects/spring-ai)
- [Vue 3 官方文档](https://vuejs.org/)
- [Vite 官方文档](https://vitejs.dev/)
- [OpenAI API 文档](https://platform.openai.com/docs)

## 贡献

欢迎提交 Issue 和 Pull Request！

---

**注意**：本项目是一个演示项目，生产环境使用时需要考虑安全性、性能优化、错误处理等因素。
