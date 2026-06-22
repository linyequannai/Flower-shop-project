<template>
  <div>
    <h2>订单管理</h2>
    <el-table :data="orders" v-loading="loading">
      <el-table-column prop="orderNo" label="订单号" width="200" />
      <el-table-column prop="username" label="用户" width="100" />
      <el-table-column prop="payAmount" label="金额" width="100">
        <template #default="{ row }">¥{{ row.payAmount }}</template>
      </el-table-column>
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="getOrderStatusType(row.status)">{{ getOrderStatusText(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="时间" width="160">
        <template #default="{ row }">{{ formatDate(row.createdAt) }}</template>
      </el-table-column>
      <el-table-column label="操作" min-width="200">
        <template #default="{ row }">
          <el-button size="small" @click="$router.push('/admin/orders/'+row.id)">详情</el-button>
          <el-button v-if="row.status === 0" size="small" type="success" @click="ship(row.id)">发货</el-button>
          <el-button v-if="row.status === 1" size="small" type="success" @click="ship(row.id)">发货</el-button>
          <el-button v-if="row.status === 1 || row.status === 2" size="small" type="primary" @click="updateStatus(row.id, 3)">完成</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getAdminOrders, shipOrder, updateOrderStatus } from '../../api/order'
import { formatDate, getOrderStatusText, getOrderStatusType } from '../../utils/format'

const orders = ref([])
const loading = ref(false)

onMounted(() => fetchData())

async function fetchData() {
  loading.value = true
  try { const data = await getAdminOrders({ page: 1, size: 50 }); orders.value = data.records } finally { loading.value = false }
}

async function ship(id) { await shipOrder(id); ElMessage.success('已发货'); fetchData() }
async function updateStatus(id, status) { await updateOrderStatus(id, status); ElMessage.success('状态已更新'); fetchData() }
</script>
