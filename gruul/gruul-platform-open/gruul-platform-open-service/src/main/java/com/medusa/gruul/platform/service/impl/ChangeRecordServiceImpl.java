package com.medusa.gruul.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.platform.api.entity.ChangeRecord;
import com.medusa.gruul.platform.mapper.ChangeRecordMapper;
import com.medusa.gruul.platform.service.IChangeRecordService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 小程序变更记录 服务实现类
 * </p>
 *
 * @author whh
 * @since 2019-09-07
 */
@Service
public class ChangeRecordServiceImpl extends ServiceImpl<ChangeRecordMapper, ChangeRecord>
        implements IChangeRecordService {

}
