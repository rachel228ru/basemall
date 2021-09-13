package com.medusa.gruul.logistics.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medusa.gruul.logistics.api.entity.LogisticsShippingModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author zhaozheng
 */
@Repository
public interface LogisticsShippingModelMapper extends BaseMapper<LogisticsShippingModel> {

    /**
     * 根据条件查询物流基础运送方式信息
     * @param param
     * @return List<LogisticsIncludePostage>
     */
    @SqlParser(filter = true)
    List<LogisticsShippingModel> selectByParamMap(Map<String, Object> param);
}