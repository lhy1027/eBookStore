<template>
  <div class="container">
    <h2>我的留言</h2>
    <el-input v-model="content" placeholder="请输入留言内容" type="textarea" :rows="3" style="margin-bottom: 10px;" />
    <el-button type="primary" @click="handleAdd" style="margin-bottom: 20px;">提交留言</el-button>

    <div v-for="m in messages" :key="m.id" class="message-item">
      <p class="msg-content">{{ m.content }}</p>
      <p class="msg-time">{{ m.createTime }}</p>
      <div v-if="m.reply" class="reply-box">
        <p><strong>管理员回复：</strong>{{ m.reply }}</p>
        <p class="msg-time">{{ m.replyTime }}</p>
      </div>
    </div>
    <div v-if="messages.length === 0" style="text-align:center;padding:40px;color:#999;">暂无留言</div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getMessages, addMessage } from '@/api/client/message';
import { ElMessage } from 'element-plus';

const messages = ref([]);
const content = ref('');

onMounted(() => fetchMessages());
async function fetchMessages() {
  const res = await getMessages();
  messages.value = res.data || [];
}

async function handleAdd() {
  if (!content.value.trim()) { ElMessage.warning('请输入留言内容'); return; }
  await addMessage(content.value);
  ElMessage.success('留言成功');
  content.value = '';
  fetchMessages();
}
</script>

<style scoped>
.container { max-width: 1200px; margin: 0 auto; padding: 20px; }
h2 { margin-bottom: 20px; }
.message-item { border-bottom: 1px solid #eee; padding: 12px 0; }
.msg-content { margin-bottom: 4px; }
.msg-time { color: #999; font-size: 12px; }
.reply-box { background: #f5f7fa; padding: 10px; margin-top: 8px; border-radius: 4px; }
</style>