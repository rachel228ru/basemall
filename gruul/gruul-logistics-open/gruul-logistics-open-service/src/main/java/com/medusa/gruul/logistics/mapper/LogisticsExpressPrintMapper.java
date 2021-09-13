package com.medusa.gruul.logistics.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.medusa.gruul.logistics.api.entity.LogisticsExpressPrint;
import com.medusa.gruul.logistics.model.param.LogisticsExpressPrintParam;
import com.medusa.gruul.logistics.model.vo.LogisticsExpressPrintVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author lcysike
 */
@Repository
public interface LogisticsExpressPrintMapper extends BaseMapper<LogisticsExpressPrint> {

    /**
     * 电子面单打印设置列表查询
     * @param page
     * @param logisticsExpressPrintParam
     * @return List<LogisticsExpressPrintVo>
     */
    List<LogisticsExpressPrintVo> queryAllLogisticsExpressPrintList(IPage page, @Param("logisticsExpressPrintParam") LogisticsExpressPrintParam logisticsExpressPrintParam);


    /**
     * 已连接电子面单打印设置列表查询
     * @param page
     * @param logisticsExpressPrintParam
     * @return List<LogisticsExpressPrintVo>
     */
    List<LogisticsExpressPrintVo> queryLogisticsExpressPrintList(IPage page, @Param("logisticsExpressPrintParam") LogisticsExpressPrintParam logisticsExpressPrintParam);

    /**
     * 查询单条电子面单打印机信息
     * @param id
     * @return LogisticsExpressPrintVo
     */
    LogisticsExpressPrintVo queryLogisticsExpressPrintInfo(@Param("id") Long id);


}