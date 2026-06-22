<template>
  <div>
    <AppHeader />
    <div class="page-container">
      <h1 class="page-title">我的订单</h1>
      <el-table :data="orders" v-loading="loading" style="width:100%">
        <el-table-column prop="orderNo" label="订单号" width="200" />
        <el-table-column label="商品" min-width="250">
          <template #default="{ row }">
            <span v-for="item in row.items" :key="item.id" style="margin-right:8px">
              {{ item.flowerName }} x{{ item.quantity }}
            </span>
          </template>
        </el-table-column>
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
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button size="small" @click="$router.push('/orders/'+row.id)">详情</el-button>
            <el-button v-if="row.status === 0" size="small" type="danger" @click="cancelOrder(row.id)">取消</el-button>
            <el-button v-if="row.status === 2" size="small" type="success" @click="confirmOrder(row.id)">确认收货</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-if="!loading && orders.length === 0" description="还没有订单">
        <el-button type="primary" @click="$router.push('/flowers')">去逛逛</el-button>
      </el-empty>
    </div>
    <AppFooter />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import AppHeader from '../components/AppHeader.vue'
import AppFooter from '../components/AppFooter.vue'
import { getUserOrders, cancelOrder as cancelOrderAPI, confirmOrder as confirmOrderAPI } from '../api/order'
import { formatDate, getOrderStatusText, getOrderStatusType } from '../utils/format'

const orders = ref([])
const loading = ref(false)

onMounted(() => fetchOrders())

async function fetchOrders() {
  loading.value = true
  try {
    const data = await getUserOrders({ page: 1, size: 50 })
    orders.value = data.records
  } finally { loading.value = false }
}

async function cancelOrder(id) {
  await ElMessageBox.confirm('确定取消该订单吗？', '提示', { type: 'warning' })
  await cancelOrderAPI(id)
  ElMessage.success('已取消')
  fetchOrders()
}

async function confirmOrder(id) {
  await ElMessageBox.confirm('确认已收到商品吗？', '提示', { type: 'info' })
  await confirmOrderAPI(id)
  ElMessage.success('已确认收货')
  fetchOrders()
}
</script>
