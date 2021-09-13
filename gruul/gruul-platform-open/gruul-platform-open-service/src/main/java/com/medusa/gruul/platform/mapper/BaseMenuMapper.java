package com.medusa.gruul.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medusa.gruul.platform.api.entity.BaseMenu;
import com.medusa.gruul.platform.model.dto.MenuDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单基础版本维护 Mapper 接口
 * </p>
 *
 * @author whh
 * @since 2020-01-09
 */
public interface BaseMenuMapper extends BaseMapper<BaseMenu> {

    /**
     * 获取指定租户的菜单
     *
     * @param tenantId 租户id
     * @return com.medusa.gruul.platform.model.dto.MenuDto
     */
    List<MenuDto> selectByTenantIdMenu(@Param("tenantId") String tenantId);

    /**
     * 获取最大的模板id菜单
     *
     * @return com.medusa.gruul.platform.api.entity.BaseMenu
     */
    List<BaseMenu> selectLastBaseMenu();

}
