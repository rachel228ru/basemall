/*
 * @description:
 * @Author: vikingShip
 * @Date: 2021-07-28 09:25:02
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-30 14:25:23
 */
import {
	VuexModule,
	Module,
	Mutation,
	Action,
	getModule
} from 'vuex-module-decorators'
import store from '@/store'

import { ModuleStateType, ShopInfoType } from '../modulesType/shopsetType'
import { getShopManage } from '@/api/modules/user'
import storage from '@/utils/storage'
import isEqual from 'lodash/isEqual'

@Module({ name: 'shopSetStore', dynamic: true, namespaced: true, store })
export class ShopSet extends VuexModule implements ModuleStateType {
	public shopInfo = {} as ShopInfoType
	@Mutation
	private SET_SHOP_INFO(value: ShopInfoType) {
		this.shopInfo = value
	}
	@Action
	public async getshopset(value?: ShopInfoType) {
		const res = await getShopManage({})
		if (res && !isEqual(res, storage.get('getshopsetData'))) {
			storage.set('getshopsetData', res, 3 * 24 * 60 * 60 * 1000)
		}
		if (value) {
			this.SET_SHOP_INFO(value)
		}
	}
}

export const shopSetStore = getModule(ShopSet)
