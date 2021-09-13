package com.medusa.gruul.account.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.account.api.entity.MiniAccountExtends;
import com.medusa.gruul.account.api.model.IntegraChangelDto;
import com.medusa.gruul.account.api.model.MiniAccountExtendsUpdateDto;
import com.medusa.gruul.account.mapper.MiniAccountExtendsMapper;
import com.medusa.gruul.account.model.dto.UpdateUserExtendsInfoDto;
import com.medusa.gruul.account.service.IMiniAccountExtendsService;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.CurUserUtil;
import com.medusa.gruul.common.core.util.SystemCode;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.common.dto.CurUserDto;
import com.medusa.gruul.order.api.model.OrderVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户信息扩展表 服务实现类
 * </p>
 *
 * @author whh
 * @since 2019-11-18
 */
@Service
@Slf4j
public class MiniAccountExtendsServiceImpl extends ServiceImpl<MiniAccountExtendsMapper, MiniAccountExtends> implements IMiniAccountExtendsService {


    @Override
    public MiniAccountExtends findByUserId(String userId) {
        return this.baseMapper.selectOne(new QueryWrapper<MiniAccountExtends>().eq("user_id", userId));
    }



    @Override
    public Boolean portionAttributeModify(String userId, MiniAccountExtendsUpdateDto miniAccountExtendsUpdateDto) {
        if (miniAccountExtendsUpdateDto == null) {
            log.info("miniAccountExtendsUpdateDto 对象为null");
            return Boolean.FALSE;
        }
        Integer optionType = miniAccountExtendsUpdateDto.getOptionType();
        boolean isCorrect = optionType < CommonConstants.NUMBER_ZERO || optionType > CommonConstants.NUMBER_THREE;
        if (ObjectUtil.isNull(optionType) || isCorrect) {
            log.info("optionType={},操作类型无效", optionType);
            return Boolean.FALSE;
        }
        MiniAccountExtends accountExtends = findByShopUserId(userId);
        if (accountExtends == null) {
            log.info("userId={},用户扩展数据不存在", userId);
            return Boolean.FALSE;
        }
        MiniAccountExtends updateData = new MiniAccountExtends();
        updateData.setId(accountExtends.getId());
        Boolean flag = Boolean.FALSE;
        switch (optionType) {
            case 1:
                if (ObjectUtil.isEmpty(miniAccountExtendsUpdateDto.getCumsumType()) || ObjectUtil.isEmpty(miniAccountExtendsUpdateDto.getCumsumLimit())
                        || StrUtil.isEmpty(miniAccountExtendsUpdateDto.getBusinessId())) {
                    return Boolean.FALSE;
                }
                flag = consumptionAccumulation(accountExtends, miniAccountExtendsUpdateDto.getCumsumLimit(),
                        miniAccountExtendsUpdateDto.getCumsumType(), miniAccountExtendsUpdateDto.getBusinessId(), miniAccountExtendsUpdateDto.getLastDealTime());
                break;
            case 2:
                //Todo 小区数据
                flag = Boolean.TRUE;
                break;
            //签到数据
            case 3:
               //Todo 签到数据
                flag = Boolean.TRUE;
                break;
            default:
                break;
        }
        return flag;
    }

    /**
     * 用户累加消费处理
     *
     * @param accountExtends 用户扩展数据
     * @param cumsumLimit    累加金额
     * @param cumsumType     累加类型  1积分消费  2-订单完成
     * @param businessId     业务id
     * @param lastDealTime   最后交易时间
     */
    private boolean consumptionAccumulation(MiniAccountExtends accountExtends, BigDecimal cumsumLimit, Integer cumsumType, String businessId, LocalDateTime lastDealTime) {
        boolean flag;
        MiniAccountExtends updateData = new MiniAccountExtends();
        updateData.setId(accountExtends.getId());
        updateData.setConsumeTotleMoney(accountExtends.getConsumeTotleMoney().add(cumsumLimit));
        updateData.setConsumeNum(accountExtends.getConsumeNum() + CommonConstants.NUMBER_ONE);
        updateData.setLastDealTime(lastDealTime);
        flag = this.updateById(updateData);
        if (StrUtil.isEmpty(TenantContextHolder.getTenantId())) {
            TenantContextHolder.setTenantId(accountExtends.getTenantId());
        }
        if (!flag) {
            return Boolean.FALSE;
        }
        //Todo 累加金额数据

        return flag;
    }

    @Override
    public void updateUserExtendsInfo(UpdateUserExtendsInfoDto updateUserExtendsInfoDto) {
        CurUserDto curUser = CurUserUtil.getHttpCurUser();
        if (curUser == null) {
            throw new ServiceException("用户数据不存在", SystemCode.DATA_NOT_EXIST.getCode());
        }
        MiniAccountExtends miniAccountExtends = this.findByShopUserId(curUser.getUserId());
        if (miniAccountExtends == null) {
            throw new ServiceException("无效用户", SystemCode.DATA_NOT_EXIST.getCode());
        }
        MiniAccountExtends updata = new MiniAccountExtends();
        updata.setId(miniAccountExtends.getId());
        updata.setShopId(updateUserExtendsInfoDto.getShopId());
        updata.setLastChooseLcation(updateUserExtendsInfoDto.getLastChooseLcation());
        this.updateById(updata);
    }

    @Override
    public void orderCompleted(OrderVo orderVo) {
        MiniAccountExtends accountExtends = findByShopUserId(orderVo.getUserId());
        if (ObjectUtil.isNull(accountExtends)) {
            log.debug("用户数据不存在,无法增加消息记录");
            return;
        }
        //用户累加消费处理
        consumptionAccumulation(accountExtends,
                orderVo.getPayAmount(), CommonConstants.NUMBER_TWO, orderVo.getId().toString(), null);
    }

    @Override
    public MiniAccountExtends findByCurrentStatus(String userId) {
        return this.baseMapper.selectByCurrentStatus(userId, CommonConstants.NUMBER_ONE);
    }

    @Override
    public MiniAccountExtends findByShopUserId(String shopUserId) {
        return this.baseMapper.selectOne(new QueryWrapper<MiniAccountExtends>().eq("shop_user_id", shopUserId));
    }

    @Override
    public void modifyLastDealTime(String shopUserId, LocalDateTime lastDealTime) {
        MiniAccountExtends miniAccountExtends = this.findByShopUserId(shopUserId);
        if (miniAccountExtends == null) {
            throw new ServiceException("无效用户");
        }
        MiniAccountExtends updateData = new MiniAccountExtends();
        updateData.setId(miniAccountExtends.getId());
        updateData.setLastDealTime(lastDealTime);
        this.updateById(updateData);
    }

    @Override
    public MiniAccountExtends findByShopIdAndUserId(String shopId, String userId) {
        return this.baseMapper.selectOne(new QueryWrapper<MiniAccountExtends>().eq("user_id", userId).eq("shop_id", shopId));
    }
}
