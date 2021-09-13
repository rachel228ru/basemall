/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-30 14:30:29
 */
import Bxios from './bxios'
import errorHandler from './errorHandler'
import { userStore } from '@/store/modules/user'
const excludesUrl = [
	'/shipping/get/point/note/now',
	'/shipping/get/point/note/history',
	'/shops/renovation/template/def/one'
]

const assmbleUrl = [
	'/assemble/association/ass_point',
	'/assemble/association/ass',
	'/assemble/partner/admin/send_code',
	'/assemble/partner/admin/check_message_code',
	'/assemble/activity/ass/list/product_show_category'
]

const bxios = new Bxios({ baseUrl: process.env.VUE_APP_BASE_URL })

bxios.interceptors.request.use(
	(conf) => {
		const { token, tenantId, infoExtends } = userStore.userInfo

		Object.assign(conf.header, {
			token,
			tenantId: tenantId || process.env.VUE_APP_TENAN_ID,
			shopId: infoExtends && infoExtends.shopId,
			version: process.env.VUE_APP_APP_REQUEST_VERSION
		})

		return conf
	},
	(err) => {
		return Promise.reject(err)
	}
)

bxios.interceptors.response.use<any>(
	(res) => {
		if (assmbleUrl.find((i) => res.config.url.indexOf(i) !== -1)) {
			return res
		}
		if (res.data.code === 200) {
			if (excludesUrl.find((i) => res.config.url.indexOf(i) !== -1)) {
				return res.data
			} else {
				return res.data.data
			}
		}
		return Promise.reject(errorHandler(res.data))
	},
	(err) => {
		return Promise.reject(err)
	}
)

export default bxios
