package com.medusa.gruul.account.mapper;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 会员储值规则表 Mapper 接口
 * </p>
 *
 * @author zhaokw
 * @since 2020-07-04
 */
@RunWith(SpringRunner.class)
@MybatisPlusTest
@ActiveProfiles("unit-test")
public class MemberPayRuleMapperTest {

    @Resource
    private MemberPayRuleMapper memberPayRuleMapper;

    @Test
    public void testGetMemberPayRule() {
        String tenantId = "100002";
        List<MemberPayRule> memberPayRules = memberPayRuleMapper.getMemberPayRule(tenantId);
        Assert.assertNotNull(memberPayRules);
        System.out.println(memberPayRules);
    }

}
