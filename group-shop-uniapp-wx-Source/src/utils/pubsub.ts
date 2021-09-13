/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-20 14:12:35
 * 123
 */
type IEvent = 'app-launch'
type IModuleEvent = 'onReachBottom'
type ShowEvent = 'onShow'
type RollEvent = 'onPageScroll'
type TapEvent = 'onTap'
type Search = 'OnSearch'
type Popup = 'OnPopup'

type IEv =
	| IEvent
	| IModuleEvent
	| ShowEvent
	| RollEvent
	| TapEvent
	| Search
	| Popup

export default class Pubsub {
	private handles: any = {}

	/**
	 * @description
	 * @param type 触发名
	 * @param handle 触发方法
	 */
	public on(type: IEv, handle: () => void) {
		if (!this.handles[type]) {
			this.handles[type] = []
		}
		this.handles[type].push(handle)
	}

	/**
	 *
	 * @param type 添加的监听名
	 * @param arg
	 */
	public emit(
		type: IEvent | IModuleEvent | ShowEvent | RollEvent | TapEvent,
		...arg: any[]
	) {
		if (this.handles[type]) {
			for (const handle of this.handles[type]) {
				handle.apply(this, arg)
			}
		}
	}

	public off(type: IEv, handle: any) {
		const handles = this.handles[type]
		if (handles) {
			if (!handle) {
				handles.length = []
			} else {
				for (let i = 0; i < handles.length; i++) {
					const pHandle = handles[i]
					if (pHandle === handle) {
						handles.splice(i, 1)
					}
				}
			}
		}
	}
}
