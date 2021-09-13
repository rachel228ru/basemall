package com.medusa.gruul.platform.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceApacheHttpImpl;
import com.ijpay.alipay.AliPayApi;
import com.ijpay.alipay.AliPayApiConfig;
import com.ijpay.alipay.AliPayApiConfigKit;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.constant.enums.CloudServiceEnum;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.common.core.util.SystemCode;
import com.medusa.gruul.platform.api.entity.SystemConf;
import com.medusa.gruul.platform.api.model.dto.OssConfigDto;
import com.medusa.gruul.platform.conf.*;
import com.medusa.gruul.platform.mapper.SystemConfMapper;
import com.medusa.gruul.platform.model.dto.*;
import com.medusa.gruul.platform.model.vo.CodeEmailConfVo;
import com.medusa.gruul.platform.model.vo.KfmsgVo;
import com.medusa.gruul.platform.model.vo.PayConfigVo;
import com.medusa.gruul.platform.model.vo.SystemConfigVo;
import com.medusa.gruul.platform.service.ISystemConfService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpTemplateMsgService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;


/**
 * <p>
 * 系统配置 服务实现类
 * </p>
 *
 * @author whh
 * @since 2019-09-20
 */
@Slf4j
@Service
public class SystemConfServiceImpl extends ServiceImpl<SystemConfMapper, SystemConf> implements ISystemConfService {

    @Autowired
    @Lazy
    private WxMpService wxMpService;

    @Override
    public void saveValue(SaveConfigDto saveConfigDto) {

        Integer type = saveConfigDto.getType();
        SystemConf systemConf = new SystemConf();
        String paramKey = "";
        //0-系统配置 1：七牛  2：阿里云  3：腾讯云
        switch (type) {
            case 0:
                systemConf.setParamValue(JSONObject.toJSONString(saveConfigDto.getSystemConfig()));
                paramKey = SystemConstant.SERVER_CONFIG;
                break;
            case 1:
                systemConf.setParamValue(JSONObject.toJSONString(saveConfigDto.getQiniouStorageConfig()));
                paramKey = SystemConstant.STORAGE_QINIOUYUN;
                break;
            case 2:
                systemConf.setParamValue(JSONObject.toJSONString(saveConfigDto.getAliyunStorageConfig()));
                paramKey = SystemConstant.STORAGE_ALIYUN;
                break;
            case 3:
                systemConf.setParamValue(JSONObject.toJSONString(saveConfigDto.getTencentStorageConfig()));
                paramKey = SystemConstant.STORAGE_TENCENT_CLOUD;
                break;
            default:
                throw new ServiceException("数据修改异常", SystemCode.DATA_UPDATE_FAILED.getCode());
        }
        SystemConf ossConf = this.getByParamKey(SystemConstant.CURRENT_OSS_TYPE);
        if (this.baseMapper.update(systemConf, new QueryWrapper<SystemConf>().eq(MeConstant.PARAM_KEY, paramKey)) == 0) {
            systemConf.setParamKey(paramKey);
            this.baseMapper.insert(systemConf);
        }
        if (ossConf == null) {
            ossConf = new SystemConf();
            ossConf.setParamKey(SystemConstant.CURRENT_OSS_TYPE);
        }
        ossConf.setParamValue(type.toString());
        this.saveOrUpdate(ossConf);
    }

    @Override
    public SystemConfigVo getTypeInfo(Integer type) {
        String paramKey = "";
        if (type.equals(0)) {
            paramKey = SystemConstant.SERVER_CONFIG;
        } else if (type.equals(CloudServiceEnum.QINIU.getType())) {
            paramKey = SystemConstant.STORAGE_QINIOUYUN;
        } else if (type.equals(CloudServiceEnum.ALIYUN.getType())) {
            paramKey = SystemConstant.STORAGE_ALIYUN;
        } else if (type.equals(CloudServiceEnum.QCLOUD.getType())) {
            paramKey = SystemConstant.STORAGE_TENCENT_CLOUD;
        } else {
            throw new ServiceException("数据获取异常", SystemCode.DATA_NOT_EXIST.getCode());
        }
        SystemConfigVo systemConfigVo = new SystemConfigVo();
        SystemConf systemConf = this.getByParamKey(paramKey);
        if (systemConf == null) {
            return systemConfigVo;
        }
        if (type == 0) {
            SystemConfig systemConfig = JSONObject.parseObject(systemConf.getParamValue(), SystemConfig.class);
            systemConfigVo.setSystemConfig(systemConfig);
        } else {
            if (type.equals(CloudServiceEnum.QINIU.getType())) {
                QiniouStorageConfig qiniouStorageConfig = JSONObject.parseObject(systemConf.getParamValue(), QiniouStorageConfig.class);
                systemConfigVo.setQiniouStorageConfig(qiniouStorageConfig);
            } else if (type.equals(CloudServiceEnum.ALIYUN.getType())) {
                AliyunStorageConfig aliyunStorageConfig = JSONObject.parseObject(systemConf.getParamValue(), AliyunStorageConfig.class);
                systemConfigVo.setAliyunStorageConfig(aliyunStorageConfig);
            } else if (type.equals(CloudServiceEnum.QCLOUD.getType())) {
                TencentStorageConfig tencentStorageConfig = JSONObject.parseObject(systemConf.getParamValue(), TencentStorageConfig.class);
                systemConfigVo.setTencentStorageConfig(tencentStorageConfig);
            }
        }
        return systemConfigVo;
    }

    @Override
    public Result<OssConfigDto> currentOssConfig() {
        SystemConf ossConf = this.getByParamKey(SystemConstant.CURRENT_OSS_TYPE);
        Integer type = CommonConstants.NUMBER_ONE;
        if (ossConf != null) {
            type = Integer.valueOf(ossConf.getParamValue());
        }
        return ossConfig(type);
    }

    private SystemConf getByParamKey(String paramKey) {
        return this.getBaseMapper().selectOne(new QueryWrapper<SystemConf>().eq("param_key", paramKey));
    }

    @Override
    public Result<OssConfigDto> ossConfig(Integer type) {
        if (type == 0) {
            SystemConf ossConf = this.getByParamKey(SystemConstant.CURRENT_OSS_TYPE);
            if (ossConf == null) {
                type = CommonConstants.NUMBER_ONE;
            } else {
                type = Integer.valueOf(ossConf.getParamValue());
            }
        }
        SystemConfigVo typeInfo = getTypeInfo(type);
        if (typeInfo == null) {
            return Result.failed("当前oss配置为空无法使用");
        }
        OssConfigDto dto = new OssConfigDto();
        dto.setType(type);
        if (type.equals(CloudServiceEnum.QINIU.getType()) && typeInfo.getQiniouStorageConfig() != null) {
            BeanUtils.copyProperties(typeInfo.getQiniouStorageConfig(), dto);
        } else if (type.equals(CloudServiceEnum.ALIYUN.getType()) && typeInfo.getAliyunStorageConfig() != null) {
            BeanUtils.copyProperties(typeInfo.getAliyunStorageConfig(), dto);
        } else if (type.equals(CloudServiceEnum.QCLOUD.getType()) && typeInfo.getTencentStorageConfig() != null) {
            BeanUtils.copyProperties(typeInfo.getTencentStorageConfig(), dto);
        }
        return Result.ok(dto);
    }

    @Override
    public void savePayConfig(SavePayConfigDto savePayConfigDto) {
        SystemConf systemConf = this.getByParamKey(SystemConstant.PLATFROM_PAY_CONFIG);
        if (ObjectUtil.isNotNull(systemConf)) {
            systemConf.setParamValue(JSONObject.toJSONString(savePayConfigDto));
            this.baseMapper.updateById(systemConf);
        } else {
            systemConf = new SystemConf();
            systemConf.setParamKey(SystemConstant.PLATFROM_PAY_CONFIG);
            systemConf.setParamValue(JSONObject.toJSONString(savePayConfigDto));
            this.baseMapper.insert(systemConf);
        }
        if (StrUtil.isNotEmpty(savePayConfigDto.getAlipayAppId()) &&
                StrUtil.isNotEmpty(savePayConfigDto.getAlipayPrivateKey()) &&
                StrUtil.isNotEmpty(savePayConfigDto.getAlipayPublicKey())) {
            //获取支付配置
            PayConfigVo payConfig = getPayConfig();
            //更新设置支付宝相关配置
            setAliConfigKit(payConfig);
        }
    }

    private void setAliConfigKit(PayConfigVo payConfig) {
        AliPayApiConfig aliPayApiConfig = AliPayApiConfig.builder()
                .setAppId(payConfig.getAlipayAppId())
                .setCharset("UTF-8")
                .setPrivateKey(payConfig.getAlipayPrivateKey())
                .setAliPayPublicKey(payConfig.getAlipayPublicKey())
                .setSignType("RSA2")
                .setServiceUrl("")
                // 普通公钥方式
                .build();
        AliPayApiConfigKit.setThreadLocalAliPayApiConfig(aliPayApiConfig);
    }

    @Override
    public PayConfigVo getPayConfig() {
        SystemConf systemConf = this.getByParamKey(SystemConstant.PLATFROM_PAY_CONFIG);
        if (ObjectUtil.isNull(systemConf)) {
            return new PayConfigVo();
        }
        return JSONObject.parseObject(systemConf.getParamValue(), PayConfigVo.class);
    }

    @Override
    public PayDto wxQrcodePay(String notifyUrl, String outTradeNo, String money, String body, String productId) throws WxPayException {
        //获取支付配置
        PayConfigVo payConfig = getPayConfig();
        if (ObjectUtil.isEmpty(payConfig) || StrUtil.isEmpty(payConfig.getWxMchId())) {
            throw new ServiceException("支付渠道未开发,请联系平台!");
        }
        //封装支付配置
        WxPayConfig wxPayConfig = new WxPayConfig();
        wxPayConfig.setAppId(payConfig.getWxAppId());
        wxPayConfig.setMchId(payConfig.getWxMchId());
        wxPayConfig.setMchKey(payConfig.getWxMchKey());
        wxPayConfig.setNotifyUrl(notifyUrl);
        wxPayConfig.setTradeType(WxPayConstants.TradeType.NATIVE);
        WxPayService wxPayService = new WxPayServiceApacheHttpImpl();
        wxPayService.setConfig(wxPayConfig);
        //发起统一支付请求
        WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
        orderRequest.setBody(body);
        orderRequest.setOutTradeNo(outTradeNo);
        orderRequest.setTotalFee(BaseWxPayRequest.yuanToFen(money));
        orderRequest.setSpbillCreateIp("192.168.1.1");
        orderRequest.setNonceStr(IdUtil.simpleUUID());
        orderRequest.setTradeType(WxPayConstants.TradeType.NATIVE);
        orderRequest.setSignType("MD5");
        orderRequest.setProductId(productId);
        WxPayUnifiedOrderResult payUnifiedOrderResult = wxPayService.unifiedOrder(orderRequest);
        PayDto dto = new PayDto();
        dto.setPrepayId(payUnifiedOrderResult.getPrepayId());
        dto.setCodeUrl(payUnifiedOrderResult.getCodeURL());
        return dto;
    }

    @Override
    public PayDto aliPayQrcodePay(String notifyUrl, String subject, String price, String outTradeNo) {
        try {
            AliPayApiConfigKit.getAliPayApiConfig();
        } catch (Exception e) {
            //获取支付配置
            PayConfigVo payConfig = getPayConfig();
            if (ObjectUtil.isEmpty(payConfig) || StrUtil.isEmpty(payConfig.getAlipayAppId())) {
                throw new ServiceException("支付渠道未开发,请联系平台!");
            }
            setAliConfigKit(payConfig);
        }

        AlipayTradePrecreateModel alipayTradePrecreateModel = new AlipayTradePrecreateModel();
        alipayTradePrecreateModel.setOutTradeNo(outTradeNo);
        alipayTradePrecreateModel.setTotalAmount(price);
        alipayTradePrecreateModel.setSubject(subject);
        PayDto payDto = new PayDto();
        AlipayTradePrecreateResponse alipayTradePrecreateResponse = null;
        try {
            alipayTradePrecreateResponse = AliPayApi.tradePrecreatePayToResponse(alipayTradePrecreateModel, notifyUrl);
            if (!MeConstant.STATUS_10000.toString().equals(alipayTradePrecreateResponse.getCode())) {
                log.error("支付生成订单异常： {}", alipayTradePrecreateResponse.toString());
                throw new ServiceException("支付生成订单异常");
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            throw new ServiceException("支付生成订单异常");
        }
        payDto.setCodeUrl(alipayTradePrecreateResponse.getQrCode());
        return payDto;
    }

    @Override
    public KfmsgVo getKfmsg() {
        SystemConf systemConf = this.getByParamKey(SystemConstant.KF_MSG_NOTIFY);
        if (ObjectUtil.isNull(systemConf)) {
            return new KfmsgVo();
        }
        return JSONObject.parseObject(systemConf.getParamValue(), KfmsgVo.class);
    }

    @Override
    public void saveKfmsg(KfAddDto dto) {
        SystemConf systemConf = this.getByParamKey(SystemConstant.KF_MSG_NOTIFY);
        if (ObjectUtil.isNotNull(systemConf)) {
            systemConf.setParamValue(JSONObject.toJSONString(dto));
            this.baseMapper.updateById(systemConf);
        } else {
            systemConf = new SystemConf();
            systemConf.setParamKey(SystemConstant.KF_MSG_NOTIFY);
            systemConf.setParamValue(JSONObject.toJSONString(dto));
            this.baseMapper.insert(systemConf);
        }
    }

    @Override
    public void sendKfmsg(Integer sendType, JSONObject sendCentent) {
        KfmsgVo kfmsg = getKfmsg();
        String templateId = "";
        String oepnIdListStr = null;
        //1-客服模板消息 2-开票模板消息
        log.info("sendType : {}", sendType);
        if (sendType.equals(CommonConstants.NUMBER_ONE)) {
            templateId = kfmsg.getKfmsgTemplateId();
            oepnIdListStr = kfmsg.getKfOpenIds();
        } else if (sendType.equals(CommonConstants.NUMBER_TWO)) {
            templateId = kfmsg.getInvoiceTemplateId();
            oepnIdListStr = kfmsg.getInvoiceOpenIds();
        } else {
            throw new ServiceException("错误发送类型");
        }
        if (StrUtil.isEmpty(templateId)) {
            log.info("不存在有效的模板id");
            return;
        }
        if (StrUtil.isEmpty(oepnIdListStr)) {
            log.info("不存在配置openId");
            return;
        }
        //使用笔歌公众号发送模板消息通知客服
        WxMpService wxMpService = this.wxMpService.switchoverTo(MeConstant.BGN_APPID);
        WxMpTemplateMsgService templateMsgService = wxMpService.getTemplateMsgService();
        try {
            for (String oepnId : oepnIdListStr.split(MeConstant.DOUHAO)) {
                WxMpTemplateMessage message = new WxMpTemplateMessage();
                message.setToUser(oepnId);
                message.setTemplateId(templateId);
                message.addData(new WxMpTemplateData("first", sendCentent.getString("first")));
                JSONArray keyword = sendCentent.getJSONArray("keyword");
                for (int i = 0; i < keyword.size(); i++) {
                    message.addData(new WxMpTemplateData("keyword" + (i + 1), keyword.getString(i)));
                }
                message.addData(new WxMpTemplateData("remark", sendCentent.getString("remark")));
                templateMsgService.sendTemplateMsg(message);
            }
        } catch (WxErrorException wxErrorException) {
            wxErrorException.printStackTrace();
            log.info("错误：{}", wxErrorException.getMessage());
        }
    }

    @Override
    public void saveCodeEmailConf(CodeEmailConfDto emailConfDto) {
        SystemConf systemConf = this.getByParamKey(SystemConstant.CODE_EMAIL_CONF);
        if (ObjectUtil.isNotNull(systemConf)) {
            systemConf.setParamValue(JSONObject.toJSONString(emailConfDto));
            this.baseMapper.updateById(systemConf);
        } else {
            systemConf = new SystemConf();
            systemConf.setParamKey(SystemConstant.CODE_EMAIL_CONF);
            systemConf.setParamValue(JSONObject.toJSONString(emailConfDto));
            this.baseMapper.insert(systemConf);
        }
    }

    @Override
    public CodeEmailConfVo getCodeEmailConf() {
        SystemConf systemConf = this.getByParamKey(SystemConstant.CODE_EMAIL_CONF);
        if (ObjectUtil.isNull(systemConf)) {
            return new CodeEmailConfVo();
        }
        return JSONObject.parseObject(systemConf.getParamValue(), CodeEmailConfVo.class);
    }

}
