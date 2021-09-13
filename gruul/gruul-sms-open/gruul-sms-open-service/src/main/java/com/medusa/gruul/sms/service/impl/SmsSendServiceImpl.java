package com.medusa.gruul.sms.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.sms.AliYunSmsUtil;
import com.medusa.gruul.common.core.sms.SmsSendConfig;
import com.medusa.gruul.common.core.util.LogUtil;
import com.medusa.gruul.sms.constant.SmsEnum;
import com.medusa.gruul.sms.constant.SmsSmsStatus;
import com.medusa.gruul.sms.dao.entity.TSmsOrderEntity;
import com.medusa.gruul.sms.dao.mapper.TSmsOrderEntityMapper;
import com.medusa.gruul.sms.model.dto.SmsDoSendDto;
import com.medusa.gruul.sms.service.SmsSendService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copyright(C) 2019-12-30 21:57 美杜莎 Inc.ALL Rights Reserved.
 *
 * @version V1.0
 * @description: 美杜莎
 * @author: wangpeng
 * @date: 2019-12-30 21:57
 **/
@Service
@Slf4j
public class SmsSendServiceImpl implements SmsSendService {

    private static  Pattern compile = Pattern.compile("\\$\\{([^}]*)\\}");

    @Resource
    private TSmsOrderEntityMapper tSmsOrderEntityMapper;

    @Override
    public void sendSms(TSmsOrderEntity smsOrderEntity) {
        try {
            SmsDoSendDto smsDoSendDto = tSmsOrderEntityMapper.getSendSmsCfg(smsOrderEntity.getId());
            if (StringUtils.isBlank(smsOrderEntity.getSmsSendMobiles())) {
                throw new ServiceException(SmsEnum.SMS_MOBILES_ERROR.getMsg());
            }
            List<String> phones = null;
            if (smsOrderEntity.getSmsSendMobiles().contains(SmsSendConfig.SMS_PHONE_SEPARATOR)) {
                String[] split = smsOrderEntity.getSmsSendMobiles().split(SmsSendConfig.SMS_PHONE_SEPARATOR);
                phones = new ArrayList<String>(Arrays.asList(split));
            } else {
                phones = new ArrayList<String>(1);
                phones.add(smsOrderEntity.getSmsSendMobiles());
            }
            for (String phone : phones) {
                //阿里短信
                if (SmsSendConfig.SMS_TYPE_ALI == smsOrderEntity.getSmsType()) {

                    String res = sendAliYunSms(smsDoSendDto.getProviderAppId(),
                            smsDoSendDto.getProviderAppSecret(),
                            phone,
                            smsDoSendDto.getSmsSign(),
                            smsDoSendDto.getTemplateCode(), smsOrderEntity.getSmsSendParam(), smsDoSendDto.getSmsTemplateContent());
                }//腾讯短信
                else if (SmsSendConfig.SMS_TYPE_TX == smsOrderEntity.getSmsType()) {
                    ArrayList<String> params = covertTxParams(smsOrderEntity.getSmsSendParam());
                    String res = sendTxSms(Integer.parseInt(smsDoSendDto.getProviderAppId()),
                            smsDoSendDto.getProviderAppSecret(),
                            phone, smsDoSendDto.getSmsSign()
                            , Integer.parseInt(smsDoSendDto.getTemplateCode()), params);
                }
            }
        } catch (Exception e) {
            LogUtil.error(e, "", "短信发送异常");
        } finally {
            smsOrderEntity.setSmsSendStatus(SmsSmsStatus.SEND_SUCCESS);
            tSmsOrderEntityMapper.updateByPrimaryKeySelective(smsOrderEntity);
        }


    }

    /**
     * 阿里短信源码自带单例
     */
    public String sendAliYunSms(String appKey, String appSecret, String phone, String sign, String templateCode,String params,String content) {

        try {
            params = covertAliParams(params, content);
            String res = AliYunSmsUtil.sendSms(appKey, appSecret, phone, sign, templateCode, params);
            log.info("ali call back :"+ res);
            return res;
        } catch (Exception e) {
            log.error("send ali sms error :",e);
            return null;
        }
    }


    public String sendTxSms(int appKey, String appSecret, String phone, String sign, int templateCode, ArrayList<String> params) {
        SmsSingleSender sender = new SmsSingleSender(appKey, appSecret);
        try {
            SmsSingleSenderResult smsSingleSenderResult = sender.sendWithParam(SmsSendConfig.ZONE, phone,
                    templateCode, params, sign, "", "");
            String res = JSON.toJSONString(smsSingleSenderResult);
            log.info("tx call back :"+ res);
        } catch (Exception e) {
            LogUtil.error(e, "腾讯短信发送异常");
        }
           return null;

    }



    private String covertAliParams(String params,String content) {

        Map<String,Object> param = new HashMap<>(5);
        if (StringUtils.isBlank(params)) {
            return "";
        }
        List<String> vars = getVars(content);
        if (params.contains(SmsSendConfig.SMS_PARAM_SEPARATOR)) {
            String[] split = params.split(SmsSendConfig.SMS_PARAM_SPLIT_SEPARATOR);
            for (int i = 0; i < split.length; i++) {

                param.put(vars.get(i),split[i]);
            }
        }else {
            param.put(vars.get(0),params);
        }
        return JSON.toJSONString(param);
    }

    private ArrayList<String> covertTxParams(String params) {
        ArrayList<String> paramsList = null;
        if (StringUtils.isBlank(params)) {
            return new ArrayList<String>();
        }
        if (params.contains(SmsSendConfig.SMS_PARAM_SEPARATOR)) {

            String[] split = params.split(SmsSendConfig.SMS_PARAM_SPLIT_SEPARATOR);
            paramsList = new ArrayList<String>(Arrays.asList(split));
            return paramsList;
        }
        paramsList = new ArrayList<String>(1);
        paramsList.add(params);
        return paramsList;
    }

    private List<String> getVars(String content) {
        List<String> vars = new ArrayList<>(5);
        Matcher matcher = compile.matcher(content);
        while(matcher.find()) {
            vars.add(matcher.group(1));
        }
       return vars;
    }

}
