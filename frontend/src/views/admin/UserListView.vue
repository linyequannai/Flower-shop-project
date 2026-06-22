<template>
  <div>
    <h2>用户管理</h2>
    <el-table :data="users" v-loading="loading">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="nickname" label="昵称" />
      <el-table-column prop="email" label="邮箱" />
      <el-table-column prop="phone" label="手机号" />
      <el-table-column label="角色" width="80">
        <template #default="{ row }"><el-tag :type="row.role === 1 ? 'danger' : ''">{{ row.role === 1 ? '管理员' : '用户' }}</el-tag></template>
      </el-table-column>
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-switch :model-value="row.status === 1" @change="toggleStatus(row)" />
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getUserList, updateUserStatus } from '../../api/user'

const users = ref([])
const loading = ref(false)

onMounted(() => fetchUsers())

async function fetchUsers() {
  loading.value = true
  try {
    const data = await getUserList(1, 100)
    users.value = data.records
  } finally { loading.value = false }
}

async function toggleStatus(row) {
  const newStatus = row.status === 1 ? 0 : 1
  await updateUserStatus(row.id, newStatus)
  row.status = newStatus
  ElMessage.success(newStatus === 1 ? '已启用' : '已禁用')
}
</script>
