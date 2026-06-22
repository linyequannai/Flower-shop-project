<template>
  <div>
    <h2>花卉管理</h2>
    <el-button type="primary" style="margin-bottom:16px" @click="$router.push('/admin/flowers/add')">新增花卉</el-button>
    <el-table :data="flowers" v-loading="loading">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column label="图片" width="80">
        <template #default="{ row }"><img :src="row.coverImage" style="width:50px;height:50px;object-fit:cover;border-radius:4px" /></template>
      </el-table-column>
      <el-table-column prop="name" label="名称" />
      <el-table-column prop="categoryName" label="分类" width="100" />
      <el-table-column prop="price" label="价格" width="80">
        <template #default="{ row }">¥{{ row.price }}</template>
      </el-table-column>
      <el-table-column prop="stock" label="库存" width="60" />
      <el-table-column prop="sales" label="销量" width="60" />
      <el-table-column label="状态" width="80">
        <template #default="{ row }">
          <el-switch :model-value="row.status === 1" @change="toggleStatus(row)" />
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button size="small" @click="$router.push('/admin/flowers/'+row.id+'/edit')">编辑</el-button>
          <el-button size="small" type="danger" @click="del(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getFlowers, updateFlowerStatus, deleteFlower } from '../../api/flower'

const flowers = ref([])
const loading = ref(false)

onMounted(() => fetchData())

async function fetchData() {
  loading.value = true
  try { const data = await getFlowers({ page: 1, size: 200 }); flowers.value = data.records } finally { loading.value = false }
}

async function toggleStatus(row) {
  const newStatus = row.status === 1 ? 0 : 1
  await updateFlowerStatus(row.id, newStatus)
  row.status = newStatus
  ElMessage.success(newStatus === 1 ? '已上架' : '已下架')
}

async function del(id) {
  await ElMessageBox.confirm('确定删除？', '提示', { type: 'warning' })
  await deleteFlower(id)
  ElMessage.success('已删除')
  fetchData()
}
</script>
