---
name: "minecraft-server-manager"
description: "管理Minecraft Java版服务器，包括使用RCON查看TPS、重启服务器、查看日志和在线玩家。Invoke when user wants to manage or check Minecraft server status."
---

# Minecraft Server Manager

本技能用于管理 Minecraft Java 版服务器，提供常用的服务器管理操作。

## 环境变量配置

本技能使用环境变量管理敏感信息，执行前需加载配置：

```powershell
# 加载环境变量
Get-Content "$env:WORKSPACE\.trae\secrets\mc.env" | ForEach-Object {
    if ($_ -match '^([^#][^=]+)=(.*)$') {
        [Environment]::SetEnvironmentVariable($matches[1].Trim(), $matches[2].Trim(), "Process")
    }
}
```

## 服务器信息

- **容器名称**: `$env:MC_CONTAINER_NAME`
- **RCON端口**: `$env:MC_RCON_PORT`
- **游戏端口**: 25565
- **RCON密码**: `$env:MC_RCON_PASSWORD`

## 远程连接（通过SSH）

当需要远程执行命令时，使用以下SSH连接：
```bash
ssh -o StrictHostKeyChecking=no -o PreferredAuthentications=publickey -o PasswordAuthentication=no -i $env:MC_SSH_KEY_PATH -p $env:MC_GAME_SERVER_PORT $env:MC_GAME_SERVER_USER@$env:MC_GAME_SERVER_HOST
```

## RCON 基本用法

所有 MC 游戏命令使用以下格式：
```bash
mcrcon -H $env:MC_RCON_HOST -P $env:MC_RCON_PORT -p "$env:MC_RCON_PASSWORD" "你的命令"
```

## 常用操作

### 1. 查看服务器TPS
```bash
mcrcon -H $env:MC_RCON_HOST -P $env:MC_RCON_PORT -p "$env:MC_RCON_PASSWORD" "tps"
```

### 2. 查看在线玩家
```bash
mcrcon -H $env:MC_RCON_HOST -P $env:MC_RCON_PORT -p "$env:MC_RCON_PASSWORD" "list"
```

### 3. 查看服务器日志
```bash
docker logs $env:MC_CONTAINER_NAME --tail 100
```

### 4. 实时查看日志
```bash
docker logs -f $env:MC_CONTAINER_NAME
```

### 5. 重启服务器
```bash
docker restart $env:MC_CONTAINER_NAME
```

### 6. 查看服务器状态
```bash
docker ps | grep $env:MC_CONTAINER_NAME
```

## 常用MC命令（通过RCON执行）

```bash
# 查看玩家列表
mcrcon -H $env:MC_RCON_HOST -P $env:MC_RCON_PORT -p "$env:MC_RCON_PASSWORD" "list"

# 查看服务器TPS
mcrcon -H $env:MC_RCON_HOST -P $env:MC_RCON_PORT -p "$env:MC_RCON_PASSWORD" "tps"

# 发送服务器消息
mcrcon -H $env:MC_RCON_HOST -P $env:MC_RCON_PORT -p "$env:MC_RCON_PASSWORD" "say 消息内容"

# 查看服务器性能
mcrcon -H $env:MC_RCON_HOST -P $env:MC_RCON_PORT -p "$env:MC_RCON_PASSWORD" "timings report"

# 保存世界
mcrcon -H $env:MC_RCON_HOST -P $env:MC_RCON_PORT -p "$env:MC_RCON_PASSWORD" "save-all"

# 关闭服务器（带警告）
mcrcon -H $env:MC_RCON_HOST -P $env:MC_RCON_PORT -p "$env:MC_RCON_PASSWORD" "stop"
```

## TPS说明

Minecraft服务器的理想TPS是20.0。当TPS低于20时，表示服务器可能存在性能问题：
- **20.0**: 正常运行
- **18-20**: 轻微卡顿
- **15-18**: 明显卡顿
- **<15**: 严重卡顿

## 故障排查

### Minecraft服务器异常
1. 查看容器状态: `docker ps -a | grep mc`
2. 检查日志: `docker logs $env:MC_CONTAINER_NAME --tail 50`
3. 重启服务: `docker restart $env:MC_CONTAINER_NAME`
4. 检查资源: `docker stats $env:MC_CONTAINER_NAME`

### RCON连接失败
1. 确认服务器容器运行中: `docker ps | grep $env:MC_CONTAINER_NAME`
2. 检查RCON端口是否映射: `docker port $env:MC_CONTAINER_NAME`
3. 查看服务器日志: `docker logs $env:MC_CONTAINER_NAME --tail 20`

### 玩家无法连接
1. 检查服务器是否满: `mcrcon -H $env:MC_RCON_HOST -P $env:MC_RCON_PORT -p "$env:MC_RCON_PASSWORD" "list"`
2. 检查服务器人数限制配置
3. 查看服务器日志中的连接错误

### 环境变量未加载
1. 确认 `.trae/secrets/mc.env` 文件存在
2. 检查环境变量是否正确加载: `$env:MC_RCON_PASSWORD`
3. 重新执行环境变量加载命令

## 注意事项

- 确保服务器容器正在运行才能执行RCON命令
- 重启服务器会踢掉所有在线玩家
- 敏感信息存储在 `.trae/secrets/mc.env` 中，已通过 .gitignore 排除
