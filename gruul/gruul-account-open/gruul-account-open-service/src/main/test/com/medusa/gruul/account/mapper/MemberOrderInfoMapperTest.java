package com.medusa.gruul.account.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 会员储值信息表 Mapper 接口
 * </p>
 *
 * @author zhaokw
 * @since 2020-07-04
 */
@RunWith(SpringRunner.class)
@MybatisPlusTest
@ActiveProfiles("unit-test")
public class MemberOrderInfoMapperTest {

    @Resource
    private MemberOrderInfoMapper memberOrderInfoMapper;

    @Test
    public void testSelectMemberOrderList() {
        Integer page = 1;
        Integer size = 20;
        Map<String, Object> paramMap = new HashMap<>();
        IPage<MemberOrderInfoResVo> iPage = memberOrderInfoMapper.selectMemberOrderList(new Page<MemberOrderInfoResVo>(page, size), paramMap);
        Assert.assertNotNull(iPage);
        System.out.println(iPage);
    }

}
