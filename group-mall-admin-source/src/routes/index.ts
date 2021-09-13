/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-26 17:23:33
 */
import Vue from "vue";
import Router from "vue-router";
import { beforeCallBackasync, afterCallBackasync } from "@/routes/tool";
import MMain from "@/components/addShopMM/MMain.vue";
import MContent from "@/components/addShopMM/MContent.vue";

Vue.use(Router);

//获取原型对象上的push函数
const originalPush = Router.prototype.push;
//修改原型对象中的push方法
Router.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err);
};

const constantRouterMap = [
  {
    path: "/sign",
    name: "sign",
    component: () =>
      import(/* webpackChunkName: "sign" */ "@/views/sign/Index.vue"),
    meta: {
      title: "登录",
      isShow: 1,
    },
  },
  {
    path: "/",
    component: MMain,
    children: [
      {
        path: "/console",
        component: MContent,
        meta: {
          title: "我的店铺",
        },
        children: [
          {
            path: "",
            name: "shops",
            component: () => import("@/views/shops/Index.vue"),
          },
        ],
      },
      {
        path: "",
        component: MContent,
        meta: {
          title: "创建店铺",
        },
        children: [
          {
            path: "/console/create",
            name: "create",
            component: () => import("@/views/shops/Create.vue"),
          },
        ],
      },
    ],
  },
];

const router = new Router({
  mode: "history",
  base: process.env.VUE_APP_BASEPATH,
  routes: constantRouterMap,
});

router.beforeEach(beforeCallBackasync);

router.afterEach(afterCallBackasync);

export default router;
