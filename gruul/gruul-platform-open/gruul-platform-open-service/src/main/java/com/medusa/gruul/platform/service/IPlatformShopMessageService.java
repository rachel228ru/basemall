package com.medusa.gruul.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.platform.api.entity.PlatformShopMessage;
import com.medusa.gruul.platform.api.model.dto.SubscribeMsgSendDto;
import com.medusa.gruul.platform.api.model.vo.MiniMsgVo;
import com.medusa.gruul.platform.api.model.vo.ShopMessageVo;
import com.medusa.gruul.platform.model.dto.MotifyMsgStateDto;

import java.util.List;

/**
 * <p>
 * 店铺消息配置 服务类
 * </p>
 *
 * @author whh
 * @since 2020-05-22
 */
public interface IPlatformShopMessageService extends IService<PlatformShopMessage> {

    /**
     * 店铺创建生成店铺消息默认值
     *
     * @param version 版本号
     */
    void generateInitMsg(String version);

    /**
     * 获取当前小程序版本可使用订阅消息
     *
     * @return com.medusa.gruul.platform.api.model.vo.MiniMsgVo
     */
    List<MiniMsgVo> getCurrentMiniMsg();

    /**
     * 获取店铺消息
     *
     * @return com.medusa.gruul.platform.api.model.vo.ShopMessageVo
     */
    List<ShopMessageVo> msgAll();


    /**
     * 更新店铺订阅消息,在小程序审核通过之后才会去生成,因为消息最多25个
     *
     * @param version  版本
     * @param tenantId 租户id
     */
    void upSubscriptionMsg(String version, String tenantId);

    /**
     * 获取指定类型版本消息
     *
     * @param version 版本号
     * @param useType 消息类型
     * @return com.medusa.gruul.platform.api.entity.PlatformShopMessage
     */

    List<PlatformShopMessage> getByVersionMes(String version, Integer useType);

    /**
     * 发送订阅消息
     *
     * @param msgSendDto com.medusa.gruul.platform.api.model.dto.SubscribeMsgSendDto
     */
    void subscribeMsgSend(SubscribeMsgSendDto msgSendDto);

    /**
     * 修改消息状态
     *
     * @param msgStateDto com.medusa.gruul.platform.model.dto.MotifyMsgStateDto
     */
    void motifyState(MotifyMsgStateDto msgStateDto);

}
