<template>
  <div>
    <h2>评价管理</h2>
    <el-table :data="reviews" v-loading="loading">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="username" label="用户" width="100" />
      <el-table-column prop="rating" label="评分" width="150">
        <template #default="{ row }"><el-rate :model-value="row.rating" disabled size="small" /></template>
      </el-table-column>
      <el-table-column prop="content" label="内容" min-width="200" />
      <el-table-column label="时间" width="160">
        <template #default="{ row }">{{ formatDate(row.createdAt) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="80">
        <template #default="{ row }">
          <el-button size="small" type="danger" @click="del(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getReviews, adminDeleteReview } from '../../api/review'
import { formatDate } from '../../utils/format'

const reviews = ref([])
const loading = ref(false)

onMounted(async () => {
  loading.value = true
  try {
    // 简单方案：直接获取所有评价（实际应做分页）
    const data = await getReviews(0, 1, 200)
    reviews.value = data.records
  } finally { loading.value = false }
})

async function del(id) {
  await ElMessageBox.confirm('确定删除？', '提示', { type: 'warning' })
  await adminDeleteReview(id)
  ElMessage.success('已删除')
  reviews.value = reviews.value.filter(r => r.id !== id)
}
</script>
