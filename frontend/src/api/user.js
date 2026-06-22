import request from './request'

export function updateProfile(data) {
  return request.put('/user/profile', data)
}

export function updatePassword(data) {
  return request.put('/user/password', data)
}

export function getUserList(page = 1, size = 10) {
  return request.get('/admin/users', { params: { page, size } })
}

export function updateUserStatus(id, status) {
  return request.put(`/admin/users/${id}/status`, null, { params: { status } })
}
