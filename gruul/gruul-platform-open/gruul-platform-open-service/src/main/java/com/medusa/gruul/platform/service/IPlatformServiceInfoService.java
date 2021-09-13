package com.medusa.gruul.platform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.common.core.monitor.MonitorServiceConfig;
import com.medusa.gruul.platform.api.entity.PlatformServiceInfo;
import com.medusa.gruul.platform.model.dto.PlatformServiceInfoDto;

import java.util.List;

/**
 * <p>
 * 服务信息表 服务类
 * </p>
 *
 * @author alan
 * @since 2020-02-26
 */
public interface IPlatformServiceInfoService extends IService<PlatformServiceInfo> {

    /**
     * 心跳生成
     *
     * @param monitorServiceConfig com.medusa.gruul.common.core.monitor.MonitorServiceConfig
     */
    void baseWarehouse(MonitorServiceConfig monitorServiceConfig);

    /**
     * 获取指定基础库库下的所有服务
     *
     * @param id 基础库id(业务基础库,支撑基础库)
     * @return 服务
     */
    List<PlatformServiceInfo> getByLibrariesId(Long id);

    /**
     * 获取指定服务的当前活动数量
     *
     * @param obj com.medusa.gruul.platform.api.entity.PlatformServiceInfo
     * @return 当前活动数量
     */
    Integer getServiceActivityCount(PlatformServiceInfo obj);

    /**
     * 分页查询获取指定基础库库下的所有服务
     *
     * @param id          基础库id
     * @param page        页数
     * @param size        条数
     * @param serviceType 服务类型 0-通用服务,1定制服务
     * @return
     */
    IPage<PlatformServiceInfo> getByLibrariesId(Long id, Integer page, Integer size, Integer serviceType);

    /**
     * 更新指定指定信息
     *
     * @param serviceId 服务id
     * @param dto
     */
    void updateService(Long serviceId, PlatformServiceInfoDto dto);

    /**
     * 删除指定
     *
     * @param serviceId
     */
    void deleteService(Long serviceId);
}
