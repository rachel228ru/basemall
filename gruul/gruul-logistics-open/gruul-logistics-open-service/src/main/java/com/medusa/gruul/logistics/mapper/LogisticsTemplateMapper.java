package com.medusa.gruul.logistics.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.medusa.gruul.logistics.api.entity.LogisticsTemplate;
import com.medusa.gruul.logistics.model.param.LogisticsTemplateParam;
import com.medusa.gruul.logistics.model.vo.LogisticsTemplateVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhaozheng
 */
@Repository
public interface LogisticsTemplateMapper extends BaseMapper<LogisticsTemplate> {

    /**
     * 查询模板列表
     * @param page
     * @param logisticsTemplateParam
     * @return List<LogisticsTemplateVo>
     */
    List<LogisticsTemplateVo> queryTemplateList(IPage page, @Param("logisticsTemplateParam") LogisticsTemplateParam logisticsTemplateParam);

    /**
     * 主键查询
     * @param id
     * @return LogisticsTemplateVo
     */
    LogisticsTemplateVo selectByPrimaryKey(Long id);

}