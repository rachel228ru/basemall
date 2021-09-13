/*
 * @description:
 * @Author: chuyinlong
 * @Date: 2021-07-19 09:13:57
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-20 14:01:41
 * 123
 */
import DateUtil from '@/mixins/date'

export class Storage {
	/**
	 * 设置缓存
	 * @param key 键名
	 * @param data 数据
	 * @param expired 持续时间（可选）
	 */
	set(key: string, data: any, expired?: number) {
		uni.setStorageSync(
			key + process.env.VUE_APP_APP_REQUEST_VERSION,
			JSON.stringify({ data, time: +new Date(), expired })
		)
	}

	/**
	 * 获取缓存
	 * @param key 键名
	 */
	get(key: string): any | null {
		try {
			const { data, time = 0, expired = 0 } = JSON.parse(
				uni.getStorageSync(key + process.env.VUE_APP_APP_REQUEST_VERSION)
			)

			if (!expired) return null

			if (time + expired > +new Date()) return data

			uni.removeStorageSync(key)
			return null
		} catch {
			return null
		}
	}

	/**
	 * 清除缓存
	 * @param key 键名
	 */
	remove(key: string) {
		return uni.removeStorageSync(key + process.env.VUE_APP_APP_REQUEST_VERSION)
	}

	/**
	 * 日常清空老缓存，一日一次
	 */
	clear() {
		const dateUtil = new DateUtil(new Date())
		const lastClearTime = uni.getStorageSync('clearDate')
		const toady = dateUtil.getYMD(new Date())

		if (lastClearTime === toady) return

		uni.clearStorageSync()
		uni.setStorageSync('clearDate', toady)
	}
}

export default new Storage()
