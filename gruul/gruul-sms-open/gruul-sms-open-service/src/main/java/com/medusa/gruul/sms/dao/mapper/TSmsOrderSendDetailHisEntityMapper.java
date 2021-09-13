package com.medusa.gruul.sms.dao.mapper;
import com.medusa.gruul.sms.dao.entity.TSmsOrderSendDetailHisEntity;

import java.util.List;

/**
 * t_sms_order_send_detail_his_201912表
 *
 * @author system
 *
 */
public interface TSmsOrderSendDetailHisEntityMapper {
     /**
      物理删除
     */
    int deleteByPrimaryKey(Long id);
     /**
      根据主键查询
     */
    TSmsOrderSendDetailHisEntity selectByPrimaryKey(Long id);
    /**
      插入
     */
    int insert(TSmsOrderSendDetailHisEntity record);
     /**
     条件 插入
     */
    int insertSelective(TSmsOrderSendDetailHisEntity record);
    /**
     条件 更新
     */
   int updateByPrimaryKeySelective(TSmsOrderSendDetailHisEntity record);
   /**
     主键 更新
     */
   int updateByPrimaryKey(TSmsOrderSendDetailHisEntity record);
   /**
     条件统计
     */
   int searchByEntityCount(TSmsOrderSendDetailHisEntity record);
   /**
     条件查询
     */
   List<TSmsOrderSendDetailHisEntity> searchByEntity(TSmsOrderSendDetailHisEntity record);
}