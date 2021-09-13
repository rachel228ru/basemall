/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:20
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-30 14:12:30
 */
import api from '@/utils/request'

/** 获取页面装修数据 */
export const getAssembly = (data: any) => {
	return api.post('/shops-open/renovation/template/page/assembly/list', data)
}

/** 获取装修控件列表 */
export const getControl = (params: any) => {
	return api.post('/shops-open/renovation/plugin/list', params)
}

/** 获取当前模板id */
export const getTemplate = (params: any) => {
	return api.post('/shops-open/renovation/template/list', params)
}

/** 装修聚合接口 */
export const getDecorationAll = () => {
	return api.get('/shops-open/renovation/template/def/one', null, {
		cache: true,
		duration: 1000
	})
}

/**
 * 获取所有专区
 */
export const getAllRegionList = (data: any) => {
	return api.get(`/goods-open/manager/sale/mode/get/all`, data)
}
