/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-17 11:18:01
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-17 11:18:43
 * 123
 */
export interface ISearchPlugin {
	name: String
	show: Boolean
	query: {
		name: string
	}
}
