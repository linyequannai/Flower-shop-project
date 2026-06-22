import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { getCartList, addToCart, removeFromCart, updateCartQuantity } from '../api/cart'

export const useCartStore = defineStore('cart', () => {
  const items = ref([])
  const loading = ref(false)

  const totalCount = computed(() =>
    items.value.reduce((sum, item) => sum + item.quantity, 0)
  )

  const selectedItems = computed(() =>
    items.value.filter(item => item.selected === 1)
  )

  const totalPrice = computed(() =>
    selectedItems.value.reduce((sum, item) =>
      sum + item.price * item.quantity, 0
    )
  )

  async function fetchCart() {
    loading.value = true
    try {
      items.value = await getCartList() || []
    } finally {
      loading.value = false
    }
  }

  async function add(flowerId, quantity = 1) {
    await addToCart(flowerId, quantity)
    await fetchCart()
  }

  async function remove(cartItemId) {
    await removeFromCart(cartItemId)
    await fetchCart()
  }

  async function updateQuantity(cartItemId, quantity) {
    await updateCartQuantity(cartItemId, quantity)
    await fetchCart()
  }

  function clear() {
    items.value = []
  }

  return { items, loading, totalCount, selectedItems, totalPrice, fetchCart, add, remove, updateQuantity, clear }
})
