package com.medusa.gruul.shops.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.shops.api.entity.ShopGuidePageSwitch;


/**
 * @author xiaoq
 * @date created in 2020/10/17
 */
public interface IShopGuidePageSwitchService extends IService<ShopGuidePageSwitch> {

	/**
	 * 引导页开关获取
	 * @return
	 */
	ShopGuidePageSwitch getShopGuidePageSwitch();

	/**
	 * 修改引导页开启or关闭状态
	 * @param status 状态值
	 */
	void updateShopGuidePageSwitch(Boolean status);

	/**
	 * 生成默认店铺开关
	 * @param jsonStr
	 */
	void init(String jsonStr);

}
