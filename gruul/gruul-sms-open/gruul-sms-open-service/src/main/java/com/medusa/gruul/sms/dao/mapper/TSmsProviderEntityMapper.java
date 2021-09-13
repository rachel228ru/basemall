package com.medusa.gruul.sms.dao.mapper;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.medusa.gruul.sms.dao.entity.TSmsProviderEntity;

import java.util.List;

/**
 * t_sms_provider表
 *
 * @author system
 *
 */
public interface TSmsProviderEntityMapper {
     /**
      物理删除
     */
    int deleteByPrimaryKey(Long id);
     /**
      根据主键查询
     */
     @SqlParser(filter = true)
    TSmsProviderEntity selectByPrimaryKey(Long id);
    /**
      插入
     */
    int insert(TSmsProviderEntity record);
     /**
     条件 插入
     */
    int insertSelective(TSmsProviderEntity record);
    /**
     条件 更新
     */
   int updateByPrimaryKeySelective(TSmsProviderEntity record);
   /**
     主键 更新
     */
   int updateByPrimaryKey(TSmsProviderEntity record);
   /**
     条件统计
     */
   int searchByEntityCount(TSmsProviderEntity record);
   /**
     条件查询
     */
   @SqlParser(filter = true)
   List<TSmsProviderEntity> searchByEntity(TSmsProviderEntity record);
}