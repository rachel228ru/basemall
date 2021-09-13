/*
 * @description:
 * @Author: vikingShip
 * @Date: 2021-07-20 10:47:49
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 10:55:36
 */
import {
  VuexModule,
  Module,
  Action,
  getModule,
  Mutation,
} from "vuex-module-decorators";
import store from "@/store";
import { MenuState } from "../modulesType/menuType";
import { RouteConfig } from "vue-router";
import { getAsyncRouterMap } from "@/api/sign";
import routerMap from "@/routes/routes";

@Module({ dynamic: true, store, namespaced: true, name: "menuStore" })
export class MenuStore extends VuexModule implements MenuState {
  public routersId = "";

  public routers: RouteConfig[] = [];

  public addRouters = [];
  /**
   * @LastEditors: vikingShip
   * @description: 设置路由
   */

  @Mutation
  private SET_ROUTERS(value) {
    this.routers = value;
  }

  /**
   * @LastEditors: vikingShip
   * @description: 设置路由ID
   */

  @Mutation
  private SET_ROUTERS_ID(value) {
    this.routersId = value;
  }

  /**
   * @LastEditors: vikingShip
   * @description: 获取用户目录
   */

  @Action
  public getInfo() {
    return new Promise(resolve => {
      getAsyncRouterMap().then(res => {
        const data = res.data;
        resolve(data);
      });
    });
  }
  /**
   * @LastEditors: vikingShip
   * @description: 初始化路由
   */

  @Action
  public generateRoutes() {
    return new Promise(resolve => {
      const accessedRouters = routerMap;
      this.SET_ROUTERS(accessedRouters);
      this.SET_ROUTERS_ID("");
      resolve(null);
    });
  }
}

export const menuStore = getModule(MenuStore);
