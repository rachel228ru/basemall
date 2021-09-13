package com.medusa.gruul.afs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.afs.api.entity.AfsReason;

/**
 * <p>
 * 售后工单 服务类
 * </p>
 *
 * @author alan
 * @since 2020 -08-05
 */
public interface IAfsReasonService extends IService<AfsReason> {

    /**
     * 初始化
     */
    void init();
}
