package com.medusa.gruul.shops.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.shops.api.entity.ShopGuidePage;
import com.medusa.gruul.shops.model.dto.ShopGuidePageDto;
import com.medusa.gruul.shops.model.vo.ShopGuidePageVo;

import java.util.List;

/**
 * @author create by xiaoq
 * @date created in 2020/10/15
 */
public interface IShopGuidePageService extends IService<ShopGuidePage> {

	/**
	 * 店铺自定义引导页获取
	 * @return 自定义引导页Vo
	 */
	List<ShopGuidePageDto> getShopGuidePage();

	/**
	 * 店铺自定义引导页更新
	 * @param shopGuidePageDtos 自定义引导页dto
	 */
	void updateShopGuidePage(List<ShopGuidePageDto> shopGuidePageDtos);

    /**
     * 店铺自定义引导页更新
     * @param shopGuidePageDtos
     */
    void updateShopGuide(List<ShopGuidePageDto> shopGuidePageDtos);


    /**
	 * 店铺默认引导页获取
	 * @return
	 */
	List<ShopGuidePageDto> getShopGuidePageDefault();

	/**
	 * 店铺默认引导页信息新增
	 * @param jsonStr
	 */
	void init(String jsonStr);

}
