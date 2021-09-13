package com.medusa.gruul.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.platform.api.entity.MiniExperience;
import com.medusa.gruul.platform.model.dto.MiniTesterDto;

import java.util.List;

/**
 * <p>
 * 小程序体验者表 服务类
 * </p>
 *
 * @author whh
 * @since 2020-01-14
 */
public interface IMiniExperienceService extends IService<MiniExperience> {

    /**
     * 获取的体验版二维码
     *
     * @param path 指定二维码扫码后直接进入指定页面并可同时带上参数）
     * @return 体验二维码base64
     */
    Result wxaGetQrcode(String path);

    /**
     * 获取体验者列表
     *
     * @return 唯一字符数组
     */
    List<String> wxaMemberauth();

    /**
     * 解除绑定或绑定提体验者
     *
     * @param testerDto com.medusa.gruul.platform.model.dto.MiniTesterDto
     */
    void tester(MiniTesterDto testerDto);
}
