/*
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-08-21 09:28:28
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-26 10:52:54
 */
export class Library implements ILibrary {
    activetyCount= null;
    
    belongVersion= "";

    belongId= null;

    createTime= "";

    id= null;

    name= "";

    remark= "";

    status= null;

    totleCount= null;
    
    version= "";
}

export interface ILibrary {
    activetyCount: number | null;
    belongVersion: string;
    belongId: number | null;
    createTime: string;
    id: number | null;
    name: string;
    remark: string;
    status: number | null;
    totleCount: number | null;
    version: string;
}

