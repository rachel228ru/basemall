/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-30 14:30:09
 * 123
 */
import { LinkSelectItem } from '@/pages/index/modules/home/components/cube-box/cubeBoxType'

export const navtofun = (
	itemLink: LinkSelectItem,
	self: any,
	formType?: any
) => {
	if (itemLink.url === '/pages/integralShop/integralShop') {
		const userInfo = self.$STORE.userStore.userInfo
		if (!userInfo.whetherAuthorization) {
			self.authType = true
			return
		}
	}
	if (itemLink.type === 7 && itemLink.append === 'appmodel') {
		//点击跳转小程序
		uni.navigateToMiniProgram({
			appId: itemLink.url,
			envVersion: 'release'
		})
		return
	}
	if (itemLink.type === 0 && itemLink.id === 8) {
		//点击客服
		return
	}
	if (itemLink.url) {
		const pageList = self.$STORE.setStore.pageList
		const newPages = pageList.filter((item: { isDef: string }) => {
			return item.isDef === '1'
		})

		const buttomList = self.$STORE.setStore.tabBar.list
		const jumpToButtom = buttomList.filter(
			(item: { linkName: string | undefined }) => {
				return item.linkName === itemLink.name
			}
		)
		if (jumpToButtom.length > 0) {
			if (itemLink.type === 5) {
				self.$STORE.setStore.setCurrentPageId(jumpToButtom[0].id)
				self.$STORE.setStore.setNewCurrentPageId(jumpToButtom[0].id)
				self.$STORE.setStore.setCurrentTab(jumpToButtom[0].name)
				self.$STORE.setStore.setNewJump({
					nid: jumpToButtom[0].id,
					name: jumpToButtom[0].text
				})
			} else {
				self.$STORE.setStore.setCurrentPageId(jumpToButtom[0].id)
				self.$STORE.setStore.setCurrentTab(jumpToButtom[0].name)
				self.$STORE.setStore.setIndexTitle(jumpToButtom[0].text)
			}
			return
		}
		if (itemLink.type === 0 || itemLink.type === 5 || itemLink.type === 2) {
			// 0功能页面  2展示分类
			if (itemLink.append || itemLink.type === 5) {
				if (itemLink.type === 5 && itemLink.append !== 'home') {
					self.$STORE.setStore.setHasInCustom(true)
					self.$STORE.setStore.setNewCurrentPageId(itemLink.id)
					uni.navigateTo({
						url: `/pages/index/custom/custom`
					})
					return
				} else {
					if (itemLink.url.indexOf('pages/index/') === -1) {
						// 如果跳转路径不存在首页路径，判断append是否有值，有跳转到对应路径, 需要与底部导航的路径做区分
						uni.navigateTo({
							url: `/subcontract${itemLink.url}`
						})
						return
					}
					if (itemLink.append === 'mall' && itemLink.type === 2) {
						if (itemLink.selectedIconPath) {
							// 选择商超某个分类后进入分类页面
							const selectItem = buttomList.filter((item: { name: string }) => {
								return item.name === itemLink.append
							})
							self.$STORE.setStore.setCurrentPageId(selectItem[0].id)
							self.$STORE.setStore.setCurrentTab(itemLink.append)
							self.$STORE.setStore.setIndexTitle(selectItem[0].text)
							return
						} else {
							uni.navigateTo({
								url: `/subcontract/pages/spellPage/index?titleName=${itemLink.name}&id=${itemLink.id}`
							})
							return
						}
					}
					if (Number(itemLink.id) === 471) {
						self.$STORE.setStore.setNewCurrentPageId(newPages[0].id)
					} else {
						self.$STORE.setStore.setNewCurrentPageId(itemLink.id)
					}
					self.$STORE.setStore.setCurrentPageId(itemLink.id)
					self.$STORE.setStore.setCurrentTab(itemLink.append)
				}
				self.$STORE.setStore.setHistoryPage(itemLink)
				if (formType === 'poster') {
					const selectItem = buttomList.filter((item: { name: string }) => {
						return item.name === itemLink.append
					})
					self.$STORE.setStore.setIndexTitle(selectItem[0].text)
				}
			}
		} else if (itemLink.type === 1) {
			// 1商超页面
			uni.navigateTo({
				url: `/subcontract${itemLink.url}?id=${itemLink.id}`
			})
		} else if (itemLink.type === 6) {
			// 外部链接
			uni.navigateTo({
				url: `/subcontract/pages/linkPage/linkPage?src=${itemLink.url}`
			})
		}
		if (itemLink.type !== 6) {
			self.$STORE.setStore.setNewCurrentPage(itemLink)
			self.$Pubsub.emit('onTap')
		}
	} else {
		// console.error("页面路径为空");
	}
}
