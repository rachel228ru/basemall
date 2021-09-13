/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 16:04:02
 * 123
 */
export class global {
	/**
	 * @description 是否为身份证
	 * @param str 身份证号
	 */
	public isIDnumber(str: string) {
		return /^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/.test(
			str
		)
	}

	/**
	 * @description 是否为手机号
	 * @param str 手机号
	 */
	public isPhone(str: string) {
		return /^1(3[0-9]|4[0-9]|5[0-9]|6[0-9]|7[0-9]|8[0-9]|9[1,8,9])\d{8}$/.test(
			str
		)
	}
	/**
	 * @description: 是否为质数
	 * @LastEditors: vikingShip
	 * @param {number} num 传入数字
	 * @returns {boolean} true | false
	 */

	public isPrimeNum = (num: number): boolean => {
		for (let i = 2; i < num; i++) {
			if (num % i === 0) {
				return false
			}
		}
		return true
	}

	/**
	 * @description 简单的方法防抖
	 * @param fn 要执行的函数
	 * @param awit  时间
	 * @param immediate 是否在触发事件后 在时间段n开始，立即执行，否则是时间段n结束，才执行
	 */
	public debounced(fn: () => void, awit = 1000, immediate = false) {
		let timer: number | null = null

		if (timer) clearTimeout(timer)

		if (immediate) {
			if (!timer) fn()

			timer = setTimeout(function() {
				timer = null
			}, awit)
		} else {
			timer = setTimeout(() => {
				fn()
			}, awit)
		}
	}

	/**
	 * @description 简单的方法节流
	 * @param fn  需要包装的函数
	 * @param awit  延迟时间，单位ms
	 * @param immediate true 是启用时间戳版，false 是启用定时器版，作用一样
	 */

	public throttle(fn: () => void, awit: number) {
		let enterTime = 0
		const gapTime = awit || 500
		return (...args: any[]) => {
			const backTime = +new Date()
			if (backTime - enterTime > gapTime) {
				fn.call(args[0])
				enterTime = backTime
			}
		}
	}

	/**
	 * @description 对比基础库
	 * @param v1
	 * @param v2
	 */
	public compareVersion(v1: string, v2: string): 1 | 0 | -1 {
		const _v1 = v1.split('.')
		const _v2 = v2.split('.')

		const len = Math.max(_v1.length, _v2.length)

		while (_v1.length < len) {
			_v1.push('0')
		}
		while (_v2.length < len) {
			_v2.push('0')
		}

		for (let i = 0; i < len; i++) {
			const num1 = parseInt(_v1[i])
			const num2 = parseInt(_v2[i])

			if (num1 > num2) {
				return 1
			} else if (num1 < num2) {
				return -1
			}
		}

		return 0
	}

	/**
	 * 获取用户id
	 */
	public getUsetId(): string {
		return ''
	}
	/**
	 * 获取用户id
	 */
	public getUsetPhone(): string {
		return ''
	}
	/**
	 * 获取用户姓名
	 */
	public getUsetName(): string {
		return ''
	}
	/**
	 * 获取用户姓名
	 */
	getUseNicktName(): string {
		return ''
	}
}
export class Popup {
	/**
	 * @description 提示框，默认显示2000ms
	 * @param text 提示文字，
	 * @param type 可不填，true为成功提示，空或false为失败提示
	 */
	public toast(text: string | any, type?: boolean) {
		if (text) {
			uni.showToast({
				title: String(text),
				icon: type ? 'success' : 'none'
			})
		}
	}

	/**
	 * @description 模态弹窗，显示确认和取消按钮
	 * @param title 提示标题，
	 * @param content 提示内容，
	 * @returns  Promise 使用.then({})表示确定操作.catch({})表示取消操作
	 */
	public modal({ title, content }: { title: string; content: string }) {
		return new Promise<void>((resolve, reject) => {
			uni.showModal({
				title,
				content,
				success: (res) => {
					res.confirm ? resolve() : reject()
				},
				fail: (err) => {
					console.log('modal模态弹窗报错：', err)
				}
			})
		})
	}

	/**
	 * @description 模态弹窗，只显示确认按钮
	 * @param title 提示标题，
	 * @param content 提示内容，
	 * @returns  Promise 使用.then({})表示确定操作
	 */
	public alert({ title, content }: { title: string; content: string }) {
		return new Promise<void>((resolve) => {
			uni.showModal({
				title,
				content,
				showCancel: false,
				success: () => {
					resolve()
				},
				fail: (err) => {
					console.log('alert模态弹窗报错：', err)
				}
			})
		})
	}
}
class wxapp {
	public static setData(obj: { [x: string]: any }, callback?: () => void) {
		const that = this as any
		let keys = []
		let val: any, data: { [x: string]: any }
		Object.keys(obj).forEach(function(key) {
			keys = key.split('.')
			val = obj[key]
			data = that
			keys.forEach(function(key2, index) {
				if (index + 1 == keys.length) {
					that.$set(data, key2, val)
				} else {
					if (!data[key2]) {
						that.$set(data, key2, {})
					}
				}
				data = data[key2]
			})
		})
		if (callback) {
			callback()
		}
	}
}

export const setData = wxapp.setData

// 获取用户当前设置
export const getSetting = () => {
	return new Promise((resolve, reject) => {
		uni.getSetting({
			success: function(res) {
				resolve(res)
			},
			fail: function(err) {
				reject(err)
			}
		})
	})
}

// 选择地图位置

export const chooseLocation = () => {
	return new Promise((resolve, reject) => {
		uni.chooseLocation({
			success: function(res) {
				resolve(res)
			},
			fail: function(err) {
				reject(err)
			}
		})
	})
}
