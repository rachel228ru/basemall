package com.medusa.gruul.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.platform.api.entity.PlatformLibrariesInfo;
import com.medusa.gruul.platform.api.entity.PlatformServiceInfo;
import com.medusa.gruul.platform.api.entity.PlatformShopTemplateDetail;
import com.medusa.gruul.platform.constant.BaseCategoryTypeEnum;
import com.medusa.gruul.platform.mapper.PlatformLibrariesInfoMapper;
import com.medusa.gruul.platform.model.dto.ServiceLibrariesDto;
import com.medusa.gruul.platform.model.vo.BaseLibrariesVo;
import com.medusa.gruul.platform.model.vo.LibrariesServiceListVo;
import com.medusa.gruul.platform.service.IPlatformLibrariesInfoService;
import com.medusa.gruul.platform.service.IPlatformServiceInfoService;
import com.medusa.gruul.platform.service.IPlatformShopTemplateDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 基础库信息表 服务实现类
 * </p>
 *
 * @author alan
 * @since 2020-02-27
 */
@Service
public class PlatformLibrariesInfoServiceImpl extends ServiceImpl<PlatformLibrariesInfoMapper, PlatformLibrariesInfo> implements IPlatformLibrariesInfoService {

    @Autowired
    private IPlatformServiceInfoService platformServiceInfoService;
    @Autowired
    private IPlatformShopTemplateDetailService platformShopTemplateDetailService;

    @Override
    public PageUtils getBaseLibraries(Integer page, Integer size, Integer categoryType) {
        IPage<PlatformLibrariesInfo> iPage = this.baseMapper.selectPage(new Page<>(page, size),
                new QueryWrapper<PlatformLibrariesInfo>().eq("category_type", categoryType).orderByDesc("version"));
        List<PlatformLibrariesInfo> records = iPage.getRecords();
        if (records.size() == 0) {
            return new PageUtils(null, (int) iPage.getTotal(), (int) iPage.getSize(), (int) iPage.getCurrent());
        }

        Map<Long, String> belongIdMap;
        if (categoryType == 1) {
            belongIdMap = new HashMap<>(Long.bitCount(iPage.getSize()));
            List<Long> publicLibrariesId = records.stream().map(PlatformLibrariesInfo::getBelongId).collect(Collectors.toList());
            List<PlatformLibrariesInfo> platformLibrariesInfos = this.getBaseMapper().selectBatchIds(publicLibrariesId);
            if (platformLibrariesInfos.size() > 0) {
                belongIdMap = platformLibrariesInfos.stream().collect(Collectors.toMap(PlatformLibrariesInfo::getId, PlatformLibrariesInfo::getVersion));
            }
        } else {
            belongIdMap = new HashMap<>(0);
        }
        List<BaseLibrariesVo> librariesVos = new ArrayList<>(Long.bitCount(records.size()));
        for (PlatformLibrariesInfo record : records) {
            BaseLibrariesVo baseLibrariesVo = BeanUtil.toBean(record, BaseLibrariesVo.class);
            String belongVersion = belongIdMap.get(record.getBelongId());
            baseLibrariesVo.setBelongVersion(belongVersion);
            baseLibrariesVo.setBelongId(record.getBelongId());
            //获取指定基础库库下的所有服务
            List<PlatformServiceInfo> serviceInfos = platformServiceInfoService.getByLibrariesId(baseLibrariesVo.getId());
            baseLibrariesVo.setTotleCount(serviceInfos.size());
            baseLibrariesVo.setActivetyCount(0);
            if (baseLibrariesVo.getTotleCount() > 0) {
                Long activityCount = serviceInfos.stream().filter(obj -> platformServiceInfoService.getServiceActivityCount(obj) > 0).count();
                baseLibrariesVo.setActivetyCount(activityCount.intValue());
            }
            //总服务数量为0则未运行服务
            if (baseLibrariesVo.getTotleCount().equals(CommonConstants.NUMBER_ZERO)) {
                baseLibrariesVo.setStatus(CommonConstants.NUMBER_ZERO);
            } else if (baseLibrariesVo.getTotleCount().equals(baseLibrariesVo.getActivetyCount())) {
                baseLibrariesVo.setStatus(CommonConstants.NUMBER_ONE);
                //活动服务数量为0则服务已下线
            } else if (baseLibrariesVo.getActivetyCount().equals(CommonConstants.NUMBER_ZERO)) {
                baseLibrariesVo.setStatus(CommonConstants.NUMBER_THREE);
                //活动服务数量小于总数则存在异常服务未运行
            } else if (baseLibrariesVo.getActivetyCount() < baseLibrariesVo.getTotleCount()) {
                baseLibrariesVo.setStatus(CommonConstants.NUMBER_TWO);
            }
            librariesVos.add(baseLibrariesVo);
        }
        return new PageUtils(librariesVos, (int) iPage.getTotal(), (int) iPage.getSize(), (int) iPage.getCurrent());
    }

    @Override
    public void createServiceLibraries(ServiceLibrariesDto dto) {
        PlatformLibrariesInfo platformLibrariesInfo = this.getBaseMapper().selectOne(new QueryWrapper<PlatformLibrariesInfo>()
                .eq("category_type", CommonConstants.NUMBER_TWO)
                .eq("id", dto.getBelongId()));
        if (platformLibrariesInfo == null) {
            throw new ServiceException("不存在该支持数据库");
        }
        PlatformLibrariesInfo info = new PlatformLibrariesInfo();
        info.setName(dto.getName());
        info.setVersion(dto.getVersion());
        platformLibrariesInfo = this.getBaseMapper().selectOne(new QueryWrapper<PlatformLibrariesInfo>().eq("name", info.getName()).eq("version", info.getVersion()));
        if (platformLibrariesInfo != null) {
            throw new ServiceException("不能存在相同版本或名称基础库");
        }
        info.setCount(0);
        info.setStatus(0);
        info.setBelongId(dto.getBelongId());
        info.setRemark(dto.getRemark());
        info.setCategoryType(BaseCategoryTypeEnum.BUSINESS.getValue());
        info.setUniqueness(generateUniqueness());
        this.getBaseMapper().insert(info);
    }

    /**
     * 生成唯一标识
     *
     * @return 放回店铺唯一标识
     */
    private String generateUniqueness() {
        String randomNumbers = RandomUtil.randomNumbers(8);
        PlatformLibrariesInfo librariesInfo =
                this.getBaseMapper().selectOne(new QueryWrapper<PlatformLibrariesInfo>().eq("uniqueness", randomNumbers));
        if (librariesInfo == null) {
            return randomNumbers;
        }
        return generateUniqueness();
    }

    @Override
    public void updateServiceLibraries(ServiceLibrariesDto dto) {
        PlatformLibrariesInfo platformLibrariesInfo = this.getBaseMapper().selectById(dto.getId());
        if (platformLibrariesInfo == null) {
            throw new ServiceException("不存在该基础库");
        }

        if (platformLibrariesInfo.getCategoryType().equals(CommonConstants.NUMBER_TWO)) {

            PlatformLibrariesInfo info = this.getBaseMapper().selectOne(new QueryWrapper<PlatformLibrariesInfo>()
                    .eq("version", dto.getVersion())
                    .notIn("id", Collections.singletonList(platformLibrariesInfo.getId())));
            if (info != null) {
                throw new ServiceException("不能存在相同版本基础库");
            }
            platformLibrariesInfo.setVersion(dto.getVersion());
            platformLibrariesInfo.setRemark(dto.getRemark());
        } else {
            if (StrUtil.isEmpty(platformLibrariesInfo.getName())) {
                throw new ServiceException("业务基础库名称不能为空");
            }
            PlatformLibrariesInfo info = this.getBaseMapper().selectOne(new QueryWrapper<PlatformLibrariesInfo>()
                    .eq("name", dto.getName())
                    .eq("version", dto.getVersion())
                    .notIn("id", Collections.singletonList(platformLibrariesInfo.getId())));
            if (info != null) {
                throw new ServiceException("不能存在相同版本或名称基础库");
            }
            platformLibrariesInfo.setName(dto.getName());
            platformLibrariesInfo.setVersion(dto.getVersion());
            platformLibrariesInfo.setBelongId(dto.getBelongId());
            platformLibrariesInfo.setRemark(dto.getRemark());
        }
        this.getBaseMapper().updateById(platformLibrariesInfo);
    }



    @Override
    public PageUtils<LibrariesServiceListVo> getByBaseLibrariesId(Integer page, Integer size, Integer serviceType, Integer libraiesId) {
        PlatformLibrariesInfo platformLibrariesInfo = this.getBaseMapper().selectById(libraiesId);
        if (platformLibrariesInfo == null) {
            throw new ServiceException("不存在该基础库");
        }

        if (platformLibrariesInfo.getCategoryType().equals(CommonConstants.NUMBER_TWO)) {
            serviceType = null;
        }
        IPage<PlatformServiceInfo> iPage = platformServiceInfoService.getByLibrariesId(platformLibrariesInfo.getId(), page, size, serviceType);
        List<PlatformServiceInfo> records = iPage.getRecords();
        if (records.size() == CommonConstants.NUMBER_ZERO) {
            return new PageUtils(null, (int) iPage.getTotal(), (int) iPage.getSize(), (int) iPage.getCurrent());
        }
        List<LibrariesServiceListVo> listVos = new LinkedList<>();
        for (PlatformServiceInfo record : records) {
            LibrariesServiceListVo vo = new LibrariesServiceListVo();
            Integer activityCount = platformServiceInfoService.getServiceActivityCount(record);
            vo.setId(record.getId());
            vo.setNumber(activityCount);
            vo.setStatus(CommonConstants.NUMBER_ONE);
            if (vo.getNumber().equals(CommonConstants.NUMBER_ZERO)) {
                vo.setStatus(CommonConstants.NUMBER_TWO);
            }
            vo.setVersion(record.getVersion());
            vo.setDescription(record.getDescription());
            vo.setName(record.getName());
            listVos.add(vo);
        }
        return new PageUtils(listVos, (int) iPage.getTotal(), (int) iPage.getSize(), (int) iPage.getCurrent());
    }
}
