package com.medusa.gruul.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.medusa.gruul.account.api.entity.MiniAccount;
import com.medusa.gruul.account.api.model.MiniAccountExtDto;
import com.medusa.gruul.account.model.dto.BlacklistUserDto;
import com.medusa.gruul.account.model.dto.UserListDto;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 小程序用户表 Mapper 接口
 * </p>
 *
 * @author whh
 * @since 2019-11-18
 */
public interface MiniAccountMapper extends BaseMapper<MiniAccount> {

    /**
     * PC端获取用户列表
     *
     * @param userListDtoPage 分页数据
     * @param paramMap        查询条件参数
     * @return com.medusa.gruul.account.model.dto.UserListDto
     */
    IPage<UserListDto> selectByUserList(Page<UserListDto> userListDtoPage, @Param("paramMap") Map<String, Object> paramMap);

    /**
     * 获取黑名单列表
     *
     * @param blacklistUserDtoPage 分页数据
     * @param paramMap             查询条件参数
     * @return com.medusa.gruul.account.model.dto.BlacklistUserDto
     */
    IPage<BlacklistUserDto> selectByBlackListUser(Page<BlacklistUserDto> blacklistUserDtoPage, @Param("paramMap") Map<String, Object> paramMap);

    /**
     * 根据用户店铺id查询用户基本信息
     *
     * @param shopUserIds 用户店铺id
     * @return
     */
    List<MiniAccountExtDto> selectByShopUserIds(@Param("shopUserIds") List<String> shopUserIds);


    /**
     * 根据租户用户获取会员信息
     * @param tenantId 租户id
     * @param userId 用户id
     * @return com.medusa.gruul.account.api.entity.MiniAccount
     *
     */
    MiniAccount getMemberInfo(@Param("tenantId") String tenantId, @Param("userId") String userId);

    /**
     * 获取指定用户的唯一id
     *
     * @param tenantId   租户id
     * @param shopUserId 店铺用户id
     * @return 唯一id
     */
    String getUserInfoByShopUserId(@Param("tenantId") String tenantId, @Param("shopUserId") String shopUserId);

    /**
     * 获取指定用户的积分值
     *
     * @param tenantId   租户id
     * @param shopUserId 店铺用户id
     * @return 积分值
     */
    BigDecimal getIntegralByShopUserId(@Param("tenantId") String tenantId, @Param("shopUserId") String shopUserId);

    /**
     * 根据用户更新用户余额
     *
     * @param tenantId 租户id
     * @param userId   店铺用户id
     * @param amount   更新余额
     * @return 0 or 1
     */
    int updateBonus(@Param("tenantId") String tenantId, @Param("userId") String userId, @Param("amount") BigDecimal amount);

    /**
     * 根据用户更新用户返利余额
     *
     * @param tenantId 租户id
     * @param userId   店铺用户id
     * @param amount   余额
     * @return 0 or 1
     */
    int updateRebateBonus(@Param("tenantId") String tenantId, @Param("userId") String userId, @Param("amount") BigDecimal amount);

    /**
     * 根据店铺用户id查询用户所在店铺唯一id
     *
     * @param shopUserId 店铺用户id
     * @return com.medusa.gruul.account.api.entity.MiniAccount
     */
    MiniAccount selectByShopUserId(@Param("shopUserId") String shopUserId);
}
