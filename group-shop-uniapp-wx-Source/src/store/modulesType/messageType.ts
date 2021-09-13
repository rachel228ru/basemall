/*
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-08-07 10:55:40
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-07 11:12:06
 */
/**
 * @LastEditors: vikingShip
 * @description: 
 * @param templates 模板列表
 * @param type 模板类型
 */
export interface MessageStoreState {
    templates: IMessage[]
    type:MessageType
}

export interface IMessage {
    id: string;
    mark: string;
    miniTemplateId: string;
    miniOpen: number;
    miniMsg: string;
}

export type MessageType = Record<"sendMsg" | "auditMsg" | "memberCartOpen" | "orderRefund" | "orderRefundNotify", string>