/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 16:03:08
 * 123
 */
import { GoodDetailInfo } from './detailType'

const page = {
	methods: {
		canvCard(
			good: GoodDetailInfo,
			type: number,
			shareImgUrl: string,
			codeImg: string
		) {
			const options = {
				actPrice: good.actPrice,
				price: good.price,
				goodsName: good.name
			}
			return new Promise((resolve) => {
				const textPosY = 500

				const priceY = textPosY + 95

				const goodsNameY = textPosY + 25

				const canvas = uni.createCanvasContext(
					'cardCanvas',
					'componentInstance'
				)

				canvas.save()
				canvas.rect(0, 0, 560, 880)
				canvas.setFillStyle('white')
				canvas.fill()
				canvas.drawImage(shareImgUrl, 74, 64, 420, 420)
				canvas.save()

				canvas.beginPath()
				canvas.setFillStyle('#050505')
				this.dealWords({
					canvas,
					fontSize: 28,
					word: `${options.goodsName}`,
					maxWidth: 500,
					x: 40,
					y: goodsNameY,
					maxLine: 1
				})
				canvas.setFontSize(26)
				canvas.setFillStyle('red')
				canvas.fillText(`${type === 0 ? '￥' : '积分'}`, 40, priceY + 10, 50)
				canvas.setFontSize(34)

				canvas.fillText(
					`${options.actPrice}`,
					Number(`${type === 0 ? 0 : 30}`) + 65,
					priceY + 10,
					150
				)
				canvas.fillText(
					`${options.actPrice}`,
					Number(`${type === 0 ? 0 : 31}`) + 66,
					priceY + 10,
					150
				)
				canvas.fillText(
					`${options.actPrice}`,
					Number(`${type === 0 ? 0 : 29}`) + 66,
					priceY + 11,
					150
				)
				if (options.price) {
					const priceWidth = canvas.measureText(`${options.actPrice}`)
					let newWidth = 85
					let temWidth = 105
					uni.getSystemInfo({
						success(res) {
							if (res.model.indexOf('iPhone X') > -1) {
								newWidth = 115
								temWidth = 135
							}
						}
					})
					canvas.setFontSize(22)
					canvas.setFillStyle('#9E9E9E')
					canvas.fillText(
						`￥${options.price}`,
						Number(
							`${
								type === 0 ? 50 + priceWidth.width : newWidth + priceWidth.width
							}`
						) + 30,
						priceY + 10,
						150
					)
					const delPriceWidth = canvas.measureText(`${options.price}`)
					canvas.moveTo(
						Number(
							`${
								type === 0 ? 50 + priceWidth.width : newWidth + priceWidth.width
							}`
						) + 30,
						priceY + 2
					)
					canvas.lineTo(
						Number(
							`${
								type === 0
									? 70 + priceWidth.width + delPriceWidth.width
									: temWidth + priceWidth.width + delPriceWidth.width
							}`
						) + 35,
						priceY + 2
					)
					canvas.setStrokeStyle('#9E9E9E')
				}
				canvas.stroke()

				canvas.drawImage(codeImg, 320, textPosY + 150, 180, 180)
				canvas.setFontSize(24)
				canvas.setFillStyle('#050505')
				canvas.fillText(`长按识别小程序码`, 40, textPosY + 225, 250)
				canvas.setFontSize(24)
				canvas.fillText(`更多爆款等你来抢`, 40, textPosY + 265, 250)
				canvas.draw(false, () => {
					uni.canvasToTempFilePath({
						canvasId: 'cardCanvas',
						success(res) {
							resolve(res.tempFilePath)
						}
					})
				})
			})
		},

		dealWords(options: ICanvasWords) {
			options.canvas.setFontSize(options.fontSize)
			const allRow = Math.ceil(
				options.canvas.measureText(`${options.word}`).width / options.maxWidth
			)
			const count = allRow >= options.maxLine ? options.maxLine : allRow

			const endPos = 0
			for (let j = 0; j < count; j++) {
				const nowStr = options.word.slice(endPos)
				const rowWid = 0
				if (options.canvas.measureText(`${nowStr}`).width > options.maxWidth) {
					this.dealWidth(endPos, j, rowWid, nowStr, options)
				} else {
					options.canvas.fillText(
						nowStr.slice(0),
						options.x,
						options.y + (j + 1) * 18
					)
				}
			}
		},

		dealWidth(
			endPos: number,
			j: number,
			rowWid: number,
			nowStr: string,
			options: ICanvasWords
		) {
			for (let m = 0; m < nowStr.length; m++) {
				rowWid += options.canvas.measureText(`${nowStr[m]}`).width
				if (rowWid > options.maxWidth) {
					if (j === options.maxLine - 1) {
						options.canvas.fillText(
							nowStr.slice(0, m - 1) + '...',
							options.x,
							options.y + (j + 1) * 18
						)
					} else {
						options.canvas.fillText(
							nowStr.slice(0, m),
							options.x,
							options.y + (j + 1) * 18
						)
					}
					endPos += m
					break
				}
			}
		}
	}
}

interface ICanvasWords {
	canvas: UniApp.CanvasContext
	fontSize: number
	word: string
	maxWidth: number
	x: number
	y: number
	maxLine: number
}
export default page
