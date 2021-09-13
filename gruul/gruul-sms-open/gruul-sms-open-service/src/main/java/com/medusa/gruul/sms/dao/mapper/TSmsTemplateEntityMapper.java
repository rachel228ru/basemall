package com.medusa.gruul.sms.dao.mapper;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.medusa.gruul.sms.dao.entity.TSmsTemplateEntity;

import java.util.List;

/**
 * t_sms_template表
 *
 * @author system
 *
 */
public interface TSmsTemplateEntityMapper {
     /**
      物理删除
     */
    int deleteByPrimaryKey(Long id);
     /**
      根据主键查询
     */
     @SqlParser(filter = true)
    TSmsTemplateEntity selectByPrimaryKey(Long id);
    /**
      插入
     */
    @SqlParser(filter = true)
    int insert(TSmsTemplateEntity record);
     /**
     条件 插入
     */
     @SqlParser(filter = true)
    int insertSelective(TSmsTemplateEntity record);
    /**
     条件 更新
     */
    @SqlParser(filter = true)
   int updateByPrimaryKeySelective(TSmsTemplateEntity record);
   /**
     主键 更新
     */
   @SqlParser(filter = true)
   int updateByPrimaryKey(TSmsTemplateEntity record);
   /**
     条件统计
     */
   @SqlParser(filter = true)
   int searchByEntityCount(TSmsTemplateEntity record);
   /**
     条件查询
     */
   @SqlParser(filter = true)
   List<TSmsTemplateEntity> searchByEntity(TSmsTemplateEntity record);
}