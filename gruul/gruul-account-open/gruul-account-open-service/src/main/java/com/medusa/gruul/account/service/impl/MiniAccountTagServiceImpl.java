package com.medusa.gruul.account.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.account.api.entity.MiniAccountTag;
import com.medusa.gruul.account.api.entity.MiniAccountTagGroup;
import com.medusa.gruul.account.mapper.MiniAccountTagMapper;
import com.medusa.gruul.account.model.dto.TagDto;
import com.medusa.gruul.account.model.dto.UserTagDto;
import com.medusa.gruul.account.model.vo.TagVo;
import com.medusa.gruul.account.service.IMiniAccountTagGroupService;
import com.medusa.gruul.account.service.IMiniAccountTagService;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.SystemCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户标签表 服务实现类
 * </p>
 *
 * @author whh
 * @since 2019-11-18
 */
@Service
public class MiniAccountTagServiceImpl extends ServiceImpl<MiniAccountTagMapper, MiniAccountTag> implements IMiniAccountTagService {

    @Autowired
    private IMiniAccountTagGroupService miniAccountTagGroupService;

    @Override
    public List<MiniAccountTag> getByIdList(List<Long> tagIdList) {
        return this.baseMapper.selectList(new QueryWrapper<MiniAccountTag>().in("id", tagIdList));
    }

    @Override
    public List<TagVo> listAll() {
        //租户id 自动封装进行!
        List<MiniAccountTag> tagList = this.baseMapper.selectList(new QueryWrapper<MiniAccountTag>());
        if (CollectionUtil.isEmpty(tagList)) {
            return new ArrayList<>(0);
        }
        List<TagVo> vos = new ArrayList<>(tagList.size());
        for (MiniAccountTag miniAccountTag : tagList) {
            TagVo tagVo = new TagVo();
            tagVo.setTagId(miniAccountTag.getId());
            tagVo.setOption(Boolean.FALSE);
            tagVo.setTagName(miniAccountTag.getTagName());
            vos.add(tagVo);
        }
        return vos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void settingUserTag(UserTagDto userTagDto) {
        //用户需要设置的标签
        List<TagDto> tagDtos = new ArrayList<>(16);
        //需要去掉的用户标签
        List<TagDto> tagDtosRemove = new ArrayList<>(16);
        //删除清空标签
        if (CollectionUtil.isNotEmpty(userTagDto.getDel())) {
            List<Long> tagId = userTagDto.getDel().stream().map(TagDto::getTagId).collect(Collectors.toList());
            //尝试删除标签
            miniAccountTagGroupService.removeByTagId(tagId);
            //删除标签
            this.baseMapper.deleteBatchIds(tagId);
            miniAccountTagGroupService.removeByUserId(userTagDto.getUserIds());
        }
        //更新标签名称
        if (CollectionUtil.isNotEmpty(userTagDto.getUpdate())) {
            for (TagDto tagDto : userTagDto.getUpdate()) {
                MiniAccountTag tag = new MiniAccountTag();
                if (tagDto.getOption().equals(Boolean.TRUE)) {
                    tagDtos.add(tagDto);
                } else {
                    tagDtosRemove.add(tagDto);
                }
                if (StrUtil.isEmpty(tagDto.getTagName())) {
                    throw new ServiceException("标签名称不能为空", SystemCode.DATA_NOT_EXIST.getCode());
                }
                tag.setId(tagDto.getTagId());
                tag.setTagName(tagDto.getTagName());
                this.baseMapper.updateById(tag);
            }
        }
        //添加新标签
        if (CollectionUtil.isNotEmpty(userTagDto.getAdd())) {
            for (TagDto tagDto : userTagDto.getAdd()) {
                MiniAccountTag tag = new MiniAccountTag();
                tag.setTagName(tagDto.getTagName());
                if (StrUtil.isEmpty(tag.getTagName())) {
                    throw new ServiceException("标签名称不能为空", SystemCode.DATA_NOT_EXIST.getCode());
                }
                this.baseMapper.insert(tag);
                if (tagDto.getOption().equals(Boolean.TRUE)) {
                    tagDto.setTagId(tag.getId());
                    tagDtos.add(tagDto);
                }
            }
        }
        if (CollectionUtil.isNotEmpty(tagDtosRemove)) {
            //标签id数组
            List<Long> tagId = tagDtosRemove.stream().map(TagDto::getTagId).collect(Collectors.toList());
            //分组用户标签
            miniAccountTagGroupService.removeUserTag(userTagDto.getUserIds(), tagId);
        }

        //判断是否存有需要为用户设置某些标签
        if (CollectionUtil.isNotEmpty(tagDtos)) {
            //清空指定用户所有标签,重新添加
            miniAccountTagGroupService.removeByUserId(userTagDto.getUserIds());
            //标签id数组
            List<Long> tagId = tagDtos.stream().map(TagDto::getTagId).collect(Collectors.toList());
            //分组用户标签
            miniAccountTagGroupService.addUserTag(userTagDto.getUserIds(), tagId);
        }

    }
}
