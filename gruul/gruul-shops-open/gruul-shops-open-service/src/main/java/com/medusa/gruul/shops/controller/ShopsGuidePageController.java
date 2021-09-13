package com.medusa.gruul.shops.controller;

import com.medusa.gruul.common.core.annotation.EscapeLogin;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.shops.model.dto.ShopGuidePageDto;
import com.medusa.gruul.shops.model.vo.ShopGuidePageVo;
import com.medusa.gruul.shops.service.IShopGuidePageService;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: 引导页信息
 * @Author: xiaoq
 * @Date : 2020/10/15 15:54
 */
@RestController
@RequestMapping(value = "/shop/guide")
public class ShopsGuidePageController {

	@Autowired
	private IShopGuidePageService shopGuidePageService;

	@GetMapping("page/get")
	@ApiOperation(value = "店铺自定义引导页获取")
	public Result<List<ShopGuidePageDto>> getShopGuidePage() {
		List<ShopGuidePageDto> shopGuidePage = shopGuidePageService.getShopGuidePage();
		return Result.ok(shopGuidePage);
	}


	@PostMapping("page/update")
	@ApiOperation(value = "店铺自定义引导页更新")
	public Result updateShopGuidePage(@RequestBody List<ShopGuidePageDto> shopGuidePageDto) {
		shopGuidePageService.updateShopGuidePage(shopGuidePageDto);
		return Result.ok();
	}


	@GetMapping("page/getDefault")
	@ApiOperation(value = "店铺自定义引导页默认值获取")
	public Result<List<ShopGuidePageDto>> getShopGuidePageDefault() {
		List<ShopGuidePageDto> shopGuidePage = shopGuidePageService.getShopGuidePageDefault();
		return Result.ok(shopGuidePage);
	}

    @PostMapping("page/guidePage/update")
    @ApiOperation(value = "店铺自定义引导页更新")
    public Result updateShopGuide(@RequestBody List<ShopGuidePageDto> shopGuidePageDto) {
        shopGuidePageService.updateShopGuide(shopGuidePageDto);
        return Result.ok();
    }
}
