/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:06
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 09:48:58
 */
import api from "@/libs/request";
import { modifySignStatus, login as signIn } from "@/libs";
import storage from "@/libs/storage";

export const register = (form: any) => {
  return api.post("/platform-open/account-info/register", form);
};

export const login = (form: any) => {
  return api.post("/platform-open/account-info/login", form);
};

export const loginByCode = (form: any) => {
  return api.put("/platform-open/account-info/login/code", form);
};

// 获取商家路由配置
export const getAsyncRouterMap = () => {
  return api.get("/shops-open/shop/menu/one", {});
};

// 更新商家路由配置
export const upDateAsyncRouterMap = (data: any) => {
  return api.put("/shops-open/shop/menu/update", data);
};

// 账号校验
export const checkoutAccount = (data: any) => {
  return api.get("/platform-open/account-info/checkout/account", data);
};

// 发送一次性验证,后端在common中定义校验类型,有效期统一5分钟,使用即失效,60秒可发送一次
export const getCode = (data?: any) => {
  return api.post("/platform-open/send-code", data, {
    headers: { "Content-Type": "application/json" },
  });
};

// 网站应用扫码
export const scanCode = (data: any) => {
  return api.get(`/platform-open/account-info/scancode`, data);
};

// 根据请求token获取当前用户最新的信息
export const getAccountInfo = async () => {
  const response = await api.get("/platform-open/account-info/info", {});
  const { code, data } = response;
  if (code === 200) {
    modifySignStatus(storage.get("token"), data);
    signIn(storage.get("token"), data);
  }
  return api.get("/platform-open/account-info/info", {});
};

// 发送手机号校验码,验证码有效期5分钟,仅限用户修改个人信息时调用
export const getPhoneCode = (data: any) => {
  return api.get("/platform-open/account-info/phone/code/send", data);
};

// 校验手机号验证码是否正确,验证校验正确一次之后验证码立马无效,校验成功返回有效凭证5分钟之内有效,使用即失效,仅限用户修改个人信息时调用
export const getPhoneVerifyCode = (data: any) => {
  return api.get("/platform-open/account-info/phone/code/verify", data);
};

// 预扫码校验接口,返回state
export const getScanCodeUrl = (data: any) => {
  return api.post("/platform-open/account-info/pre/account/verify", data, {
    headers: { "Content-Type": "application/json" },
  });
};

// 手机号换绑
export const changePhone = (data: any) => {
  return api.put("/platform-open/account-info/phone/change/tie", data, {
    headers: { "Content-Type": "application/json" },
  });
};

// 修改密码
export const changePassword = (data: any) => {
  return api.put("/platform-open/account-info/pass/change/tie", data, {
    headers: { "Content-Type": "application/json" },
  });
};

// 获取指定版本默认值
export const getUniDefaultValue = (uniqueIdentification: any, version: any) => {
  return api.get(
    `/platform-open/default-value/${uniqueIdentification}/${version}`,
    {},
  );
};

// 获取指定系统配置
export const getSystemConfig = () => {
  return api.get(`/platform-open/system-conf/service/config`, {});
};

/**
 * 商户登录接口
 * @param data
 * @query: tenementLoginDto -->  (必须)
 */
export const unionAccountLogin = (data: any) => {
  return api.post("/platform-open/account-info/login-v1", data, {
    headers: { "Content-Type": "application/json" },
  });
};

/**
 * 扫码成功回调,根据code获取制定场景值
 * @query: code --> 微信扫码返回的codo
 */
export const verifyStateResult = (data: any) => {
  return api.get("/platform-open/account-info/verify/state/result", data);
};

// 忘记密码 重置密码接口(目前仅限商户账号修改,商户子账号无法修改)
export const forgetPassword = (data: any) => {
  return api.put("/platform-open/account-info/password-retrieve", data, {
    headers: { "Content-Type": "application/json" },
  });
};

// 验证码校验,校验成功返回凭证码
export const verifyTheCode = (data: any) => {
  return api.put("/platform-open/send-code/verify/code", data, {
    headers: { "Content-Type": "application/json" },
  });
};

// 验证码校验,校验成功返回凭证码
export const getOssConfig = (data: any) => {
  return api.get(`/platform-open/system-conf/current/oss/config`, data);
};

export const modifySystemConfig = (data: any) => {
  return api.post("/platform-open/system-conf", data, {
    headers: { "Content-Type": "application/json" },
  });
};

/**
 * 商户自行注册接口
 * @param data
 * @query: accountRegisterDto -->  商户自行注册表单(必须)
 */
export const registerAccount = (data: any) => {
  return api.post("/platform-open/account-info/oneself/register", data, {
    headers: { "Content-Type": "application/json" },
  });
};

// 根据经纬度返回省市区
export const analysisLatAndLon = (data: any) => {
  return api.get(
    "/account-open/mini-account-address/analysis/latitude-longitude",
    data,
  );
};
