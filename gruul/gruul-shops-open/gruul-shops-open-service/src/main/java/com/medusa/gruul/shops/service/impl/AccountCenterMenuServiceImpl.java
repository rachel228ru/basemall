package com.medusa.gruul.shops.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.common.core.util.CurUserUtil;
import com.medusa.gruul.common.core.util.StringUtil;
import com.medusa.gruul.common.data.annotation.EscapeShop;
import com.medusa.gruul.common.dto.CurShopInfoDto;
import com.medusa.gruul.shops.api.entity.AccountCenterMenu;
import com.medusa.gruul.shops.api.model.MenuVo;
import com.medusa.gruul.shops.mapper.AccountCenterMenuMapper;
import com.medusa.gruul.shops.properties.GlobalConstant;
import com.medusa.gruul.shops.service.IAccountCenterMenuService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户中心菜单配置 服务实现类
 * </p>
 *
 * @author whh
 * @since 2019-11-18
 */
@Service
public class AccountCenterMenuServiceImpl extends ServiceImpl<AccountCenterMenuMapper, AccountCenterMenu> implements IAccountCenterMenuService {

    @Override
    @EscapeShop
    public void clear() {
        this.baseMapper.delete(null);
    }

    @Override
    @EscapeShop
    public List<MenuVo> getByTenantId() {
        List<AccountCenterMenu> accountCenterMenus = this.baseMapper.selectList(null);
        Map<Long, List<AccountCenterMenu>> pidGroup = accountCenterMenus.stream().collect(Collectors.groupingBy(AccountCenterMenu::getPId));
        //获取一级id
        List<AccountCenterMenu> oneMenu = pidGroup.get(0L);
        List<MenuVo> menuVos = new ArrayList<>(oneMenu.size());
        for (AccountCenterMenu menu : oneMenu) {
            if ( StrUtil.isNotBlank(menu.getLinkSelectItem())) {
                JSONObject linkSelectItem = JSONObject.parseObject(menu.getLinkSelectItem());
                String name = linkSelectItem.getString("name");
                if (StrUtil.isNotBlank(name) && ("团长中心".equals(name)
                        || "团长信息".equals(name)
                        || "提货点信息".equals(name)
                        || "提货点后台".equals(name))) {
                    continue;
                }
            }
            MenuVo menuVo = new MenuVo();
            BeanUtils.copyProperties(menu, menuVo);
            List<AccountCenterMenu> subMenu = pidGroup.get(menu.getId());
            if (CollectionUtil.isNotEmpty(subMenu)) {
                List<MenuVo> subVos = new ArrayList<>(subMenu.size());
                for (AccountCenterMenu subAccountCenterMenu : subMenu) {
                    MenuVo subMenuVo = new MenuVo();
                    BeanUtils.copyProperties(subAccountCenterMenu, subMenuVo);
                    subVos.add(subMenuVo);
                }
                menuVo.setSubMenu(subVos);
            }
            menuVos.add(menuVo);
        }
        return menuVos;
    }
}
