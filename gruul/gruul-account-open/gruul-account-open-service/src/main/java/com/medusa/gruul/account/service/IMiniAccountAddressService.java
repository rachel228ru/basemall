package com.medusa.gruul.account.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.account.api.entity.MiniAccountAddress;
import com.medusa.gruul.account.model.dto.AddressCraeteDto;
import com.medusa.gruul.account.model.dto.AddressUpdateDto;
import com.medusa.gruul.account.model.vo.AccountAddressListVo;

import java.util.List;

/**
 * <p>
 * 会员地址表 服务类
 * </p>
 *
 * @author whh
 * @since 2019-11-18
 */
public interface IMiniAccountAddressService extends IService<MiniAccountAddress> {

    /**
     * 添加用户地址
     *
     * @param addersVO com.medusa.gruul.account.model.dto.AddressCraeteDto
     */
    void addressCraete(AddressCraeteDto addersVO);


    /**
     * 获取用户地址
     *
     * @param type type 1-获取所有 2-获取默认地址,默认type=1
     * @return com.medusa.gruul.account.model.vo.AccountAddressListVo []
     */
    List<AccountAddressListVo> addersGet(Integer type);

    /**
     * 删除指定地址
     *
     * @param adderssId 地址id
     */
    void addressDelete(Integer adderssId);

    /**
     * 更新用户地址
     *
     * @param updateDto com.medusa.gruul.account.model.dto.AddressUpdateDto
     */
    void addersUpdate(AddressUpdateDto updateDto);

    /**
     * 获取指定用户地址
     *
     * @param userId 用户id
     * @return com.medusa.gruul.account.api.entity.MiniAccountAddress
     */
    List<MiniAccountAddress> getUserAddress(String userId);
}
