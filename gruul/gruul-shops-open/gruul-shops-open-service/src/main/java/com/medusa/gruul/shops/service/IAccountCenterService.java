package com.medusa.gruul.shops.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.shops.api.entity.AccountCenter;
import com.medusa.gruul.shops.api.model.AccountCenterSettingDto;
import com.medusa.gruul.shops.api.model.AccountCenterVo;

/**
 * <p>
 * 用户中心配置 服务类
 * </p>
 *
 * @author whh
 * @since 2019-11-18
 */
public interface IAccountCenterService extends IService<AccountCenter> {

    /**
     * 积分默认值
     * @param accountCenterSettingDto  com.medusa.gruul.account.api.model.AccountCenterSettingDto
     */
    void add(AccountCenterSettingDto accountCenterSettingDto);


    /**
     * 获取用户中心配置
     * @return com.medusa.gruul.account.model.vo.AccountCenterVo
     */
    AccountCenterVo accountCenterSetting();


    /**
     * 修改用户中心
     * @param dto  com.medusa.gruul.account.api.model.AccountCenterSettingDto
     */
    void accountCenterSettingMotify(AccountCenterSettingDto dto);
}
