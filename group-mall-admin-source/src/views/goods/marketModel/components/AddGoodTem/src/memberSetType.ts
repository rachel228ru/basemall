/*
 * @description:
 * @Author: vikingShip
 * @Date: 2021-08-16 16:15:47
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-16 16:23:14
 */
import { ApiSkuType, ApiMemberType } from "../../../goodType";
export interface MemberSetState {
  memberList: MemberListItem[];
  vipList: ApiMemberType[];
  chooseRadio:string
  setValue:boolean
}

export interface MemberListItem
  extends Pick<ApiSkuType, "originalPrice" | "price"> {
  vipList: ApiMemberType[];
}
