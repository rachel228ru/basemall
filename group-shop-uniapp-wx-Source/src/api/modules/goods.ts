/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:20
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-28 13:15:45
 * 123
 */

import api from '@/utils/request'

// 查看商超商品列表
export const getGoodsList = (data: any, params: any) => {
	return api.get(
		'/goods-open/api/product/supermarket/list',
		{ ...data, ...params },
		{ cache: true }
	)
}

// 查看有效的一级商超分类及二级分类和商品
export const getEffectCategoryGoods = (data: any, param: any) => {
	return api.get(
		`/goods-open/api/product/get/alive/product/list/by/category/${data}`,
		{ ...param },
		{ cache: true }
	)
}

// 查看商品列表
export const searchGoodsList = (data: any) => {
	return api.get(
		'/goods-open/api/product/index/list',
		{ ...data },
		{ cache: true }
	)
}

// 查看商品详情
export const getGoodDetail = (id: number | string, data: any) => {
	return api.get(`/goods-open/api/product/get/${id}`, data)
}

/**
 * 获取商超有效展示分类  0-商超 1-拼团
 * @param data
 */
export const getEffectiveCategory = (data: any) => {
	return api.get(`/goods-open/api/show/category/get/${data}`, {})
}

// 查看商品分类详情 0-商超 1-拼团
export const getGoodClass = (saleMode: any, data: any) => {
	return api.get(`/goods-open/api/show/category/get/${saleMode}`, data, {
		cache: true
	})
}

/**
 * 获取所有展示分类
 * @param data
 */
export const getAllCategory = (data: any) => {
	return api.get('/goods-open/api/show/category/get/all', data)
}

// 获取商品评价
export const getProductEvaluate = (data: any) => {
	return api.get(`/order-open/mini/product/evaluate`, data)
}

// 获取商品概况
export const getProductEvaluateOverview = (data: any) => {
	return api.get(`/order-open/mini/product/evaluate/overview`, data)
}

// 商品是否被收藏
export const checkIsCollect = (data: any) => {
	return api.get(`/account-open/api/collect/findAccountIsCollect`, data)
}

// 收藏商品
export const userAddCollect = (data: any) => {
	return api.post(`/account-open/api/collect/plus`, data)
}

// 删除用户收藏
export const userDelCollect = (data: any) => {
	return api.post(`/account-open/api/collect/delAccountCollect`, data)
}

// 获取收藏列表
export const userCollectList = (data: any) => {
	return api.get(`/account-open/api/collect/getUserCollectInfo`, data)
}

// 获取收藏数量
export const userCollectListNum = (data: any) => {
	return api.get(`/account-open/api/collect/getCollect/Count`, data)
}

// 添加用户足迹
export const userFooterAdd = (data: any) => {
	return api.post(`/account-open/api/FootMark/add/FootMarkInfo`, data)
}

// 查看用户足迹列表
export const userFooterList = (data: any) => {
	return api.get(`/account-open/api/FootMark/getUserFootMarkInfo`, data)
}

// 删除用户足迹商品
export const userFooterDel = (type: any, data: any) => {
	return api.put(`/account-open/api/FootMark/del/FootMarkInfo/${type}`, data)
}

// 获取用户足迹数量
export const userFooterListNum = (data: any) => {
	return api.get(`/account-open/api/FootMark/getAccountFootMarkCount`, data)
}

/**
 * 获取商品码
 */
export const getImgCode = (data: any) => {
	return api.post('/platform-open/mini-info/wxa/getwxacode', data)
}

/**
 * 过滤不可用的商品
 */
export const discountList = (data: any, params: any) => {
	return api.get(`/goods-open/api/product/alive/product/list/${data}`, params)
}
