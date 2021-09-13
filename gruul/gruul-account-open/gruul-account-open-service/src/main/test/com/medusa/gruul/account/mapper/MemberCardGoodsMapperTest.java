package com.medusa.gruul.account.mapper;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * <p>
 * 会员卡商品表 Mapper 接口
 * </p>
 *
 * @author zhaokw
 * @since 2020-07-04
 */
@RunWith(SpringRunner.class)
@MybatisPlusTest
@ActiveProfiles("unit-test")
public class MemberCardGoodsMapperTest {

    @Resource
    private MemberCardGoodsMapper memberCardGoodsMapper;

    @Test
    public void testSelectMemberCardGoods() {
        String tenantId = "100002";
        Long memberCardId = 1L;
        MemberCardGoodsVo memberCardGoodsVo = memberCardGoodsMapper.selectMemberCardGoods(tenantId, memberCardId);
        Assert.assertNotNull(memberCardGoodsVo);
        System.out.println(memberCardGoodsVo);
    }

}
