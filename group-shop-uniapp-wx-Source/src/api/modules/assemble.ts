/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:20
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-28 13:14:20
 */
import api from '@/utils/request'

// 获取city
export const getCityList = (data: any = {}) => {
	return api.get('/assemble-open/association/list_ass_city', data)
}

// 获取id
export const getPoint = (id: string | number) => {
	return api.get(`/assemble-open/association/ass_point/${id}`)
}

/**
 * 发送验证码
 * @param data
 */

export const sendCode = (data: any) => {
	return api.post('/assemble-open/partner/admin/send_code', data)
}

/**
 * 校验验证码
 * @param data
 */

export const checkCode = (data: any) => {
	return api.get('/assemble-open/partner/admin/check_message_code', data)
}

/**
 * 获取信息
 * @param data
 */

export const getApprovalInfo = (data: any) => {
	return api.get('/assemble-open/association/check_approval_status', data)
}
