package com.medusa.gruul.common.core.sms;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.medusa.gruul.common.core.util.LogUtil;

/**
 * Copyright(C) 2019-12-29 23:50 美杜莎 Inc.ALL Rights Reserved.
 *
 * @version V1.0
 * @description: 美杜莎 不直接实例化DefaultProfile，根据用户选择从库里面取配置
 * @author: wangpeng
 * @date: 2019-12-29 23:50
 **/
public class AliYunSmsUtil {




    /***
    * @description: 发送阿里短信公共方法
    * @param:appKey
    * @param:appSecret
    * @param:phoneNumbers
    * @param:signName
    * @param:templateCode
    * @param:params
    * @return: java.lang.String
    * @throws:
    * @author: wangpeng
    * @version : v1.0
    * @date: 2020/1/5 6:15 PM
    */
    public static String sendSms(String appKey ,String appSecret,
                                          String phoneNumbers,String signName
                                          , String templateCode , String params) {
        DefaultProfile profile = DefaultProfile.getProfile(SmsSendConfig.ALI_YUN_REGION_ID, appKey, appSecret);
        IAcsClient client = new DefaultAcsClient(profile);
        String res = null;
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain(SmsSendConfig.DOMAIN);
        request.setVersion(SmsSendConfig.VERSION);
        request.setAction(SmsSendConfig.METHOD_SEND_SMS);
        request.putQueryParameter("PhoneNumbers", phoneNumbers);
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", templateCode);
        request.putQueryParameter("TemplateParam", params);
        try {
            CommonResponse response = client.getCommonResponse(request);
            res = response.getData();
            LogUtil.info("ali call back ",res);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.error(e, "短信发送异常");
        }
        return res;
    }


    /****
    * @description:  推送阿里模版公共方法
    * @param:appKey
    * @param:appSecret
    * @param:templateType
    * @param:templateName
    * @param:templateContent
    * @param:remark
    * @return: java.lang.String
    * @throws:
    * @author: wangpeng
    * @version : v1.0
    * @date: 2020/1/5 6:16 PM
    */
    public static String doVerifyTemplate(String appKey
            , String appSecret, String templateType, String templateName
            , String templateContent,String remark) {
        DefaultProfile profile = DefaultProfile.getProfile(SmsSendConfig.ALI_YUN_REGION_ID, appKey, appSecret);
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain(SmsSendConfig.DOMAIN);
        request.setVersion(SmsSendConfig.VERSION);
        request.setAction(SmsSendConfig.METHOD_ADD_SMS_TEMPLATE);
        request.putQueryParameter("TemplateType", templateType);
        request.putQueryParameter("TemplateName", templateName);
        request.putQueryParameter("TemplateContent", templateContent);
        request.putQueryParameter("Remark", remark);
        try {
            CommonResponse response = client.getCommonResponse(request);
            return  response.getData();
        } catch (Exception e) {
            LogUtil.error(e, "模版推审异常");
            return  null;
        }
    }
}
