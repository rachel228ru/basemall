package com.medusa.gruul.platform.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaSubscribeService;
import cn.binarywang.wx.miniapp.bean.template.WxMaPubTemplateTitleListResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.SystemCode;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.platform.api.entity.MiniInfo;
import com.medusa.gruul.platform.api.entity.PlatformShopMessage;
import com.medusa.gruul.platform.model.dto.MiniSubscriberiMessageDto;
import com.medusa.gruul.platform.model.vo.WxOpenMaCodeTemplateVo;
import com.medusa.gruul.platform.service.IMiniInfoService;
import com.medusa.gruul.platform.service.IPlatformShopInfoService;
import com.medusa.gruul.platform.service.IPlatformShopMessageService;
import com.medusa.gruul.platform.service.IWechatPlatformService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.open.api.WxOpenFastMaService;
import me.chanjar.weixin.open.api.WxOpenMaService;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.bean.WxOpenMaCodeTemplate;
import me.chanjar.weixin.open.bean.ma.WxOpenMaCategory;
import me.chanjar.weixin.open.bean.result.WxOpenMaCategoryListResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 微信第三方相关 服务实现类
 * </p>
 *
 * @author whh
 * @since 2020-02-02
 */
@Service
@Slf4j
public class WechatPlatformServiceImpl implements IWechatPlatformService {


    @Autowired
    private WxOpenService wxOpenService;
    @Autowired
    private IMiniInfoService miniInfoService;
    @Autowired
    private IPlatformShopMessageService platformShopMessageService;
    @Autowired
    private IPlatformShopInfoService platformShopInfoService;

    @Override
    public WxOpenMaCategoryListResult getAppIdServiceClass(String appId) {
        WxOpenMaService openMaService = wxOpenService.getWxOpenComponentService().getWxMaServiceByAppid(appId);
        WxMaSubscribeService subscribeService = openMaService.getSubscribeService();
        try {
            return openMaService.getCategoryList();
        } catch (WxErrorException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public WxMaPubTemplateTitleListResult getpubtemplatetitles(String appId, String serviceIds, Integer page, Integer size) {
        WxOpenMaService openMaService = wxOpenService.getWxOpenComponentService().getWxMaServiceByAppid(appId);
        WxMaSubscribeService subscribeService = openMaService.getSubscribeService();
        String[] ids = serviceIds.split(",");
        try {
            return subscribeService.getPubTemplateTitleList(ids, page, size);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<WxMaSubscribeService.PubTemplateKeyword> getpubtemplatekeywords(String appId, String tid) {
        WxOpenMaService openMaService = wxOpenService.getWxOpenComponentService().getWxMaServiceByAppid(appId);
        WxMaSubscribeService subscribeService = openMaService.getSubscribeService();
        //获取模板库某个模板标题下关键词库
        try {
            return subscribeService.getPubTemplateKeyWordsById(tid);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void clearSubscribeMsg(String tenantId) {
        MiniInfo miniInfo = miniInfoService.getByMiniTenantId(tenantId);
        if (miniInfo == null) {
            throw new ServiceException("非法租户");
        }
        WxOpenMaService openMaService = wxOpenService.getWxOpenComponentService().getWxMaServiceByAppid(miniInfo.getAppId());
        WxMaSubscribeService subscribeService = openMaService.getSubscribeService();
        try {
            List<WxMaSubscribeService.TemplateInfo> templateList = subscribeService.getTemplateList();
            if (CollectionUtil.isEmpty(templateList)) {
                return;
            }
            for (WxMaSubscribeService.TemplateInfo templateInfo : templateList) {
                subscribeService.delTemplate(templateInfo.getPriTmplId());
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
        }


    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void addSubscribeMsg(String tenantId, String version) {
        MiniInfo miniInfo = miniInfoService.getByMiniTenantId(tenantId);
        if (miniInfo == null) {
            throw new ServiceException("不存在小程序信息");
        }
        TenantContextHolder.setTenantId(tenantId);
        List<PlatformShopMessage> old = platformShopMessageService.getByVersionMes(version, CommonConstants.NUMBER_ONE);
        Map<String, PlatformShopMessage> oldMap = new HashMap<>(old.size());
        //清空微信端现有的所有消息
        this.clearSubscribeMsg(tenantId);
        if (CollectionUtil.isNotEmpty(old)) {
            oldMap = old.stream().collect(Collectors.toMap(PlatformShopMessage::getTitle, v -> v));
        }
        //更新指定版本买家消息
        platformShopMessageService.generateInitMsg(version);
        List<PlatformShopMessage> newShopMsg = platformShopMessageService.getByVersionMes(version, CommonConstants.NUMBER_ONE);
        WxMaSubscribeService subscribeService = wxOpenService.getWxOpenComponentService().getWxMaServiceByAppid(miniInfo.getAppId()).getSubscribeService();
        try {
            WxOpenMaCategoryListResult categoryList = wxOpenService.getWxOpenComponentService().getWxMaServiceByAppid(miniInfo.getAppId()).getCategoryList();
            Map<String, List<WxOpenMaCategory>> classMap = categoryList.getCategoryList().stream().collect(Collectors.groupingBy(WxOpenMaCategory::getSecondClass));
            //设置新的默认消息的消息模板
            for (PlatformShopMessage shopMessage : newShopMsg) {
                if (StrUtil.isEmpty(shopMessage.getMiniMsg())) {
                    continue;
                }
                MiniSubscriberiMessageDto miniSubscriberiMessageDto = JSON.parseObject(shopMessage.getMiniMsg(), MiniSubscriberiMessageDto.class);
                if (CollectionUtil.isEmpty(classMap.get(miniSubscriberiMessageDto.getSecondClass()))) {
                    log.warn("该小程序({})不存在所属类目({}),无法生成", miniInfo.getAppId(), miniSubscriberiMessageDto.getSecondClass());
                    return;
                }
                List<Integer> kids = Arrays.stream(miniSubscriberiMessageDto.getKIds().split(",")).map(Integer::valueOf).collect(Collectors.toList());
                String templateId = subscribeService.addTemplate(miniSubscriberiMessageDto.getTId(), kids, miniSubscriberiMessageDto.getTitle());
                PlatformShopMessage platformShopMessage = new PlatformShopMessage();
                platformShopMessage.setId(shopMessage.getId());
                platformShopMessage.setMiniTemplateId(templateId);
                //设置原本的消息开关
                PlatformShopMessage oldMessage = oldMap.get(shopMessage.getTitle());
                if (oldMessage != null) {
                    oldMap.remove(shopMessage.getTitle());
                    platformShopMessage.setMiniOpen(oldMessage.getMiniOpen());
                    platformShopMessageService.updateById(platformShopMessage);
                }
            }
            if (oldMap.size() > 0) {
                oldMap.forEach((s, platformShopMessage) -> {
                    platformShopMessageService.removeById(platformShopMessage.getId());
                });
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }

    }

    @Override
    public void addClass(String appId) {
        WxOpenFastMaService wxOpenFastMaService = wxOpenService.getWxOpenComponentService().getWxFastMaServiceByAppid(appId);
        WxOpenMaService wxOpenMaService = wxOpenService.getWxOpenComponentService().getWxMaServiceByAppid(appId);
        try {
            List<WxOpenMaCategory> categoryList = wxOpenMaService.getCategoryList().getCategoryList();
            System.out.printf(categoryList.toString());
            wxOpenFastMaService.deleteCategory(categoryList.get(0).getFirstId(), categoryList.get(0).getSecondId());
        } catch (WxErrorException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<WxOpenMaCodeTemplateVo> getTemplateList(Long templateId) {
        List<WxOpenMaCodeTemplate> templateList = null;
        try {
            templateList = wxOpenService.getWxOpenComponentService().getTemplateList();
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage(), SystemCode.WX_PLATEFROM_EXCEPTION);
        }
        //筛选指定模板
        if (CollectionUtil.isNotEmpty(templateList) && templateId != null) {
            templateList = templateList.stream().filter(obj -> ObjectUtil.equal(obj.getTemplateId(), templateId)).collect(Collectors.toList());
        }

        if (CollectionUtil.isEmpty(templateList)) {
            return new ArrayList<>(0);
        }
        List<WxOpenMaCodeTemplateVo> vos = templateList.stream().map(obj -> {
            WxOpenMaCodeTemplateVo vo = new WxOpenMaCodeTemplateVo();
            BeanUtil.copyProperties(obj, vo, "createTime");
            vo.setCreateTime(DateUtil.date(obj.getCreateTime() * 1000).toStringDefaultTimeZone());
            return vo;
        }).sorted(Comparator.comparing(WxOpenMaCodeTemplateVo::getTemplateId).reversed()).collect(Collectors.toList());
        return vos;

    }


}
