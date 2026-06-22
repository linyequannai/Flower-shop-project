import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getToken, setToken, removeToken } from '../utils/auth'
import { login as loginAPI, register as registerAPI, getMe } from '../api/auth'

export const useAuthStore = defineStore('auth', () => {
  const token = ref(getToken())
  const user = ref(null)

  async function login(username, password) {
    const data = await loginAPI(username, password)
    token.value = data.token
    setToken(data.token)
    await fetchUser()
  }

  async function register(form) {
    await registerAPI(form)
  }

  async function fetchUser() {
    if (!token.value) return
    try {
      user.value = await getMe()
    } catch {
      logout()
    }
  }

  function logout() {
    token.value = null
    user.value = null
    removeToken()
  }

  const isAdmin = () => user.value?.role === 1

  return { token, user, login, register, fetchUser, logout, isAdmin }
})
