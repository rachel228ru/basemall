package com.medusa.gruul.afs.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.afs.api.entity.AfsNegotiateHistory;
import com.medusa.gruul.afs.api.entity.AfsOrder;
import com.medusa.gruul.afs.api.entity.AfsOrderItem;
import com.medusa.gruul.afs.mapper.AfsNegotiateHistoryMapper;
import com.medusa.gruul.afs.mapper.AfsOrderItemMapper;
import com.medusa.gruul.afs.mapper.AfsOrderMapper;
import com.medusa.gruul.afs.model.BaseApplyDto;
import com.medusa.gruul.afs.model.NegotiateHistoryDto;
import com.medusa.gruul.afs.service.IAfsNegotiateHistoryService;
import com.medusa.gruul.common.core.util.CurUserUtil;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.common.core.util.SystemCode;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.common.dto.CurUserDto;
import com.medusa.gruul.logistics.api.feign.RemoteLogisticsFeginService;
import com.medusa.gruul.logistics.model.vo.LogisticsAddressVo;
import com.medusa.gruul.order.api.feign.RemoteOrderService;
import com.medusa.gruul.order.api.model.OrderVo;
import com.medusa.gruul.platform.api.feign.RemoteMiniInfoService;
import com.medusa.gruul.platform.api.model.dto.ShopInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 协商历史 服务实现类
 * </p>
 *
 * @author alan
 * @since 2020 -08-05
 */
@Slf4j
@Service
public class AfsNegotiateHistoryServiceImpl extends ServiceImpl<AfsNegotiateHistoryMapper, AfsNegotiateHistory> implements IAfsNegotiateHistoryService {
    @Resource
    private AfsOrderItemMapper afsOrderItemMapper;
    @Resource
    private AfsOrderMapper afsOrderMapper;
    @Resource
    private RemoteMiniInfoService remoteMiniInfoService;
    @Resource
    private RemoteOrderService remoteOrderService;
    @Resource
    private RemoteLogisticsFeginService remoteLogisticsFeginService;
    private final static String MERGE_POINT = "0";

    /**
     * 初始化协商历史
     *
     * @param afsOrderItemList
     * @param dto
     * @param name
     * @return void
     * @author alan
     * @date 2021/3/17 22:21
     */
    @Override
    public void init(List<AfsOrderItem> afsOrderItemList, BaseApplyDto dto, String name) {
        CurUserDto curUserDto = CurUserUtil.getHttpCurUser();
        for (AfsOrderItem afsOrderItem : afsOrderItemList) {
            AfsNegotiateHistory negotiateHistory = AfsNegotiateHistory.userCreate(curUserDto);
            setOrderId(afsOrderItem.getOrderId(), negotiateHistory);
            negotiateHistory.setApplyUserType(dto.getType().isLeaderApply());
            StringBuilder content = new StringBuilder(String.format("%s发起了%s申请", name,
                    dto.getType().getDesc()));

            if (dto.getType().isRefund()) {
                content.append(String.format(",金额：￥%s ", dto.getRefundAmount()));
            } else {
                content.append(String.format(",数量：%s ",
                        afsOrderItem.getProductQuantity()));
            }
            if (StringUtils.isNotBlank(dto.getDescription())) {
                content.append(String.format(",说明：%s ", dto.getDescription()));
            }
            if (StringUtils.isNotBlank(dto.getImages())) {
                content.append(",凭证:");
                negotiateHistory.setImage(dto.getImages());
            }
            negotiateHistory.setInfo(content.toString());
            this.save(negotiateHistory);
        }

    }



    /**
     * 用户主动撤销
     *
     * @param afsOrder
     * @param isSystem
     * @return void
     * @author alan
     * @date 2021/3/17 22:22
     */
    @Override
    public void userCancel(AfsOrder afsOrder, boolean isSystem) {
        List<AfsOrderItem> afsOrderItemList = afsOrderItemMapper.selectList(new LambdaQueryWrapper<AfsOrderItem>()
                .select(AfsOrderItem::getOrderId)
                .eq(AfsOrderItem::getAfsId, afsOrder.getId()));
        for (AfsOrderItem afsOrderItem : afsOrderItemList) {
            AfsNegotiateHistory negotiateHistory;
            StringBuilder content = new StringBuilder();
            if (isSystem) {
                negotiateHistory = initCreator(true);
                content.append(String.format("由于买家超时未退货，%s失败，本次售后关闭", afsOrder.getType().getDesc()));
            } else {
                CurUserDto curUserDto = CurUserUtil.getHttpCurUser();
                negotiateHistory = AfsNegotiateHistory.userCreate(curUserDto);
                content.append(String.format("%s主动撤销了本次%s申请，本次售后关闭", "买家",
                        afsOrder.getType().getDesc()));
            }
            negotiateHistory.setApplyUserType(afsOrder.getType().isLeaderApply());
            negotiateHistory.setInfo(content.toString());
            setOrderId(afsOrderItem.getOrderId(), negotiateHistory);
            this.save(negotiateHistory);
        }
    }

    /**
     * 买家已退货
     *
     * @param afsOrder
     * @param isLogistics
     * @return void
     * @author alan
     * @date 2021/3/17 22:22
     */
    @Override
    public void userReturn(AfsOrder afsOrder, boolean isLogistics) {
        CurUserDto curUserDto = CurUserUtil.getHttpCurUser();
        List<AfsOrderItem> afsOrderItemList = afsOrderItemMapper.selectList(new LambdaQueryWrapper<AfsOrderItem>()
                .select(AfsOrderItem::getOrderId)
                .eq(AfsOrderItem::getAfsId, afsOrder.getId()));

        for (AfsOrderItem afsOrderItem : afsOrderItemList) {
            AfsNegotiateHistory negotiateHistory = AfsNegotiateHistory.userCreate(curUserDto);
            negotiateHistory.setApplyUserType(afsOrder.getType().isLeaderApply());
            if (isLogistics) {
                negotiateHistory.setInfo(String.format("买家已退货，物流公司：%s，物流单号：%s，配送方式：物流", afsOrder.getDeliveryCompany(),
                        afsOrder.getDeliverySn()));
            }
            setOrderId(afsOrderItem.getOrderId(), negotiateHistory);
            this.save(negotiateHistory);
        }
    }


    /**
     * 卖家拒绝
     *
     * @param afsOrder
     * @return void
     * @author alan
     * @date 2021/3/17 22:23
     */
    @Override
    public void sellerRefuse(AfsOrder afsOrder) {
        List<AfsOrderItem> afsOrderItemList = afsOrderItemMapper.selectList(new LambdaQueryWrapper<AfsOrderItem>()
                .select(AfsOrderItem::getOrderId)
                .eq(AfsOrderItem::getAfsId, afsOrder.getId()));
        for (AfsOrderItem afsOrderItem : afsOrderItemList) {
            AfsNegotiateHistory negotiateHistory = initCreator(false);
            negotiateHistory.setApplyUserType(afsOrder.getType().isLeaderApply());
            StringBuilder content = new StringBuilder(
                    String.format("卖家拒绝%s申请,%s失败,本次售后关闭", afsOrder.getType().getDesc(), afsOrder.getType().getDesc()));
            if (StrUtil.isNotBlank(afsOrder.getRefusalReason())) {
                content.append(String.format(",拒绝原因：%s", afsOrder.getRefusalReason()));
            }
            negotiateHistory.setInfo(content.toString());
            setOrderId(afsOrderItem.getOrderId(), negotiateHistory);
            this.save(negotiateHistory);
        }
    }


    /**
     * 卖家发货
     *
     * @param afsOrder
     * @return void
     * @author alan
     * @date 2021/3/17 22:24
     */
    @Override
    public void sellerShipped(AfsOrder afsOrder) {
        AfsNegotiateHistory negotiateHistory = initCreator(true);
        List<AfsOrderItem> afsOrderItemList = afsOrderItemMapper.selectList(new LambdaQueryWrapper<AfsOrderItem>()
                .select(AfsOrderItem::getOrderId)
                .eq(AfsOrderItem::getAfsId, afsOrder.getId()));
        OrderVo orderVo = remoteOrderService.orderInfo(afsOrder.getOrderId());
        for (AfsOrderItem afsOrderItem : afsOrderItemList) {
            negotiateHistory.setApplyUserType(afsOrder.getType().isLeaderApply());
            String content =
                    String.format("卖家已经发出新货，正在配送中请耐心等待\n" +
                            "收货地址：%s", orderVo.getOrderDelivery().getReceiverDetailAddress());
            negotiateHistory.setInfo(content);
            setOrderId(afsOrderItem.getOrderId(), negotiateHistory);
            this.save(negotiateHistory);
        }
    }

    /**
     * 卖家同意申请等待退货
     *
     * @param afsOrder
     * @param isSystem
     * @param address
     * @param isLogistics
     * @return void
     * @author alan
     * @date 2021/3/17 22:24
     */
    @Override
    public void sellerApproveNeedReturn(AfsOrder afsOrder, Boolean isSystem, String address, Boolean isLogistics) {
        List<AfsOrderItem> afsOrderItemList = afsOrderItemMapper.selectList(new LambdaQueryWrapper<AfsOrderItem>()
                .select(AfsOrderItem::getOrderId)
                .eq(AfsOrderItem::getAfsId, afsOrder.getId()));
        for (AfsOrderItem afsOrderItem : afsOrderItemList) {
            AfsNegotiateHistory negotiateHistory = initCreator(isSystem);
            negotiateHistory.setApplyUserType(afsOrder.getType().isLeaderApply());
            StringBuilder content = new StringBuilder();
            if (isSystem) {
                content.append("卖家超时未操作，系统自动确认");
            }
            if (isLogistics) {
                LogisticsAddressVo logisticsAddressVo = remoteLogisticsFeginService.getFeignDefaultAddress(2);
                if (ObjectUtil.isNotNull(logisticsAddressVo)) {
                    log.info(JSONUtil.toJsonStr(logisticsAddressVo));
                    String phone = logisticsAddressVo.getPhone();
                    String name = logisticsAddressVo.getName();
                    address =
                            logisticsAddressVo.getProvince() + logisticsAddressVo.getCity() + logisticsAddressVo.getCountry() + logisticsAddressVo.getAddress();
                    content.append(
                            String.format("卖家同意本次%s申请,等待用户退货", afsOrder.getType().getDesc()));
                    content.append(String.format(",退货地址：%s,%s,%s", address, phone, name));
                } else {
                    log.error("获取默认退货地址失败");
                    content.append(
                            String.format("卖家同意本次%s申请,等待用户退货", afsOrder.getType().getDesc()));
                }

            } else {
                content.append(String.format(",退货地址：%s", address));
                content.append(String.format(",请及时退货以免%s超时关闭", afsOrder.getType().getDesc()));
            }
            negotiateHistory.setInfo(content.toString());
            setOrderId(afsOrderItem.getOrderId(), negotiateHistory);
            this.save(negotiateHistory);
        }
    }

    /**
     * 获取创建人
     *
     * @param isSystem the is system
     * @return the afs negotiate history
     */
    public AfsNegotiateHistory initCreator(Boolean isSystem) {
        AfsNegotiateHistory negotiateHistory = null;
        Result<ShopInfoDto> dtoResult = remoteMiniInfoService.getShopInfo(TenantContextHolder.getTenantId());
        String logoUrl = "";
        if (dtoResult.getCode() == SystemCode.SUCCESS_CODE) {
            logoUrl = dtoResult.getData().getLogoUrl();
        }
        if (isSystem) {
            negotiateHistory = AfsNegotiateHistory.systemCreate(logoUrl);
        } else {
            negotiateHistory = AfsNegotiateHistory.sellerCreate(logoUrl);
        }
        return negotiateHistory;
    }

    /**
     * 商家同意退款申请
     *
     * @param afsOrder
     * @param isSystem
     * @return void
     * @author alan
     * @date 2021/3/17 22:25
     */
    @Override
    public void refund(AfsOrder afsOrder, boolean isSystem) {
        List<AfsOrderItem> afsOrderItemList = afsOrderItemMapper.selectList(new LambdaQueryWrapper<AfsOrderItem>()
                .select(AfsOrderItem::getOrderId)
                .eq(AfsOrderItem::getAfsId, afsOrder.getId()));
        for (AfsOrderItem afsOrderItem : afsOrderItemList) {
            AfsNegotiateHistory negotiateHistory = initCreator(isSystem);
            negotiateHistory.setApplyUserType(afsOrder.getType().isLeaderApply());
            StringBuilder content = new StringBuilder();
            if (isSystem) {
                content.append("超时未操作,系统自动确认");
            } else {
                content.append("商家同意本次退款申请");

            }
            content.append(String.format(",退款金额：￥%s，退款成功", afsOrder.getRefundAmount()));
            negotiateHistory.setInfo(content.toString());
            setOrderId(afsOrderItem.getOrderId(), negotiateHistory);
            this.save(negotiateHistory);
        }
    }

    /**
     * setOrderId
     *
     * @param orderId
     * @param negotiateHistory
     * @return void
     * @author alan
     * @date 2021/3/17 22:25
     */
    private void setOrderId(Long orderId, AfsNegotiateHistory negotiateHistory) {
        Long originalOrderId = afsOrderMapper.selectOriginalOrderByOrderId(orderId);
        if (ObjectUtil.isNotNull(originalOrderId)) {
            negotiateHistory.setOrderId(originalOrderId);
        } else {
            negotiateHistory.setOrderId(orderId);
        }
    }

    /**
     * 查询协商历史
     *
     * @param dto
     * @return java.util.List<com.medusa.gruul.afs.api.entity.AfsNegotiateHistory>
     * @author alan
     * @date 2021/3/17 22:25
     */
    @Override
    public List<AfsNegotiateHistory> negotiateHistoryList(NegotiateHistoryDto dto) {
        if ("2".equalsIgnoreCase(dto.getType())) {
            dto.setType("0");
        }
        Long orderId = afsOrderMapper.selectOriginalOrderByOrderId(dto.getOrderId());
        if (ObjectUtil.isNull(orderId)) {
            orderId = dto.getOrderId();
        }
        return baseMapper.negotiateHistoryList(orderId, dto.getType());
    }


}
