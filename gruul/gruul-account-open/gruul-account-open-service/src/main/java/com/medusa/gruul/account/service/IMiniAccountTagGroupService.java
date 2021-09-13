package com.medusa.gruul.account.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.account.api.entity.MiniAccountTagGroup;

import java.util.List;

/**
 * <p>
 * 用户所属分组表 服务类
 * </p>
 *
 * @author whh
 * @since 2019-11-18
 */
public interface IMiniAccountTagGroupService extends IService<MiniAccountTagGroup> {

    /**
     * 获取指定用户的标签
     *
     * @param userIdList 用户id数组
     * @return com.medusa.gruul.account.api.entity.MiniAccountTagGroup
     */
    List<MiniAccountTagGroup> getByUserListId(List<String> userIdList);

    /**
     * 移除指定用户所有分组
     *
     * @param userIds 用户ids
     */
    void removeByUserId(List<String> userIds);

    /**
     * 添加用户标签
     *
     * @param userIds 用户ids
     * @param tagIds  标签ids
     */
    void addUserTag(List<String> userIds, List<Long> tagIds);

    /**
     * 移除指定用户指定标签
     *
     * @param userIds 用户ids
     * @param tagId   标签id数组
     */
    void removeUserTag(List<String> userIds, List<Long> tagId);

    /**
     * 删除用户标签
     *
     * @param tagId
     *
     */
    void removeByTagId(List<Long> tagId);
}
