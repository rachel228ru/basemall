package com.medusa.gruul.logistics.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medusa.gruul.logistics.api.entity.LogisticsCompany;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;


/**
 * @author zhaozheng
 */
@Repository
public interface LogisticsCompanyMapper extends BaseMapper<LogisticsCompany> {

    /**
     * 查询快递公司列表
     * @param param
     * @return List<LogisticsCompany>
     */
    @SqlParser(filter = true)
    List<LogisticsCompany> selectListCompany(Map<String, Object> param);

    /**
     * 根据条件查询快递公司列表
     * @param param
     * @return LogisticsCompany
     */
    @SqlParser(filter = true)
    LogisticsCompany selectListCompanyByParam(Map<String, Object> param);
}