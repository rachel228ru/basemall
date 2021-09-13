package com.medusa.gruul.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.platform.api.entity.TenementMenu;
import com.medusa.gruul.platform.mapper.TenementMenuMapper;
import com.medusa.gruul.platform.service.IBaseMenuService;
import com.medusa.gruul.platform.service.ITenementMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 平台租户菜单表 服务实现类
 * </p>
 *
 * @author whh
 * @since 2020-01-09
 */
@Service
public class TenementMenuServiceImpl extends ServiceImpl<TenementMenuMapper, TenementMenu> implements ITenementMenuService {


    @Autowired
    private IBaseMenuService baseMenuService;


}
