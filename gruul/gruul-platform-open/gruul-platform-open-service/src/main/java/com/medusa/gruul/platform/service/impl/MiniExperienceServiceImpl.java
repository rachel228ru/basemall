package com.medusa.gruul.platform.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.util.Base64;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.platform.api.entity.MiniExperience;
import com.medusa.gruul.platform.api.entity.MiniInfo;
import com.medusa.gruul.platform.constant.ErrorCodeEnum;
import com.medusa.gruul.platform.mapper.MiniExperienceMapper;
import com.medusa.gruul.platform.model.dto.MiniTesterDto;
import com.medusa.gruul.platform.service.IMiniExperienceService;
import com.medusa.gruul.platform.service.IMiniInfoService;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.open.api.WxOpenMaService;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.bean.result.WxOpenMaBindTesterResult;
import me.chanjar.weixin.open.bean.result.WxOpenResult;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 小程序体验者表 服务实现类
 * </p>
 *
 * @author whh
 * @since 2020-01-14
 */
@Service
public class MiniExperienceServiceImpl extends ServiceImpl<MiniExperienceMapper, MiniExperience> implements IMiniExperienceService {

    @Autowired
    private IMiniInfoService miniInfoService;
    @Autowired
    private WxOpenService wxOpenService;

    @Override
    public Result wxaGetQrcode(String path) {
        String tenantId = TenantContextHolder.getTenantId();
        if (StrUtil.isEmpty(tenantId)) {
            throw new ServiceException("无效租户id");
        }
        MiniInfo miniInfo = miniInfoService.getByMiniTenantId(tenantId);
        WxOpenMaService wxOpenMaService = wxOpenService.getWxOpenComponentService().getWxMaServiceByAppid(miniInfo.getAppId());
        try {
            File testQrcode = wxOpenMaService.getTestQrcode(path, null);
            byte[] bytes = IOUtils.toByteArray(testQrcode.toURI());
            String base64 = "data:image/png;base64," + Base64.byteArrayToBase64(bytes);
            return Result.ok(base64);
        } catch (WxErrorException | IOException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<String> wxaMemberauth() {
        String tenantId = TenantContextHolder.getTenantId();
        if (StrUtil.isEmpty(tenantId)) {
            throw new ServiceException("无效租户id");
        }
        List<MiniExperience> miniExperiences = this.baseMapper.selectList(new QueryWrapper<MiniExperience>().eq("tenant_id", tenantId));
        return miniExperiences.stream().map(MiniExperience::getExperienceName).collect(Collectors.toList());
    }

    @Override
    public void tester(MiniTesterDto testerDto) {
        String tenantId = TenantContextHolder.getTenantId();
        if (StrUtil.isEmpty(tenantId)) {
            throw new ServiceException("无效租户id");
        }
        MiniInfo miniInfo = miniInfoService.getByMiniTenantId(tenantId);
        WxOpenMaService wxOpenMaService = wxOpenService.getWxOpenComponentService().getWxMaServiceByAppid(miniInfo.getAppId());
        try {
            switch (testerDto.getOption()) {
                case 1:
                    WxOpenMaBindTesterResult wxOpenMaBindTesterResult = wxOpenMaService.bindTester(testerDto.getWechatid());
                    if (!wxOpenMaBindTesterResult.getErrcode().equals(CommonConstants.NUMBER_ZERO.toString())) {
                        throw new ServiceException("绑定失败");
                    }
                    MiniExperience miniExperience = new MiniExperience();
                    miniExperience.setExperienceName(testerDto.getWechatid());
                    miniExperience.setMiniId(miniInfo.getId());
                    miniExperience.setUserstr(wxOpenMaBindTesterResult.getUserstr());
                    this.baseMapper.insert(miniExperience);
                    break;
                case 2:
                    WxOpenResult wxOpenResult = wxOpenMaService.unbindTester(testerDto.getWechatid());
                    if (!wxOpenResult.getErrcode().equals(CommonConstants.NUMBER_ZERO.toString())) {
                        throw new ServiceException("解绑失败");
                    }
                    this.remove(new QueryWrapper<MiniExperience>().eq("experience_name", testerDto.getWechatid()));
                    break;
                default:
                    break;
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
            String msg = ErrorCodeEnum.findMsgByCode(e.getError().getErrorCode());
            if (StrUtil.isEmpty(msg)) {
                throw new ServiceException(e.getMessage());
            }
            throw new ServiceException(msg);
        }
    }

}
