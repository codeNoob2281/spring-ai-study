<template>
  <div class="chat-container">
    <div class="chat-header">
      <h1>AI 对话流式输出测试</h1>
    </div>

    <div class="chat-messages" ref="messagesContainer">
      <div v-for="(msg, index) in messages" :key="index" :class="['message', msg.role]">
        <div class="message-content">
          <strong>{{ msg.role === 'user' ? '用户:' : 'AI:' }}</strong>
          <p>{{ msg.content }}</p>
        </div>
      </div>
      
      <!-- 流式输出中的消息 -->
      <div v-if="streamingMessage" class="message ai streaming">
        <div class="message-content">
          <strong>AI:</strong>
          <p>{{ streamingMessage }}</p>
          <span class="cursor">|</span>
        </div>
      </div>
    </div>

    <div class="chat-input">
      <input
        v-model="inputMessage"
        @keyup.enter="sendMessage"
        :disabled="isStreaming"
        placeholder="输入消息..."
        type="text"
      />
      <button @click="sendMessage" :disabled="isStreaming || !inputMessage.trim()">
        {{ isStreaming ? '发送中...' : '发送' }}
      </button>
    </div>
  </div>
</template>

<script setup>
import {nextTick, ref} from 'vue'

const inputMessage = ref('')
const messages = ref([])
const streamingMessage = ref('')
const isStreaming = ref(false)
const messagesContainer = ref(null)

const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  })
}

const sendMessage = async () => {
  if (!inputMessage.value.trim() || isStreaming.value) return

  const userMessage = inputMessage.value.trim()
  
  // 添加用户消息
  messages.value.push({
    role: 'user',
    content: userMessage
  })
  
  inputMessage.value = ''
  streamingMessage.value = ''
  isStreaming.value = true
  
  scrollToBottom()

  try {
    // 使用 EventSource 接收 SSE 流
    const eventSource = new EventSource(`/api/chat-client-demo/stream-send-message?message=${encodeURIComponent(userMessage)}`)
    
    eventSource.onmessage = (event) => {
      streamingMessage.value += event.data
      scrollToBottom()
    }
    
    eventSource.onerror = (error) => {
      console.error('SSE Error:', error)
      eventSource.close()
      
      // 将流式消息添加到消息列表
      if (streamingMessage.value) {
        messages.value.push({
          role: 'ai',
          content: streamingMessage.value
        })
        streamingMessage.value = ''
      }
      
      isStreaming.value = false
      scrollToBottom()
    }
    
    // 监听连接关闭
    eventSource.addEventListener('close', () => {
      eventSource.close()
      
      // 将流式消息添加到消息列表
      if (streamingMessage.value) {
        messages.value.push({
          role: 'ai',
          content: streamingMessage.value
        })
        streamingMessage.value = ''
      }
      
      isStreaming.value = false
      scrollToBottom()
    })
    
  } catch (error) {
    console.error('Error sending message:', error)
    isStreaming.value = false
  }
}
</script>

<style scoped>
.chat-container {
  max-width: 1200px;
  width: 90%;
  margin: 0 auto;
  height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f5f5f5;
}

.chat-header {
  background-color: #4CAF50;
  color: white;
  padding: 20px;
  text-align: center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.chat-header h1 {
  margin: 0;
  font-size: 24px;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.message {
  display: flex;
  animation: fadeIn 0.3s ease-in;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.message.user {
  justify-content: flex-end;
}

.message.ai {
  justify-content: flex-start;
}

.message-content {
  max-width: 80%;
  padding: 12px 16px;
  border-radius: 12px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.message.user .message-content {
  background-color: #4CAF50;
  color: white;
}

.message.ai .message-content {
  background-color: white;
  color: #333;
}

.message.streaming .message-content {
  background-color: #e8f5e9;
}

.message-content p {
  margin: 8px 0 0 0;
  line-height: 1.6;
  white-space: pre-wrap;
  word-wrap: break-word;
}

.cursor {
  display: inline-block;
  animation: blink 1s infinite;
  color: #4CAF50;
  font-weight: bold;
}

@keyframes blink {
  0%, 50% {
    opacity: 1;
  }
  51%, 100% {
    opacity: 0;
  }
}

.chat-input {
  display: flex;
  gap: 10px;
  padding: 20px;
  background-color: white;
  box-shadow: 0 -2px 4px rgba(0, 0, 0, 0.1);
}

.chat-input input {
  flex: 1;
  padding: 12px 16px;
  border: 2px solid #ddd;
  border-radius: 8px;
  font-size: 14px;
  outline: none;
  transition: border-color 0.3s;
}

.chat-input input:focus {
  border-color: #4CAF50;
}

.chat-input input:disabled {
  background-color: #f5f5f5;
  cursor: not-allowed;
}

.chat-input button {
  padding: 12px 24px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.chat-input button:hover:not(:disabled) {
  background-color: #45a049;
}

.chat-input button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}
</style>
