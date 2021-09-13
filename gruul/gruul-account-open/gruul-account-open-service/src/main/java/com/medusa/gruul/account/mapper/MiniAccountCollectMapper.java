package com.medusa.gruul.account.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medusa.gruul.account.api.entity.MiniAccountCollect;
import com.medusa.gruul.account.model.dto.UserCollectDto;
import com.medusa.gruul.account.model.vo.UserCollectVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xiaoq
 * <p>
 * 用户收藏表 Mapper 接口
 * <p/>
 *
 * @data 2020/2/22 13:40
 */
public interface MiniAccountCollectMapper extends BaseMapper<MiniAccountCollect> {


    /**
     * 根据用户条件获取 用户收藏信息
     *
     * @param userId 用戶id
     * @return 用户收藏信息
     */
    @SqlParser(filter = true)
    List<UserCollectVo> getUserCollectInfoById(String userId);

    /**
     * 查看用户是否收藏该商品
     *
     * @param productId 商品id
     * @param userId    用户id
     * @return UserCollectDto
     */
    UserCollectDto findAccountCollectByProductId(@Param("productId") Long productId, @Param("userId") String userId);
}
