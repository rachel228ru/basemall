package com.medusa.gruul.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.platform.api.entity.BaseMenu;
import com.medusa.gruul.platform.mapper.BaseMenuMapper;
import com.medusa.gruul.platform.model.dto.MenuDto;
import com.medusa.gruul.platform.service.IBaseMenuService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 菜单基础版本维护 服务实现类
 * </p>
 *
 * @author whh
 * @since 2020-01-09
 */
@Service
public class BaseMenuServiceImpl extends ServiceImpl<BaseMenuMapper, BaseMenu> implements IBaseMenuService {


    @Override
    public List<MenuDto> getByTenantIdMenu(String tenantId) {
        return this.baseMapper.selectByTenantIdMenu(tenantId);
    }

    @Override
    public List<BaseMenu> getBaseMenu(Long templateCodeId) {
        return this.baseMapper.selectList(new QueryWrapper<BaseMenu>().eq("template_code_id", templateCodeId));
    }

    @Override
    public List<BaseMenu> getLastBaseMenu() {
        return this.baseMapper.selectLastBaseMenu();
    }
}
