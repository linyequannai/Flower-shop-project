<template>
  <div>
    <AppHeader />
    <div class="page-container" style="max-width:800px">
      <h1 class="page-title">订单详情</h1>
      <div v-loading="loading">
        <el-card v-if="order" style="margin-bottom:20px">
          <template #header>订单信息</template>
          <p>订单号：{{ order.orderNo }}</p>
          <p>状态：<el-tag :type="getOrderStatusType(order.status)">{{ order.statusText }}</el-tag></p>
          <p>下单时间：{{ formatDate(order.createdAt) }}</p>
          <p>收货人：{{ order.receiverName }} ({{ order.receiverPhone }})</p>
          <p>地址：{{ order.receiverAddress }}</p>
          <p>商品总价：¥{{ order.totalAmount }}</p>
          <p v-if="order.discountAmount > 0">优惠：-¥{{ order.discountAmount }}
            <span v-if="order.couponName">({{ order.couponName }})</span>
          </p>
          <p style="font-size:18px;color:#e74c8b;font-weight:bold">实付：¥{{ order.payAmount }}</p>
        </el-card>
        <el-card>
          <template #header>商品清单</template>
          <div v-for="item in order.items" :key="item.id" class="item-row">
            <img :src="item.flowerImage" />
            <span>{{ item.flowerName }}</span>
            <span>¥{{ item.price }} x {{ item.quantity }}</span>
            <span>¥{{ item.subtotal }}</span>
          </div>
        </el-card>
        <div style="text-align:center;margin-top:20px">
          <el-button v-if="order.status === 0" type="danger" @click="cancel">取消订单</el-button>
          <el-button v-if="order.status === 2" type="success" @click="confirm">确认收货</el-button>
        </div>
      </div>
    </div>
    <AppFooter />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import AppHeader from '../components/AppHeader.vue'
import AppFooter from '../components/AppFooter.vue'
import { getOrderDetail, cancelOrder, confirmOrder } from '../api/order'
import { formatDate, getOrderStatusType } from '../utils/format'

const route = useRoute()
const router = useRouter()
const order = ref(null)
const loading = ref(false)

onMounted(async () => {
  loading.value = true
  try { order.value = await getOrderDetail(route.params.id) } finally { loading.value = false }
})

async function cancel() {
  await ElMessageBox.confirm('确定取消该订单吗？', '提示', { type: 'warning' })
  await cancelOrder(order.value.id)
  ElMessage.success('已取消')
  order.value = await getOrderDetail(order.value.id)
}

async function confirm() {
  await ElMessageBox.confirm('确认已收到商品吗？', '提示', { type: 'info' })
  await confirmOrder(order.value.id)
  ElMessage.success('已确认收货')
  order.value = await getOrderDetail(order.value.id)
}
</script>

<style scoped>
.item-row { display: flex; align-items: center; gap: 16px; padding: 10px 0; border-bottom: 1px solid #f0f0f0; }
.item-row img { width: 50px; height: 50px; object-fit: cover; border-radius: 4px; }
.item-row span:last-child { margin-left: auto; color: #e74c8b; }
</style>
