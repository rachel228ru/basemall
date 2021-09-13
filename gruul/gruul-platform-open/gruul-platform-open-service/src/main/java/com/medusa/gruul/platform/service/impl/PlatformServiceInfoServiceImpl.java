package com.medusa.gruul.platform.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.constant.TimeConstants;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.monitor.MonitorServiceConfig;
import com.medusa.gruul.platform.api.entity.PlatformLibrariesInfo;
import com.medusa.gruul.platform.api.entity.PlatformServiceInfo;
import com.medusa.gruul.platform.conf.PlatformRedis;
import com.medusa.gruul.platform.constant.BaseCategoryTypeEnum;
import com.medusa.gruul.platform.constant.RedisConstant;
import com.medusa.gruul.platform.constant.ServiceTypeEnum;
import com.medusa.gruul.platform.mapper.PlatformServiceInfoMapper;
import com.medusa.gruul.platform.model.dto.PlatformServiceInfoDto;
import com.medusa.gruul.platform.service.IPlatformLibrariesInfoService;
import com.medusa.gruul.platform.service.IPlatformServiceInfoService;
import com.medusa.gruul.platform.service.IPlatformShopInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * <p>
 * 服务信息表 服务实现类
 * </p>
 *
 * @author alan
 * @since 2020-02-26基础库类型错误
 */
@Service
@Slf4j
public class PlatformServiceInfoServiceImpl extends ServiceImpl<PlatformServiceInfoMapper, PlatformServiceInfo> implements IPlatformServiceInfoService {

    @Autowired
    private IPlatformShopInfoService platformShopInfoService;
    @Autowired
    private IPlatformLibrariesInfoService platformLibrariesInfoService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized void baseWarehouse(MonitorServiceConfig monitorServiceConfig) {
        //基础库分类类型
        BaseCategoryTypeEnum baseCategoryTypeEnum = BaseCategoryTypeEnum.findByValue(monitorServiceConfig.getBaseType());
        //服务类型
        ServiceTypeEnum serviceTypeEnum = ServiceTypeEnum.findByValue(monitorServiceConfig.getServiceType());
        if (baseCategoryTypeEnum == null || serviceTypeEnum == null) {
            throw new ServiceException("基础库类型错误");
        }
        //业务基础库判断,不存在基础库名称则抛出异常
        boolean businessBaseFlag = baseCategoryTypeEnum.getValue().equals(CommonConstants.NUMBER_ONE) &&
                StrUtil.isEmpty(monitorServiceConfig.getUniqueness());
        if (businessBaseFlag) {
            throw new ServiceException("基础库唯一标识为空:" + monitorServiceConfig.toString());
        }
        PlatformRedis platformRedis = new PlatformRedis();
        String baseLibraryKey = RedisConstant.BASE_LIBRARY_KEY
                .concat(monitorServiceConfig.getBaseType()).concat(":")
                .concat(monitorServiceConfig.getVersion()).concat(":")
                .concat(monitorServiceConfig.getServiceType());
        if (baseCategoryTypeEnum.getValue().equals(CommonConstants.NUMBER_ONE)) {
            baseLibraryKey = baseLibraryKey.concat(":")
                    .concat(monitorServiceConfig.getUniqueness());
        }

        String jsonData = platformRedis.get(baseLibraryKey);
        //基础库数据生成
        PlatformLibrariesInfo platformLibrariesInfo;
        if (StrUtil.isEmpty(jsonData)) {
            //当前为业务基础库
            if (baseCategoryTypeEnum.getValue().equals(CommonConstants.NUMBER_ONE)) {
                platformLibrariesInfo = platformLibrariesInfoService.getBaseMapper().selectOne(new QueryWrapper<PlatformLibrariesInfo>()
                        .eq("uniqueness", monitorServiceConfig.getUniqueness())
                        .eq("version", monitorServiceConfig.getVersion())
                        .eq("category_type", baseCategoryTypeEnum.getValue()));
                //不存在业务基础库则抛出异常,无法进行添加,
                if (platformLibrariesInfo == null) {
                    throw new ServiceException("不存在业务基础库:" + monitorServiceConfig.toString());
                }
            } else {
                //支持基础库
                platformLibrariesInfo = platformLibrariesInfoService.getBaseMapper().selectOne(new QueryWrapper<PlatformLibrariesInfo>()
                        .eq("version", monitorServiceConfig.getVersion())
                        .eq("category_type", baseCategoryTypeEnum.getValue()));
                if (platformLibrariesInfo == null) {
                    platformLibrariesInfo = new PlatformLibrariesInfo();
                    platformLibrariesInfo.setCategoryType(baseCategoryTypeEnum.getValue());
                    platformLibrariesInfo.setVersion(monitorServiceConfig.getVersion());
                    platformLibrariesInfo.setCount(CommonConstants.NUMBER_ZERO);
                    platformLibrariesInfo.setStatus(CommonConstants.NUMBER_ZERO);
                    platformLibrariesInfoService.save(platformLibrariesInfo);
                }
            }
            Date date = new Date();
            DateTime endOfDay = DateUtil.endOfDay(date);
            long between = DateUtil.between(date, endOfDay, DateUnit.MS);
            platformRedis.setNxPx(baseLibraryKey, JSON.toJSONString(platformLibrariesInfo), between);
        } else {
            platformLibrariesInfo = JSON.parseObject(jsonData, PlatformLibrariesInfo.class);
        }
        //基础库下的服务数据生成
        processing(monitorServiceConfig, platformLibrariesInfo);
    }

    @Override
    public IPage<PlatformServiceInfo> getByLibrariesId(Long id, Integer page, Integer size, Integer serviceType) {
        QueryWrapper<PlatformServiceInfo> query = new QueryWrapper<PlatformServiceInfo>().eq("libraries_info_id", id);
        if (serviceType != null) {
            query.eq("type", serviceType.equals(CommonConstants.NUMBER_ONE) ? CommonConstants.NUMBER_ZERO : CommonConstants.NUMBER_ONE);
        }
        return this.getBaseMapper().selectPage(new Page<>(page, size), query);
    }

    @Override
    public void updateService(Long serviceId, PlatformServiceInfoDto dto) {
        PlatformServiceInfo platformServiceInfo = this.getBaseMapper().selectById(serviceId);
        if (platformServiceInfo == null) {
            throw new ServiceException("不存在该服务");
        }
        platformServiceInfo.setDescription(dto.getServiceDesc());
        this.getBaseMapper().updateById(platformServiceInfo);
    }

    @Override
    public void deleteService(Long serviceId) {
        PlatformServiceInfo platformServiceInfo = this.baseMapper.selectById(serviceId);
        if (ObjectUtil.isNull(platformServiceInfo)) {
            throw new ServiceException("非法删除");
        }
        if (getServiceActivity(platformServiceInfo)) {
            throw new ServiceException("当前服务存在活动无法删除");
        }
        this.removeById(serviceId);
    }

    /**
     * 判断当前服务是否存在活动
     * 存在返回true  不存在返回false
     *
     * @param platformServiceInfo 基础库服务
     */
    private Boolean getServiceActivity(PlatformServiceInfo platformServiceInfo) {
        PlatformRedis platformRedis = new PlatformRedis();
        //该键值数据在心跳机制中存入
        String redisKey = RedisConstant.BASE_LIBRARY_SERVICE_KEY.concat(platformServiceInfo.getId().toString());
        String jsonData = platformRedis.get(redisKey);
        return StrUtil.isNotEmpty(jsonData);
    }

    @Override
    public Integer getServiceActivityCount(PlatformServiceInfo platformServiceInfo) {
        PlatformRedis platformRedis = new PlatformRedis();
        //不存在在服务名称则是支撑基础库
        String baseType = BaseCategoryTypeEnum.BUSINESS.getType();

        PlatformLibrariesInfo librariesInfo = platformLibrariesInfoService.getById(platformServiceInfo.getLibrariesInfoId());
        if (librariesInfo.getCategoryType().equals(CommonConstants.NUMBER_TWO)) {
            baseType = BaseCategoryTypeEnum.PUBLIC.getType();
        }
        String serviceType = ServiceTypeEnum.UNIVERSAL_SERVICE.getType();
        if (ServiceTypeEnum.COMMISSION_SERVICE.getValue().equals(platformServiceInfo.getType())) {
            serviceType = ServiceTypeEnum.COMMISSION_SERVICE.getType();
        }

        //获取除hash相同但当前启动标识不同的同一服务数量
        String redisKey = platformRedis.getBaseKey().concat(":")
                .concat(RedisConstant.BASE_LIBRARY_SERVICE_KEY)
                .concat(platformServiceInfo.getVersion()).concat(":")
                .concat(baseType).concat("-")
                .concat(serviceType).concat("-")
                .concat(platformServiceInfo.getName()).concat("-")
                .concat("*");
        Set<String> keys = platformRedis.keys(redisKey);
        return keys.size();
    }

    @Override
    public List<PlatformServiceInfo> getByLibrariesId(Long id) {
        return this.getBaseMapper().selectList(new QueryWrapper<PlatformServiceInfo>()
                .eq("libraries_info_id", id));
    }

    /**
     * public服务数据生成
     *
     * @param monitorServiceConfig  接收数据
     * @param platformLibrariesInfo 已生成的基础库或
     */
    private void processing(MonitorServiceConfig monitorServiceConfig, PlatformLibrariesInfo platformLibrariesInfo) {
        if (!monitorServiceConfig.getVersion().equals(platformLibrariesInfo.getVersion())) {
            throw new ServiceException("服务版本不一致");
        }
        PlatformRedis platformRedis = new PlatformRedis();
        String redisKey = RedisConstant.BASE_LIBRARY_SERVICE_KEY
                .concat(monitorServiceConfig.getVersion()).concat(":")
                .concat(monitorServiceConfig.getBaseType()).concat("-")
                .concat(monitorServiceConfig.getServiceType()).concat("-")
                .concat(monitorServiceConfig.getApplicationName()).concat("-")
                .concat(monitorServiceConfig.getStartIdentifier());
        String jsonData = platformRedis.get(redisKey);
        if (StrUtil.isEmpty(jsonData)) {
            Integer type = Objects.requireNonNull(ServiceTypeEnum.findByValue(monitorServiceConfig.getServiceType())).getValue();
            PlatformServiceInfo platformServiceInfo = this.getBaseMapper().selectOne(new QueryWrapper<PlatformServiceInfo>()
                    .eq("name", monitorServiceConfig.getApplicationName())
                    .eq("type", type)
                    .eq("version", monitorServiceConfig.getVersion()));
            if (platformServiceInfo == null) {
                platformServiceInfo = new PlatformServiceInfo();
                platformServiceInfo.setName(monitorServiceConfig.getApplicationName());
                platformServiceInfo.setVersion(monitorServiceConfig.getVersion());
                platformServiceInfo.setType(type);
                platformServiceInfo.setStatus(CommonConstants.NUMBER_ZERO);
                platformServiceInfo.setLibrariesInfoId(platformLibrariesInfo.getId());
                this.getBaseMapper().insert(platformServiceInfo);
                platformRedis.setNxPx(redisKey, JSON.toJSONString(platformServiceInfo), TimeConstants.SIX_SECONDS.intValue());
            }
            platformRedis.setNxPx(RedisConstant.BASE_LIBRARY_SERVICE_KEY.concat(platformServiceInfo.getId().toString()), JSON.toJSONString(platformServiceInfo), TimeConstants.SIX_SECONDS.intValue());
            platformRedis.setNxPx(redisKey, JSON.toJSONString(platformServiceInfo), TimeConstants.SIX_SECONDS.intValue());
        } else {
            //存储当前活动服务id(直接存储拼接ID省去查询缓存时的拼接,可用于判断当前服务是否存在活动),以及重置到期时间
            PlatformServiceInfo platformServiceInfo = JSONObject.parseObject(jsonData, PlatformServiceInfo.class);
            platformRedis.setNxPx(RedisConstant.BASE_LIBRARY_SERVICE_KEY.concat(platformServiceInfo.getId().toString()), jsonData, TimeConstants.SIX_SECONDS.intValue());
            platformRedis.expire(redisKey, TimeConstants.SIX_SECONDS.intValue() / 1000);
        }
        if (platformLibrariesInfo.getStatus().equals(CommonConstants.NUMBER_ZERO)) {
            platformLibrariesInfo.setStatus(CommonConstants.NUMBER_ONE);
            platformLibrariesInfoService.updateById(platformLibrariesInfo);
            String baseLibraryKey = platformRedis.getBaseKey().concat(RedisConstant.BASE_LIBRARY_KEY)
                    .concat(monitorServiceConfig.getBaseType()).concat(":")
                    .concat(monitorServiceConfig.getVersion()).concat(":")
                    .concat(monitorServiceConfig.getServiceType());
            //基础库分类类型
            BaseCategoryTypeEnum baseCategoryTypeEnum = BaseCategoryTypeEnum.findByValue(monitorServiceConfig.getBaseType());
            if (Objects.requireNonNull(baseCategoryTypeEnum).getValue().equals(CommonConstants.NUMBER_ONE)) {
                baseLibraryKey = baseLibraryKey.concat(":")
                        .concat(monitorServiceConfig.getUniqueness());
            }
            Date date = new Date();
            DateTime endOfDay = DateUtil.endOfDay(date);
            long between = DateUtil.between(date, endOfDay, DateUnit.MS);
            platformRedis.setNxPx(baseLibraryKey, JSON.toJSONString(platformLibrariesInfo), between);
        }
    }
}
