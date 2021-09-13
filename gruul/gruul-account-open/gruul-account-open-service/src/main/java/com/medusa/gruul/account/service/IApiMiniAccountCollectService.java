package com.medusa.gruul.account.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.account.api.entity.MiniAccountCollect;
import com.medusa.gruul.account.model.dto.UserCollectDto;
import com.medusa.gruul.account.model.vo.UserCollectVo;
import com.medusa.gruul.account.mq.CollectMessage;

import java.util.List;

/**
 * @author  xiaoq
 * 小程序用戶收藏 服務類
 *
 * @data 2020/2/22 15:41
 */
public interface IApiMiniAccountCollectService extends IService<MiniAccountCollect> {


    /**
     * 添加商品信息到用戶收藏
     *
     * @param userCollectDtos
     */
    void addAccountCollect(List<UserCollectDto> userCollectDtos);

    /**
     * 更新用户收藏sql 表数据
     *
     * @param collectMessage mq消息体
     */
    void updateAccountCollect(CollectMessage collectMessage);


    /**
     * 获取用户收藏的商品信息
     *
     * @return 用户收藏list<商品>信息
     */
    List<UserCollectVo> getUserCollectInfo();

    /**
     * 刪除 用戶收藏信息  sql表数据及缓存
     *
     * @param userCollectDto 用户收藏信息
     */
    void delAccountCollect(UserCollectDto userCollectDto);

    /**
     * 查看用户是否收藏过该商品
     *
     * @param productId
     * @return
     */
    Boolean findAccountIsCollect(Long productId);


    /**
     *  获取用户收藏数量
     * @return   用户收藏数量
     */
    int getCollectCount();
}
