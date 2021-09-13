package com.medusa.gruul.account.web.controller;

import com.medusa.gruul.account.model.vo.AggregateVo;
import com.medusa.gruul.account.service.IApiMiniAccountCollectService;
import com.medusa.gruul.account.service.IApiMiniAccountFootMarkService;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.CurUserUtil;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.common.dto.CurUserDto;
import com.medusa.gruul.order.api.feign.RemoteOrderService;
import com.medusa.gruul.platform.api.feign.RemoteMiniInfoService;
import com.medusa.gruul.platform.api.model.dto.ShopPackageFunctionDto;
import com.medusa.gruul.shops.api.feign.RemoteShopsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 聚合接口
 *
 * @author whh
 * @description
 * @data: 2020/5/13
 */
@RestController
@RequestMapping("/aggregate")
@Api(tags = "聚合接口")
@Log4j2
public class AggregateController {

    @Autowired
    private RemoteOrderService remoteOrderService;
    @Autowired
    private IApiMiniAccountCollectService miniAccountCollectService;
    @Autowired
    private IApiMiniAccountFootMarkService miniAccountFootMarkService;
    @Autowired
    private RemoteShopsService remoteShopsService;
    @Autowired
    private RemoteMiniInfoService remoteMiniInfoService;

    private static ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(6,
            new BasicThreadFactory.Builder().namingPattern("aggregate-schedule-pool-%d").daemon(true).build());

    @GetMapping(value = "/more")
    @ApiOperation(value = "首页聚合接口")
    public Result<AggregateVo> getCommanderInfo() {
        CurUserDto httpCurUser = CurUserUtil.getHttpCurUser();
        if (httpCurUser == null) {
            throw new ServiceException("非法访问");
        }
        AggregateVo vo = new AggregateVo();
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            throw new ServiceException("数据异常");
        }
        ServletRequestAttributes att = (ServletRequestAttributes) requestAttributes;
        //将RequestAttributes对象设置为子线程共享
        RequestContextHolder.setRequestAttributes(att, true);
        //获取用户中心
        vo.setAccountCenterVo(remoteShopsService.accountCenterSetting());
        //用户订单数据聚合
        vo.setOrderOverviewVo(remoteOrderService.orderOverview(httpCurUser.getUserId()));
        //获取用户 收藏数量
        vo.setCollectCount(miniAccountCollectService.getCollectCount());
        //获取用户足迹数量
        vo.setAccountFootMarkCount(miniAccountFootMarkService.getAccountFootMarkCount());
        //获取套餐开关
        Result<ShopPackageFunctionDto> shopFunction = remoteMiniInfoService.getShopFunction(TenantContextHolder.getTenantId());
        if (shopFunction != null) {
            vo.setPackageFunctionVo(shopFunction.getData());
        }
        return Result.ok(vo);

    }
}
