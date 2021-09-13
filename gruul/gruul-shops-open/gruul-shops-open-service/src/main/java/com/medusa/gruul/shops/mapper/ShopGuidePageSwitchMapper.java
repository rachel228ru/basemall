package com.medusa.gruul.shops.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medusa.gruul.shops.api.entity.ShopGuidePageSwitch;
import org.apache.ibatis.annotations.Param;


/**
 * @author xiaoq
 */
public interface ShopGuidePageSwitchMapper extends BaseMapper<ShopGuidePageSwitch> {
	/**
	 * 引导页开关获取
	 * @return
	 */
	ShopGuidePageSwitch seleteByShopId();

	/**
	 *  引导页开关关闭或开启
	 * @param status 状态值
	 * @return
	 */
	Integer updateShopGuidePageSwitchStatus(@Param("status") Boolean status);
}
