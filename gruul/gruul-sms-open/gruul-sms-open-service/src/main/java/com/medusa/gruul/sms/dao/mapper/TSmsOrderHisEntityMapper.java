package com.medusa.gruul.sms.dao.mapper;
import com.medusa.gruul.sms.dao.entity.TSmsOrderHisEntity;

import java.util.List;

/**
 * t_sms_order_his_201912表
 *
 * @author system
 *
 */
public interface TSmsOrderHisEntityMapper {
     /**
      物理删除
     */
    int deleteByPrimaryKey(Long id);
     /**
      根据主键查询
     */
    TSmsOrderHisEntity selectByPrimaryKey(Long id);
    /**
      插入
     */
    int insert(TSmsOrderHisEntity record);
     /**
     条件 插入
     */
    int insertSelective(TSmsOrderHisEntity record);
    /**
     条件 更新
     */
   int updateByPrimaryKeySelective(TSmsOrderHisEntity record);
   /**
     主键 更新
     */
   int updateByPrimaryKey(TSmsOrderHisEntity record);
   /**
     条件统计
     */
   int searchByEntityCount(TSmsOrderHisEntity record);
   /**
     条件查询
     */
   List<TSmsOrderHisEntity> searchByEntity(TSmsOrderHisEntity record);
}