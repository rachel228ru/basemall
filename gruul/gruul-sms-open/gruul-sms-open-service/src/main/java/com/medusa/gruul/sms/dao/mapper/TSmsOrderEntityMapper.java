package com.medusa.gruul.sms.dao.mapper;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.medusa.gruul.sms.dao.entity.TSmsOrderEntity;
import com.medusa.gruul.sms.model.dto.SmsDoSendDto;

import java.util.List;

/**
 * t_sms_order表
 *
 * @author system
 *
 */
public interface TSmsOrderEntityMapper {
     /**
      物理删除
     */
    int deleteByPrimaryKey(Long id);
     /**
      根据主键查询
     */
     @SqlParser(filter = true)
    TSmsOrderEntity selectByPrimaryKey(Long id);
    /**
      插入
     */
    int insert(TSmsOrderEntity record);
     /**
     条件 插入
     */
     @SqlParser(filter = true)
    int insertSelective(TSmsOrderEntity record);
    /**
     条件 更新
     */
    @SqlParser(filter = true)
   int updateByPrimaryKeySelective(TSmsOrderEntity record);
   /**
     主键 更新
     */
   @SqlParser(filter = true)
   int updateByPrimaryKey(TSmsOrderEntity record);
   /**
     条件统计
     */
   int searchByEntityCount(TSmsOrderEntity record);
   /**
     条件查询
     */
    @SqlParser(filter = true)
    List<TSmsOrderEntity> searchByEntity(TSmsOrderEntity record);


    @SqlParser(filter = true)
    List<TSmsOrderEntity> doListWaitSendOrder(int smsSendStatus);

    @SqlParser(filter = true)
    SmsDoSendDto getSendSmsCfg(Long id);
}