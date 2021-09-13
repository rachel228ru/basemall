/*
 * @description:
 * @Author: vikingShip
 * @Date: 2021-07-28 09:25:02
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-16 13:41:50
 */
/** 当前环境 */
declare const NODE_ENV: 'develpment' | 'production'

/** 基础请求地址 */
declare const BASE_URL: string

/** 上传地址 */
declare const UPLOAD_URL: string

/** 版本 */
declare const VUE_APP_APP_REQUEST_VERSION: string

/** 版本 */
declare const APP_SHOW_VERSION: string

declare const APP_ID: string

/** 小程序最低基础库版本 */
declare const APP_BASE_LIB_VERSION: string

declare module '*.json' {
	const value: any
	export default value
}
declare const UniApp: UniApp

export type IidType = null | number | string
