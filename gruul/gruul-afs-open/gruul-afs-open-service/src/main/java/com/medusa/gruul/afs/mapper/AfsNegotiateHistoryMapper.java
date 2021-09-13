package com.medusa.gruul.afs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medusa.gruul.afs.api.entity.AfsNegotiateHistory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 协商历史 Mapper 接口
 * </p>
 *
 * @author alan
 * @since 2020 -08-05
 */
public interface AfsNegotiateHistoryMapper extends BaseMapper<AfsNegotiateHistory> {

    /**
     * 查询协商历史
     *
     * @param orderId the order id
     * @param type    the type
     * @return the list
     */
    List<AfsNegotiateHistory> negotiateHistoryList(@Param(value = "orderId") Long orderId,
                                                   @Param(value = "type") String type);
}
