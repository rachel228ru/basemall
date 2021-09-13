package com.medusa.gruul.platform.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.platform.api.entity.SystemConf;
import com.medusa.gruul.platform.api.model.dto.OssConfigDto;
import com.medusa.gruul.platform.model.dto.*;
import com.medusa.gruul.platform.model.vo.CodeEmailConfVo;
import com.medusa.gruul.platform.model.vo.KfmsgVo;
import com.medusa.gruul.platform.model.vo.PayConfigVo;
import com.medusa.gruul.platform.model.vo.SystemConfigVo;

/**
 * <p>
 * 系统配置 服务类
 * </p>
 *
 * @author whh
 * @since 2019-09-20
 */
public interface ISystemConfService extends IService<SystemConf> {

    /**
     * 保存系统配置
     *
     * @param saveConfigDto dto
     */
    void saveValue(SaveConfigDto saveConfigDto);

    /**
     * 获取指定系统配置信息
     *
     * @param type 类型
     * @return 类型详情信息
     */
    SystemConfigVo getTypeInfo(Integer type);

    /**
     * 获取当前使用oss配置
     *
     * @return com.medusa.gruul.platform.api.model.dto.OssConfigDto
     */
    Result<OssConfigDto> currentOssConfig();

    /**
     * 获取指定类型的存储配置
     *
     * @param type 查询类型 0-当前使用的配置 1：七牛  2：阿里云  3：腾讯云
     * @return com.medusa.gruul.platform.api.model.dto.OssConfigDto
     */
    Result<OssConfigDto> ossConfig(Integer type);

    /**
     * 保存支付配置
     *
     * @param savePayConfigDto com.medusa.gruul.platform.model.dto.SavePayConfigDto
     */
    void savePayConfig(SavePayConfigDto savePayConfigDto);

    /**
     * 获取当前系统支付配置
     *
     * @return com.medusa.gruul.platform.model.vo.PayConfigVo
     */
    PayConfigVo getPayConfig();


    /**
     * 发起微信支付二维码生成
     *
     * @param notifyUrl  回调地址
     * @param outTradeNo 订单号
     * @param money      金额
     * @param body       交易信息
     * @param productId  商品id
     * @return com.medusa.gruul.platform.model.dto.PayDto
     * @throws WxPayException
     */
    PayDto wxQrcodePay(String notifyUrl, String outTradeNo, String money, String body, String productId) throws WxPayException;


    /**
     * 支付宝二维码支付
     *
     * @param notifyUrl  回调地址
     * @param subject    标题
     * @param price      价格(单位元)
     * @param outTradeNo 订单号
     * @return com.medusa.gruul.platform.model.dto.PayDto 调用成功返回二维码地址
     * @throws WxPayException com.github.binarywang.wxpay.exception.WxPayException
     */
    PayDto aliPayQrcodePay(String notifyUrl, String subject, String price, String outTradeNo) throws WxPayException;

    /**
     * 获取管理台客服消息相关配置
     *
     * @return com.medusa.gruul.platform.model.vo.KfmsgVo
     */
    KfmsgVo getKfmsg();

    /**
     * 保存或更新客服消息配置
     *
     * @param dto com.medusa.gruul.platform.model.dto.KfAddDto
     */
    void saveKfmsg(KfAddDto dto);

    /**
     * 发送指定客服消息
     *
     * @param sendType    //1-注册通知 2-开票模板消息
     * @param sendCentent 发送的内容   k-v
     */
    void sendKfmsg(Integer sendType, JSONObject sendCentent);

    /**
     * 保存或更新短信邮箱配置
     *
     * @param emailConfDto com.medusa.gruul.platform.model.dto.CodeEmailConfDto
     */
    void saveCodeEmailConf(CodeEmailConfDto emailConfDto);

    /**
     * 获取短信邮箱配置
     *
     * @return com.medusa.gruul.platform.model.vo.CodeEmailConfVo
     */
    CodeEmailConfVo getCodeEmailConf();

}
