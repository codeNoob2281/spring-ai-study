---
name: "mc-plugin-updater"
description: "更新Minecraft服务器插件的完整步骤指南。当用户需要更新MC服务器插件时调用此技能。"
---

# MC服务器插件更新指南

本技能提供更新Minecraft服务器插件的完整步骤。

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

## 前置准备

### 1. 准备插件文件

将新的插件JAR文件放置到本地工作目录：
```
e:\Work\workspace\floyd-backpack-release\Floyd-Backpack-1.0.4.jar
```

### 2. 服务器信息

- **服务器IP**: `$env:MC_GAME_SERVER_HOST`
- **SSH端口**: `$env:MC_GAME_SERVER_PORT`
- **插件目录**: `$env:MC_PLUGIN_DIR`
- **容器名称**: `$env:MC_CONTAINER_NAME`

## 更新步骤

### 步骤1: 查看当前插件目录

```bash
ssh -o StrictHostKeyChecking=no -o PreferredAuthentications=publickey -o PasswordAuthentication=no -i $env:MC_SSH_KEY_PATH -p $env:MC_GAME_SERVER_PORT $env:MC_GAME_SERVER_USER@$env:MC_GAME_SERVER_HOST "ls -la $env:MC_PLUGIN_DIR"
```

### 步骤2: 删除旧版本插件

```bash
ssh -o StrictHostKeyChecking=no -o PreferredAuthentications=publickey -o PasswordAuthentication=no -i $env:MC_SSH_KEY_PATH -p $env:MC_GAME_SERVER_PORT $env:MC_GAME_SERVER_USER@$env:MC_GAME_SERVER_HOST "rm $env:MC_PLUGIN_DIR旧插件名称.jar"
```

### 步骤3: 上传新插件

```bash
scp -o StrictHostKeyChecking=no -o PreferredAuthentications=publickey -o PasswordAuthentication=no -i $env:MC_SSH_KEY_PATH -P $env:MC_GAME_SERVER_PORT e:\Work\workspace\floyd-backpack-release\新插件名称.jar $env:MC_GAME_SERVER_USER@$env:MC_GAME_SERVER_HOST:$env:MC_PLUGIN_DIR
```

### 步骤4: 验证上传结果

```bash
ssh -o StrictHostKeyChecking=no -o PreferredAuthentications=publickey -o PasswordAuthentication=no -i $env:MC_SSH_KEY_PATH -p $env:MC_GAME_SERVER_PORT $env:MC_GAME_SERVER_USER@$env:MC_GAME_SERVER_HOST "ls -la $env:MC_PLUGIN_DIR新插件名称.jar"
```

### 步骤5: 重启MC服务器

```bash
ssh -o StrictHostKeyChecking=no -o PreferredAuthentications=publickey -o PasswordAuthentication=no -i $env:MC_SSH_KEY_PATH -p $env:MC_GAME_SERVER_PORT $env:MC_GAME_SERVER_USER@$env:MC_GAME_SERVER_HOST "docker restart $env:MC_CONTAINER_NAME"
```

### 步骤6: 确认服务器启动成功

```bash
ssh -o StrictHostKeyChecking=no -o PreferredAuthentications=publickey -o PasswordAuthentication=no -i $env:MC_SSH_KEY_PATH -p $env:MC_GAME_SERVER_PORT $env:MC_GAME_SERVER_USER@$env:MC_GAME_SERVER_HOST "docker ps | grep $env:MC_CONTAINER_NAME"
```

## 示例: 更新Floyd-Backpack插件

```bash
# 删除旧版本
ssh -o StrictHostKeyChecking=no -o PreferredAuthentications=publickey -o PasswordAuthentication=no -i $env:MC_SSH_KEY_PATH -p $env:MC_GAME_SERVER_PORT $env:MC_GAME_SERVER_USER@$env:MC_GAME_SERVER_HOST "rm $env:MC_PLUGIN_DIR/Floyd-Backpack-1.0.3.jar"

# 上传新版本
scp -o StrictHostKeyChecking=no -o PreferredAuthentications=publickey -o PasswordAuthentication=no -i $env:MC_SSH_KEY_PATH -P $env:MC_GAME_SERVER_PORT e:\Work\workspace\floyd-backpack-release\Floyd-Backpack-1.0.4.jar $env:MC_GAME_SERVER_USER@$env:MC_GAME_SERVER_HOST:$env:MC_PLUGIN_DIR

# 重启服务器
ssh -o StrictHostKeyChecking=no -o PreferredAuthentications=publickey -o PasswordAuthentication=no -i $env:MC_SSH_KEY_PATH -p $env:MC_GAME_SERVER_PORT $env:MC_GAME_SERVER_USER@$env:MC_GAME_SERVER_HOST "docker restart $env:MC_CONTAINER_NAME"
```

## 注意事项

1. **备份**: 在更新插件前建议备份旧版本插件和相关配置文件
2. **兼容性**: 确保新插件版本与服务器版本兼容
3. **配置迁移**: 如果插件有配置文件，注意迁移或合并配置
4. **重启影响**: 重启服务器会断开所有在线玩家，请提前通知
5. **日志检查**: 更新后可查看服务器日志确认插件加载状态

## 故障排查

### 上传失败
- 检查本地文件路径是否正确
- 确认SSH密钥文件存在: `$env:MC_SSH_KEY_PATH`
- 验证网络连接: `Test-NetConnection -ComputerName $env:MC_GAME_SERVER_HOST -Port $env:MC_GAME_SERVER_PORT`

### 插件未加载
- 检查插件文件权限: `chmod 644 $env:MC_PLUGIN_DIR/插件名称.jar`
- 查看服务器日志: `docker logs $env:MC_CONTAINER_NAME`
- 确认插件与服务端版本兼容

### 服务器启动失败
- 查看容器日志排查错误
- 移除有问题的插件后重启
- 检查插件依赖是否完整

### 环境变量未加载
1. 确认 `.trae/secrets/mc.env` 文件存在
2. 检查环境变量是否正确加载: `$env:MC_CONTAINER_NAME`
3. 重新执行环境变量加载命令
