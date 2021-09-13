/*
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-07-17 13:29:52
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-13 18:40:50
 */
import { VuexModule, Module, Action, getModule, Mutation } from 'vuex-module-decorators'
import store from '@/store'
@Module({ dynamic: true, store, namespaced: true, name: 'orderStore' })
export class OrderStore extends VuexModule {
  public showDeliverId = ""

  @Mutation
  private SET_DELIVER_ID(deliverId: string) {
    this.showDeliverId = deliverId
  }

  @Action
  public setShowDeliverId(data: string) {
    this.SET_DELIVER_ID(data)
  }
}


export const orderStore = getModule(OrderStore)