import request from './request'

export function getFlowers(params) {
  return request.get('/flowers', { params })
}

export function getFlowerDetail(id) {
  return request.get(`/flowers/${id}`)
}

export function getHotFlowers(limit = 8) {
  return request.get('/flowers/hot', { params: { limit } })
}

export function createFlower(data) {
  return request.post('/admin/flowers', data)
}

export function updateFlower(id, data) {
  return request.put(`/admin/flowers/${id}`, data)
}

export function updateFlowerStatus(id, status) {
  return request.put(`/admin/flowers/${id}/status`, null, { params: { status } })
}

export function deleteFlower(id) {
  return request.delete(`/admin/flowers/${id}`)
}

export function uploadImage(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request.post('/file/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}
