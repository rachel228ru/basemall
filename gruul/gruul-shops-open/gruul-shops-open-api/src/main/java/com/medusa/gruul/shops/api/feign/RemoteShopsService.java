

package com.medusa.gruul.shops.api.feign;

import com.medusa.gruul.common.core.annotation.EscapeLogin;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.shops.api.entity.ShopsPartner;
import com.medusa.gruul.shops.api.model.AccountCenterVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;


/**
 * @author create by zq
 * @date created in 2019/11/15
 */
@FeignClient(value = "shops-open")
public interface RemoteShopsService {

    /**
     * 根据平台用户id获取店铺信息
     *
     * @param platformId 平台用户id
     * @param tenantId 租户id
     * @return Result
     */
    @RequestMapping(value = "/get/platform_id/{platformId}", method = RequestMethod.GET)
    ShopsPartner getByPlatformIdAndTenantId(@PathVariable(name = "platformId") Long platformId, @RequestParam(value = "tenantId") String tenantId);

    /**
     * 新增默认店铺
     *
     * @param tenantId 租户id
     * @param pass 密码
     * @param phone 手机号
     * @param platformId 平台用户id
     * @return Result
     */
    @RequestMapping(value = "/shops_partner/save", method = RequestMethod.GET)
    Result<ShopsPartner> save(@RequestParam(value = "tenantId") @NotNull String tenantId,
                @RequestParam(value = "pass") @NotNull String pass,
                @RequestParam(value = "phone") @NotNull String phone,
                @RequestParam(value = "platformId") @NotNull Long platformId);


    /**
     * 获取店铺 by tenantId
     *
     * @param tenantId
     * @return ShopsPartner
     */
    @RequestMapping(value = "/shops_partner/one_by_tenant_id/{tenantId}", method = RequestMethod.GET)
    ShopsPartner oneByTenantId(@NotNull @PathVariable(value = "tenantId") String tenantId);


    /**
     * 获取店铺 by shopId
     *
     * @param shopId
     * @return ShopsPartner
     */
    @RequestMapping(value = "/shops_partner/one/{shopId}", method = RequestMethod.GET)
    ShopsPartner oneByShopId(@NotNull @PathVariable(value = "shopId") String shopId);


    /**
     * 获取店铺 by shopId
     *
     * @return ShopsPartner
     */
    @RequestMapping(value = "/fegin/account-center", method = RequestMethod.GET)
    AccountCenterVo accountCenterSetting();

    /**
     *专区删除时匹配删除 t_shop_renovation_page info
     *
     * @param modelId 专区id
     * @return 删除结果
     */
    @RequestMapping(value = "/del/shopRenovationPage", method = RequestMethod.DELETE)
    Boolean delShopRenovationPage(@RequestParam(value = "modelId",required = true) @NotNull(message="modelId不能为null") String modelId);

    /**
     * 修改专修专区名称
     * @param tenantId 租户id
     * @param shopId 店铺id
     * @param linkName 专区原名称
     * @param newLinkName 专区现名称
     * @return
     */
    @RequestMapping(value = "/fegin/updateSpecialArea", method = RequestMethod.GET)
    boolean updateSpecialArea(@RequestParam(value = "tenantId",required = true)String tenantId,
                                      @RequestParam(value = "shopId",required = true)String shopId,
                                      @RequestParam(value = "linkName",required = true)String linkName,
                                      @RequestParam(value = "newLinkName",required = true)String newLinkName);


}
