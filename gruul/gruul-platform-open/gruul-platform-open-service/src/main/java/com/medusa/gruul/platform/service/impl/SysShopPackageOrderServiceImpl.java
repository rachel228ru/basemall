package com.medusa.gruul.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.CurUserUtil;
import com.medusa.gruul.common.core.util.LocalDateTimeUtils;
import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.platform.api.entity.*;
import com.medusa.gruul.platform.conf.MeConstant;
import com.medusa.gruul.platform.constant.AgentNoticeEnum;
import com.medusa.gruul.platform.mapper.SysShopPackageOrderMapper;
import com.medusa.gruul.platform.model.dto.OrderOptionDto;
import com.medusa.gruul.platform.model.dto.PayDto;
import com.medusa.gruul.platform.model.dto.ShopPackageOrderDto;
import com.medusa.gruul.platform.model.dto.SysShopPackageOrderDto;
import com.medusa.gruul.platform.model.vo.*;
import com.medusa.gruul.platform.service.*;
import com.medusa.gruul.platform.stp.StpAgentUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.groovy.util.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 店铺套餐订单表 服务实现类
 * </p>
 *
 * @author whh
 * @since 2020-08-01
 */
@Service
@Slf4j
public class SysShopPackageOrderServiceImpl extends ServiceImpl<SysShopPackageOrderMapper, SysShopPackageOrder> implements ISysShopPackageOrderService {

    @Autowired
    private ISysShopPackageService sysShopPackageService;
    @Autowired
    private IPlatformShopInfoService platformShopInfoService;
    @Autowired
    private ISystemConfService systemConfService;
    @Autowired
    private ISysShopInvoiceOrderService sysShopInvoiceOrderService;
    @Autowired
    private IAccountInfoService accountInfoService;
    @Autowired
    private IPlatformShopTemplateInfoService templateInfoService;
    @Autowired
    private IPlatformAccountBalanceRecordService platformAccountBalanceRecordService;
    @Autowired
    private IMiniInfoService miniInfoService;
    @Autowired
    private IPlatformShopTemplateInfoService platformShopTemplateInfoService;


    /**
     * 支付回调地址后缀
     */
    private static final Map<Integer, String> PAY_NOTIFY = new HashMap<Integer, String>() {
        private static final long serialVersionUID = -8177029131574364968L;

        {
            put(2, "wx");
            put(3, "alipay");
        }
    };

    /**
     * 生成订单
     *
     * @param shopPackageOrderDto com.medusa.gruul.platform.model.dto.ShopPackageOrderDto
     * @return
     */
    private SysShopPackageOrder generateOrder(ShopPackageOrderDto shopPackageOrderDto) {
        SysShopPackageOrder order = null;
        //校验指定套餐是否存在
        SysShopPackage buyShopPackage = sysShopPackageService.getById(shopPackageOrderDto.getPackageId());
        if (buyShopPackage == null) {
            throw new ServiceException("套餐不存在");
        }
        //获取当前店铺数据
        PlatformShopInfo platformShopInfo = platformShopInfoService.getById(shopPackageOrderDto.getShopId());

        //1-订购 2-续费  3-升级
        switch (shopPackageOrderDto.getOptionType()) {
            case 1:
                order = order(buyShopPackage, platformShopInfo, shopPackageOrderDto);
                break;
            case 2:
                order = pacakgeRenew(buyShopPackage, platformShopInfo, shopPackageOrderDto);
                break;
            case 3:
                order = upgrade(buyShopPackage, platformShopInfo, shopPackageOrderDto);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + shopPackageOrderDto.getOptionType());
        }
        if (ObjectUtil.isNull(order)) {
            throw new ServiceException("订单生成失败");
        }
        return order;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void adminBuy(ShopPackageOrderDto shopPackageOrderDto) {
        //生成订单
        SysShopPackageOrder order = generateOrder(shopPackageOrderDto);
        //回调订单
        packagePayNotify(order.getOrderNum());
    }

    @Override
    public CalculateOrderPriceVo calculateOrderPrice(ShopPackageOrderDto shopPackageOrderDto) {
        if (shopPackageOrderDto.getPackageId() == null) {
            throw new ServiceException("套餐错误");
        }
        Boolean flag = (shopPackageOrderDto.getOptionType() < CommonConstants.NUMBER_ONE &&
                shopPackageOrderDto.getOptionType() > CommonConstants.NUMBER_THREE);
        if (shopPackageOrderDto.getOptionType() == null || flag) {
            throw new ServiceException("错误操作");
        }
        if (shopPackageOrderDto.getBuyPeriod() == null || shopPackageOrderDto.getBuyPeriod() < 0) {
            throw new ServiceException("请确认购买周期");
        }

        SysShopPackage buyShopPackage = sysShopPackageService.getById(shopPackageOrderDto.getPackageId());
        if (buyShopPackage == null) {
            throw new ServiceException("套餐不存在");
        }
        CalculateOrderPriceVo vo = new CalculateOrderPriceVo();
        switch (shopPackageOrderDto.getOptionType()) {
            case 1:
            case 2:
                vo.setPreferentialPrice(calculatePreferentialPrice(buyShopPackage, shopPackageOrderDto.getBuyPeriod()));
                vo.setActualPrice(calculateActualPrice(buyShopPackage, shopPackageOrderDto.getBuyPeriod()));
                break;
            case 3:
                Long shopId = shopPackageOrderDto.getShopId();
                if (shopId == null) {
                    throw new ServiceException("请确认店铺数据");
                }
                PlatformShopInfo shopInfo = platformShopInfoService.getById(shopId);
                if (shopInfo == null) {
                    throw new ServiceException("店铺不存在");
                }
                if (shopInfo.getPackageOrderId() == null) {
                    throw new ServiceException("店铺不存在使用套餐套餐");
                }
                //获取现有套餐是否到期
                SysShopPackageOrder oldPackageOrder = this.getById(shopInfo.getPackageOrderId());
                //价格计算为: 现有套餐剩余天数 * (新购买套餐总价 / 365)
                //计算当前套餐剩余天数
                Long betweenTwoTime = LocalDateTimeUtils.betweenTwoTime(LocalDateTime.now(), oldPackageOrder.getPackageEndTime(), ChronoUnit.DAYS);
                BigDecimal dayPrice = null;
                //计算新套餐每天的价格
                switch (buyShopPackage.getPackagePriceUnit()) {
                    case 1:
                        dayPrice = buyShopPackage.getPackagePrice();
                        break;
                    case 2:
                        dayPrice = NumberUtil.div(buyShopPackage.getPackagePrice(), 30);
                        break;
                    case 3:
                        dayPrice = NumberUtil.div(buyShopPackage.getPackagePrice(), 365);
                        break;
                    default:
                        throw new ServiceException("套餐单位错误");
                }
                BigDecimal price = NumberUtil.mul(dayPrice, betweenTwoTime);
                vo.setActualPrice(NumberUtil.round(price, 2));
                break;
            default:
                throw new ServiceException("系统异常");
        }
        if (vo.getPreferentialPrice() == null) {
            vo.setPreferentialPrice(NumberUtil.round(vo.getActualPrice(), 2));
        }
        //Todo 如果代理购买，则计算代理出代理价格
        return vo;
    }

    @Override
    public void adminCreateShopBuy(Integer orderSource, PlatformShopInfo info, SysShopPackage sysShopPackage, BigDecimal orderPirce, Integer givePackageTime) {
        SysShopPackageOrder packageEntify = createPackageEntify(info, sysShopPackage);
        //加入订单相关数据
        packageEntify.setPackageTime(givePackageTime);
        packageEntify.setPayType(CommonConstants.NUMBER_FIVE);
        packageEntify.setOrderSource(orderSource);
        packageEntify.setIsAgreed(CommonConstants.NUMBER_ONE);
        packageEntify.setIsAutomaticDeduction(CommonConstants.NUMBER_ZERO);
        packageEntify.setOrderType(CommonConstants.NUMBER_ONE);
        packageEntify.setAmountPayable(orderPirce);
        packageEntify.setPaidPayable(orderPirce);
        packageEntify.setPackageStartTime(info.getCreateTime());
        LocalDateTime packageEndTime = LocalDateTimeUtil.offset(packageEntify.getPackageStartTime(), givePackageTime, ChronoUnit.DAYS);
        packageEntify.setPackageEndTime(packageEndTime);
        this.save(packageEntify);
        packagePayNotify(packageEntify.getOrderNum());
    }

    @Override
    public String notifyAlipay(Map<String, String> toMap) {
        if (MapUtil.isEmpty(toMap)) {
            log.warn("无效数据");
            return "fail";
        }
        String outTradeNo = toMap.get("out_trade_no");
        if (packagePayNotify(outTradeNo)) {
            return "success";
        }
        return "fail";
    }

    @Override
    public Integer selectBoughtEnterpriseVersion(String tenantId) {
        return this.baseMapper.selectBoughtEnterpriseVersion(tenantId);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public BuyPackageOrderVo optionPackage(ShopPackageOrderDto shopPackageOrderDto) {
        //生成订单
        SysShopPackageOrder order = generateOrder(shopPackageOrderDto);

        //获取系统配置,请求域名
        SystemConfigVo systemConfigVo = systemConfService.getTypeInfo(CommonConstants.NUMBER_ZERO);
        if (ObjectUtil.isEmpty(systemConfigVo)) {
            throw new ServiceException("请求域名错误");
        }

        try {
            BuyPackageOrderVo vo = new BuyPackageOrderVo();
            vo.setOrderId(order.getId());
            String url = "/api/platform/sys-shop-package-order/notify/";
//            if (StpAgentUtil.isLogin()) {
//                url = "/api/platform/agent/console/package/notify/";
//            }
            //获取回调地址
            String notifyUrl = systemConfigVo.getSystemConfig().getMiniDomain()
                    .concat(url);
            //1:余额支付2:微信3:支付宝4:汇款支付
            switch (order.getPayType()) {
                case 1:
                    //余额支付支付成功直接返回
                    accountBlanceBuy(order);
                    break;
                case 2:
                    notifyUrl = notifyUrl.concat(PAY_NOTIFY.get(shopPackageOrderDto.getPayType()));
                    PayDto payDto = systemConfService.wxQrcodePay(notifyUrl,
                            order.getOrderNum(), order.getPaidPayable().toString(), "套餐购买", "package");
                    vo.setCodeUrl(payDto.getCodeUrl());
                    break;
                case 3:
                    notifyUrl = notifyUrl.concat(PAY_NOTIFY.get(shopPackageOrderDto.getPayType()));
                    PayDto payDto1 = systemConfService.aliPayQrcodePay(notifyUrl, "套餐购买", order.getPaidPayable().toString(), order.getOrderNum());
                    vo.setCodeUrl(payDto1.getCodeUrl());
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + order.getPayType());
            }

            return vo;
        } catch (WxPayException e) {
            e.printStackTrace();
            throw new ServiceException("生成支付失败");
        }


    }


    /**
     * 使用余额支付套餐(用户或者代理)
     *
     * @param order com.medusa.gruul.platform.api.entity.SysShopPackageOrder
     */
    private void accountBlanceBuy(SysShopPackageOrder order) {

        AccountInfo accountInfo = accountInfoService.getById(order.getAccountId());
        if (accountInfo == null) {
            throw new ServiceException("无效的账号");
        }
        BigDecimal newBalance = NumberUtil.sub(accountInfo.getBalance(), order.getPaidPayable());
        BigDecimal zero = new BigDecimal("0.0");
        if (newBalance.compareTo(zero) < 0) {
            throw new ServiceException("账户余额不足");
        }
        AccountInfo up = new AccountInfo();
        up.setId(accountInfo.getId());
        up.setBalance(newBalance);
        accountInfoService.updateById(up);
        Integer consumptionType = CommonConstants.NUMBER_ONE;
        if (order.getOrderType().equals(CommonConstants.NUMBER_THREE) ||
                order.getOrderType().equals(CommonConstants.NUMBER_TWO)) {
            consumptionType = CommonConstants.NUMBER_TWO;
        }
        //增加用户余额明细
        platformAccountBalanceRecordService.newBlanceDeail(
                accountInfo, consumptionType, order.getOrderNum(), order.getPaidPayable());
        //订单支付回调
        packagePayNotify(order.getOrderNum());
    }


    @Override
    public String notifyWx(String xmlResult) {
        WxPayOrderNotifyResult payOrderNotifyResult = WxPayOrderNotifyResult.fromXML(xmlResult);
        String outTradeNo = payOrderNotifyResult.getOutTradeNo();
        if (packagePayNotify(outTradeNo)) {
            return WxPayNotifyResponse.success("处理成功");
        } else {
            return WxPayNotifyResponse.fail("处理失败");
        }
    }

    /**
     * @param outTradeNo 订单id
     * @return true 成功  false 失败
     * @throws ServiceException
     */
    public boolean packagePayNotify(String outTradeNo) throws ServiceException {
        try {
            SysShopPackageOrder order = this.getByOrderNum(outTradeNo);
            if (order == null) {
                log.warn("orderNum :{} 不存在在", outTradeNo);
                return Boolean.FALSE;
            }
            //已处理过订单
            if (order.getStatus().equals(CommonConstants.NUMBER_TWO)) {
                return Boolean.TRUE;
            }
            PlatformShopInfo shopInfo = platformShopInfoService.getByTenantId(order.getTenantId());
            if (shopInfo == null) {
                log.error("店铺数据不存在 TenantId: {}", order.getTenantId());
                return Boolean.FALSE;
            }
            //处理订单状态
            SysShopPackageOrder up = new SysShopPackageOrder();
            up.setId(order.getId());
            up.setStatus(CommonConstants.NUMBER_TWO);
            //跟随修改,供后面流程使用
            order.setStatus(CommonConstants.NUMBER_TWO);
            //如果是汇款订单
            if (order.getPayType().equals(CommonConstants.NUMBER_FOUR)) {
                up.setAuditorStatus(CommonConstants.NUMBER_ONE);
                up.setIsReceived(CommonConstants.NUMBER_ONE);
            }
            if (!this.updateById(up)) {
                log.error("套餐订单回调更新失败");
                return Boolean.FALSE;
            }
            PlatformShopInfo upShopInfo = new PlatformShopInfo();
            upShopInfo.setId(shopInfo.getId());
            upShopInfo.setPackageId(order.getPackageId());
            upShopInfo.setPackageOrderId(order.getId());
            upShopInfo.setDueTime(order.getPackageEndTime());
            upShopInfo.setIsDue(CommonConstants.NUMBER_ZERO);
            platformShopInfoService.updateById(upShopInfo);
            AccountInfo accountInfo = accountInfoService.getById(order.getAccountId());
            //Todo 用户自行购买,则需判断是否存在归属代理,为其增加代理分账和上级代理分账
            return Boolean.TRUE;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("支付回调异常====> 订单号 :{}", outTradeNo);
            throw new ServiceException("订单回流处理失败");
        }
    }

    /**
     * 代理通知
     *
     * @param order      店铺套餐订单
     * @param shopInfo   店铺信息
     * @param upShopInfo 最新的店铺信息
     */
    private void agentNotice(SysShopPackageOrder order, PlatformShopInfo shopInfo, PlatformShopInfo upShopInfo, Long agentId) {
        //代理id为空
        if (agentId == null || agentId == 0) {
            return;
        }
        //先通知用户所属代理,在通知用户代理的上级代理
        LinkedList<String> titles = new LinkedList<>();
        MiniInfo miniInfo = miniInfoService.getById(shopInfo.getTenantId());
        String miniName = "-";
        if (miniInfo != null) {
            miniName = miniInfo.getMiniName();
        }
        titles.add(miniName);
        titles.add(shopInfo.getShopName());
        PlatformShopTemplateInfo shopTemplateInfo = platformShopTemplateInfoService.getById(shopInfo.getShopTemplateId());
        String key3 = "";
        AgentNoticeEnum noticeEnum = null;
        //1-订购  2-续费  3-升级
        switch (order.getOrderType()) {
            case 1:
                noticeEnum = AgentNoticeEnum.MSG_003;
                key3 = order.getPackageName();
                break;
            case 2:
                switch (order.getPackagePriceUnit()) {
                    case 1:
                        key3 = key3.concat(order.getPackageTime().toString()).concat("天");
                        break;
                    case 2:
                        key3 = key3.concat(((Double) NumberUtil.div(order.getPackageTime().doubleValue(), 30)).toString()).concat("月");
                        break;
                    case 3:
                        key3 = key3.concat(((Double) NumberUtil.div(order.getPackageTime().doubleValue(), 365)).toString()).concat("年");
                        break;
                    default:
                        break;
                }
                noticeEnum = AgentNoticeEnum.MSG_001;
                break;
            case 3:
                key3 = order.getPackageName();
                noticeEnum = AgentNoticeEnum.MSG_002;
                break;
            default:
                break;
        }
        String key4 = "";
        Long day = DateUtil.betweenDay(new Date(), LocalDateTimeUtils.convertLDTToDate(upShopInfo.getDueTime()), false);

        double surplus = NumberUtil.div(day.doubleValue(), 365, 0, RoundingMode.DOWN);
        if (surplus > 0) {
            key4 = surplus + "年";
            double daySurplus = (day - (surplus * 365));
            if (day > 0) {
                key4 = daySurplus + "天";
            }
        } else {
            key4 = day + "天";
        }
        //套餐续费,订购,升级通知
        Map<String, String> content = Maps.of("key1", miniName, "key2", shopTemplateInfo.getName(), "key3", key3, "key4", key4);
        //Todo 上级代理通知
    }




    @Override
    public SysShopPackageOrder getByOrderNum(String orderNum) {
        return this.getOne(new QueryWrapper<SysShopPackageOrder>().eq("order_num", orderNum));
    }

    @Override
    public List<BigDecimal> selectByAgentOrder(Long agentId) {
        return this.baseMapper.selectByAgentOrder(agentId);
    }


    /**
     * 订购升级
     *
     * @param buyShopPackage 新套餐
     * @param shopInfo       店铺信息
     * @param orderDto       下单数据
     * @return com.medusa.gruul.platform.api.entity.SysShopPackageOrder
     */
    private SysShopPackageOrder upgrade(SysShopPackage buyShopPackage, PlatformShopInfo shopInfo, ShopPackageOrderDto orderDto) {
        Long packageOrderId = shopInfo.getPackageOrderId();
        if (packageOrderId == null) {
            throw new ServiceException("店铺不存在有效使用套餐,无法升级");
        }
        //获取现有套餐是否到期
        SysShopPackageOrder oldPackageOrder = this.getById(shopInfo.getPackageOrderId());
        if (System.currentTimeMillis() >=
                LocalDateTimeUtils.getMilliByTime(oldPackageOrder.getPackageEndTime())) {
            throw new ServiceException("店铺当前套餐已过期,无法进行升级");
        }

        //套餐升级,只能往高版本套餐升级
        SysShopPackage oldPackage = JSONObject.parseObject(oldPackageOrder.getPackageData(), SysShopPackage.class);
        if (oldPackage == null) {
            throw new ServiceException("套餐数据错误");
        }
        if (oldPackage.getLevel() > buyShopPackage.getLevel()) {
            throw new ServiceException("不能购买低于当前使用的套餐版本");
        }
        //价格计算为: 现有套餐剩余天数 * (新购买套餐总价 / 365)
        //计算当前套餐剩余天数
        Long betweenTwoTime = LocalDateTimeUtils.betweenTwoTime(LocalDateTime.now(), oldPackageOrder.getPackageEndTime(), ChronoUnit.DAYS);
        BigDecimal dayPrice = null;
        //计算新套餐每天的价格
        switch (buyShopPackage.getPackagePriceUnit()) {
            case 1:
                dayPrice = buyShopPackage.getPackagePrice();
                break;
            case 2:
                dayPrice = NumberUtil.div(buyShopPackage.getPackagePrice(), 30);
                break;
            case 3:
                dayPrice = NumberUtil.div(buyShopPackage.getPackagePrice(), 365);
                break;
            default:
                throw new ServiceException("套餐单位错误");
        }
        BigDecimal price = NumberUtil.mul(dayPrice, betweenTwoTime);
        //Todo 如果代理购买，则计算代理出代理价格
        SysShopPackageOrder sysShopPackageOrder = createOrderEntify(shopInfo, buyShopPackage, orderDto);
        sysShopPackageOrder.setPackageTime(betweenTwoTime.intValue());
        sysShopPackageOrder.setOrderType(CommonConstants.NUMBER_THREE);
        sysShopPackageOrder.setAmountPayable(price);
        sysShopPackageOrder.setPaidPayable(price);
        //使用原有套餐时间
        sysShopPackageOrder.setPackageStartTime(oldPackageOrder.getPackageStartTime());
        sysShopPackageOrder.setPackageEndTime(oldPackageOrder.getPackageEndTime());
        this.save(sysShopPackageOrder);
        return sysShopPackageOrder;
    }

    /**
     * 生成套餐相关数据订单对象
     *
     * @param buyShopPackage 新套餐
     * @param shopInfo       店铺信息
     * @return com.medusa.gruul.platform.api.entity.SysShopPackageOrder
     */
    private SysShopPackageOrder createPackageEntify(PlatformShopInfo shopInfo, SysShopPackage buyShopPackage) {
        SysShopPackageOrder sysShopPackageOrder = new SysShopPackageOrder();
        sysShopPackageOrder.setAccountId(shopInfo.getAccountId());
        sysShopPackageOrder.setShopTemplateInfoId(shopInfo.getShopTemplateId());
        sysShopPackageOrder.setOrderNum(IdUtil.createSnowflake(1, 2).nextIdStr());
        sysShopPackageOrder.setPackageId(buyShopPackage.getId());
        sysShopPackageOrder.setPackageData(JSONObject.toJSONString(buyShopPackage));
        sysShopPackageOrder.setPackagePriceUnit(buyShopPackage.getPackagePriceUnit());
        sysShopPackageOrder.setPackagePrice(buyShopPackage.getPackagePrice());
        sysShopPackageOrder.setStatus(CommonConstants.NUMBER_ZERO);
        sysShopPackageOrder.setShopName(shopInfo.getShopName());
        sysShopPackageOrder.setPackageName(buyShopPackage.getName());
        sysShopPackageOrder.setInvoiceStatus(CommonConstants.NUMBER_ZERO);
        PlatformShopTemplateInfo shopTemplateInfo = templateInfoService.getById(buyShopPackage.getTemplateId());
        sysShopPackageOrder.setTemplateName(shopTemplateInfo.getName());
        sysShopPackageOrder.setTenantId(shopInfo.getTenantId());
        return sysShopPackageOrder;
    }

    /**
     * 生成下单对象
     *
     * @param buyShopPackage 新套餐
     * @param shopInfo       店铺信息
     * @param orderDto       下单数据
     * @return com.medusa.gruul.platform.api.entity.SysShopPackageOrder
     */
    private SysShopPackageOrder createOrderEntify(PlatformShopInfo shopInfo, SysShopPackage buyShopPackage, ShopPackageOrderDto orderDto) {
        //生成套餐相关订单对象
        SysShopPackageOrder sysShopPackageOrder = createPackageEntify(shopInfo, buyShopPackage);
        //加入订单相关数据
        sysShopPackageOrder.setPackageTime(orderDto.getBuyPeriod());
        sysShopPackageOrder.setPayType(orderDto.getPayType());
        sysShopPackageOrder.setOrderSource(orderDto.getOrderSource());
        sysShopPackageOrder.setIsAgreed(orderDto.getAgreeProtocol());
        sysShopPackageOrder.setIsAutomaticDeduction(orderDto.getAutoDeduct());
        //汇款支付需填写的信息
        if (orderDto.getPayType().equals(CommonConstants.NUMBER_FOUR)) {
            sysShopPackageOrder.setIsReceived(CommonConstants.NUMBER_ZERO);
            sysShopPackageOrder.setPayInfo(orderDto.getPayInfo());
            sysShopPackageOrder.setAuditorStatus(CommonConstants.NUMBER_ZERO);
            sysShopPackageOrder.setStatus(CommonConstants.NUMBER_ONE);
        }
        //判断是否代理订单
        if (StpAgentUtil.isLogin()) {
            sysShopPackageOrder.setAgentId(StpAgentUtil.getLoginIdAsLong());
        }
        return sysShopPackageOrder;
    }

    /**
     * 订购套餐
     *
     * @param buyShopPackage 新套餐
     * @param shopInfo       店铺信息
     * @param orderDto       下单数据
     */
    private SysShopPackageOrder order(SysShopPackage buyShopPackage, PlatformShopInfo shopInfo, ShopPackageOrderDto orderDto) {
        //当前店铺不存在套餐,直接计算套餐价格进行结算
        if (shopInfo.getPackageOrderId() != null && shopInfo.getIsDue().equals(CommonConstants.NUMBER_ZERO)) {
            //店铺当前存在套餐订单,判断是否过期,未过期无法进行套餐订购
            SysShopPackageOrder shopPackageOrder = this.getById(shopInfo.getPackageOrderId());
            if (shopPackageOrder != null && System.currentTimeMillis() >=
                    LocalDateTimeUtils.getMilliByTime(shopPackageOrder.getPackageEndTime())) {
                throw new ServiceException("店铺当前使用的套餐未到期,无法进行订购套餐");
            }
        }
        BigDecimal price = new BigDecimal("0.0");
        //如果不是平台赠送则计算价格
        if (!orderDto.getOrderSource().equals(CommonConstants.NUMBER_TWO)) {
            //计算需支付价格
            price = calculatePrice(buyShopPackage, orderDto.getBuyPeriod());
        }
        //Todo 如果代理购买，则计算代理出代理价格
        SysShopPackageOrder sysShopPackageOrder = createOrderEntify(shopInfo, buyShopPackage, orderDto);
        sysShopPackageOrder.setOrderType(CommonConstants.NUMBER_ONE);
        sysShopPackageOrder.setAmountPayable(price);
        sysShopPackageOrder.setPaidPayable(price);
        sysShopPackageOrder.setPackageStartTime(LocalDateTime.now());
        LocalDateTime packageEndTime = LocalDateTimeUtil.offset(sysShopPackageOrder.getPackageStartTime(), orderDto.getBuyPeriod(), ChronoUnit.DAYS);
        sysShopPackageOrder.setPackageEndTime(packageEndTime);
        sysShopPackageOrder.setTenantId(shopInfo.getTenantId());

        this.save(sysShopPackageOrder);
        return sysShopPackageOrder;
    }

    /**
     * 套餐续费
     *
     * @param buyShopPackage 新套餐
     * @param shopInfo       店铺信息
     * @param orderDto       下单数据
     * @return com.medusa.gruul.platform.api.entity.SysShopPackageOrder
     */
    private SysShopPackageOrder pacakgeRenew(SysShopPackage buyShopPackage, PlatformShopInfo shopInfo, ShopPackageOrderDto orderDto) {
        Long shopInfoPackageId = shopInfo.getPackageId();
        if (ObjectUtil.isNull(shopInfoPackageId)) {
            throw new ServiceException("当前店铺不存在套餐");
        }
        SysShopPackageOrder shopPackageOrder = this.getById(shopInfo.getPackageOrderId());
        if (shopPackageOrder == null) {
            throw new ServiceException("订单不存在");
        }
        SysShopPackage sysShopPackage = JSONObject.parseObject(shopPackageOrder.getPackageData(), SysShopPackage.class);
        //校验续费套餐与当前套餐是否一致
        if (!buyShopPackage.getLevel().equals(sysShopPackage.getLevel())) {
            throw new ServiceException("续费套餐跟当前使用的套餐不一致无法进行续费");
        }

        //判断套餐是否已到期
        if (LocalDateTimeUtils.getMilliByTime(shopPackageOrder.getPackageEndTime()) <= System.currentTimeMillis()) {
            throw new ServiceException("套餐已到期无法续费");
        }

        //套餐一致进入购买正常流程计算
        //计算需支付价格
        BigDecimal price = calculatePrice(buyShopPackage, orderDto.getBuyPeriod());
        //Todo 如果代理购买，则计算代理出代理价格
        SysShopPackageOrder order = createOrderEntify(shopInfo, buyShopPackage, orderDto);
        order.setOrderType(CommonConstants.NUMBER_TWO);
        order.setAmountPayable(price);
        order.setPaidPayable(price);
        //开始时间为续费套餐的开始时间
        order.setPackageStartTime(shopPackageOrder.getPackageStartTime());
        //结束时间为续费套餐的结束时间开始计算
        LocalDateTime packageEndTime = LocalDateTimeUtil.offset(shopInfo.getDueTime(), orderDto.getBuyPeriod(), ChronoUnit.DAYS);
        order.setPackageEndTime(packageEndTime);
        order.setTenantId(shopInfo.getTenantId());
        this.save(order);
        return order;
    }

    /**
     * 计算套餐实际价格
     *
     * @param buyShopPackage 套餐
     * @param buyPeriod      购买天数
     * @return 实际价格
     */
    private BigDecimal calculateActualPrice(SysShopPackage buyShopPackage, Integer buyPeriod) {
        //未达到优惠要求计算套餐价格,套餐使用价格单位 1天，2月，3年
        //判断计算单位是否成立
        switch (buyShopPackage.getPackagePriceUnit()) {
            case 1:
                if (buyPeriod < 1) {
                    throw new ServiceException("当前套餐计算单位为天:请选择购买周期");
                }
                break;
            case 2:
                //计算单位为月,购买周期为365的倍数
                if (buyPeriod % MeConstant.STATUS_365 == 0) {
                    int subPeriod = 5 * (buyPeriod / 365);
                    buyPeriod = buyPeriod - subPeriod;
                } else if (buyPeriod % MeConstant.STATUS_30 != 0) {
                    throw new ServiceException("当前套餐计算单位为月:请选择购买周期");
                }
                //按月计算 30天
                buyPeriod = buyPeriod / 30;
                break;
            case 3:
                if (buyPeriod % MeConstant.STATUS_365 != 0) {
                    throw new ServiceException("当前套餐计算单位为年:请选择购买周期");
                }
                buyPeriod = buyPeriod / 365;
                break;
            default:
                throw new ServiceException("套餐单位错误");
        }

        return NumberUtil.round(NumberUtil.mul(buyShopPackage.getPackagePrice(), buyPeriod), 2);
    }

    /**
     * 计算套餐优惠价格
     *
     * @param buyShopPackage 套餐
     * @param buyPeriod      购买天数
     * @return 优惠价格  返回null则不存在优惠价格
     */
    private BigDecimal calculatePreferentialPrice(SysShopPackage buyShopPackage, Integer buyPeriod) {
        //是否达到优惠价格
        String discountsJson = buyShopPackage.getDiscountsJson();
        if (StrUtil.isNotEmpty(discountsJson)) {
            JSONArray array = JSONObject.parseArray(discountsJson);
            for (Object o : array) {
                JSONObject jsonObject = (JSONObject) o;
                Integer value = jsonObject.getInteger("value");
                //判断购买天数是否达到优惠价格,是直接返回优惠价格计算
                if (buyPeriod.equals(value)) {
                    return jsonObject.getBigDecimal("price");
                }
            }
        }
        return null;
    }

    /**
     * 计算套餐付费价格
     *
     * @param buyShopPackage 套餐
     * @param buyPeriod      购买天数
     * @return 需支付价格
     */
    private BigDecimal calculatePrice(SysShopPackage buyShopPackage, Integer buyPeriod) {
        BigDecimal price = calculatePreferentialPrice(buyShopPackage, buyPeriod);
        if (price != null) {
            return price;
        }
        return calculateActualPrice(buyShopPackage, buyPeriod);
    }


    @Override
    public PageUtils<PackageOrderVo> orders(Integer page, Integer size, Integer status, String payStartTime, String payEndTime, String phone,
                                            String orderNum, String nikeName, Integer payType, Long templateId, Long userId) {
        IPage<SysShopPackageOrderDto> resultIpage = this.baseMapper.selectOrders(new Page<>(page, size),
                status, payStartTime, payEndTime, phone, orderNum, nikeName, payType, templateId, userId);

        if (CollectionUtil.isEmpty(resultIpage.getRecords())) {
            return new PageUtils(null, (int) resultIpage.getTotal(),
                    (int) resultIpage.getSize(), (int) resultIpage.getCurrent());
        }
        List<PackageOrderVo> orderVos = resultIpage.getRecords().stream().map(obj -> BeanUtil.toBean(obj, PackageOrderVo.class)).collect(Collectors.toList());
        return new PageUtils<>(orderVos, (int) resultIpage.getTotal(),
                (int) resultIpage.getSize(), (int) resultIpage.getCurrent());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderOption(OrderOptionDto dto) {
        SysShopPackageOrder packageOrder = getById(dto.getOrderId());
        if (packageOrder == null) {
            throw new ServiceException("订单不存在");
        }
        if (!CommonConstants.NUMBER_FOUR.equals(packageOrder.getPayType())) {
            throw new ServiceException("非汇款订单无法进行操作");
        }
        if (!packageOrder.getStatus().equals(CommonConstants.NUMBER_ONE)) {
            throw new ServiceException("该订单不处于可修改状态");
        }
        //收到汇款
        if (dto.getOptionType().equals(CommonConstants.NUMBER_ONE)) {
            //处理成功的订单
            packagePayNotify(packageOrder.getOrderNum());
            return;
        }
        //汇款关闭
        if (dto.getOptionType().equals(CommonConstants.NUMBER_TWO)) {
            SysShopPackageOrder up = new SysShopPackageOrder();
            up.setId(packageOrder.getId());
            up.setStatus(CommonConstants.NUMBER_THREE);
            up.setAuditorStatus(CommonConstants.NUMBER_THREE);
            this.updateById(up);
            if (packageOrder.getOrderSource().equals(CommonConstants.NUMBER_FOUR)
                    && packageOrder.getAgentId() != null && packageOrder.getAgentId() > 0) {
            }
            return;
        }

    }

    @Override
    public PageUtils<PackageOrderConsoleVo> consoleOrders(Integer page, Integer size, Integer status, String payStartTime, String payEndTime, Long packageId, Integer platfromType, Integer selectType) {
        String tenantId = "";
        //店铺商家中心查询，需过滤掉所有非当前所在店铺的订单
        if (selectType.equals(CommonConstants.NUMBER_TWO)) {
            tenantId = TenantContextHolder.getTenantId();
        }
        List<Long> packageIds = null;
        if (packageId != null) {
            SysShopPackage sysShopPackage = sysShopPackageService.getById(packageId);
            List<SysShopPackage> sysShopPackages = sysShopPackageService.getByTeamplteId(sysShopPackage.getTemplateId());
            packageIds = sysShopPackages.stream().filter(obj -> obj.getLevel().equals(sysShopPackage.getLevel())).map(SysShopPackage::getId).collect(Collectors.toList());
        }
        List<Integer> orderSources = null;
        if (platfromType != null && platfromType > 0) {
            orderSources = new ArrayList<>(4);
            if (platfromType.equals(CommonConstants.NUMBER_ONE)) {
                orderSources.add(CommonConstants.NUMBER_ZERO);
            }
            if (platfromType.equals(CommonConstants.NUMBER_TWO)) {
                orderSources.add(CommonConstants.NUMBER_ONE);
                orderSources.add(CommonConstants.NUMBER_TWO);
                orderSources.add(CommonConstants.NUMBER_THREE);
                orderSources.add(CommonConstants.NUMBER_FOUR);
            }
        }
        IPage<SysShopPackageOrder> resultIpage = this.getBaseMapper().selectPage(new Page<>(page, size), new QueryWrapper<SysShopPackageOrder>()
                .eq("account_id", CurUserUtil.getPcRqeustAccountInfo().getUserId())
                .ge(CommonConstants.NUMBER_ZERO.equals(status), "status", CommonConstants.NUMBER_ONE)
                .eq(status > CommonConstants.NUMBER_ZERO, "status", status)
                .in(CollectionUtil.isNotEmpty(packageIds), "package_id", packageIds)
                .in(CollectionUtil.isNotEmpty(orderSources), "order_source", orderSources)
                .ge(StrUtil.isNotEmpty(payStartTime), "create_time", payStartTime)
                .le(StrUtil.isNotEmpty(payEndTime), "create_time", payEndTime)
                .eq(StrUtil.isNotEmpty(tenantId), "tenant_id", tenantId)
                .orderByDesc("create_time")
        );
        if (CollectionUtil.isEmpty(resultIpage.getRecords())) {
            return new PageUtils(null, (int) resultIpage.getTotal(),
                    (int) resultIpage.getSize(), (int) resultIpage.getCurrent());
        }
        List<Long> orderIds = resultIpage.getRecords().stream().filter(obj -> CommonConstants.NUMBER_ONE.equals(obj.getInvoiceStatus()))
                .map(SysShopPackageOrder::getId).collect(Collectors.toList());
        Map<Long, InvoiceOrderApplyVo> invoiceOrderApplyVosMap = new HashMap<>(0);
        if (CollectionUtil.isNotEmpty(orderIds)) {
            List<InvoiceOrderApplyVo> invoiceOrderApplyVos = sysShopInvoiceOrderService.getByOrderTypeAndOrderIds(CommonConstants.NUMBER_TWO, orderIds);
            if (CollectionUtil.isNotEmpty(invoiceOrderApplyVos)) {
                invoiceOrderApplyVosMap = invoiceOrderApplyVos.stream().collect(Collectors.toMap(InvoiceOrderApplyVo::getOrderId, v -> v));
            }
        }
        Map<Long, InvoiceOrderApplyVo> finalOrderApplyVoMap = invoiceOrderApplyVosMap;
        List<PackageOrderConsoleVo> orderVos = resultIpage.getRecords().stream().map(obj -> {
            PackageOrderConsoleVo packageOrderConsoleVo = BeanUtil.toBean(obj, PackageOrderConsoleVo.class);
            packageOrderConsoleVo.setInvoiceOrderApplyVo(finalOrderApplyVoMap.get(obj.getId()));
            return packageOrderConsoleVo;
        }).collect(Collectors.toList());

        return new PageUtils(orderVos, (int) resultIpage.getTotal(),
                (int) resultIpage.getSize(), (int) resultIpage.getCurrent());
    }

    @Override
    public Boolean orderPayIfOk(Long orderId) {
        SysShopPackageOrder packageOrder = this.getById(orderId);
        if (packageOrder == null) {
            throw new ServiceException("不存在该订单");
        }
        if (packageOrder.getStatus().equals(CommonConstants.NUMBER_TWO)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }


}
