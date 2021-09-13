package com.medusa.gruul.sms.dao.mapper;
import com.medusa.gruul.sms.dao.entity.TSmsOrderSendDetailEntity;

import java.util.List;

/**
 * t_sms_order_send_detail表
 *
 * @author system
 *
 */
public interface TSmsOrderSendDetailEntityMapper {
     /**
      物理删除
     */
    int deleteByPrimaryKey(Long id);
     /**
      根据主键查询
     */
    TSmsOrderSendDetailEntity selectByPrimaryKey(Long id);
    /**
      插入
     */
    int insert(TSmsOrderSendDetailEntity record);
     /**
     条件 插入
     */
    int insertSelective(TSmsOrderSendDetailEntity record);
    /**
     条件 更新
     */
   int updateByPrimaryKeySelective(TSmsOrderSendDetailEntity record);
   /**
     主键 更新
     */
   int updateByPrimaryKey(TSmsOrderSendDetailEntity record);
   /**
     条件统计
     */
   int searchByEntityCount(TSmsOrderSendDetailEntity record);
   /**
     条件查询
     */
   List<TSmsOrderSendDetailEntity> searchByEntity(TSmsOrderSendDetailEntity record);
}