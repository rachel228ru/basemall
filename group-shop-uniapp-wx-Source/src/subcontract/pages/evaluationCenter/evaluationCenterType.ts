/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-19 13:34:51
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-19 13:45:46
 * 123
 */
export interface IEvaluate {
	createTime: string
	id: string
	itemList: IEvaluateItem[]
	orderId: string
	shopRate: number
}

export interface IEvaluateItem {
	choice: false
	comment: string
	createTime: string
	id: string
	orderId: string
	picture: string
	pictureArr?: string[]
	productId: string
	productName: 'Dior - 伊甸洛克 香水'
	productPic: string
	productPrice: number
	productQuantity: number
	productSkuId: string
	rate: number
	reply?: string
	shopId: string
	specs: string
	tenantId: string
	updateTime: string
}
