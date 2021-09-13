package com.medusa.gruul.account.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.account.api.entity.MiniAccount;
import com.medusa.gruul.account.api.model.AccountInfoDto;
import com.medusa.gruul.account.api.model.MiniAccountExtDto;
import com.medusa.gruul.account.api.model.WxMpUserDto;
import com.medusa.gruul.account.model.dto.DecodePhoneInfo;
import com.medusa.gruul.account.model.dto.UpdateUserBaseInfoDto;
import com.medusa.gruul.account.model.vo.BlacklistUserVo;
import com.medusa.gruul.account.model.vo.LoginBaseInfoVo;
import com.medusa.gruul.account.model.vo.UserInfoVo;
import com.medusa.gruul.account.model.vo.UserListVo;
import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.common.core.util.Result;

import java.util.List;

/**
 * <p>
 * 小程序用户表 服务类
 * </p>
 *
 * @author whh
 * @since 2019-11-18
 */
public interface IMiniAccountService extends IService<MiniAccount> {

    /**
     * 小程序用户登录
     *
     * @param code code值
     * @return com.medusa.gruul.account.model.vo.LoginBaseInfoVo
     */
    LoginBaseInfoVo login(String code);

    /**
     * 获取微信用户绑定的手机号
     *
     * @param decodePhoneInfo dto
     * @return java.lang.String
     */
    String decodePhoneInfo(DecodePhoneInfo decodePhoneInfo);

    /**
     * 更新用户基础信息
     *
     * @param updateUserBaseInfoDto dto
     */
    void updateUserBaseInfo(UpdateUserBaseInfoDto updateUserBaseInfoDto);

    /**
     * 获取用户信息
     *
     * @param infoLevel 数据级别 1-基础信息 2-用户扩展数据
     * @return com.medusa.gruul.account.model.vo.UserInfoVo
     */
    UserInfoVo getUserInfo(Integer infoLevel);


    /**
     * 根据用户id获取用户基础数据
     *
     * @param userId 用户id
     * @return com.medusa.gruul.account.api.entity.MiniAccount
     */
    MiniAccount getByUserId(String userId);


    /**
     * 获取pc端用户列表
     *
     * @param nikeName              微信昵称
     * @param becomeMemberStartTime 成为会员开始时间 2019-11-11 11:23:23
     * @param becomeMemberEndTime   成为会员结束时间 2019-11-11 11:23:23
     * @param tagId                 标签id
     * @param orderSuccessStartTime 成交时间 2019-11-11 11:23:23
     * @param orderSuccessEndTime   成交时间 2019-11-11 11:23:23
     * @param memberNumber          会员等级代码
     * @param page                  页数
     * @param size                  条数
     * @param sortType
     * @return com.medusa.gruul.account.model.vo.UserListVo
     */
    PageUtils<List<UserListVo>> userList(String nikeName, String becomeMemberStartTime, String becomeMemberEndTime, Long tagId,
                                         String orderSuccessStartTime, String orderSuccessEndTime,  String memberNumber, Integer page, Integer size, Integer sortType);

    /**
     * 获取用户信息接口
     *
     * @param userId 用户id
     * @param infos  [1,2,3]  1,基本信息,2,扩展信息,3-地址信息,4-授权信息
     * @return com.medusa.gruul.account.api.model.AccountInfoDto
     */
    AccountInfoDto accountInfo(String userId, List<Integer> infos);


    /**
     * 分页查询黑名单用户
     *
     * @param page
     * @param size
     * @param permission 权限 0-全部 1-下单 2-评论
     * @param fuzzy      手机号或微信昵称模糊搜索
     * @return com.medusa.gruul.account.model.vo.BlacklistUserVo
     */
    PageUtils<List<BlacklistUserVo>> blacklist(Integer page, Integer size, Integer permission, String fuzzy);


    /**
     * 切换用户当前所在店铺
     *
     * @param shopId 店铺id
     * @return java.lang.String
     */
    String switchShops(String shopId);

    /**
     * 生成默认账号
     *
     * @param jsonData json数据(租户id和shopId)
     */
    void generateAccountDefault(String jsonData);

    /**
     * 批量获取指定用户id(用户店铺id)的用户基本信息
     *
     * @param shopUserId 用户id数组
     * @return com.medusa.gruul.account.api.entity.MiniAccount
     */
    List<MiniAccountExtDto> accountsInfoList(List<String> shopUserId);


    /**
     * 获取当前用户二维码
     *
     * @return base64二维码
     */
    String qrCode();


    /**
     * 公众号授权登录,返回登录token
     *
     * @param wxMpUserDto com.medusa.gruul.platform.api.model.dto.WxMpUserDto
     * @return java.lang.String
     */
    Result mpLogin(WxMpUserDto wxMpUserDto);

    /**
     * 根据店铺用户id查询用户所在店铺唯一id
     *
     * @param shopUserId 店铺用户id
     * @return com.medusa.gruul.account.api.entity.MiniAccount
     */
    MiniAccount getByShopUserId(String shopUserId);
}
