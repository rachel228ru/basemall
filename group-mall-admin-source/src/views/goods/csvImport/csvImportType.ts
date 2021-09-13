/*
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-08-18 13:42:21
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-18 13:49:03
 */
import {GoodDetailInfo} from '@/views/goods/marketModel/goodType'
/**
 * @LastEditors: vikingShip
 * @description: CsvImport/state
 * @param searchText 搜索关键词
 * @param csvList csv数组
 * @param uploadDialog 上传loading显隐标识
 * @param canClick 是否可批量删除
 */
export interface CsvImportState{
    searchText:string
    csvList:GoodDetailInfo[]
    pageSize:number
    pageNum:number
    total:number
    uploadDialog:boolean
    canClick:boolean
}