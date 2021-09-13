package com.medusa.gruul.shops.controller.remote;

import com.medusa.gruul.common.core.annotation.EscapeLogin;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.shops.api.entity.ShopsPartner;
import com.medusa.gruul.shops.api.model.AccountCenterVo;
import com.medusa.gruul.shops.service.IAccountCenterService;
import com.medusa.gruul.shops.service.ShopsPartnerService;
import com.medusa.gruul.shops.service.ShopsRenovationTemPageAssService;
import com.medusa.gruul.shops.service.ShopsRenovationTemPageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;


/**
 * @author whh
 */
@RestController(value = "remoteController")
@Api(tags = "远程调用接口,仅限后端fegin调用-->RemoteShopsService")
public class RemoteController {

    @Autowired
    private ShopsPartnerService shopsPartnerService;
    @Autowired
    private IAccountCenterService accountCenterService;

    @Autowired
    private ShopsRenovationTemPageService shopsRenovationTemPageService;
    @Autowired
    private ShopsRenovationTemPageAssService shopsRenovationTemPageAssService;


    @GetMapping("/get/platform_id/{platformId}")
    @EscapeLogin
    @ApiOperation(value = "根据平台用户id获取店铺信息")
    public ShopsPartner getByPlatformIdAndTenantId(@PathVariable(name = "platformId") Long platformId, @RequestParam(value = "tenantId") String tenantId) {
        return  shopsPartnerService.getByPlatformIdAndTenantId(platformId,tenantId);
    }


    @GetMapping("/fegin/account-center")
    @EscapeLogin
    @ApiOperation(value = "获取用户中心配置")
    public AccountCenterVo accountCenterSetting() {
        return accountCenterService.accountCenterSetting();
    }


    /**
     *专区删除时匹配删除 t_shop_renovation_page info
     *
     * @param modelId 专区id
     * @return 删除结果
     */
    @DeleteMapping(value = "/del/shopRenovationPage")
    @ApiOperation(value = "专区删除匹配")
    @EscapeLogin
    public Boolean delShopRenovationPage(@RequestParam(value = "modelId",required = true) @NotNull(message="modelId不能为null") String modelId){

        return  shopsRenovationTemPageService.delShopRenovationPageByModelId(modelId);
    }

    /**
     * 修改专区名称
     * @param tenantId 租户id
     * @param shopId 店铺id
     * @param linkName 专区名称
     * @param newLinkName 专区现名称
     * @return
     */
    @GetMapping("/fegin/updateSpecialArea")
    @EscapeLogin
    @ApiOperation(value = "获取用户中心配置")
    public  boolean updateSpecialArea(@RequestParam(value = "tenantId",required = true)String tenantId,
                                      @RequestParam(value = "shopId",required = true)String shopId,
                                      @RequestParam(value = "linkName",required = true)String linkName,
                                      @RequestParam(value = "newLinkName",required = true)String newLinkName) {
        return shopsRenovationTemPageAssService.updateSpecialArea(tenantId, shopId, linkName, newLinkName);
    }

}
