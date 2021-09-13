package com.medusa.gruul.platform.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.medusa.gruul.common.core.constant.RegexConstants;
import com.medusa.gruul.common.core.constant.TimeConstants;
import com.medusa.gruul.common.core.constant.enums.AuthCodeEnum;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.common.core.util.SystemCode;
import com.medusa.gruul.common.dto.SendCodeVerifyDto;
import com.medusa.gruul.platform.conf.MeConstant;
import com.medusa.gruul.platform.conf.PlatformRedis;
import com.medusa.gruul.platform.constant.RedisConstant;
import com.medusa.gruul.platform.constant.SmsConstant;
import com.medusa.gruul.platform.model.dto.SendCodeDto;
import com.medusa.gruul.platform.service.ISendCodeService;
import com.medusa.gruul.sms.api.dto.SendSmsFeginDto;
import com.medusa.gruul.sms.api.feign.RemoteSmsSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author whh
 * @since 2019-10-07
 */
@Service
public class SendCodeServiceImpl implements ISendCodeService {

    @Autowired
    private RemoteSmsSendService remoteSmsSendService;


    @Override
    public void sendCode(SendCodeDto sendCodeDto) {
        if (sendCodeDto.getPhone() == null || !ReUtil.isMatch(RegexConstants.REGEX_MOBILE_EXACT, sendCodeDto.getPhone())) {
            throw new ServiceException("手机号错误", SystemCode.DATA_NOT_EXIST.getCode());
        }
        if (!AuthCodeEnum.isExistValue(sendCodeDto.getType())) {
            throw new ServiceException("非法校验", SystemCode.FAILURE.getCode());
        }
        PlatformRedis platformRedis = new PlatformRedis();
        String redisKey = RedisConstant.PHONE_KEY.concat(sendCodeDto.getType().toString()).concat(":").concat(sendCodeDto.getPhone());
        String code = platformRedis.get(redisKey);
        if (StrUtil.isNotEmpty(code)) {
            return;
        }
        code = RandomUtil.randomNumbers(6);
        //验证码有效期未过则重新发送
        SendSmsFeginDto sendSmsFeginDto = new SendSmsFeginDto();
        sendSmsFeginDto.setSmsSendTime(Calendar.getInstance().getTimeInMillis());
        sendSmsFeginDto.setSmsSendMobiles(sendCodeDto.getPhone());
        sendSmsFeginDto.setSmsSendParam(code);
        sendSmsFeginDto.setSmsSendZone(SmsConstant.ZONE);
        sendSmsFeginDto.setSmsType(SmsConstant.SMS_TYPE_ALIYUN);
        sendSmsFeginDto.setSmsSendZone(SmsConstant.ZONE);
        sendSmsFeginDto.setSignId(SmsConstant.SIGN_ID);
        sendSmsFeginDto.setTemplateId(SmsConstant.TEMPLATE_ID);
        sendSmsFeginDto.setProviderId(SmsConstant.PROVIDER_ID);
        sendSmsFeginDto.setUserId(SmsConstant.USER_ID);
        Result order = remoteSmsSendService.createOrder(sendSmsFeginDto);
        if ((MeConstant.STATUS_OK == order.getCode()) == Boolean.FALSE) {
            throw new ServiceException("短信发送失败,".concat(order.getMsg()));
        }
        platformRedis.setNxPx(redisKey, code, TimeConstants.FIVE_MINUTES);
    }

    @Override
    public String verifyCode(SendCodeVerifyDto sendCodeVerifyDto) {
        PlatformRedis platformRedis = new PlatformRedis();
        String redisKey = RedisConstant.PHONE_KEY.concat(sendCodeVerifyDto.getType().toString()).concat(":").concat(sendCodeVerifyDto.getPhone());
        String code = platformRedis.get(redisKey);
        if (StrUtil.isNotEmpty(code) && code.equals(sendCodeVerifyDto.getCode())) {
            String codeRedisKey = redisKey.concat(":").concat(code);
            String certificate = platformRedis.get(codeRedisKey);
            if(StrUtil.isNotEmpty(certificate)){
                return certificate;
            }
            //生成有效凭证
            certificate = SecureUtil.md5(sendCodeVerifyDto.getPhone().concat(code).concat(String.valueOf(System.currentTimeMillis())));
            String certificateKey = RedisConstant.PHONE_CERTIFICATE_KEY.concat(sendCodeVerifyDto.getType().toString()).concat(":").concat(certificate);
            platformRedis.setNxPx(certificateKey, sendCodeVerifyDto.getPhone(), TimeConstants.FIVE_MINUTES);
            platformRedis.setNxPx(codeRedisKey, certificate, TimeConstants.FIVE_MINUTES);
            return certificate;
        }
        throw new ServiceException("验证码不正确");
    }


    @Override
    public void certificateCheck(String certificateCheck, String phone, Integer type) {
        PlatformRedis platformRedis = new PlatformRedis();
        String phoneCheche = platformRedis.get(RedisConstant.PHONE_CERTIFICATE_KEY.concat(type.toString()).concat(":").concat(certificateCheck));
        if (StrUtil.isEmpty(phoneCheche)) {
            throw new ServiceException("无效验证码");
        }
        if (!phone.equals(phoneCheche)) {
            throw new ServiceException("无效验证码,请输入获取验证码的手机号");
        }
        platformRedis.del(phone);
    }

}
