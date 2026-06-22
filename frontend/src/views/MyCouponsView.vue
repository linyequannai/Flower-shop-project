<template>
  <div>
    <AppHeader />
    <div class="page-container">
      <h1 class="page-title">我的优惠券</h1>
      <el-tabs v-model="activeTab" @tab-change="fetchCoupons">
        <el-tab-pane label="未使用" name="0" />
        <el-tab-pane label="已使用" name="1" />
        <el-tab-pane label="已过期" name="2" />
      </el-tabs>
      <!-- 可领取 -->
      <h3 style="margin:20px 0 10px">可领取优惠券</h3>
      <div v-loading="loading2">
        <el-card v-for="c in availableCoupons" :key="c.id" class="coupon-card" :class="{ claimed: c.userCouponStatus === -1 }">
          <div class="coupon-value">
            <template v-if="c.type === 0">¥{{ c.value }}</template>
            <template v-else>{{ (c.value * 10).toFixed(1) }}折</template>
          </div>
          <div class="coupon-info">
            <h4>{{ c.name }}</h4>
            <p v-if="c.minAmount > 0">满¥{{ c.minAmount }}可用</p>
            <p>有效期：{{ formatDate(c.startTime) }} ~ {{ formatDate(c.endTime) }}</p>
            <p>剩余 {{ c.remainCount }} 张</p>
          </div>
          <el-button v-if="c.userCouponStatus !== -1" type="primary" size="small" @click="claim(c.id)" :loading="claiming === c.id">立即领取</el-button>
          <el-tag v-else>已领取</el-tag>
        </el-card>
      </div>
      <!-- 我的券 -->
      <h3 style="margin:20px 0 10px">我的优惠券</h3>
      <div v-loading="loading1">
        <el-card v-for="c in myCoupons" :key="c.userCouponId" class="coupon-card" :class="{ used: c.userCouponStatus !== 0 }">
          <div class="coupon-value">
            <template v-if="c.type === 0">¥{{ c.value }}</template>
            <template v-else>{{ (c.value * 10).toFixed(1) }}折</template>
          </div>
          <div class="coupon-info">
            <h4>{{ c.name }}</h4>
            <p v-if="c.minAmount > 0">满¥{{ c.minAmount }}可用</p>
          </div>
          <el-tag v-if="c.userCouponStatus === 1" type="info">已使用</el-tag>
          <el-tag v-else-if="c.userCouponStatus === 2" type="danger">已过期</el-tag>
        </el-card>
        <el-empty v-if="myCoupons.length === 0" description="暂无优惠券" />
      </div>
    </div>
    <AppFooter />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import AppHeader from '../components/AppHeader.vue'
import AppFooter from '../components/AppFooter.vue'
import { getAvailableCoupons, getMyCoupons, claimCoupon } from '../api/coupon'
import { formatDate } from '../utils/format'

const activeTab = ref('0')
const myCoupons = ref([])
const availableCoupons = ref([])
const loading1 = ref(false)
const loading2 = ref(false)
const claiming = ref(null)

onMounted(() => { fetchCoupons(); fetchAvailable() })

async function fetchCoupons() {
  loading1.value = true
  try { myCoupons.value = await getMyCoupons(Number(activeTab.value)) } finally { loading1.value = false }
}

async function fetchAvailable() {
  loading2.value = true
  try { availableCoupons.value = await getAvailableCoupons() } finally { loading2.value = false }
}

async function claim(id) {
  claiming.value = id
  try {
    await claimCoupon(id)
    ElMessage.success('领取成功')
    fetchCoupons()
    fetchAvailable()
  } finally { claiming.value = null }
}
</script>

<style scoped>
.coupon-card { display: flex; align-items: center; gap: 16px; margin-bottom: 12px; }
.coupon-card.claimed, .coupon-card.used { opacity: 0.6; }
.coupon-value { font-size: 28px; font-weight: bold; color: #e74c8b; min-width: 100px; text-align: center; }
.coupon-info { flex: 1; }
.coupon-info h4 { margin-bottom: 4px; }
.coupon-info p { font-size: 12px; color: #999; }
</style>
