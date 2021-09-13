/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:20
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-28 13:16:29
 */
import api from '@/utils/request'

// 供应商申请
export const supApply = (data: any) => {
	return api.post('/goods-open/api/supplier/save', data)
}
