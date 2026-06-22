<template>
  <div>
    <AppHeader />
    <div class="page-container" style="max-width:600px">
      <h1 class="page-title">个人资料</h1>
      <el-card>
        <el-form :model="form" label-width="100px">
          <el-form-item label="用户名"><el-input v-model="user.username" disabled /></el-form-item>
          <el-form-item label="昵称"><el-input v-model="form.nickname" /></el-form-item>
          <el-form-item label="邮箱"><el-input v-model="form.email" /></el-form-item>
          <el-form-item label="手机号"><el-input v-model="form.phone" /></el-form-item>
          <el-form-item><el-button type="primary" @click="saveProfile" :loading="saving">保存</el-button></el-form-item>
        </el-form>
      </el-card>
      <el-card style="margin-top:20px">
        <template #header>修改密码</template>
        <el-form :model="pwdForm" label-width="100px">
          <el-form-item label="原密码"><el-input v-model="pwdForm.oldPassword" type="password" show-password /></el-form-item>
          <el-form-item label="新密码"><el-input v-model="pwdForm.newPassword" type="password" show-password /></el-form-item>
          <el-form-item><el-button type="primary" @click="savePassword" :loading="savingPwd">修改密码</el-button></el-form-item>
        </el-form>
      </el-card>
    </div>
    <AppFooter />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import AppHeader from '../components/AppHeader.vue'
import AppFooter from '../components/AppFooter.vue'
import { useAuthStore } from '../stores/auth'
import { updateProfile, updatePassword } from '../api/user'

const auth = useAuthStore()
const user = ref({})
const form = reactive({ nickname: '', email: '', phone: '' })
const pwdForm = reactive({ oldPassword: '', newPassword: '' })
const saving = ref(false)
const savingPwd = ref(false)

onMounted(async () => {
  await auth.fetchUser()
  if (auth.user) {
    user.value = auth.user
    Object.assign(form, { nickname: auth.user.nickname || '', email: auth.user.email || '', phone: auth.user.phone || '' })
  }
})

async function saveProfile() {
  saving.value = true
  try {
    await updateProfile({ nickname: form.nickname, email: form.email, phone: form.phone })
    ElMessage.success('保存成功')
    await auth.fetchUser()
  } finally { saving.value = false }
}

async function savePassword() {
  if (!pwdForm.oldPassword || !pwdForm.newPassword) { ElMessage.warning('请填写完整'); return }
  savingPwd.value = true
  try {
    await updatePassword(pwdForm)
    ElMessage.success('密码修改成功')
    pwdForm.oldPassword = ''
    pwdForm.newPassword = ''
  } finally { savingPwd.value = false }
}
</script>
