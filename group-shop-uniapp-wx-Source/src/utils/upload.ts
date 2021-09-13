/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-20 13:54:20
 * 123
 */
export const uploadFile = (filePath: any, formData?: any) => {
	return new Promise(
		(
			resolve: (value: string) => void,
			reject: (value: UniApp.GeneralCallbackResult) => void
		) => {
			uni.uploadFile({
				url: process.env.VUE_APP_UPLOAD_URL
					? process.env.VUE_APP_UPLOAD_URL
					: '',
				filePath,
				name: 'file',
				header: {
					'Content-Type': 'multipart/form-data'
				},
				formData,
				success: ({ data }) => {
					resolve(JSON.parse(data).data)
				},
				fail: (err) => {
					reject(err)
				},
				complete: () => null
			})
		}
	)
}
