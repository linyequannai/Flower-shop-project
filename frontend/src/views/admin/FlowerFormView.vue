<template>
  <div>
    <h2>{{ isEdit ? '编辑花卉' : '新增花卉' }}</h2>
    <el-card style="max-width:700px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="名称"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.categoryId" style="width:100%">
            <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="售价"><el-input-number v-model="form.price" :precision="2" :min="0" /></el-form-item>
        <el-form-item label="原价"><el-input-number v-model="form.originalPrice" :precision="2" :min="0" /></el-form-item>
        <el-form-item label="库存"><el-input-number v-model="form.stock" :min="0" /></el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status"><el-radio :value="1">上架</el-radio><el-radio :value="0">下架</el-radio></el-radio-group>
        </el-form-item>
        <el-form-item label="描述"><el-input v-model="form.description" type="textarea" :rows="4" /></el-form-item>
        <el-form-item label="封面图">
          <div><el-upload :action="''" :http-request="uploadCover" :show-file-list="false" accept="image/*">
            <el-button>选择图片</el-button>
          </el-upload></div>
          <img v-if="form.coverImage" :src="form.coverImage" style="width:150px;height:150px;object-fit:cover;margin-top:8px;border-radius:4px" />
        </el-form-item>
        <el-form-item><el-button type="primary" @click="save" :loading="saving">保存</el-button></el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getCategories } from '../../api/category'
import { getFlowerDetail, createFlower, updateFlower, uploadImage } from '../../api/flower'

const route = useRoute()
const router = useRouter()
const isEdit = ref(!!route.params.id)
const categories = ref([])
const saving = ref(false)

const form = ref({
  name: '', categoryId: null, price: 0, originalPrice: 0, stock: 0,
  status: 1, description: '', coverImage: '', images: ''
})

onMounted(async () => {
  categories.value = await getCategories()
  if (isEdit.value) {
    const f = await getFlowerDetail(route.params.id)
    Object.assign(form.value, {
      name: f.name, categoryId: f.categoryId, price: f.price,
      originalPrice: f.originalPrice || 0, stock: f.stock, status: f.status,
      description: f.description || '', coverImage: f.coverImage || '', images: f.images || ''
    })
  }
})

async function uploadCover({ file }) {
  const data = await uploadImage(file)
  form.value.coverImage = data.url
}

async function save() {
  if (!form.value.name || !form.value.categoryId) { ElMessage.warning('请填写必填项'); return }
  saving.value = true
  try {
    if (isEdit.value) {
      await updateFlower(route.params.id, form.value)
    } else {
      await createFlower(form.value)
    }
    ElMessage.success(isEdit.value ? '已更新' : '已创建')
    router.push('/admin/flowers')
  } finally { saving.value = false }
}
</script>
