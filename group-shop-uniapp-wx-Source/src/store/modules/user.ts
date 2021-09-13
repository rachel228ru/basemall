/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 16:02:52
 * 123
 */
import {
	VuexModule,
	Module,
	Mutation,
	Action,
	getModule
} from 'vuex-module-decorators'
import store from '@/store'

import { UserStoreState, PolyUserInfo } from '../modulesType/userType'
import { GoodInfo } from '@/pages/index/modules/shopCar/shopCarType'
import { userMsgUpdate, userMsgGet, login } from '@/api/modules/user'
import { getCarsList } from '@/api/modules/shopCar'
import storage from '@/utils/storage'
import isEqual from 'lodash/isEqual'

interface GoodInfoPoly extends GoodInfo {
	skuList: number[]
}
@Module({ name: 'userStore', dynamic: true, namespaced: true, store })
export class UserStore extends VuexModule implements UserStoreState {
	public userInfo = {
		tenantId: '',
		token: '',
		couponType: true
	} as PolyUserInfo
	public goodsNumber = 0
	/**
	 * @LastEditors: vikingShip
	 * @description: 设置用户信息
	 * @param 用户信息集合
	 */
	@Mutation
	private SET_USER_INFO(value: PolyUserInfo) {
		this.userInfo = value
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 设置全局购物车数量
	 * @param value 购物车数量
	 */
	@Mutation
	private SET_GOODS_NUMBER(value: number) {
		this.goodsNumber = value
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 获取code
	 */
	@Action
	public getCode(): Promise<{ code: string }> {
		return new Promise((resolve) => {
			//获取微信小程序 OPENID
			uni.login({
				success: (login_res) => {
					if (login_res.errMsg == 'login:ok') {
						resolve(login_res)
					}
				}
			})
		})
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 微信登陆方法
	 * @param type 是否清除登录缓存，1是，0否
	 */
	@Action
	public async login(type = 0) {
		if (type === 1) {
			storage.remove('getLoginData')
		}

		let userInfo = this.userInfo
		const { code } = await this.getCode()
		const { sessionKey, token } = await login({ code })
		userInfo.token = token
		const user = await userMsgGet({ infoLevel: 2 })
		userInfo = {
			...userInfo,
			...user,
			...{
				couponType: true,
				sessionKey
			},
			nickName: user.nikeName
		}
		const transData = {
			goodData: {},
			shopUserId: userInfo.shopUserId
		}
		if (user.whetherAuthorization) {
			userMsgUpdate(userInfo)
		}
		userInfo.transData = transData
		userInfo.infoExtends = user.infoExtends
		const res = userInfo
		if (res && !isEqual(res, storage.get('getLoginData'))) {
			storage.set('getLoginData', res, 3 * 24 * 60 * 60 * 1000)
		}
		this.SET_USER_INFO(userInfo)
	}
	/**
	 * @LastEditors: vikingShip
	 * @description:  获取购物车数量
	 */
	@Action
	public async getShopCarNum() {
		const res: GoodInfoPoly[] = await getCarsList()
		const useList: GoodInfoPoly[] = []
		const noList: GoodInfoPoly[] = []
		let goodsNumber = 0
		if (!res) {
			goodsNumber = 0
		} else {
			const list = res
			list.forEach((item) => {
				item.skuList = []
				item.skuStocks.forEach((v) => {
					item.skuList.push(v.id)
				})
				const type = item.skuStocks.every((v) => {
					return v.stock === 0
				})
				item.status = type ? 2 : item.status
				if (item.deleted === 1) {
					item.status = 2
				}
				item.status = item.skuList.indexOf(item.skuId) === -1 ? 0 : item.status
				if (item.activityEndTime) {
					item.status =
						item.activityStatus === '1' || item.activityStatus === '0' ? 1 : 0
				}
				if (item.status === 1) {
					useList.push(item)
				} else {
					noList.push(item)
				}
			})
			goodsNumber = useList.reduce((preVal: number, item) => {
				return preVal + item.goodsNumber
			}, 0)
		}
		this.SET_GOODS_NUMBER(goodsNumber)
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 更新用户数据
	 * @param userInfo 用户数据信息
	 */
	@Action
	public async updateUser(userInfo: UniApp.UserInfo) {
		await userMsgUpdate(userInfo)
		await this.login()
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 获取第三方tenantId
	 */
	@Action
	public getTenId() {
		const tenantId = uni.getExtConfigSync
			? uni.getExtConfigSync().tenantId
			: process.env.VUE_APP_TENAN_ID
		const userInfo = {
			...this.userInfo,
			tenantId: tenantId
		}
		this.context.commit('SET_USER_INFO', userInfo)
	}
	@Action
	public setUserInfo(value: PolyUserInfo) {
		this.SET_USER_INFO(value)
	}
}

export const userStore = getModule(UserStore)
