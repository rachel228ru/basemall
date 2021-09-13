/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:20
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-30 14:12:46
 */
import api from '@/utils/request'

export const shopsPartnerList = () => {
	return api.post('/shops-open/shops_partner/list', null)
}
