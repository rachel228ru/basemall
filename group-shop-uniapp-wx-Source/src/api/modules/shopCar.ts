/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:20
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-30 14:12:40
 */
import api from '@/utils/request'

// 加入购物车
export const addCars = (data: any) => {
	return api.post('/goods-open/api/shopping/cart/add', data)
}

// 获取购物车
export const getCarsList = () => {
	return api.get(`/goods-open/api/shopping/cart/getByUserId`)
}

// 删除购物车商品
export const delCarsList = (data: any) => {
	return api.post(`/goods-open/api/shopping/cart/delete?${+new Date()}`, data)
}

// 编辑购物车
export const editCarsList = (data: any) => {
	return api.post(`/goods-open/api/shopping/cart/update?${+new Date()}`, data)
}

// 清空失效商品
export const emptyCarsList = (data: any) => {
	return api.delete(`/goods-open/api/shopping/cart/clean/effect`, data)
}

// 修改购物车选中状态
export const changeType = (selectStatus: any, data: any) => {
	return api.delete(
		`/goods-open/api/shopping/cart/change/select/status/${selectStatus}`,
		data
	)
}
