/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:20
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-16 09:50:42
 * 123
 */
import Date from '../mixins/date'
import { Component, Vue } from 'vue-property-decorator'

@Component({})
export default class indexModuleHook extends Vue {
	public date: Date

	public ms: any

	constructor() {
		super()
		this.date = new Date(this.ms)
	}
}
