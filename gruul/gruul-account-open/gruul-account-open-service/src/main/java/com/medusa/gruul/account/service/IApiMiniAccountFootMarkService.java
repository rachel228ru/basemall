package com.medusa.gruul.account.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.account.api.entity.MiniAccountFootMark;
import com.medusa.gruul.account.model.dto.AccountFootMarkDto;
import com.medusa.gruul.account.model.vo.AccountFootMarkParam;

import java.util.List;

/**
 * @author xiaoq
 *
 * @data 2020/2/28 10:36
 */
public interface IApiMiniAccountFootMarkService extends IService<MiniAccountFootMark> {
    /**
     * 用户足迹信息
     *
     * @param accountFootMarkParam 分页对象
     * @return 分页数据
     */
    List<String> getAccountFootMark(AccountFootMarkParam accountFootMarkParam);

    /**
     * 添加用户足迹
     *
     * @param accountFootMarkDto 商品信息
     */
    void addAccountFootMark(AccountFootMarkDto accountFootMarkDto);


    /**
     * 删除 或清空用户足迹
     *
     * @param longs 用户足迹ids
     * @param type  :0-删除, 1-清空
     */
    void delAccountFootMark(List<Long> longs, Integer type);

    /**
     * 查看用户收藏数量
     *
     * @return 收藏数量
     */
    int getAccountFootMarkCount();
}
