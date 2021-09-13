/*
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-08-03 09:35:47
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-03 09:52:59
 */

/**
 * @LastEditors: vikingShip
 * @description: 组件state中类型
 * @param style 搜索框样式
 * @param formData 父级传参
 * @param historyWords 历史关键词数组
 * @param show 弹窗显示标识
 * @param query 输入关键词
 * @param scope 搜索范围 2商超
 */
export interface SearchState{
    style: string 
    formData: SearchProp
    historyWords:string[]
    show: boolean
    query: {
      name: string
    }
    scope:number
}

/**
 * @LastEditors: vikingShip
 * @description: 父组件传值
 * @param showStyle 展示样式
 * @param keyWord 关键词
 * @param hotWord 历史搜索词
 */
export interface SearchProp{
    showStyle:string
    keyWord:string
    hotWord:string[]
}