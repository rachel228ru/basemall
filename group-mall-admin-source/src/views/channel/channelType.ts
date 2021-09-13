/*
 * @description:
 * @Author: vikingShip
 * @Date: 2021-08-23 13:54:58
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 13:27:53
 */

/**
 * @LastEditors: vikingShip
 * @description: 微信小程序设置
 * @param miniName 小程序名称
 * @param logo 小程序头像||公众号头像
 * @param qrcode 小程序码||公众号二维码
 * @param signature 小程序介绍|公众号介绍
 * @param principalName 主体信息
 * @param serviceClass 服务类目
 * @param appid 小程序id
 * @param runFlag 运行状态 false-已关闭  true-运行中
 * @param authorizerFlag 小程序状态授权  true授权  false未授权
 * @param auditStatus 审核状态 0审核通过,1审核失败，2审核中
 * @param currentVersionNumName 当前版本号名称
 * @param currentVersionSendTime 当前版本号发布时间
 * @param versionUpdateNumName 最新的版本号名称
 * @param versionUpdateTime 最新版本号更新时间
 * @param auditingVersionNumName 审核中的版本名称
 * @param auditingVersionSummitTime 审核提交时间
 * @param auditingComeToNothingReason 审核失败原因
 * @param auditingVersionEndTime 审核结束时间
 * @param serviceTypeInfo 授权方类型
 * @param verifyTypeInfo 授权方认证类型
 * @param alias 微信号/公众号
 * @param mchId 微信支付商户号
 * @param certificatesState 支付证书上传状态 false-未上传 true-已上传
 * @param auditingTemplateDetailMinisId 审核店铺模版详情小程序版本子表id
 */
export interface MpSettingType {
  miniName: string;
  logo: string;
  qrcode: string;
  signature: string;
  principalName: string;
  serviceClass: string;
  appid: string;
  runFlag: string;
  authorizerFlag: string;
  auditStatus: number | null;
  currentVersionNumName: string;
  currentVersionSendTime: string;
  versionUpdateNumName: string;
  versionUpdateTime: string;
  auditingVersionNumName: string;
  auditingVersionSummitTime: string;
  auditingComeToNothingReason: string;
  auditingVersionEndTime: string;
  serviceTypeInfo: number | null;
  verifyTypeInfo: number | null;
  alias: string;
  authInfo?: any[];
  mchId?: string | number;
  certificatesState?: boolean;
  codeVersionVos?: any[];
  auditingTemplateDetailMinisId?: string;
}
