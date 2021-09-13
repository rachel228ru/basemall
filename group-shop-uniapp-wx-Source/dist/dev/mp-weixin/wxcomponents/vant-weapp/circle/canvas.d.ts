/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-30 14:31:25
 */
/// <reference types="miniprogram-api-typings" />
declare type CanvasContext = WechatMiniprogram.CanvasContext
export declare function adaptor(
	ctx: CanvasContext & Record<string, unknown>
): CanvasContext
export {}
