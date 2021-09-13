/*
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-08-21 09:28:28
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-23 16:37:30
 */
import {ITemplateVersion} from "./TemplateVersion";

export class Template implements ITemplate {
    id: number | null;

    name: string;

    code: string;

    type: number | null;

    description: string;

    createTime: string;

    shopTemplateVos: ITemplateVersion[];

    constructor(type?: number) {
      this.type = null;
      this.id = null;
      this.name = "";
      this.code = "";
      this.description = "";
      this.createTime = "";
      this.shopTemplateVos = [];
      if (type !== undefined) {
        this.type = type;
      }
    }

}

export interface ITemplate {
    id: number | null;
    name: string;
    code: string;
    type: number | null;
    description: string;
    createTime: string;
    shopTemplateVos: ITemplateVersion[];
}