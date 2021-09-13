package com.medusa.gruul.account.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.account.api.entity.MiniAccountExtends;
import com.medusa.gruul.account.api.model.IntegraChangelDto;
import com.medusa.gruul.account.api.model.MiniAccountExtendsUpdateDto;
import com.medusa.gruul.account.model.dto.UpdateUserExtendsInfoDto;
import com.medusa.gruul.order.api.model.OrderVo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户信息扩展表 服务类
 * </p>
 *
 * @author whh
 * @since 2019-11-18
 */
public interface IMiniAccountExtendsService extends IService<MiniAccountExtends> {

    /**
     * 根据用户id获取用户详情信息
     *
     * @param userId 用户id
     * @return com.medusa.gruul.account.api.entity.MiniAccountExtends
     */
    MiniAccountExtends findByUserId(String userId);



    /**
     * 修改用户部分字段
     *
     * @param userId                      用户id
     * @param miniAccountExtendsUpdateDto com.medusa.gruul.account.api.model.MiniAccountExtendsUpdateDto
     * @return 修改成功-true  未修改-false
     */
    Boolean portionAttributeModify(String userId, MiniAccountExtendsUpdateDto miniAccountExtendsUpdateDto);

    /**
     * 更新用户部分字段
     *
     * @param updateUserExtendsInfoDto com.medusa.gruul.account.model.dto.UpdateUserExtendsInfoDto
     */
    void updateUserExtendsInfo(UpdateUserExtendsInfoDto updateUserExtendsInfoDto);

    /**
     * 订单完成,
     *
     * @param orderVo com.medusa.gruul.order.api.model.OrderVo
     */
    void orderCompleted(OrderVo orderVo);

    /**
     * 查询当前用户使用的扩展数据  CurrentStatu=1
     *
     * @param userId 用户id
     * @return com.medusa.gruul.account.api.entity.MiniAccountExtends
     */
    MiniAccountExtends findByCurrentStatus(String userId);

    /**
     * 店铺用户id
     *
     * @param shopUserId 店铺用户id
     * @return com.medusa.gruul.account.api.entity.MiniAccountExtends
     */
    MiniAccountExtends findByShopUserId(String shopUserId);

    /**
     * 修改用户最后交易时间为当前时间
     *
     * @param shopUserId   店铺用户id
     * @param lastDealTime 最后交易时间
     */
    void modifyLastDealTime(String shopUserId, LocalDateTime lastDealTime);

    /**
     * 获取指定店铺下用户唯一的数据
     *
     * @param shopId 店铺id
     * @param userId 用户唯一id
     * @return com.medusa.gruul.account.api.entity.MiniAccountExtends
     */
    MiniAccountExtends findByShopIdAndUserId(String shopId, String userId);
}
