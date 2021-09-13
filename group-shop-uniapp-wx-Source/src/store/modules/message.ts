/*
 * @description:
 * @Author: vikingShip
 * @Date: 2021-07-28 09:25:02
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 16:02:03
 */
// 通知消息模块

import {
	VuexModule,
	Module,
	Mutation,
	Action,
	getModule
} from 'vuex-module-decorators'
import store from '@/store'
import { MessageStoreState, IMessage } from '../modulesType/messageType'

@Module({ name: 'messageStore', dynamic: true, namespaced: true, store })
export class MessageStore extends VuexModule implements MessageStoreState {
	public templates: IMessage[] = []
	public type = {
		sendMsg: 'sendMsg',
		auditMsg: 'auditMsg',
		memberCartOpen: 'memberCartOpen',
		orderRefund: 'orderRefund',
		orderRefundNotify: 'orderRefundNotify'
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 获取模板ID
	 */
	get templateMap() {
		const map: { [x: string]: string } = {}
		this.templates.forEach(({ mark, miniTemplateId }) => {
			map[mark] = miniTemplateId
		})
		return map
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 模板可用状态
	 */
	get templateSatusMap() {
		const map: { [x: string]: number } = {}
		this.templates.forEach(({ mark, miniOpen }) => {
			map[mark] = miniOpen
		})
		return map
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 获取模板列表
	 * @param templates 模板
	 */
	@Mutation
	private GET_TEMPLATES(templates: IMessage[]) {
		this.templates = templates
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 订阅消息
	 * @param tmplIds 模板id数组
	 * @returns 只会返回resolve true 表示成功 false 表示失败
	 */
	@Action
	public subscribe(ids: Array<string>): Promise<boolean> {
		if (!this.templates || !this.templates.length) {
			return Promise.resolve(false)
		}

		const tmplIds = ids
			.filter((item) => this.templateSatusMap[item])
			.map((key) => this.templateMap[key])

		if (!tmplIds.length) return Promise.resolve(false)

		return new Promise((resolve) => {
			uni.requestSubscribeMessage({
				tmplIds,
				success: () => resolve(true),
				fail: () => {
					resolve(false)
				},
				complete: () => resolve(false)
			})
		})
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 获取templatid
	 */
	@Action
	public getTemplateId(mark: string): string {
		return this.templateMap[mark] || ''
	}
	@Action
	public setTemplates(value: IMessage[]) {
		this.GET_TEMPLATES(value)
	}
	@Action
	public getTemplates(value: IMessage[]) {
		this.GET_TEMPLATES(value)
	}
}

export const messageStore = getModule(MessageStore)
