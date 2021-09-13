package com.medusa.gruul.logistics.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medusa.gruul.logistics.api.entity.LogisticsExpress;
import com.medusa.gruul.logistics.model.param.LogisticsExpressParam;
import com.medusa.gruul.logistics.model.vo.LogisticsExpressVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author lcysike
 */
@Repository
public interface LogisticsExpressMapper extends BaseMapper<LogisticsExpress> {

    /**
     * 查询快递公司地址列表
     * @param logisticsExpressParam
     * @return List<LogisticsExpressVo>
     */
    List<LogisticsExpressVo> queryLogisticsExpressList(@Param("logisticsExpressParam") LogisticsExpressParam logisticsExpressParam);

    /**
     * 查询单条物流公司信息
     * @param id
     * @return LogisticsExpressVo
     */
    LogisticsExpressVo queryLogisticsExpressInfo(@Param("id") Long id);


}