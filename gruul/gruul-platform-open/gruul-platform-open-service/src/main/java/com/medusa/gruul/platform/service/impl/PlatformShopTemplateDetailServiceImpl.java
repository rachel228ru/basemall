package com.medusa.gruul.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.platform.api.entity.*;
import com.medusa.gruul.platform.mapper.PlatformShopTemplateDetailMapper;
import com.medusa.gruul.platform.model.dto.ShopTemplateVersionOptionDto;
import com.medusa.gruul.platform.model.vo.SkipUrlVo;
import com.medusa.gruul.platform.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * <p>
 * 店铺模版详情表 服务实现类
 * </p>
 *
 * @author whh
 * @since 2020-03-06
 */
@Service
public class PlatformShopTemplateDetailServiceImpl extends ServiceImpl<PlatformShopTemplateDetailMapper, PlatformShopTemplateDetail> implements IPlatformShopTemplateDetailService {

    @Autowired
    private IPlatformShopTemplateInfoService platformShopTemplateInfoService;
    @Autowired
    private IPlatformShopInfoService platformShopInfoService;
    @Autowired
    private IDefaultValueService defaultValueService;
    @Autowired
    private ISysShopPackageService sysShopPackageService;
    @Autowired
    private IPlatformShopTemplateDetailMinisService platformShopTemplateDetailMinisService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(ShopTemplateVersionOptionDto dto) {
        dto.setId(null);
        PlatformShopTemplateInfo shopTemplateInfo = platformShopTemplateInfoService.getOne(null);
        if (shopTemplateInfo == null) {
            throw new ServiceException("店铺模板不存在");
        }

        PlatformShopTemplateDetail platformShopTemplateDetail = this.getOne(new QueryWrapper<PlatformShopTemplateDetail>());
        platformShopTemplateDetail = new PlatformShopTemplateDetail();
        BeanUtils.copyProperties(dto, platformShopTemplateDetail);
        if (this.getBaseMapper().insert(platformShopTemplateDetail) == 0) {
            throw new ServiceException("创建失败");
        }
        platformShopTemplateDetailMinisService.save(dto.getMiniCodeVersions(), platformShopTemplateDetail.getId());

        //生成模板版本套餐
        //查询当前模板版本是否存在默认配置套餐,存在则使用最新的套餐配置,不存在则查找上个版本的套餐配置进行copy
        //判断指定套餐标识版本是否存在,不存在获取最高的默认套餐值
        String initVersion = "v0.1";
        DefaultValue defaultValue = defaultValueService.getByUniqueIdentificationVersion(shopTemplateInfo.getCode(), initVersion);
        List<SysShopPackage> packages = null;
        if (defaultValue != null) {
            //生成版本套餐
            sysShopPackageService.generatePackage(defaultValue, shopTemplateInfo.getId(), platformShopTemplateDetail.getId());
        } else {
            packages = sysShopPackageService.getByTemplateLastPackage(shopTemplateInfo.getId());
            if (CollectionUtil.isEmpty(packages)) {
                throw new ServiceException("不存在套餐数据");
            }
            //copy上个版本的版本详情
            sysShopPackageService.copy(packages, shopTemplateInfo.getId(), platformShopTemplateDetail.getId());
        }
        //生成最新的版本默认值
        defaultValueService.generateNew(dto.getVersion());
        PlatformShopTemplateDetail finalPlatformShopTemplateDetail = platformShopTemplateDetail;
        CompletableFuture.runAsync(() -> {
            //发布新版本时,如果存在未授权小程序的店铺,则全部使用最的pc端版本
            Long id = finalPlatformShopTemplateDetail.getId();
            if (null == id) {
                return;
            }
            //查出指定模板下的所有未授权小程序店铺
            List<PlatformShopInfo> notAuthMiniShops = platformShopInfoService.getByNotAuthMiniShops(finalPlatformShopTemplateDetail.getShopTemplateId());
            //查询指定模板下的所有取消授权小程序和从未上传审核过的小程序的店铺
            List<PlatformShopInfo> cancelAuthMiniShops = platformShopInfoService.getByCancelAuthMiniShops(finalPlatformShopTemplateDetail.getShopTemplateId());
            List<PlatformShopInfo> shopInfos = new ArrayList<>(notAuthMiniShops.size() + cancelAuthMiniShops.size());
            shopInfos.addAll(notAuthMiniShops);
            shopInfos.addAll(cancelAuthMiniShops);
            if (CollectionUtil.isNotEmpty(shopInfos)) {
                shopInfos.parallelStream().forEach(obj -> platformShopInfoService.setShopPcNewVersion(obj.getTenantId()));
            }

        }).exceptionally((e) -> {
            log.error("异步处理异常");
            e.printStackTrace();
            return null;
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(ShopTemplateVersionOptionDto dto) {
        if (dto.getId() == null) {
            throw new ServiceException("id为空");
        }
        PlatformShopTemplateInfo shopTemplateInfo = platformShopTemplateInfoService.getById(dto.getShopTemplateId());
        if (shopTemplateInfo == null) {
            throw new ServiceException("店铺模板不存在");
        }

        PlatformShopTemplateDetail detail = this.getById(dto.getId());
        if (detail == null) {
            throw new ServiceException("不存在该版本");
        }
        if (!detail.getShopTemplateId().equals(shopTemplateInfo.getId())) {
            throw new ServiceException("非法操作数据");
        }
        if (!dto.getVersion().equals(detail.getVersion())) {
            PlatformShopTemplateDetail platformShopTemplateDetail = this.getOne(new QueryWrapper<PlatformShopTemplateDetail>()
                    .eq("shop_template_id", dto.getShopTemplateId())
                    .eq("version", dto.getVersion()).notIn("id", dto.getId()));
            if (platformShopTemplateDetail != null) {
                throw new ServiceException("已存在相同版本");
            }
        }
        BeanUtils.copyProperties(dto, detail);
        this.getBaseMapper().updateById(detail);
        //更新小程序数据
        platformShopTemplateDetailMinisService.update(dto.getMiniCodeVersions(), detail.getId());

    }

    @Override
    public void deleteById(Long id) {
        PlatformShopTemplateDetail detail = this.getById(id);
        if (detail == null) {
            throw new ServiceException("不存在该版本");
        }
        List<PlatformShopInfo> shopInfos = platformShopInfoService.getByPlatformShopTemplateDetailId(detail.getId());
        if (CollectionUtil.isNotEmpty(shopInfos)) {
            throw new ServiceException(detail.getVersion().concat("该版本有存在使用店铺，无法删除"));
        }
        this.removeById(id);
    }

    @Override
    public List<PlatformShopTemplateDetail> getByShopTemplateInfoIds(List<Long> infoIds) {
        return this.getBaseMapper().selectList(new QueryWrapper<PlatformShopTemplateDetail>().in("shop_template_id", infoIds).orderByDesc("create_time"));
    }

    @Override
    public PlatformShopTemplateDetail getByShopTeamplteNewDetail(Long shopTemplateId) {
        return this.getBaseMapper().selectShopTeamplteNewDetail(shopTemplateId);
    }

    @Override
    public List<PlatformShopTemplateDetail> getByLibrariesId(Long librariesId) {
        return this.getBaseMapper().selectList(new QueryWrapper<PlatformShopTemplateDetail>().eq("libraries_info_id", librariesId));
    }

    @Override
    public PlatformShopTemplateDetail getByFilterById(Long versionId) {
        return this.getBaseMapper().selectByFilterById(versionId);
    }

    @Override
    public SkipUrlVo getSkipUrl() {
        String tenantId = TenantContextHolder.getTenantId();
        PlatformShopInfo platformShopInfo = platformShopInfoService.getByTenantId(tenantId);
        if (platformShopInfo == null) {
            throw new ServiceException("非法操作");
        }
        PlatformShopTemplateDetail platformShopTemplateDetail = this.getBaseMapper().selectById(platformShopInfo.getShopTemplateDetailId());
        if (platformShopTemplateDetail == null) {
            throw new ServiceException("不存在数据");
        }

        SkipUrlVo vo = new SkipUrlVo();
        BeanUtil.copyProperties(platformShopTemplateDetail, vo);
        String pcUrlMap = platformShopTemplateDetail.getPcUrlMap();
        if (StrUtil.isNotEmpty(pcUrlMap)) {
            JSONObject jsonObject = JSONObject.parseObject(pcUrlMap);
            vo.setRegionalUrl(jsonObject.getString("RegionalUrl"));
        }
        return vo;
    }

}
