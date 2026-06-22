<template>
  <div>
    <AppHeader />
    <div class="page-container" style="max-width:800px">
      <h1 class="page-title">确认订单</h1>
      <!-- 收货信息 -->
      <el-card style="margin-bottom:20px">
        <template #header>收货信息</template>
        <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
          <el-form-item label="收货人" prop="receiverName">
            <el-input v-model="form.receiverName" />
          </el-form-item>
          <el-form-item label="联系电话" prop="receiverPhone">
            <el-input v-model="form.receiverPhone" />
          </el-form-item>
          <el-form-item label="收货地址" prop="receiverAddress">
            <el-input v-model="form.receiverAddress" />
          </el-form-item>
        </el-form>
      </el-card>
      <!-- 商品 -->
      <el-card style="margin-bottom:20px">
        <template #header>商品清单</template>
        <div v-for="item in cart.selectedItems" :key="item.id" class="checkout-item">
          <img :src="item.flowerImage" @error="e => e.target.src='data:image/svg+xml,' + encodeURIComponent('<svg xmlns=%22http://www.w3.org/2000/svg%22 width=%22100%22 height=%22100%22><rect fill=%22%23fce4ec%22 width=%22100%22 height=%22100%22/><text fill=%22%23e74c8b%22 font-size=%2230%22 text-anchor=%22middle%22 dy=%22.35em%22 x=%2250%22 y=%2250%22>🌸</text></svg>')" />
          <span>{{ item.flowerName }}</span>
          <span>x{{ item.quantity }}</span>
          <span>¥{{ (item.price * item.quantity).toFixed(2) }}</span>
        </div>
      </el-card>
      <!-- 优惠券 -->
      <el-card style="margin-bottom:20px">
        <template #header>优惠券</template>
        <el-select v-model="form.couponId" placeholder="选择优惠券（可选）" clearable style="width:100%">
          <el-option v-for="c in myCoupons" :key="c.userCouponId"
                     :label="c.name + (c.type === 0 ? ' 满减¥'+c.value : ' '+(c.value*10).toFixed(1)+'折') + (c.minAmount > 0 ? ' 满¥'+c.minAmount : '')"
                     :value="c.id" />
        </el-select>
      </el-card>
      <!-- 合计 -->
      <div style="text-align:right;padding:20px;background:#fff;border-radius:8px">
        <span style="font-size:20px">应付金额：<span style="color:#e74c8b;font-weight:bold">¥{{ cart.totalPrice.toFixed(2) }}</span></span>
        <el-button type="primary" size="large" style="margin-left:20px" @click="placeOrder" :loading="submitting">提交订单</el-button>
      </div>
    </div>
    <AppFooter />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import AppHeader from '../components/AppHeader.vue'
import AppFooter from '../components/AppFooter.vue'
import { useCartStore } from '../stores/cart'
import { createOrder } from '../api/order'
import { getMyCoupons } from '../api/coupon'

const router = useRouter()
const cart = useCartStore()
const formRef = ref(null)
const submitting = ref(false)
const myCoupons = ref([])

const form = reactive({
  receiverName: '', receiverPhone: '', receiverAddress: '',
  couponId: null,
})

const rules = {
  receiverName: [{ required: true, message: '请输入收货人', trigger: 'blur' }],
  receiverPhone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' },
  ],
  receiverAddress: [{ required: true, message: '请输入收货地址', trigger: 'blur' }],
}

onMounted(async () => {
  myCoupons.value = (await getMyCoupons(0)).filter(c => c.userCouponStatus === 0)
})

async function placeOrder() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  if (cart.selectedItems.length === 0) { ElMessage.warning('没有选中的商品'); return }

  submitting.value = true
  try {
    await createOrder({
      cartItemIds: cart.selectedItems.map(i => i.id),
      receiverName: form.receiverName,
      receiverPhone: form.receiverPhone,
      receiverAddress: form.receiverAddress,
      couponId: form.couponId || undefined,
    })
    ElMessage.success('下单成功')
    cart.clear()
    router.push('/orders')
  } catch {} finally { submitting.value = false }
}
</script>

<style scoped>
.checkout-item {
  display: flex; align-items: center; gap: 16px; padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}
.checkout-item img { width: 50px; height: 50px; object-fit: cover; border-radius: 4px; }
.checkout-item span:last-child { margin-left: auto; font-weight: bold; color: #e74c8b; }
</style>
