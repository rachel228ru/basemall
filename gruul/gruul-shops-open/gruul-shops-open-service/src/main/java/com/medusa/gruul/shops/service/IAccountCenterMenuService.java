package com.medusa.gruul.shops.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.shops.api.entity.AccountCenterMenu;
import com.medusa.gruul.shops.api.model.MenuVo;

import java.util.List;

/**
 * <p>
 * 用户中心菜单配置 服务类
 * </p>
 *
 * @author whh
 * @since 2019-11-18
 */
public interface IAccountCenterMenuService extends IService<AccountCenterMenu> {

    /**
     * 清空当前指定租户的用户中心菜单
     */
    void clear();

    /**
     * 获取当前指定租户的用户中心菜单
     * @return com.medusa.gruul.account.model.vo.MenuVo
     */
    List<MenuVo> getByTenantId();


}
