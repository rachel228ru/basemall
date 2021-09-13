package com.medusa.gruul.platform.service;

import com.medusa.gruul.common.dto.SendCodeVerifyDto;
import com.medusa.gruul.platform.model.dto.SendCodeDto;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author whh
 * @since 2019-10-07
 */
public interface ISendCodeService {

    /**
     * 发送验证码
     *
     * @param sendCodeDto dto
     */
    void sendCode(SendCodeDto sendCodeDto);

    /**
     * 校验指定类型验证码是否正确
     *
     * @param sendCodeVerifyDto dto
     * @return boolean false 不正确 true正确
     */
    String verifyCode(SendCodeVerifyDto sendCodeVerifyDto);

    /**
     * 短信校验通过凭证是否存在,并且凭证对应接收短信时的手机号
     *
     * @param certificate 凭证
     * @param phone       手机号
     * @param type        校验类型
     */
    void certificateCheck(String certificate, String phone, Integer type);
}
