/*
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-08-21 09:28:28
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-23 16:36:28
 */
export class TemplateVersion implements ITemplateVersion {
    // 小程序模版库中templateId
    codeTempleteId= "";

    // 小程序代码版本号
    codeTempleteVersion= "";

    // id,更新接口时使用
    id= null;

    // 业务基础库id
    librariesInfoId= null;

    // 业务基础库名称
    librariesInfoName="";

    // 移动端路径
    mobileTerminalUrl="";

    // 移动端版本
    mobileTerminalVersion="";

    // pc端路径
    pcTerminaUrl="";

    // pc端版本
    pcTerminaVersion="";

    // 店铺模版id
    shopTemplateId=null;

    // pc端关联页面,json存储,键值对
    pcUrlMap="";

    serverCount=null;

    // 版本号
    version="";

    // 更新日志
    versionLog="";

    miniCodeVersions:MiniCodeVersions[]=[];
}

export interface MiniCodeVersions {
    // 小程序模版库中templateId
    codeTempleteId: string;
    // 小程序代码版本号
    codeTempleteVersion: string;
    id: number;
    versionExplain: string;
}

export interface ITemplateVersion {
    miniCodeVersions: MiniCodeVersions[];
    // id,更新接口时使用
    id: number | null;
    // 业务基础库id
    librariesInfoId: string | null;
    // 业务基础库名称
    librariesInfoName: string;
    // 移动端路径
    mobileTerminalUrl: string;
    // 移动端版本
    mobileTerminalVersion: string;
    // pc端路径
    pcTerminaUrl: string;
    // pc端版本
    pcTerminaVersion: string;
    // pc端关联页面,json存储,键值对
    pcUrlMap:string;
    // 店铺模版id
    shopTemplateId: number | null;
    // 店铺数量
    serverCount: number | null;
    // 版本号
    version: string;
    // 更新日志
    versionLog: string;
}
