package com.medusa.gruul.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.platform.api.entity.BaseMenu;
import com.medusa.gruul.platform.model.dto.MenuDto;

import java.util.List;

/**
 * <p>
 * 菜单基础版本维护 服务类
 * </p>
 *
 * @author whh
 * @since 2020-01-09
 */
public interface IBaseMenuService extends IService<BaseMenu> {

    /**
     * 获取租户的所有套餐
     *
     * @param tenantId 租户id
     * @return com.medusa.gruul.platform.model.dto.MenuDto
     */
    List<MenuDto> getByTenantIdMenu(String tenantId);


    /**
     * 获取指定模板库代码菜单
     *
     * @param templateCodeId 模板库代码id
     * @return com.medusa.gruul.platform.api.entity.BaseMenu
     */
    List<BaseMenu> getBaseMenu(Long templateCodeId);

    /**
     * 获取最新的一个模板库代码id对应的菜单
     *
     * @return com.medusa.gruul.platform.api.entity.BaseMenu
     */
    List<BaseMenu> getLastBaseMenu();

}
