/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:20
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-30 14:12:56
 * 123
 */
import api from '@/utils/request'

// 小程序登录
export const login = (data: any) => {
	return api.get('/account-open/mini-account/login', data)
}

// 更新用户信息
export const userMsgUpdate = (data: any) => {
	return api.post('/account-open/mini-account', data)
}

/** 获取用户信息 */

export const userMsgGet = (data: any) => {
	return api.get('/account-open/mini-account', data)
}

/**
 * POST
 * 更新用户扩展信息部分字段
 */
export const miniAccountExtends = (data: any) => {
	return api.post(`/account-open/mini-account-extends`, data)
}

/**
 * 获取店铺信息
 */
export const getShopManage = (data: any) => {
	return api.get(`/platform-open/shop/info`, data, { cache: true })
}
