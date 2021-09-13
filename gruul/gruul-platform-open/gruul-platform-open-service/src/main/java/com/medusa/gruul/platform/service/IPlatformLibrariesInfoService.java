package com.medusa.gruul.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.platform.api.entity.PlatformLibrariesInfo;
import com.medusa.gruul.platform.model.dto.ServiceLibrariesDto;
import com.medusa.gruul.platform.model.vo.BaseLibrariesVo;
import com.medusa.gruul.platform.model.vo.LibrariesServiceListVo;

/**
 * <p>
 * 基础库信息表 服务类
 * </p>
 *
 * @author alan
 * @since 2020-02-27
 */
public interface IPlatformLibrariesInfoService extends IService<PlatformLibrariesInfo> {


    /**
     * 获取基础库数据
     *
     * @param page         页数
     * @param size         大小
     * @param categoryType 分类类型
     * @return com.medusa.gruul.platform.model.vo.BaseLibrariesVo
     */
    PageUtils<BaseLibrariesVo> getBaseLibraries(Integer page, Integer size, Integer categoryType);

    /**
     * 创建业务基础库
     *
     * @param dto com.medusa.gruul.platform.model.dto.ServiceLibrariesDto
     */
    void createServiceLibraries(ServiceLibrariesDto dto);

    /**
     * 更新业务基础库或支撑基础库
     *
     * @param dto com.medusa.gruul.platform.model.dto.ServiceLibrariesDto
     */
    void updateServiceLibraries(ServiceLibrariesDto dto);



    /**
     * 查询指定基础库服务
     *
     * @param page        页数
     * @param size        条数
     * @param serviceType 服务类型
     * @param libraiesId  基础库id
     * @return
     */
    PageUtils<LibrariesServiceListVo> getByBaseLibrariesId(Integer page, Integer size, Integer serviceType, Integer libraiesId);
}
