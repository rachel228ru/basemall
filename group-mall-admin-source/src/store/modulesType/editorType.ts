/*
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-08-13 15:16:30
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-13 15:38:23
 */
/**
 * @LastEditors: vikingShip
 * @description: editor中store
 * @param components 组件数组
 * @param currentComponent 当前组件
 */
export interface EditorState {
    components: ComponentItemType[]
    currentComponent: string
}

export interface ComponentItemType {
    type: string
    id: number 
    banners: ComponentBannerItem[]
    padding: number
    imageStyle: string
    angle: string
    indicator: string
}
type ComponentBannerItem = Record<"img" | "link" | "title", string>
