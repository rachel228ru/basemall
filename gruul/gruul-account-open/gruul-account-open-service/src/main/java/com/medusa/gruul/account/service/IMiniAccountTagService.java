package com.medusa.gruul.account.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.medusa.gruul.account.api.entity.MiniAccountTag;
import com.medusa.gruul.account.model.dto.UserTagDto;
import com.medusa.gruul.account.model.vo.TagVo;

import java.util.List;

/**
 * <p>
 * 用户标签表 服务类
 * </p>
 *
 * @author whh
 * @since 2019-11-18
 */
public interface IMiniAccountTagService extends IService<MiniAccountTag> {

    /**
     * 获取指定标签数据
     *
     * @param tagIdList 标签id数组
     * @return
     */
    List<MiniAccountTag> getByIdList(List<Long> tagIdList);

    /**
     * 获取租户下所有标签
     *
     * @return com.medusa.gruul.account.model.vo.TagVo
     */
    List<TagVo> listAll();

    /**
     * 设置用户标签(添加|删除|设置)
     *
     * @param userTagDto com.medusa.gruul.account.model.dto.UserTagDto
     */
    void settingUserTag(UserTagDto userTagDto);

}
