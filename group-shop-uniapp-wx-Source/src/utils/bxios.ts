/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-30 14:29:49
 * 123
 */
interface IRequestConfig {
	[key: string]: any
	timeout?: number
	reqState?: boolean
	url?: string
	header?: any
	data?: string
	method?: string
	dataType?: string
	baseUrl?: string
}

import md5 from 'md5'
import storage from '@/utils/storage'

export class InterceptorManager {
	private handlers: any = []

	use<T = IRequestConfig>(
		fulfilled: (conf: T) => any,
		rejected: (res: any) => any
	) {
		this.handlers.push({
			fulfilled,
			rejected
		})
		return this.handlers.length - 1
	}

	forEach(fn: (arg: any) => void) {
		this.handlers.forEach((h: any) => fn(h))
	}
}

export class Bxios {
	public interceptors: {
		request: InterceptorManager
		response: InterceptorManager
	}

	private defaults: IRequestConfig = {
		timeout: 5000,
		reqState: false,
		url: '',
		header: {
			tenantId: '',
			token: ''
		},
		data: '',
		method: '',
		dataType: 'json',
		baseUrl: '',
		success: null,
		fail: null,
		complete: null
	}

	constructor(defaultConfig: any = {}) {
		this.defaults = Object.assign({}, this.defaults, defaultConfig)
		this.interceptors = {
			request: new InterceptorManager(),
			response: new InterceptorManager()
		}
	}

	/**
	 *
	 * @param url 请求地址
	 * @param data query
	 * @param config 配置 config.cache 开启缓存 duration 缓存时间（ms） 默认 3000
	 */
	get(
		url: string,
		data?: any,
		config: { duration?: number; cache?: boolean; [key: string]: any } = {}
	) {
		const key = md5(
			`${url}${JSON.stringify(data)}${process.env.VUE_APP_APP_REQUEST_VERSION}`
		)

		const get = () =>
			this.request(
				Object.assign({
					header: Object.assign(this.defaults.header, config),
					method: 'get',
					data,
					url
				})
			).then((res) => {
				if (config.cache) {
					storage.set(key, res, config.duration || 5000)
				}
				return res
			})

		if (config.cache) {
			try {
				const data = storage.get(key)

				if (data) return Promise.resolve(data)

				return get()
			} catch {
				return get()
			}
		}

		return get()
	}

	post(url: string, data?: any, config: any = {}) {
		return this.request(
			Object.assign({
				header: Object.assign(this.defaults.header, config),
				method: 'post',
				data,
				url
			})
		)
	}

	put(url: string, data?: any, config?: any) {
		return this.request(
			Object.assign({
				header: Object.assign(this.defaults.header, config),
				method: 'put',
				data,
				url
			})
		)
	}

	delete(url: string, data?: any, config?: any) {
		return this.request(
			Object.assign({
				header: Object.assign(this.defaults.header, config),
				method: 'delete',
				data,
				url
			})
		)
	}

	private request(config: any) {
		const chain = [this.dispatchRequest.bind(this), undefined]
		config.url = `${this.defaults.baseUrl}${config.url}`
		let promise = Promise.resolve(Object.assign(this.defaults, config))

		this.interceptors.request.forEach((interceptor: any) => {
			chain.unshift(
				interceptor.fulfilled.bind(this, config),
				interceptor.rejected
			)
		})

		this.interceptors.response.forEach((interceptor: any) => {
			chain.push(interceptor.fulfilled, interceptor.rejected)
		})
		while (chain.length) {
			promise = promise.then(chain.shift(), chain.shift())
		}
		return promise
	}

	private xhrAdapter(config: any) {
		return new Promise((resolve, reject) => {
			uni.request(
				Object.assign({}, config, {
					success: (res: any) => {
						resolve({
							resHeader: res.header,
							config: Object.assign(this.defaults, config),
							data: res.data
						})
					},
					fail: (err: any) => {
						reject(err)
					}
				})
			)
		})
	}

	private async dispatchRequest(config: any) {
		const adapter = this.xhrAdapter(config)
		try {
			return await adapter
		} catch (reason) {
			return Promise.reject(reason)
		}
	}
}

export default Bxios
