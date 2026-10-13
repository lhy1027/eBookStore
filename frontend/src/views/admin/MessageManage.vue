<template>
  <div>
    <h2>留言管理</h2>
    <el-table :data="messages" border stripe>
      <el-table-column prop="username" label="用户" width="120" />
      <el-table-column prop="content" label="留言内容" />
      <el-table-column prop="reply" label="回复" />
      <el-table-column prop="createTime" label="时间" width="180" />
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-button size="small" @click="showReplyDialog(row)">回复</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <Pagination v-model="page" :total="total" @change="fetchMessages" />

    <el-dialog v-model="replyDialogVisible" title="回复留言" width="400px">
      <el-input v-model="replyContent" type="textarea" :rows="3" placeholder="请输入回复内容" />
      <template #footer>
        <el-button @click="replyDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleReply">回复</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getMessages, replyMessage, deleteMessage } from '@/api/admin/adminBook';
import { ElMessage } from 'element-plus';
import Pagination from '@/components/Pagination.vue';

const messages = ref([]);
const page = ref(1);
const total = ref(0);
const replyDialogVisible = ref(false);
const replyContent = ref('');
let replyId = null;

onMounted(() => fetchMessages());
async function fetchMessages() {
  const res = await getMessages({ page: page.value, size: 10 });
  messages.value = res.data?.records || [];
  total.value = res.data?.total || 0;
}

function showReplyDialog(row) { replyId = row.id; replyContent.value = ''; replyDialogVisible.value = true; }
async function handleReply() {
  await replyMessage(replyId, replyContent.value);
  ElMessage.success('回复成功');
  replyDialogVisible.value = false;
  fetchMessages();
}

async function handleDelete(id) {
  await deleteMessage(id);
  ElMessage.success('删除成功');
  fetchMessages();
}
</script>