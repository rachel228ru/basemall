package com.medusa.gruul.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.medusa.gruul.account.api.entity.MiniAccountFootMark;
import com.medusa.gruul.account.model.vo.AccountFootMarkParam;
import com.medusa.gruul.account.model.vo.AccountFootMarkVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xiaoq
 *
 * @data 2020/2/27 17:24
 */
public interface MiniAccountFootMarkMapper extends BaseMapper<MiniAccountFootMark> {
    /**
     * 查看用户今日是否浏览过该商品
     *
     * @param productId 商品id
     * @param userId 用户id
     * @return  MiniAccountFootMark
     */
    MiniAccountFootMark selectByProductId(@Param("productId") String productId, @Param("userId") String userId);

    /**
     * 删除用户足迹
     * @param userId 用户id
     * @return int
     */
    int deleteByUserId(@Param("userId") String userId);

    /**
     * 获取用户足迹数量
     *
     * @param userId 用户id
     * @return
     */
    int selectAccountFootMarkCount(@Param("userId") String userId);


    /**
     * 获取用户足迹信息 根据条件
     *
     * @param iPage                分页对象
     * @param accountFootMarkParam 条件数据
     * @return 用户足迹信息
     */
    List<AccountFootMarkVo> selectAccountFootMarkList(
            IPage iPage, @Param("accountFootMarkParam") AccountFootMarkParam accountFootMarkParam);

    /**
     * 修改用戶足跡信息
     * @param userId
     * @param delCount
     * @return
     */
    int updateBeyondFootMark(@Param("userId") String userId, @Param("delCount") int delCount);
}
