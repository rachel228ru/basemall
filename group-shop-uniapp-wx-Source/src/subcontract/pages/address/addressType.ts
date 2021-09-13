/*
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-08-12 13:32:02
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-12 14:34:39
 */
import { IAdress } from '@/modules/pages/submit/interface'
export type ApiGetAddressOmit = Omit<IAdress, "tenantId" | "createTime" | "updateTime" | "deleted" | "lngLat">
export interface ApiGetAddressType extends ApiGetAddressOmit {
    defaultStatus: string | number
}
export interface AddressState {
    defaultArea: string
    showSelectLocation?: boolean
    authAddressStatus: string
    authLocationStatus: string
    redirect: string
}
type AddressFormOmit = Omit<ApiGetAddressType, "isDefault" | "userId">
export interface AddressForm extends AddressFormOmit {
    defaultStatus: string | number
}
export interface ProvinceListItemType{
    [x:number]:string
}