/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-30 14:29:54
 */
export default (errRes: { code: number; msg: string }) => {
	switch (errRes.code) {
		case 300007:
			return errRes.msg
			break
		case 3:
			return errRes.msg
			break
		default:
			return errRes.msg
	}
}
