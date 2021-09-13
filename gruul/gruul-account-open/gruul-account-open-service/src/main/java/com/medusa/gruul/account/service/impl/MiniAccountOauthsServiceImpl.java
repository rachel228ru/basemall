package com.medusa.gruul.account.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.account.api.entity.MiniAccountOauths;
import com.medusa.gruul.account.api.enums.OauthTypeEnum;
import com.medusa.gruul.account.mapper.MiniAccountOauthsMapper;
import com.medusa.gruul.account.service.IMiniAccountOauthsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户第三方Id表 服务实现类
 * </p>
 *
 * @author whh
 * @since 2019-11-18
 */
@Service
public class MiniAccountOauthsServiceImpl extends ServiceImpl<MiniAccountOauthsMapper, MiniAccountOauths> implements IMiniAccountOauthsService {

    @Override
    public MiniAccountOauths getByOpenId(String openId, Integer type) {
        return this.baseMapper.selectOne(new QueryWrapper<MiniAccountOauths>().eq("open_id", openId)
                .eq("oauth_type", type));
    }

    @Override
    public MiniAccountOauths getByUserId(Integer oauthType, String userId) {
        return this.baseMapper.selectOne(new QueryWrapper<MiniAccountOauths>().eq("user_id", userId)
                .eq("oauth_type", oauthType));
    }

    @Override
    public MiniAccountOauths getByUnionIdAndMiniInfo(String unionId) {


        List<MiniAccountOauths> accountOauths = this.baseMapper.selectList(new QueryWrapper<MiniAccountOauths>().eq("union_id", unionId));
        if (CollectionUtil.isEmpty(accountOauths)) {
            return null;
        }
        // 1-微信小程序,2-公众号
        Map<Integer, MiniAccountOauths> result = accountOauths.stream().collect(Collectors.toMap(MiniAccountOauths::getOauthType, obj -> obj));
        MiniAccountOauths miniAccountOauth = result.get(OauthTypeEnum.WX_MINI.getType());
        if (miniAccountOauth != null) {
            return miniAccountOauth;
        }
        //不存在小程序端数据但存在公众号信息，则表示已经初始化过数据
        return result.get(OauthTypeEnum.WX_MP.getType());
    }

    @Override
    public MiniAccountOauths getByUnionIdAndType(String unionId, OauthTypeEnum typeEnum) {

        return this.baseMapper.selectOne(new QueryWrapper<MiniAccountOauths>().eq("union_id", unionId).eq("oauth_type", typeEnum.getType()));
    }
}
