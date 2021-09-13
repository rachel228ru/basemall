package com.medusa.gruul.logistics.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.medusa.gruul.logistics.model.dto.manager.LogisticsFreightDto;
import com.medusa.gruul.logistics.model.dto.manager.LogisticsTemplateDto;
import com.medusa.gruul.logistics.model.param.LogisticsTemplateParam;
import com.medusa.gruul.logistics.model.vo.LogisticsTemplateVo;

import java.math.BigDecimal;
import java.util.List;

/**
 * 物流模版设置
 * @author lcysike
 */
public interface ILogisticsTemplateService {

    /**
     * getTemplateList 获取模板列表
     * <p>Version: 1.0</p>
     * <p>ModifyVersion: </p>
     * <p>Author: Mr.zhaozheng </P>
     * <p>Date: 2019/12/2022:31</p>
     *
     * @param logisticsTemplateParam
     * @return IPage<LogisticsTemplateVo>
     */
    IPage<LogisticsTemplateVo> getTemplateList(LogisticsTemplateParam logisticsTemplateParam);

    /**
     * getTemplateInfo 获取模板详情
     * <p>Version: 1.0</p>
     * <p>ModifyVersion: </p>
     * <p>Author: Mr.zhaozheng </P>
     * <p>Date: 2019/12/2022:30</p>
     *
     * @param id       模板id
     * @return LogisticsTemplateVo
     */
    LogisticsTemplateVo getTemplateInfo(Long id);


    /**
     * addOrUpdateTemplate 新增/更新模板
     * <p>Version: 1.0</p>
     * <p>ModifyVersion: </p>
     * <p>Author: Mr.zhaozheng </P>
     * <p>Date: 2019/12/2022:31</p>
     *
     * @param logisticsTemplateDto
     */
    void addOrUpdateTemplate(LogisticsTemplateDto logisticsTemplateDto);

    /**
     * 根据模板id 移除模板
     * <p>Version: 1.0</p>
     * <p>ModifyVersion: </p>
     * <p>Author: Mr.zhaozheng </P>
     * <p>Date: 2019/12/2022:22</p>
     *
     * @param id       模板id
     */
    void removeTemplateById(Long id);

    /**
     * 公共接口获取运费模板列表
     *
     * @return
     */
    List<LogisticsTemplateVo> feignGetList();

    /**
     * 公共接口根据id获取运费模板详情
     *
     * @param id
     * @return
     */
    LogisticsTemplateVo feignGetInfo(Long id);

    /**
    * 计算运费
    * @param logisticsFreightDtos
    * @param region 运送地区 json 格式 {"provinceid": ["cityId","cityId2"]}
    * @return: java.math.BigDecimal
    * @author: wangpeng
    * @version : v1.0
    * @date: 2020/2/29 11:12 上午
    */
    BigDecimal freightCalculation(List<LogisticsFreightDto> logisticsFreightDtos, String region);
}
