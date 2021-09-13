/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:20
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-28 13:17:41
 */
import api from '@/utils/request'

// 获取用户中心设置信息
export const userCenterSettingGet = () => {
	return api.get('/shops-open/account-center')
}

// 获取用户信息
export const getQrcode = (data: any) => {
	return api.get('/platform-open/mini-experience/wxa/get_qrcode', data)
}

// 获取用户信息 聚合接口
export const getAggregate = () => {
	return api.get('/account-open/aggregate/more', {}, { cache: true })
}
