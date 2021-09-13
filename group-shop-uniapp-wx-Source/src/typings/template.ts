/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-16 13:27:40
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-16 13:36:20
 * 123
 */

import { IidType } from './global'

export interface ITemplateOne {
	colour: string
	createTime: string
	id: string
	isDeleted: string
	isDevTemplate: string
	name: string
	onlineStatus: string
	operatorId: IidType
	operatorName: null | string
	pages: ITemplateOnePage[]
	plugins: []
	shopId: string
	tenantId: string
	type: null | string
	updateTime: string
}

export interface ITemplateOnePage {
	assemblyVos: IPageAssemblyVos[]
	createTime: string
	id: number
	isDef: string
	isDeleted: string
	pageName: string
	shopId: string
	templateId: number
	tenantId: string
	updateTime: string
}

export interface IPageAssemblyVos {
	createTime: string
	id: number
	isDeleted: string
	pageId: number
	properties: string
	tenantId: string
	updateTime: string
}

export interface ITemplateOnePlugins {
	createTime: string
	id: number
	isDeleted: string
	isMandatory: string
	isSelection: string
	pluginNameCn: string
	pluginNameEn: string
	pluginProperties: string
	shopId: string
	templateId: number
	tenantId: string
	updateTime: string
}
