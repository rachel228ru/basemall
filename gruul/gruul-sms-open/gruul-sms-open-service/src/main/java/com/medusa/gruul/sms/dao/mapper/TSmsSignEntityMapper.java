package com.medusa.gruul.sms.dao.mapper;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.medusa.gruul.sms.dao.entity.TSmsSignEntity;

import java.util.List;

/**
 * t_sms_sign表
 *
 * @author system
 *
 */
public interface TSmsSignEntityMapper {
     /**
      物理删除
     */
    int deleteByPrimaryKey(Long id);
     /**
      根据主键查询
     */
     @SqlParser(filter = true)
    TSmsSignEntity selectByPrimaryKey(Long id);
    /**
      插入
     */
    @SqlParser(filter = true)
    int insert(TSmsSignEntity record);
     /**
     条件 插入
     */
     @SqlParser(filter = true)
     int insertSelective(TSmsSignEntity record);
    /**
     条件 更新
     */
    @SqlParser(filter = true)
    int updateByPrimaryKeySelective(TSmsSignEntity record);
   /**
     主键 更新
     */
   @SqlParser(filter = true)
   int updateByPrimaryKey(TSmsSignEntity record);
   /**
     条件统计
     */
   @SqlParser(filter = true)
   int searchByEntityCount(TSmsSignEntity record);
   /**
     条件查询
     */
   @SqlParser(filter = true)
   List<TSmsSignEntity> searchByEntity(TSmsSignEntity record);
}