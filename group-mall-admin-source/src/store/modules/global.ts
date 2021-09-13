/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-03 09:51:17
 */
import {
  VuexModule,
  Module,
  Action,
  getModule,
  Mutation,
} from "vuex-module-decorators";
import store from "@/store";
import {
  GlobalState,
  RecycleSetting,
  OrderSettingType,
} from "../modulesType/globalType";
import { Howl } from "howler";
import { getOrderManageSetting } from "@/api/order";

@Module({ name: "globalStore", dynamic: true, namespaced: true, store })
export class GlobalStore extends VuexModule implements GlobalState {
  public sounds = [];

  public timer = null;

  public setting = {} as OrderSettingType;

  public recover = {} as RecycleSetting;

  public isOpend = false;

  @Mutation
  private SET_SHOP_SETTING(value: OrderSettingType) {
    this.setting = value;
  }

  @Mutation
  private SET_RECYCLE_INFO(value: RecycleSetting) {
    this.recover = value;
  }

  @Action({ rawError: true })
  public initSocket() {
    // FIXME 引用user
    const userInfo = this.context.rootState.userStore.userInfo;

    if (
      this.isOpend ||
      !userInfo ||
      !userInfo.shopInfoVo ||
      !userInfo.shopInfoVo.shopId
    )
      return;

    this.getShopSetting();
  }

  /**
   * @LastEditors: vikingShip
   * @description: 获取订单设置
   */
  @Action
  public async getShopSetting() {
    const res = await getOrderManageSetting();
    this.SET_SHOP_SETTING(res.data);
    return res.data;
  }
  /**
   * @LastEditors: vikingShip
   * @description: 处理播放
   */

  @Action
  public async handlePlay() {
    try {
      await this.playSound({
        url: require("@/assets/order-reminder.mp3"),
      });
      return true;
    } catch (err) {
      return Promise.reject(false);
    }
  }
  /**
   * @LastEditors: vikingShip
   * @description: 播报声音
   * @param data 音频地址
   */

  @Action
  public playSound(data: { url: string }) {
    const { url } = data;
    return new Promise((resolve, reject) => {
      const howler = new Howl({
        src: [url],
      });

      let timer = null;

      howler.play();

      howler.on("play", () => {
        setTimeout(resolve, 2000);

        clearTimeout(timer);
      });

      timer = setTimeout(() => {
        reject("time out");
      }, 3000);

      howler.on("playerror", err => {
        reject(err);
      });

      howler.on("loaderror", err => {
        reject(err);
      });
    });
  }
}

export const globalStore = getModule(GlobalStore);
