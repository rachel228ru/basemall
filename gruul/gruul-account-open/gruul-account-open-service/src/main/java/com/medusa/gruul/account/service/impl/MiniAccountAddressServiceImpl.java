package com.medusa.gruul.account.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.account.api.entity.MiniAccountAddress;
import com.medusa.gruul.account.conf.AccountRedis;
import com.medusa.gruul.account.constant.RedisConstant;
import com.medusa.gruul.account.constant.TimeValueConstant;
import com.medusa.gruul.account.mapper.MiniAccountAddressMapper;
import com.medusa.gruul.account.model.dto.AddressCraeteDto;
import com.medusa.gruul.account.model.dto.AddressUpdateDto;
import com.medusa.gruul.account.model.vo.AccountAddressListVo;
import com.medusa.gruul.account.service.IMiniAccountAddressService;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.AreaUtil;
import com.medusa.gruul.common.core.util.CurUserUtil;
import com.medusa.gruul.common.core.util.SystemCode;
import com.medusa.gruul.common.dto.AreaDto;
import com.medusa.gruul.common.dto.CurUserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 地址表 服务实现类
 * </p>
 *
 * @author whh
 * @since 2019-11-18
 */
@Service
public class MiniAccountAddressServiceImpl extends ServiceImpl<MiniAccountAddressMapper, MiniAccountAddress> implements IMiniAccountAddressService {

    @Override
    public void addressCraete(AddressCraeteDto addersVO) {
        String rex = ReUtil.get("^[\\u4E00-\\u9FA5]+$", addersVO.getUserName(), 0);
        if (StrUtil.isEmpty(rex) || rex.length() > CommonConstants.NUMBER_TEN) {
            throw new ServiceException("收货人姓名必须在10个汉字以内");
        }
        String userId = CurUserUtil.getHttpCurUser().getUserId();
        MiniAccountAddress miniAccountAddress = new MiniAccountAddress();
        BeanUtils.copyProperties(addersVO, miniAccountAddress);
        miniAccountAddress.setUserId(userId);
        //校验是否存在相同
        checkeRepetitionAddress(miniAccountAddress);
        miniAccountAddress.setIsDefault(0);
        //获取用户列表判断是否添加第一条地址,是则添加为默认地址
        List<MiniAccountAddress> userAddress = getUserAddress(miniAccountAddress.getUserId());
        if (CollectionUtil.isEmpty(userAddress)) {
            miniAccountAddress.setIsDefault(1);
        }
        //todo 兼容s1.0.7之前的版本
        if (StrUtil.isEmpty(miniAccountAddress.getPostCode())) {
            Optional<AreaDto> areaDto = AreaUtil.getByCityLabelAndAreaLabel(miniAccountAddress.getCity(), miniAccountAddress.getCounty());
            areaDto.ifPresent(obj -> {
                miniAccountAddress.setPostCode(obj.getValue());
            });
        }

        this.baseMapper.insert(miniAccountAddress);

        updateUserAdderssCache(userId);
    }

    @Override
    public List<AccountAddressListVo> addersGet(Integer type) {
        CurUserDto curUser = CurUserUtil.getHttpCurUser();
        String userId = curUser.getUserId();
        List<MiniAccountAddress> userAddress = getUserAddress(userId);
        List<AccountAddressListVo> vos = new ArrayList<>(0);
        if (CollectionUtil.isNotEmpty(userAddress)) {
            vos = new ArrayList<>(userAddress.size());
            //封装返回数据
            for (MiniAccountAddress miniAccountAddress : userAddress) {
                AccountAddressListVo vo = new AccountAddressListVo();
                BeanUtils.copyProperties(miniAccountAddress, vo);
                vos.add(vo);
            }
            //获取默认地址过滤
            if (type.equals(CommonConstants.NUMBER_TWO)) {
                vos = vos.stream().filter(obj -> obj.getIsDefault().equals(1)).collect(Collectors.toList());
            }
        }
        return vos;
    }

    @Override
    public void addressDelete(Integer adderssId) {
        this.baseMapper.deleteById(adderssId);
        CurUserDto curUser = CurUserUtil.getHttpCurUser();
        String userId = curUser.getUserId();
        updateUserAdderssCache(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addersUpdate(AddressUpdateDto updateDto) {
        String rex = ReUtil.get("^[\\u4E00-\\u9FA5]+$", updateDto.getUserName(), 0);
        if (StrUtil.isEmpty(rex) || rex.length() > CommonConstants.NUMBER_TEN) {
            throw new ServiceException("收货人姓名必须是10个汉字以内");
        }
        String userId = CurUserUtil.getHttpCurUser().getUserId();
        List<MiniAccountAddress> userAddress = getUserAddress(userId);
        Optional.ofNullable(updateDto.getDefaultStatus()).orElseThrow(() ->  new ServiceException("操作失败", SystemCode.DATA_UPDATE_FAILED.getCode()));
        MiniAccountAddress miniAccountAddress = new MiniAccountAddress();
        BeanUtils.copyProperties(updateDto, miniAccountAddress);
        miniAccountAddress.setUserId(userId);
        //todo 兼容s1.0.7之前的版本
        if (StrUtil.isEmpty(updateDto.getPostCode())) {
            Optional<AreaDto> areaDto = AreaUtil.getByCityLabelAndAreaLabel(miniAccountAddress.getCity(), miniAccountAddress.getCounty());
            areaDto.ifPresent(obj -> {
                miniAccountAddress.setPostCode(obj.getValue());
            });
        }
        if (updateDto.getDefaultStatus().equals(CommonConstants.NUMBER_ONE)) {
            userAddress.forEach(obj -> {
                boolean flag = false;
                if (obj.getIsDefault().equals(1)) {
                    obj.setIsDefault(0);
                    flag = true;
                }
                if (obj.getId().equals(updateDto.getId())) {
                    BeanUtils.copyProperties(miniAccountAddress, obj);
                    obj.setIsDefault(1);
                    flag = true;
                }
                if (flag) {
                    this.baseMapper.updateById(obj);
                }
            });
        }
        if (updateDto.getDefaultStatus().equals(CommonConstants.NUMBER_ZERO)) {
            //校验相同地址
            checkeRepetitionAddress(miniAccountAddress);
            Optional<MiniAccountAddress> first = userAddress.stream().filter(obj -> obj.getId().equals(updateDto.getId())).findFirst();
            first.ifPresent(obj -> {
                BeanUtils.copyProperties(miniAccountAddress, obj);
                this.baseMapper.updateById(obj);
            });
        }
        updateUserAdderssCache(userId);
    }


    /**
     * 更新用户地址缓存
     */
    private void updateUserAdderssCache(String userId) {
        if (userId == null) {
            throw new ServiceException("用户数据错误", SystemCode.DATA_NOT_EXIST.getCode());
        }
        new AccountRedis().del(RedisConstant.ADDRESS_KEY.concat(userId));
        getUserAddress(userId);
    }

    /**
     * 校验是否存在相同的用户地址
     *
     * @param address com.medusa.gruul.account.api.entity.MiniAccountAddress
     */
    private void checkeRepetitionAddress(MiniAccountAddress address) {
        List<MiniAccountAddress> addressList = getUserAddress(address.getUserId());
        if (CollectionUtil.isEmpty(addressList)) {
            return;
        }
        Map<String, MiniAccountAddress> addressMap = addressList.stream().collect(Collectors.toMap(k ->
                        StrUtil.concat(false, k.getUserId(), k.getProvince(), k.getCity(), k.getCounty(), k.getDetailInfo(), k.getUserName(), k.getPhone()),
                v -> v));
        String key = StrUtil.concat(false, address.getUserId(), address.getProvince(), address.getCity(), address.getCounty(), address.getDetailInfo(),
                address.getUserName(), address.getPhone());
        if (addressMap.get(key) != null) {
            throw new ServiceException("请勿添加相同地址", SystemCode.DATA_EXISTED.getCode());
        }
    }

    @Override
    public List<MiniAccountAddress> getUserAddress(String userId) {
        AccountRedis accountRedis = new AccountRedis();
        String key = RedisConstant.ADDRESS_KEY.concat(userId);
        String userAddress = accountRedis.get(key);
        List<MiniAccountAddress> accountAddresses = null;
        if (StrUtil.isNotEmpty(userAddress)) {
            accountAddresses = JSON.parseArray(userAddress, MiniAccountAddress.class);
        } else {
            accountAddresses = this.baseMapper.selectList(new QueryWrapper<MiniAccountAddress>().eq("user_id", userId));
            if (CollectionUtil.isNotEmpty(accountAddresses)) {
                accountRedis.setNxPx(key, JSON.toJSONString(accountAddresses), TimeValueConstant.SEVENDAY);
            }
        }
        return accountAddresses;
    }

}
