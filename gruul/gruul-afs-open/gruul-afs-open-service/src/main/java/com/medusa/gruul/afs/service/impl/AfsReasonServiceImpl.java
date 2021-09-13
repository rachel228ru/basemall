package com.medusa.gruul.afs.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.afs.api.entity.AfsReason;
import com.medusa.gruul.afs.mapper.AfsReasonMapper;
import com.medusa.gruul.afs.service.IAfsReasonService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 售后工单 服务实现类
 * </p>
 *
 * @author alan
 * @since 2020 -08-05
 */
@Service
public class AfsReasonServiceImpl extends ServiceImpl<AfsReasonMapper, AfsReason> implements IAfsReasonService {

    /**
     * 初始化退货原因
     *
     * @param
     * @return void
     * @author alan
     * @date 2021/3/17 22:33
     */
    @Override
    public void init() {
        AfsReason reason = this.getOne(null);
        if (ObjectUtil.isNull(reason)) {
            reason = new AfsReason();
            reason.setName("未收到货");
            this.save(reason);
            AfsReason reason2 = new AfsReason();
            reason2.setName("货物损坏");
            this.save(reason2);
        }

    }
}
