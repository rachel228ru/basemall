/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-30 14:27:44
 * 123
 */
const page = {
	methods: {
		bezier(points: IPoint[], times: number) {
			const bezierPoints = []
			const pointsD = []
			const pointsE = []
			const distAb = Math.sqrt(
				Math.pow(points[1]['x'] - points[0]['x'], 2) +
					Math.pow(points[1]['y'] - points[0]['y'], 2)
			)
			const distBc = Math.sqrt(
				Math.pow(points[2]['x'] - points[1]['x'], 2) +
					Math.pow(points[2]['y'] - points[1]['y'], 2)
			)
			const eachMoveAd = distAb / times
			const eachMoveBe = distBc / times
			const tanAb =
				(points[1]['y'] - points[0]['y']) / (points[1]['x'] - points[0]['x'])
			const tanBc =
				(points[2]['y'] - points[1]['y']) / (points[2]['x'] - points[1]['x'])
			const radiusAb = Math.atan(tanAb)
			const radiusBc = Math.atan(tanBc)
			for (let i = 1; i <= times; i++) {
				const distAd = eachMoveAd * i
				const distBe = eachMoveBe * i
				const pointD: IPoint = { x: 0, y: 0 }
				pointD['x'] = distAd * Math.cos(radiusAb) + points[0]['x']
				pointD['y'] = distAd * Math.sin(radiusAb) + points[0]['y']
				pointsD.push(pointD)
				const pointE: IPoint = { x: 0, y: 0 }
				pointE['x'] = distBe * Math.cos(radiusBc) + points[1]['x']
				pointE['y'] = distBe * Math.sin(radiusBc) + points[1]['y']
				pointsE.push(pointE)
				const tanDE = (pointE['y'] - pointD['y']) / (pointE['x'] - pointD['x'])
				const radiusDE = Math.atan(tanDE)
				const distDE = Math.sqrt(
					Math.pow(pointE['x'] - pointD['x'], 2) +
						Math.pow(pointE['y'] - pointD['y'], 2)
				)
				const distDF = (distAd / distAb) * distDE
				const pointF: IPoint = { x: 0, y: 0 }
				pointF['x'] = distDF * Math.cos(radiusDE) + pointD['x']
				pointF['y'] = distDF * Math.sin(radiusDE) + pointD['y']
				bezierPoints.push(pointF)
			}
			return {
				bezierPoints: bezierPoints
			}
		}
	}
}

export type IPoint = Record<'x' | 'y', number>

module.exports = page
