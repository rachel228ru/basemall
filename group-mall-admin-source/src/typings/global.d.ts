/*
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-07-17 13:29:52
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-26 09:42:07
 */
import Vue from "vue";
import { RootState } from '@/main'
declare module "vue/types/vue" {
  interface Vue {
    $STORE: RootState
    /** 滚动到顶部 */
    $scrollTop: (el?: any) => void;
    /** 连接socket */
    $connect: () => any;
    /** 断连 */
    $disconnect: () => any;
    
    [key:string]:any
  }
}

