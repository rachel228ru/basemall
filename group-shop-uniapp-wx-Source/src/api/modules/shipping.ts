/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:20
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-28 13:16:43
 */
import api from '@/utils/request'

const gdkey = 'f84ae9ba9009c49687d6e09c3e15c1fd'

/**
 * 根据经纬度查询地址详情
 * @param {string} location 经纬度 116.473168,39.993015
 */
export function placeAround(location = '') {
	return api.get('/integral-open/get/apiSearch', {
		key: gdkey,
		extensions: 'all',
		location
	})
}

/**
 * 根据 cityCode 获取 对应的 adCode
 * @param {string} cityCode
 */
export async function getadCodBycityCode(cityCode = '') {
	try {
		const res = await api.get('/integral-open/get/config/district', {
			key: gdkey,
			keywords: cityCode,
			subdistrict: 0,
			page: 1,
			extensions: 'base'
		})
		if (res.districts) {
			const districtItem = res.districts.find(
				(item: { level: string }) => item.level === 'city'
			)
			return districtItem.adcode
		} else {
			return Promise.reject(false)
		}
	} catch (error) {
		return Promise.reject(error)
	}
}
