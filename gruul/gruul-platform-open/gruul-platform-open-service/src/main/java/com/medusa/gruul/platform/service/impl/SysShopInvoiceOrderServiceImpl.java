package com.medusa.gruul.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.CurUserUtil;
import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.platform.api.entity.*;
import com.medusa.gruul.platform.conf.PlatformRedis;
import com.medusa.gruul.platform.constant.RedisConstant;
import com.medusa.gruul.platform.mapper.SysShopInvoiceOrderMapper;
import com.medusa.gruul.platform.model.dto.InvoiceOrderAuditDto;
import com.medusa.gruul.platform.model.dto.ShopInvoiceOrderApplyDto;
import com.medusa.gruul.platform.model.vo.InvoiceOrderApplyVo;
import com.medusa.gruul.platform.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * <p>
 * 发票工单表 服务实现类
 * </p>
 *
 * @author whh
 * @since 2020-08-01
 */
@Service
public class SysShopInvoiceOrderServiceImpl extends ServiceImpl<SysShopInvoiceOrderMapper, SysShopInvoiceOrder> implements ISysShopInvoiceOrderService {

    @Autowired
    private IAccountInfoService accountInfoService;
    @Autowired
    private ISysShopPackageOrderService shopPackageOrderService;
    @Autowired
    private IPlatformAccountRechargeService platformAccountRechargeService;
    @Autowired
    private ISystemConfService systemConfService;
    @Autowired
    private ISysShopInvoiceRiseService sysShopInvoiceRiseService;

    @Override
    public void apply(ShopInvoiceOrderApplyDto orderApplyDto) {
        SysShopInvoiceRise invoiceRise = sysShopInvoiceRiseService.getById(orderApplyDto.getInvoiceRiseId());
        if (invoiceRise == null) {
            throw new ServiceException("无效的用户发票抬头");
        }
        if (!invoiceRise.getAccountId().equals(Long.valueOf(CurUserUtil.getPcRqeustAccountInfo().getUserId()))) {
            throw new ServiceException("发票抬头与当前用户不匹配");
        }
        SysShopInvoiceOrder order = new SysShopInvoiceOrder();
        order.setType(orderApplyDto.getType());
        order.setInvoiceRiseType(invoiceRise.getHeadType());
        order.setInvoiceRiseName(invoiceRise.getInvoiceRiseName());
        PlatformRedis redis = new PlatformRedis();
        redis.get(RedisConstant.MP_NOTIFY_AUTH_CODE);
        Integer maxId = this.getBaseMapper().selectMaxId();
        String number = "FP".concat(DateUtil.format(new Date(), "yyyyMMddHHmm"));
        if (maxId == null) {
            number = number.concat("000001");
        } else {
            number = number.concat(String.format("%0" + 6 + "d", maxId));
        }
        order.setNumberNo(number);
        order.setInvoiceTaxpayerNum(invoiceRise.getInvoiceTaxpayerNum());
        String orderNum = "";
        if (orderApplyDto.getOrderType().equals(CommonConstants.NUMBER_ONE)) {
            PlatformAccountRecharge platformAccountRecharge = platformAccountRechargeService.getById(orderApplyDto.getOrderId());
            if (platformAccountRecharge == null) {
                throw new ServiceException("无效订单");
            }
            orderNum = platformAccountRecharge.getPayNum();
            order.setAmount(platformAccountRecharge.getPayAmount().toString());
        } else if (orderApplyDto.getOrderType().equals(CommonConstants.NUMBER_TWO)) {
            SysShopPackageOrder sysShopPackageOrder = shopPackageOrderService.getById(orderApplyDto.getOrderId());
            if (sysShopPackageOrder == null) {
                throw new ServiceException("无效订单");
            }
            order.setAmount(sysShopPackageOrder.getPaidPayable().toString());
            SysShopPackageOrder up = new SysShopPackageOrder();
            up.setInvoiceStatus(CommonConstants.NUMBER_ONE);
            up.setId(sysShopPackageOrder.getId());
            shopPackageOrderService.updateById(up);
            orderNum = sysShopPackageOrder.getOrderNum();
        } else {
            throw new ServiceException("异常请求");
        }
        //判断是否存在已申请订单
        SysShopInvoiceOrder selectOne = this.getBaseMapper().selectOne(new QueryWrapper<SysShopInvoiceOrder>()
                .eq("order_id", orderApplyDto.getOrderId())
                .eq("order_type", orderApplyDto.getOrderType())
                .eq("status", CommonConstants.NUMBER_ZERO));
        if (ObjectUtil.isNotNull(selectOne)) {
            throw new ServiceException("该订单已进行开票审核");
        }

        AccountInfo accountInfo = accountInfoService.getById(CurUserUtil.getPcRqeustAccountInfo().getUserId());
        order.setEmail(accountInfo.getEmail());
        order.setStatus(CommonConstants.NUMBER_ZERO);
        order.setOrderId(orderApplyDto.getOrderId());
        order.setOrderType(orderApplyDto.getOrderType());
        order.setAccountId(accountInfo.getId());
        this.save(order);
        String finalOrderNum = orderNum;
        CompletableFuture.runAsync(() -> {
            //通知客服
            JSONObject json = new JSONObject();
            json.put("first", "订单申请开票");
            List<String> keywords = CollectionUtil.newLinkedList(finalOrderNum, LocalDateTimeUtil.format(LocalDateTime.now(), "yyyy-MM-dd HH:mm:ss"),
                    order.getInvoiceRiseName(), order.getAmount());
            json.put("keyword", keywords);
            json.put("remark", "请及时审核");
            systemConfService.sendKfmsg(CommonConstants.NUMBER_TWO, json);
        });

    }

    @Override
    public PageUtils<InvoiceOrderApplyVo> applyList(Integer page, Integer size, Integer type, String startTime, String endTime, Integer status) {
        //判断是否存在已申请订单
        IPage<SysShopInvoiceOrder> resultPage = this.getBaseMapper().selectPage(new Page<>(page, size), new QueryWrapper<SysShopInvoiceOrder>()
                .eq(ObjectUtil.isNotNull(type), "invoice_rise_type", type)
                .eq("status", status)
                .ge(StrUtil.isNotEmpty(startTime), "create_time", startTime)
                .le(StrUtil.isNotEmpty(endTime), "create_time", endTime).orderByDesc("create_time")
        );
        if (CollectionUtil.isEmpty(resultPage.getRecords())) {
            return new PageUtils(null, (int) resultPage.getTotal(),
                    (int) resultPage.getSize(), (int) resultPage.getCurrent());
        }
        List<InvoiceOrderApplyVo> vos = resultPage.getRecords().stream().map(obj -> BeanUtil.toBean(obj, InvoiceOrderApplyVo.class)).collect(Collectors.toList());
        return new PageUtils(vos, (int) resultPage.getTotal(),
                (int) resultPage.getSize(), (int) resultPage.getPages());
    }

    @Override
    public void audit(InvoiceOrderAuditDto orderAuditDto) {
        SysShopInvoiceOrder shopInvoiceOrder = getById(orderAuditDto.getInvoiceOrderId());
        if (shopInvoiceOrder == null) {
            throw new ServiceException("无效的工单");
        }
        shopInvoiceOrder.setStatus(CommonConstants.NUMBER_ONE);
        shopInvoiceOrder.setAuditTime(LocalDateTime.now());
        this.updateById(shopInvoiceOrder);
        if (shopInvoiceOrder.getOrderType().equals(CommonConstants.NUMBER_TWO)) {
            SysShopPackageOrder sysShopPackageOrder = shopPackageOrderService.getById(shopInvoiceOrder.getOrderId());
            SysShopPackageOrder up = new SysShopPackageOrder();
            up.setInvoiceStatus(CommonConstants.NUMBER_TWO);
            up.setId(sysShopPackageOrder.getId());
            shopPackageOrderService.updateById(up);
        }

    }

    @Override
    public List<InvoiceOrderApplyVo> getByOrderTypeAndOrderIds(Integer orderType, List<Long> orderIds) {
        List<SysShopInvoiceOrder> invoiceOrders = list(new QueryWrapper<SysShopInvoiceOrder>().eq("order_type", orderType)
                .in("order_id", orderIds));
        if (CollectionUtil.isEmpty(invoiceOrders)) {
            return new ArrayList<>();
        }
        return invoiceOrders.stream().map(obj -> BeanUtil.toBean(obj, InvoiceOrderApplyVo.class)).collect(Collectors.toList());
    }
}
