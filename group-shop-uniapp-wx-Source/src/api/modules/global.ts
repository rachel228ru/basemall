/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:20
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-28 13:14:56
 * 123
 */
import api from '@/utils/request'

/**
 * 获取订阅模板id
 */
// export const getSubTemplate = () => {
//   return api.get("/platform/min-subscriberi-message/mini", null);
// };

/**
 * 聚合接口
 */
export const getSetting = () => {
	return api.get('/order-open/mini/setting', null, { cache: true })
}

/**
 * 获取引导页设置
 */
export const getGuide = () => {
	return api.get('/shops-open/shop/guide/page/get', null, { cache: true })
}

export const getGuideVisible = () => {
	return api.get('/shops-open/shop/guide/switch/page/get', null)
}
