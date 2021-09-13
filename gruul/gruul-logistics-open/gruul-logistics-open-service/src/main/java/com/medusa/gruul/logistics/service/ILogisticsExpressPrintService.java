package com.medusa.gruul.logistics.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.medusa.gruul.logistics.model.dto.manager.LogisticsExpressPrintDto;
import com.medusa.gruul.logistics.model.param.LogisticsExpressPrintParam;
import com.medusa.gruul.logistics.model.vo.LogisticsExpressPrintVo;

/**
 * @author lcysike
 */
public interface ILogisticsExpressPrintService {

    /**
     * 电子面单打印设置列表查询
     * @param logisticsExpressPrintParam
     * @return IPage<LogisticsExpressPrintVo>
     */
    IPage<LogisticsExpressPrintVo> getExpressPrintList(LogisticsExpressPrintParam logisticsExpressPrintParam);

    /**
     * 查询单条电子面单打印机信息
     * @param id
     * @return LogisticsExpressPrintVo
     */
    LogisticsExpressPrintVo getExpressPrintInfo(Long id);

    /**
     * 更新/新增 电子面单打印设置
     * @param logisticsExpressPrintDto
     */
    void setExpressPrintInfo(LogisticsExpressPrintDto logisticsExpressPrintDto);

    /**
     * 启用/停用电子面单打印设置
     * @param type 0-停用 1-启用
     * @param id
     */
    void setExpressPrintStatus(Integer type, Long id);

    /**
     * 删除电子面单打印设置
     * @param id 主键
     */
    void delExpressPrintInfo(Long id);

}
