/*
 * @description:
 * @Author: vikingShip
 * @Date: 2021-08-12 17:34:39
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-14 13:27:29
 */
import Axios from "./axios";
import { userStore } from "@/store/modules/user";
import storage from "@/libs/storage";
import { logout } from "@/libs";
import router from "@/routes";

const excludeUrls = [
  "/shipping/get/deliverClass/list/export",
  "/shipping/get/deliver/list/export",
  "/shipping/get/deliverPoint/list/export",
  "/assemble/activity/ass/list/product_show_category",
  "/shipping/set/shipper/pointInfo",
];

const api = new Axios({
  baseURL: process.env.VUE_APP_BASEURL,
});

api.instance.interceptors.request.use(
  config => {
    if (config.headers && config.headers.type === "formData") {
      config.headers["Content-Type"] = "application/x-www-form-urlencoded"; // 模拟form表单方式提交请求
    } else {
      config.headers.Accept = "application/json";
    }
    config.headers.Contenttype = "application/json";
    config.headers.token = userStore.token || storage.get("token");
    config.headers.version = process.env.VUE_APP_APP_VERSION;
    if (userStore.userInfo && userStore.userInfo.shopInfoVo) {
      config.headers.shopId = userStore.userInfo.shopInfoVo.shopId;
      config.headers.tenantId = userStore.userInfo.shopInfoVo.tenantId;
    }
    return config;
  },
  err => {
    return Promise.reject(err);
  },
);

api.instance.interceptors.response.use(
  res => {
    if (res.data.code === 3) {
      logout();
      router.push("/sign");
    }
    if (excludeUrls.find(i => res.config.url.indexOf(i) !== -1)) {
      return res.data;
    } else if (res.data.code !== 200 || res.status !== 200) {
      return Promise.reject(
        res.data.msg || res.data.message || "未知错误，请稍后再试",
      );
    } else {
      return res.data;
    }
  },
  err => {
    return Promise.reject(err);
  },
);
export default api;
