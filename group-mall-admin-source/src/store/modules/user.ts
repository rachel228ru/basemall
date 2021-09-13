/*
 * @description:
 * @Author: vikingShip
 * @Date: 2021-08-12 17:34:39
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-26 11:25:56
 */
import {
  VuexModule,
  Module,
  Action,
  getModule,
  Mutation,
} from "vuex-module-decorators";
import store from "@/store";
import { ApiLoginUserType, ShopInfoType, UserState } from "../modulesType/userType";
import storage from "@/libs/storage";

@Module({ dynamic: true, store, namespaced: true, name: "userStore" })
export class UserStore extends VuexModule implements UserState {
  public userInfo = storage.get("userInfo");

  public iphone = "";

  public shopInfo = {} as ShopInfoType;

  public token = storage.get("token");

  @Mutation
  private SET_USER_INFO(value: ApiLoginUserType) {
    this.userInfo = value;
  }

  @Mutation
  private SET_TOKEN(value: string) {
    this.token = value;
  }

  @Mutation
  private SET_IPHONE(value: string) {
    this.iphone = value;
  }

  @Mutation
  private SET_SHOP_INFO(value: ShopInfoType) {
    this.shopInfo = value;
  }

  @Action
  public setUserInfo(data: ApiLoginUserType) {
    this.SET_USER_INFO(data);
  }

  @Action
  public setToken(data: string) {
    this.SET_TOKEN(data);
  }

  @Action
  public setPhone(value: string) {
    this.SET_IPHONE(value);
  }

  @Action
  public setShopInfo(value: ShopInfoType) {
    this.SET_SHOP_INFO(value);
  }
}

export const userStore = getModule(UserStore);
