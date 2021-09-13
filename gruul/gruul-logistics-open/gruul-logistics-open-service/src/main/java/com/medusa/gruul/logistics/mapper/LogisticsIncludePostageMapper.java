package com.medusa.gruul.logistics.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medusa.gruul.logistics.api.entity.LogisticsIncludePostage;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


/**
 * @author zhaozheng
 */
@Repository
public interface LogisticsIncludePostageMapper extends BaseMapper<LogisticsIncludePostage> {

    /**
     * 根据条件查询运费模板包邮设置信息
     * @param param
     * @return List<LogisticsIncludePostage>
     */
    @SqlParser(filter = true)
    List<LogisticsIncludePostage> selectByParams(Map<String, Object> param);
}