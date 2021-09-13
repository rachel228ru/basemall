package com.medusa.gruul.oss.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medusa.gruul.oss.api.entity.FileEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件上传
 */
@Mapper
public interface FileDao extends BaseMapper<FileEntity> {

}
