<template>
  <div>
    <h2>优惠券管理</h2>
    <el-button type="primary" style="margin-bottom:16px" @click="openDialog()">新增优惠券</el-button>
    <el-table :data="coupons" v-loading="loading">
      <el-table-column type="index" label="#" width="50" />
      <el-table-column prop="name" label="名称" />
      <el-table-column label="类型" width="80">
        <template #default="{ row }">{{ row.typeText }}</template>
      </el-table-column>
      <el-table-column prop="value" label="面值" width="80">
        <template #default="{ row }">{{ row.type === 0 ? '¥' + row.value : (row.value * 10).toFixed(1) + '折' }}</template>
      </el-table-column>
      <el-table-column prop="minAmount" label="门槛" width="100">
        <template #default="{ row }">¥{{ row.minAmount }}</template>
      </el-table-column>
      <el-table-column label="使用/总量" width="100">
        <template #default="{ row }">{{ row.usedCount }}/{{ row.totalCount }}</template>
      </el-table-column>
      <el-table-column label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button size="small" @click="openDialog(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="del(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 弹窗 -->
    <el-dialog :title="isEdit ? '编辑优惠券' : '新增优惠券'" v-model="dialogVisible" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="名称"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="类型">
          <el-radio-group v-model="form.type"><el-radio :value="0">满减券</el-radio><el-radio :value="1">折扣券</el-radio></el-radio-group>
        </el-form-item>
        <el-form-item label="面值"><el-input-number v-model="form.value" :precision="2" :min="0" />
          <span style="margin-left:8px;color:#999">{{ form.type === 0 ? '元' : '折(0.9=9折)' }}</span>
        </el-form-item>
        <el-form-item label="最低消费"><el-input-number v-model="form.minAmount" :precision="2" :min="0" /></el-form-item>
        <el-form-item label="发放数量"><el-input-number v-model="form.totalCount" :min="1" /></el-form-item>
        <el-form-item label="开始时间">
          <el-date-picker v-model="form.startTime" type="datetime" placeholder="选择开始时间" />
        </el-form-item>
        <el-form-item label="结束时间">
          <el-date-picker v-model="form.endTime" type="datetime" placeholder="选择结束时间" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status"><el-radio :value="1">启用</el-radio><el-radio :value="0">禁用</el-radio></el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="save" :loading="saving">保 存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAdminCoupons, createCoupon, updateCoupon, deleteCoupon } from '../../api/coupon'

const coupons = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const editId = ref(null)
const saving = ref(false)
const form = ref({
  name: '', type: 0, value: 10, minAmount: 0, totalCount: 100,
  startTime: '', endTime: '', status: 1
})

onMounted(() => fetchData())

async function fetchData() {
  loading.value = true
  try { const data = await getAdminCoupons(1, 100); coupons.value = data.records } finally { loading.value = false }
}

function openDialog(row) {
  if (row) {
    isEdit.value = true; editId.value = row.id
    form.value = {
      name: row.name, type: row.type, value: row.value, minAmount: row.minAmount,
      totalCount: row.totalCount, startTime: row.startTime, endTime: row.endTime, status: row.status
    }
  } else {
    isEdit.value = false; editId.value = null
    form.value = { name: '', type: 0, value: 10, minAmount: 0, totalCount: 100, startTime: '', endTime: '', status: 1 }
  }
  dialogVisible.value = true
}

async function save() {
  saving.value = true
  try {
    if (isEdit.value) { await updateCoupon(editId.value, form.value) }
    else { await createCoupon(form.value) }
    ElMessage.success(isEdit.value ? '已更新' : '已创建')
    dialogVisible.value = false
    fetchData()
  } finally { saving.value = false }
}

async function del(id) {
  await ElMessageBox.confirm('确定删除？', '提示', { type: 'warning' })
  await deleteCoupon(id)
  ElMessage.success('已删除')
  fetchData()
}
</script>
