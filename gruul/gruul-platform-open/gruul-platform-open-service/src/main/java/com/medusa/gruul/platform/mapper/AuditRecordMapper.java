package com.medusa.gruul.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.medusa.gruul.platform.api.entity.AuditRecord;
import com.medusa.gruul.platform.model.dto.AuditRecordDto;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 小程序审核记录表 Mapper 接口
 * </p>
 *
 * @author whh
 * @since 2019-09-07
 */
public interface AuditRecordMapper extends BaseMapper<AuditRecord> {

    /**
     * 获取小程序审核记录
     *
     * @param page
     * @param appId
     * @return
     */
    IPage<AuditRecordDto> selectMiniAuditRecordList(@Param("page") Page page, @Param("appId") String appId);
}
