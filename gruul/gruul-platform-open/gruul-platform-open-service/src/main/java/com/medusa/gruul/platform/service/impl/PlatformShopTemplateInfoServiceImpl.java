package com.medusa.gruul.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.platform.api.entity.*;
import com.medusa.gruul.platform.mapper.PlatformShopTemplateInfoMapper;
import com.medusa.gruul.platform.model.dto.ShopTemplateCraeteOrUpdateDto;
import com.medusa.gruul.platform.model.vo.MiniCodeVersionVo;
import com.medusa.gruul.platform.model.vo.ShopTemplateListVo;
import com.medusa.gruul.platform.model.vo.ShopTemplateVo;
import com.medusa.gruul.platform.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 店铺模版表 服务实现类
 * </p>
 *
 * @author whh
 * @since 2020-03-06
 */
@Service
public class PlatformShopTemplateInfoServiceImpl extends ServiceImpl<PlatformShopTemplateInfoMapper, PlatformShopTemplateInfo> implements IPlatformShopTemplateInfoService {


    @Autowired
    private IPlatformShopTemplateDetailService platformShopTemplateDetailService;
    @Autowired
    private IPlatformLibrariesInfoService platformLibrariesInfoService;
    @Autowired
    private IPlatformShopInfoService platformShopInfoService;
    @Autowired
    private IDefaultValueService defaultValueService;
    @Autowired
    private IPlatformShopTemplateDetailMinisService platformShopTemplateDetailMinisService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(ShopTemplateCraeteOrUpdateDto sendCodeDto) {
        if (!defaultValueService.getByUniqueIdentificationIsExit(sendCodeDto.getCode())) {
            throw new ServiceException("不存在该模板编号套餐,请创建套餐之后在进行创建模板");
        }
        PlatformShopTemplateInfo shopTemplateInfo = this.getByCode(sendCodeDto.getCode());
        if(shopTemplateInfo != null){
            throw new ServiceException("模板编号已被使用");
        }
        PlatformShopTemplateInfo platformShopTemplateInfo = new PlatformShopTemplateInfo();
        BeanUtils.copyProperties(sendCodeDto, platformShopTemplateInfo);
        platformShopTemplateInfo.setId(null);
        this.getBaseMapper().insert(platformShopTemplateInfo);
    }

    @Override
    public void edit(ShopTemplateCraeteOrUpdateDto sendCodeDto) {
        if (sendCodeDto.getId() == null) {
            throw new ServiceException("数据id不能为空");
        }
        if (!defaultValueService.getByUniqueIdentificationIsExit(sendCodeDto.getCode())) {
            throw new ServiceException("不存在该模板编号套餐,请创建套餐之后在进行创建模板");
        }
        PlatformShopTemplateInfo shopTemplateInfo = this.getByCode(sendCodeDto.getCode());
        if(shopTemplateInfo != null && !shopTemplateInfo.getId().equals(sendCodeDto.getId())){
            throw new ServiceException("模板编号已被使用");
        }
        PlatformShopTemplateInfo templateInfo = this.getById(sendCodeDto.getId());
        if (templateInfo == null) {
            throw new ServiceException("数据不存在");
        }
        templateInfo.setCode(sendCodeDto.getCode());
        templateInfo.setDescription(sendCodeDto.getDescription());
        templateInfo.setName(sendCodeDto.getName());
        this.updateById(templateInfo);

    }

    @Override
    public PageUtils<List<ShopTemplateListVo>> listQuery(Integer page, Integer size, Integer type) {

        IPage<PlatformShopTemplateInfo> iPage = this.getBaseMapper().selectPage(new Page<>(page, size), new QueryWrapper<PlatformShopTemplateInfo>()
                .eq(!type.equals(CommonConstants.NUMBER_ZERO), "type", type).orderByDesc("create_time"));
        List<PlatformShopTemplateInfo> records = iPage.getRecords();
        if (CollectionUtil.isEmpty(records)) {
            return new PageUtils(null, (int) iPage.getTotal(), (int) iPage.getSize(), (int) iPage.getCurrent());
        }
        List<Long> infoIds = records.stream().map(PlatformShopTemplateInfo::getId).collect(Collectors.toList());
        List<PlatformShopTemplateDetail> platformShopTemplateDetails = platformShopTemplateDetailService.getByShopTemplateInfoIds(infoIds);
        Map<Long, List<ShopTemplateVo>> detailMap = new HashMap<>(platformShopTemplateDetails.size());
        if (CollectionUtil.isNotEmpty(platformShopTemplateDetails)) {

            detailMap = platformShopTemplateDetails.stream().map(obj -> {
                ShopTemplateVo shopTemplateVo = new ShopTemplateVo();
                BeanUtils.copyProperties(obj, shopTemplateVo);
                PlatformLibrariesInfo librariesInfo = platformLibrariesInfoService.getById(obj.getLibrariesInfoId());
                if (ObjectUtil.isNotNull(librariesInfo)) {
                    shopTemplateVo.setLibrariesInfoName(librariesInfo.getName());
                }
                shopTemplateVo.setServerCount(CommonConstants.NUMBER_ZERO);
                List<PlatformShopInfo> shopInfos = platformShopInfoService.getByPlatformShopTemplateDetailId(obj.getId());
                if (CollectionUtil.isNotEmpty(shopInfos)) {
                    shopTemplateVo.setServerCount(shopInfos.size());
                }
                List<PlatformShopTemplateDetailMinis> detailMinis = platformShopTemplateDetailMinisService.getByShopTemplateDetailId(obj.getId());
                if (CollectionUtil.isNotEmpty(detailMinis)) {
                    shopTemplateVo.setMiniCodeVersions(detailMinis.stream().map(de -> BeanUtil.toBean(de, MiniCodeVersionVo.class)).collect(Collectors.toList()));
                }
                return shopTemplateVo;
            }).collect(Collectors.groupingBy(ShopTemplateVo::getShopTemplateId));
        }
        List<ShopTemplateListVo> vos = new LinkedList<>();
        for (PlatformShopTemplateInfo record : records) {
            ShopTemplateListVo vo = new ShopTemplateListVo();
            BeanUtils.copyProperties(record, vo);
            List<ShopTemplateVo> shopTemplateVos = detailMap.get(vo.getId());
            if (CollectionUtil.isNotEmpty(shopTemplateVos)) {
                List<ShopTemplateVo> serviceNumber = shopTemplateVos.stream().filter(obj -> obj.getServerCount() > 1).collect(Collectors.toList());
                if (serviceNumber.size() > 0) {
                    vo.setNotDel(CommonConstants.NUMBER_ONE);
                }
            }
            vo.setShopTemplateVos(shopTemplateVos);
            vos.add(vo);
        }

        return new PageUtils(vos, (int) iPage.getTotal(), (int) iPage.getSize(), (int) iPage.getCurrent());
    }

    @Override
    public void deleteById(Long id) {
        if (id == null) {
            throw new ServiceException("数据id不能为空");
        }
        PlatformShopTemplateInfo templateInfo = this.getById(id);
        if (templateInfo == null) {
            throw new ServiceException("数据不存在");
        }
        List<PlatformShopTemplateDetail> platformShopTemplateDetails = platformShopTemplateDetailService.getByShopTemplateInfoIds(Collections.singletonList(id));
        try {
            for (PlatformShopTemplateDetail platformShopTemplateDetail : platformShopTemplateDetails) {
                platformShopTemplateDetailService.deleteById(platformShopTemplateDetail.getId());
            }
        } catch (ServiceException serviceException) {
            throw new ServiceException("该模板有店铺正在使用，不可删除");
        }
        this.removeById(id);
    }

    @Override
    public PlatformShopTemplateInfo getByCode(String code) {
        return this.baseMapper.selectOne(new QueryWrapper<PlatformShopTemplateInfo>().eq("code",code));
    }
}
