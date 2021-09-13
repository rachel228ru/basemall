package com.medusa.gruul.account.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.account.api.entity.MiniAccountTagGroup;
import com.medusa.gruul.account.mapper.MiniAccountTagGroupMapper;
import com.medusa.gruul.account.service.IMiniAccountTagGroupService;
import com.medusa.gruul.common.core.exception.ServiceException;
import org.omg.CORBA.SystemException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户所属分组表 服务实现类
 * </p>
 *
 * @author whh
 * @since 2019-11-18
 */
@Service
public class MiniAccountTagGroupServiceImpl extends ServiceImpl<MiniAccountTagGroupMapper, MiniAccountTagGroup> implements IMiniAccountTagGroupService {

    @Override
    public List<MiniAccountTagGroup> getByUserListId(List<String> userIdList) {
        return this.getBaseMapper().selectList(new QueryWrapper<MiniAccountTagGroup>().in("user_id", userIdList));
    }

    @Override
    public void removeByUserId(List<String> userIds) {
        this.getBaseMapper().delete(new QueryWrapper<MiniAccountTagGroup>().in("user_id", userIds));
    }

    @Override
    public void addUserTag(List<String> userIds, List<Long> tagIds) {
        for (Long tagId : tagIds) {
            for (String userId : userIds) {
                MiniAccountTagGroup miniAccountTagGroup = new MiniAccountTagGroup();
                miniAccountTagGroup.setTagId(tagId);
                miniAccountTagGroup.setUserId(userId);
                this.baseMapper.insert(miniAccountTagGroup);
            }
        }
    }

    @Override
    public void removeUserTag(List<String> userIds, List<Long> tagId) {
        this.getBaseMapper().delete(new QueryWrapper<MiniAccountTagGroup>().in("user_id", userIds).in("tag_id", tagId));
    }

    @Override
    public void removeByTagId(List<Long> tagId) {
        List<MiniAccountTagGroup> tagIds = this.getBaseMapper().selectList(new QueryWrapper<MiniAccountTagGroup>().in("tag_id", tagId));
        if (CollectionUtil.isNotEmpty(tagIds)){
            throw new ServiceException("当前标签已被使用,无法删除");
        }
    }

}
