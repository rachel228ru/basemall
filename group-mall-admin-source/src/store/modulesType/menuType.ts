/*
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-08-13 16:31:55
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-25 16:27:21
 */
import { RouteConfig } from "vue-router"
/**
 * @LastEditors: vikingShip
 * @description: menuStore、state
 * @param routersId routersID
 */
export interface MenuState {
    routersId: string
    routers: RouteConfig[]
    addRouters: RouteConfig[]
}