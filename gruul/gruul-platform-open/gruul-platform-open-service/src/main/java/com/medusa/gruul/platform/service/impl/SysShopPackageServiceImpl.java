package com.medusa.gruul.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.constant.enums.TemplateCodeEnum;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.platform.api.entity.DefaultValue;
import com.medusa.gruul.platform.api.entity.PlatformShopInfo;
import com.medusa.gruul.platform.api.entity.PlatformShopTemplateInfo;
import com.medusa.gruul.platform.api.entity.SysShopPackage;
import com.medusa.gruul.platform.mapper.SysShopPackageMapper;
import com.medusa.gruul.platform.model.dto.SysShopPackageUpdateDto;
import com.medusa.gruul.platform.model.vo.SysShopPackageListVo;
import com.medusa.gruul.platform.model.vo.SysShopPackageVo;
import com.medusa.gruul.platform.model.vo.agent.PackageInfoListVo;
import com.medusa.gruul.platform.service.IPlatformShopInfoService;
import com.medusa.gruul.platform.service.IPlatformShopTemplateDetailService;
import com.medusa.gruul.platform.service.IPlatformShopTemplateInfoService;
import com.medusa.gruul.platform.service.ISysShopPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 店铺套餐 服务实现类
 * </p>
 *
 * @author whh
 * @since 2020-08-01
 */
@Service
public class SysShopPackageServiceImpl extends ServiceImpl<SysShopPackageMapper, SysShopPackage> implements ISysShopPackageService {

    @Autowired
    private IPlatformShopInfoService platformShopInfoService;
    @Autowired
    private IPlatformShopTemplateDetailService templateDetailService;
    @Autowired
    private IPlatformShopTemplateInfoService templateInfoService;

    @Override
    public List<SysShopPackage> getByTemplateLastPackage(Long templateId) {
        return this.baseMapper.selectByTemplateLastPackage(templateId);
    }

    @Override
    public void copy(List<SysShopPackage> packages, Long templateId, Long templateVersionId) {
        List<SysShopPackage> sysShopPackages = packages.stream().map(obj -> {
            SysShopPackage sysShopPackage = new SysShopPackage();
            BeanUtil.copyProperties(obj, sysShopPackage,
                    "id", "templateVersionId", "templateId", "operateId", "operateId");
            sysShopPackage.setTemplateId(templateId);
            sysShopPackage.setTemplateVersionId(templateVersionId);
            return sysShopPackage;
        }).collect(Collectors.toList());
        this.saveBatch(sysShopPackages);
    }

    @Override
    public void generatePackage(DefaultValue defaultValue, Long templateId, Long templateVersionId) {
        JSONObject jsonObject = JSONObject.parseObject(defaultValue.getKv());
        List<SysShopPackage> packages = jsonObject.getJSONArray("packages").toJavaList(SysShopPackage.class);
        for (SysShopPackage shopPackage : packages) {
            shopPackage.setTemplateId(templateId);
            shopPackage.setTemplateVersionId(templateVersionId);
        }
        this.saveBatch(packages);
    }

    @Override
    public List<SysShopPackageListVo> templateVersionPackages(Long templateVersionId) {
        List<SysShopPackage> shopPackages = this.baseMapper.selectList(new QueryWrapper<SysShopPackage>().eq("template_version_id", templateVersionId));
        if (CollectionUtil.isEmpty(shopPackages)) {
            return new ArrayList<>(0);
        }
        List<Long> packageIds = shopPackages.stream().map(obj -> obj.getId()).collect(Collectors.toList());
        List<PlatformShopInfo> platformShopInfos = platformShopInfoService.list(new QueryWrapper<PlatformShopInfo>().in("package_id", packageIds));
        Map<Long, List<PlatformShopInfo>> packageIdGroup = new HashMap<>(0);
        if (CollectionUtil.isNotEmpty(platformShopInfos)) {
            packageIdGroup = platformShopInfos.stream().collect(Collectors.groupingBy(PlatformShopInfo::getPackageId));
        }

        Map<Long, List<PlatformShopInfo>> finalPackageIdGroup = packageIdGroup;
        return shopPackages.stream().map(obj -> {
            SysShopPackageListVo sysShopPackageListVo = BeanUtil.toBean(obj, SysShopPackageListVo.class);
            sysShopPackageListVo.setActiveShopNumber(0);
            sysShopPackageListVo.setClassShopNumber(0);
            List<PlatformShopInfo> shopInfos = finalPackageIdGroup.get(obj.getId());
            if (CollectionUtil.isNotEmpty(shopInfos)) {
                Map<Integer, List<PlatformShopInfo>> statusShop = shopInfos.stream().collect(Collectors.groupingBy(PlatformShopInfo::getStatus));
                //0审核中，1部署中 2正常 ，3已打烊，4禁用
                List<PlatformShopInfo> platformShopInfos1 = statusShop.get(CommonConstants.NUMBER_TWO);
                if (CollectionUtil.isNotEmpty(platformShopInfos1)) {
                    sysShopPackageListVo.setActiveShopNumber(platformShopInfos1.size());
                }
                List<PlatformShopInfo> platformShopInfos2 = statusShop.get(CommonConstants.NUMBER_THREE);
                if (CollectionUtil.isNotEmpty(platformShopInfos2)) {
                    sysShopPackageListVo.setClassShopNumber(platformShopInfos2.size());
                }
            }
            return sysShopPackageListVo;
        }).collect(Collectors.toList());
    }

    @Override
    public void updatePackageInfo(SysShopPackageUpdateDto sysShopPackageUpdateDto) {
        SysShopPackage sysShopPackage = this.baseMapper.selectById(sysShopPackageUpdateDto.getId());

        if (ObjectUtil.isNull(sysShopPackage)) {
            throw new ServiceException("不存在有效数据");
        }
        SysShopPackage up = new SysShopPackage();
        switch (sysShopPackageUpdateDto.getOptionType()) {
            case 1:
                BeanUtil.copyProperties(sysShopPackageUpdateDto, up,
                        "openState", "optionType");
                break;
            case 2:
                up.setId(sysShopPackageUpdateDto.getId());
                up.setOpenState(sysShopPackageUpdateDto.getOpenState());
                //判断套餐当前最少保持一个开启套餐
                if (sysShopPackageUpdateDto.getOpenState().equals(CommonConstants.NUMBER_ZERO)) {
                    List<SysShopPackage> sysShopPackages = this.baseMapper.selectList(new QueryWrapper<SysShopPackage>()
                            .eq("template_version_id", sysShopPackage.getTemplateVersionId())
                            .eq("open_state", CommonConstants.NUMBER_ONE)
                            .notIn("id", sysShopPackage.getId()));
                    if (CollectionUtil.isEmpty(sysShopPackages)) {
                        throw new ServiceException("最少保持一个开启套餐");
                    }
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + sysShopPackageUpdateDto.getOptionType());
        }
        this.updateById(up);
    }

    @Override
    public SysShopPackageVo findByPackageId(Long packageId) {
        SysShopPackage shopPackage = this.getById(packageId);
        if (ObjectUtil.isNull(shopPackage)) {
            return new SysShopPackageVo();
        }
        return BeanUtil.toBean(shopPackage, SysShopPackageVo.class);
    }

    @Override
    public List<SysShopPackage> getByTemplateVersionId(Long shopTemplateDetailId) {
        return list(new QueryWrapper<SysShopPackage>().eq("template_version_id", shopTemplateDetailId));
    }

    @Override
    public List<SysShopPackage> getByTeamplteId(Long templateId) {
        return list(new QueryWrapper<SysShopPackage>().eq("template_id", templateId));
    }

    @Override
    public List<SysShopPackageVo> getByTeamplteIdLastPackage(Long templateId) {
        List<SysShopPackage> list = getByTemplateLastPackage(templateId);
        if (CollectionUtil.isEmpty(list)) {
            return null;
        }
        return list.stream().map(obj -> BeanUtil.toBean(obj, SysShopPackageVo.class)).collect(Collectors.toList());
    }

    @Override
    public List<PackageInfoListVo> packageInfo() {
        PlatformShopTemplateInfo templateInfo = templateInfoService.getByCode(TemplateCodeEnum.SQPT.name());
        if (templateInfo == null) {
            return new ArrayList<>(0);
        }
        List<SysShopPackage> sysShopPackages = this.getBaseMapper().selectByTemplateLastPackage(templateInfo.getId());
        if (CollectionUtil.isEmpty(sysShopPackages)) {
            return new ArrayList<>(0);
        }
        return sysShopPackages.stream().map(obj -> BeanUtil.toBean(obj, PackageInfoListVo.class)).collect(Collectors.toList());
    }
}
