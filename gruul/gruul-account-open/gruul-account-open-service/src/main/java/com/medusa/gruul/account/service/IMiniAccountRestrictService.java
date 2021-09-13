package com.medusa.gruul.account.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.account.api.entity.MiniAccountRestrict;
import com.medusa.gruul.account.model.dto.SetBlacklistDto;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author whh
 * @since 2019-12-02
 */
public interface IMiniAccountRestrictService extends IService<MiniAccountRestrict> {

    /**
     * 获取指定用户黑名单数据
     *
     * @param userIds 用户id
     * @return com.medusa.gruul.account.api.entity.MiniAccountRestrict
     */
    List<MiniAccountRestrict> getByUserIds(List<String> userIds);

    /**
     * 移除指定用户黑名单标识
     *
     * @param userIds 用户id
     */
    void removeByUserId(List<String> userIds);

    /**
     * 新增用户限制类型
     *
     * @param userIds       用户id
     * @param rejectInteger 限制类型数组
     */
    void addUserRestrict(List<String> userIds, List<Integer> rejectInteger);

    /**
     * 修改用户限制类型
     *
     * @param userIds       用户id
     * @param rejectInteger 修改之后的限制类型数组
     */
    void updateUserRestrict(List<String> userIds, List<Integer> rejectInteger);

    /**
     * 批量设置
     *
     * @param dto com.medusa.gruul.account.model.dto.SetBlacklistDto
     */
    void setBlacklist(SetBlacklistDto dto);

    /**
     * 获取指定用户限制数组
     *
     * @param userId 用户id
     * @return java.lang.Integer
     */
    List<Integer> getByUserId(String userId);
}
