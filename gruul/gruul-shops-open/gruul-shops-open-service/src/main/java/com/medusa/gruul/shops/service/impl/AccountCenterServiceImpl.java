package com.medusa.gruul.shops.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.CurUserUtil;
import com.medusa.gruul.common.core.util.SystemCode;
import com.medusa.gruul.common.data.annotation.EscapeShop;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.common.dto.CurShopInfoDto;
import com.medusa.gruul.shops.api.entity.AccountCenter;
import com.medusa.gruul.shops.api.entity.AccountCenterMenu;
import com.medusa.gruul.shops.api.model.AccountCenterMenuDto;
import com.medusa.gruul.shops.api.model.AccountCenterSettingDto;
import com.medusa.gruul.shops.mapper.AccountCenterMapper;
import com.medusa.gruul.shops.api.model.AccountCenterVo;
import com.medusa.gruul.shops.api.model.MenuVo;
import com.medusa.gruul.shops.service.IAccountCenterMenuService;
import com.medusa.gruul.shops.service.IAccountCenterService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 用户中心配置 服务实现类
 * </p>
 *
 * @author whh
 * @since 2019-11-18
 */
@Service
public class AccountCenterServiceImpl extends ServiceImpl<AccountCenterMapper, AccountCenter> implements IAccountCenterService {

    @Autowired
    private IAccountCenterMenuService accountCenterMenuService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @EscapeShop
    public void add(AccountCenterSettingDto accountCenterSettingDto) {
        AccountCenter accountCenter = this.getByTenantId();
        if (accountCenter == null) {
            accountCenter = new AccountCenter();
        }
        accountCenter.setHeadStyle(accountCenterSettingDto.getHeadStyle());
        accountCenter.setCustomStyle(accountCenterSettingDto.getCustomStyle());
        accountCenter.setGetCartText(accountCenterSettingDto.getGetCartText());
        accountCenter.setHideCartInlet(accountCenterSettingDto.getHideCartInlet());
        accountCenter.setOrderInfo(accountCenterSettingDto.getOrderInfo());
        accountCenter.setMenuStyle(accountCenterSettingDto.getMenuStyle());
        if (accountCenter.getId() == null) {
            this.baseMapper.insert(accountCenter);
        } else {
            this.baseMapper.updateById(accountCenter);
        }
        createMenu(accountCenterSettingDto);
    }

    /**
     * 创建菜单
     *
     * @param accountCenterSettingDto com.medusa.gruul.account.api.model.AccountCenterSettingDto
     */
    @EscapeShop
    private void createMenu(AccountCenterSettingDto accountCenterSettingDto) {
        //清空原本菜单
        accountCenterMenuService.clear();
        List<AccountCenterMenuDto> menu = accountCenterSettingDto.getMenuVos();
        //拼团用户信息菜单全部插入
            for (AccountCenterMenuDto accountCenterMenuDto : menu) {
                AccountCenterMenu accountCenterMenu = getAccountCenterMenu(accountCenterMenuDto, 0L);
                accountCenterMenuService.save(accountCenterMenu);
                List<AccountCenterMenuDto> subMenu = accountCenterMenuDto.getSubMenu();
                if (CollectionUtil.isNotEmpty(subMenu)) {
                    //子菜单最多1层
                    for (AccountCenterMenuDto centerMenuDto : subMenu) {
                        AccountCenterMenu sub = getAccountCenterMenu(centerMenuDto, accountCenterMenu.getId());
                        accountCenterMenuService.save(sub);
                    }
                }
            }
    }

    @Override
    @EscapeShop
    public AccountCenterVo accountCenterSetting() {
        AccountCenter accountCenter = this.getByTenantId();
        AccountCenterVo accountCenterVo = new AccountCenterVo();
        BeanUtils.copyProperties(accountCenter, accountCenterVo);
        List<MenuVo> menuVos = accountCenterMenuService.getByTenantId();
        accountCenterVo.setMenuVos(menuVos);
        return accountCenterVo;
    }

    @Override
    @EscapeShop
    public synchronized void accountCenterSettingMotify(AccountCenterSettingDto dto) {
        if (dto.getId() == null) {
            throw new ServiceException(SystemCode.DATA_NOT_EXIST);
        }
        AccountCenter accountCenter = this.baseMapper.selectOne(new QueryWrapper<AccountCenter>().eq("id", dto.getId()));
        if (accountCenter == null) {
            throw new ServiceException(SystemCode.DATA_NOT_EXIST);
        }
        BeanUtils.copyProperties(dto, accountCenter);
        this.baseMapper.updateById(accountCenter);
        createMenu(dto);
    }

    /**
     * 创建菜单对象
     *
     * @param accountCenterMenuDto com.medusa.gruul.account.api.model.AccountCenterMenuDto
     * @param pId                  父级id
     * @return com.medusa.gruul.account.api.entity.AccountCenterMenu
     */
    private AccountCenterMenu getAccountCenterMenu(AccountCenterMenuDto accountCenterMenuDto, Long pId) {
        AccountCenterMenu accountCenterMenu = new AccountCenterMenu();
        accountCenterMenu.setMenuName(accountCenterMenuDto.getMenuName());
        accountCenterMenu.setMenuIconUrl(accountCenterMenuDto.getMenuIconUrl());
        accountCenterMenu.setDefaultIcon(accountCenterMenuDto.getDefaultIcon());
        accountCenterMenu.setStyleType(accountCenterMenuDto.getStyleType());
        accountCenterMenu.setSortIndex(accountCenterMenuDto.getSortIndex());
        accountCenterMenu.setPId(pId);
        accountCenterMenu.setHideMenu(accountCenterMenuDto.getHideMenu());
        accountCenterMenu.setSplitFlag(accountCenterMenuDto.getSplitFlag());
        accountCenterMenu.setAllowUse(accountCenterMenuDto.getAllowUse());
        accountCenterMenu.setLinkSelectItem(accountCenterMenuDto.getLinkSelectItem());
        return accountCenterMenu;
    }

    /**
     * 根据租户id 获取用户中心配置
     *
     * @return com.medusa.gruul.account.api.entity.AccountCenter
     */
    @SqlParser(filter = true)
    private AccountCenter getByTenantId() {
        return this.baseMapper.selectOne(null);
    }
}
