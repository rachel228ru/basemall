/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:20
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-28 11:40:35
 * 123
 */
import api from '@/utils/request'

/**
 * 获取用户地址
 * @param data 1-获取所有 2-获取默认地址,默认type=1
 * @returns
 */
export const getAddressList = (data?: { type: number }) => {
	return api.get('/account-open/mini-account-address', data)
}

/**
 * 更新用户地址|设置为默认
 * @param data 用户地址数据 + id
 * @returns
 */
export const updateAddress = (data: string) => {
	return api.put('/account-open/mini-account-address', data, {
		'Content-Type': 'application/json'
	})
}

/**
 * 删除用户地址
 * @param adderssId adderssId: 地址ID, string | number
 * @returns
 */
export const deleteAddress = (adderssId: string | number) => {
	return api.delete(`/account-open/mini-account-address/${adderssId}`, {})
}

/**
 * 添加用户地址
 * @param data 用户地址数据
 * @returns
 */
export const addAddress = (data: string) => {
	return api.post('/account-open/mini-account-address', data, {
		'Content-Type': 'application/json'
	})
}

/**
 * 获取省市区
 * @param data id:父级id,string;type:获取类型 1-省 2-市 3-区,1|2|3;
 * @returns
 */
export const getArea = (data: { id: string; type: 1 | 2 | 3 }) => {
	return api.get('/account-open/mini-account-address/area', data, {
		'Content-Type': 'application/json'
	})
}

/**
 * 获取经纬度
 * @param data site:详细地址,string
 * @returns
 */
export const getLatAndLon = (data: { site: string }) => {
	return api.get('/account-open/mini-account-address/latitude-longitude', data)
}

/**
 * 根据经纬度返回省市区
 * @param data site:经纬度,string
 * @returns
 */
export const analysisLatAndLon = (data: { site: string }) => {
	return api.get(
		'/account-open/mini-account-address/analysis/latitude-longitude',
		data
	)
}
