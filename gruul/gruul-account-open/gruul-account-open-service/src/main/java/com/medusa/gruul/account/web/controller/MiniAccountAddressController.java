package com.medusa.gruul.account.web.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.medusa.gruul.account.model.dto.AddressCraeteDto;
import com.medusa.gruul.account.model.dto.AddressUpdateDto;
import com.medusa.gruul.account.model.vo.AccountAddressListVo;
import com.medusa.gruul.account.model.vo.AnalysisLatitudeAndlongitudeVo;
import com.medusa.gruul.account.model.vo.LatitudeAndlongitudeVo;
import com.medusa.gruul.account.service.IMiniAccountAddressService;
import com.medusa.gruul.account.util.GeoCodeUtil;
import com.medusa.gruul.common.core.annotation.EscapeLogin;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.AreaUtil;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.common.dto.AreaDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 会员地址表 前端控制器
 * </p>
 *
 * @author whh
 * @since 2019-11-18
 */
@RestController
@RequestMapping("/mini-account-address")
@Api(tags = "用户地址管理接口")
public class MiniAccountAddressController {

    @Autowired
    private IMiniAccountAddressService miniAccountAddressService;
    @Autowired
    private GeoCodeUtil geoCodeUtil;

    @GetMapping
    @ApiOperation(value = "获取用户地址")
    public Result<List<AccountAddressListVo>> addersGet(@ApiParam(value = "type 1-获取所有 2-获取默认地址,默认type=1") @RequestParam(defaultValue = "1") @NotNull(message = "错误参数") Integer type) {
        List<AccountAddressListVo> vos = miniAccountAddressService.addersGet(type);
        return Result.ok(vos);
    }

    @PostMapping
    @ApiOperation(value = "添加用户地址")
    public Result addressCraete(@RequestBody @Validated AddressCraeteDto addersVO) {
        miniAccountAddressService.addressCraete(addersVO);
        return Result.ok();
    }

    @PutMapping
    @ApiOperation(value = "更新用户地址|设置为默认")
    public Result addersUpdate(@RequestBody @Validated AddressUpdateDto updateDto) {
        miniAccountAddressService.addersUpdate(updateDto);
        return Result.ok();
    }

    @DeleteMapping("{adderssId}")
    @ApiOperation(value = "删除用户地址")
    public Result addressDelete(@ApiParam(name = "地址id") @PathVariable(value = "adderssId") @NotNull(message = "错误参数") Integer adderssId) {
        miniAccountAddressService.addressDelete(adderssId);
        return Result.ok();
    }

    @GetMapping("/area")
    @ApiOperation(value = "获取省/市/区")
    public Result<List<AreaDto>> getArea(@ApiParam(value = "获取类型 1-省 2-市 3-区") @RequestParam @NotNull(message = "错误参数") Integer type,
                                         @ApiParam(value = "父级id") @RequestParam(required = false) String id) {
        List<AreaDto> vo;
        switch (type) {
            case 1:
                vo = AreaUtil.getProvinceList(id);
                break;
            case 2:
                vo = AreaUtil.getCityList(id);
                break;
            case 3:
                vo = AreaUtil.getDistrictList(id);
                break;
            default:
                return Result.failed("类型错误");
        }

        return Result.ok(vo);
    }

    @GetMapping("/latitude-longitude")
    @ApiOperation(value = "获取指定地理位置经纬度")
    public Result<LatitudeAndlongitudeVo> getLatitudeAndlongitude(@ApiParam(value = "详细地址") @RequestParam @NotNull(message = "地理位置为空") String site) {
        String addressEncoded = geoCodeUtil.getAddressEncoded(site, Boolean.FALSE);
        JSONObject jsonObject = JSON.parseObject(addressEncoded);
        JSONArray geocodes = jsonObject.getJSONArray("geocodes");
        if (geocodes.size() == 0) {
            throw new ServiceException("地址错误");
        }
        String location = geocodes.getJSONObject(0).getString("location");
        LatitudeAndlongitudeVo vo = new LatitudeAndlongitudeVo();
        vo.setLocation(location);
        vo.setAndlongitudeVo(this.analysisLatitudeAndlongitude(location).getData());
        return Result.ok(vo);
    }

    /**
     * 小程序地址管理在使用
     * 控制台用户注册时省市区待使用
     *
     * @param site 经纬度
     * @return
     */
    @GetMapping("/analysis/latitude-longitude")
    @EscapeLogin
    @ApiOperation(value = "根据经纬度返回省市区")
    public Result<AnalysisLatitudeAndlongitudeVo> analysisLatitudeAndlongitude(@ApiParam(value = "经纬度") @RequestParam @NotNull(message = "经纬度为空") String site) {
        String addressRegeo = geoCodeUtil.getAddressRegeo(site, Boolean.FALSE);
        JSONObject jsonObject = JSON.parseObject(addressRegeo);
        Integer status = jsonObject.getInteger("status");
        if (status != 1) {
            throw new ServiceException("数据异常");
        }
        JSONObject addressComponent = jsonObject.getJSONObject("regeocode").getJSONObject("addressComponent");
        AreaDto province = AreaUtil.getByLabel(0, addressComponent.getString("province"));
        AreaDto city = AreaUtil.getByLabel(1, addressComponent.getString("city"));
        AreaDto district = AreaUtil.getByValue(2, addressComponent.getString("adcode"));
        String adcode = addressComponent.getString("adcode");
        AnalysisLatitudeAndlongitudeVo vo = new AnalysisLatitudeAndlongitudeVo();
        vo.setProvince(province);
        vo.setCity(city);
        vo.setDistrict(district);
        vo.setAdcode(adcode);
        return Result.ok(vo);
    }


}
