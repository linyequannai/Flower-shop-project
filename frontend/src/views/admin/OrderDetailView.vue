<template>
  <div>
    <h2>订单详情</h2>
    <div v-loading="loading">
      <el-card v-if="order" style="margin-bottom:20px">
        <template #header>订单信息</template>
        <p>订单号：{{ order.orderNo }}</p>
        <p>状态：<el-tag :type="getOrderStatusType(order.status)">{{ order.statusText }}</el-tag></p>
        <p>下单时间：{{ formatDate(order.createdAt) }}</p>
        <p>收货人：{{ order.receiverName }} ({{ order.receiverPhone }})</p>
        <p>地址：{{ order.receiverAddress }}</p>
        <p>总价：¥{{ order.totalAmount }} | 优惠：¥{{ order.discountAmount }} | 实付：¥{{ order.payAmount }}</p>
        <div style="margin-top:12px">
          <el-button v-if="order.status === 0 || order.status === 1" type="success" @click="$emit('ship', order.id)">发货</el-button>
          <el-button v-if="order.status === 1 || order.status === 2" type="primary" @click="$emit('complete', order.id)">标记完成</el-button>
        </div>
      </el-card>
      <el-card>
        <template #header>商品清单</template>
        <el-table :data="order.items">
          <el-table-column prop="flowerName" label="商品" />
          <el-table-column prop="price" label="单价">
            <template #default="{ row }">¥{{ row.price }}</template>
          </el-table-column>
          <el-table-column prop="quantity" label="数量" />
          <el-table-column prop="subtotal" label="小计">
            <template #default="{ row }">¥{{ row.subtotal }}</template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getOrderDetail, shipOrder, updateOrderStatus } from '../../api/order'
import { formatDate, getOrderStatusType } from '../../utils/format'

const route = useRoute()
const order = ref(null)
const loading = ref(false)

onMounted(async () => {
  loading.value = true
  try { order.value = await getOrderDetail(route.params.id) } finally { loading.value = false }
})

async function ship(id) { await shipOrder(id); ElMessage.success('已发货'); order.value = await getOrderDetail(id) }
async function complete(id) { await updateOrderStatus(id, 3); ElMessage.success('已标记完成'); order.value = await getOrderDetail(id) }
</script>
