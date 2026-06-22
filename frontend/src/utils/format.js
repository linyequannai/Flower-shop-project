/**
 * 格式化价格为 ￥xx.xx
 */
export function formatPrice(price) {
  return '¥' + Number(price).toFixed(2)
}

/**
 * 格式化日期时间
 */
export function formatDate(dateStr) {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  const y = date.getFullYear()
  const m = String(date.getMonth() + 1).padStart(2, '0')
  const d = String(date.getDate()).padStart(2, '0')
  const h = String(date.getHours()).padStart(2, '0')
  const min = String(date.getMinutes()).padStart(2, '0')
  return `${y}-${m}-${d} ${h}:${min}`
}

/**
 * 订单状态映射
 */
export function getOrderStatusText(status) {
  const map = { 0: '待付款', 1: '已付款', 2: '已发货', 3: '已完成', 4: '已取消', 5: '已退款' }
  return map[status] || '未知'
}

export function getOrderStatusType(status) {
  const map = { 0: 'warning', 1: 'success', 2: 'primary', 3: 'success', 4: 'info', 5: 'danger' }
  return map[status] || 'info'
}
