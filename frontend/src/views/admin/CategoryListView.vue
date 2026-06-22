<template>
  <div>
    <h2>分类管理</h2>
    <el-button type="primary" style="margin-bottom:16px" @click="openDialog()">新增分类</el-button>
    <el-table :data="categories" v-loading="loading">
      <el-table-column type="index" label="#" width="50" />
      <el-table-column prop="name" label="名称" />
      <el-table-column prop="description" label="描述" />
      <el-table-column prop="sortOrder" label="排序" width="80" />
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button size="small" @click="openDialog(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="del(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 弹窗 -->
    <el-dialog :title="isEdit ? '编辑分类' : '新增分类'" v-model="dialogVisible" width="450px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="名称"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="描述"><el-input v-model="form.description" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.sortOrder" /></el-form-item>
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
import { getCategories, createCategory, updateCategory, deleteCategory } from '../../api/category'

const categories = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const editId = ref(null)
const form = ref({ name: '', description: '', sortOrder: 0 })
const saving = ref(false)

onMounted(() => fetchData())

async function fetchData() {
  loading.value = true
  try { categories.value = await getCategories() } finally { loading.value = false }
}

function openDialog(row) {
  if (row) {
    isEdit.value = true; editId.value = row.id
    form.value = { name: row.name, description: row.description || '', sortOrder: row.sortOrder || 0 }
  } else {
    isEdit.value = false; editId.value = null
    form.value = { name: '', description: '', sortOrder: 0 }
  }
  dialogVisible.value = true
}

async function save() {
  saving.value = true
  try {
    if (isEdit.value) {
      await updateCategory(editId.value, form.value)
    } else {
      await createCategory(form.value)
    }
    ElMessage.success(isEdit.value ? '已更新' : '已创建')
    dialogVisible.value = false
    fetchData()
  } finally { saving.value = false }
}

async function del(id) {
  await ElMessageBox.confirm('确定删除该分类？', '提示', { type: 'warning' })
  await deleteCategory(id)
  ElMessage.success('已删除')
  fetchData()
}
</script>
