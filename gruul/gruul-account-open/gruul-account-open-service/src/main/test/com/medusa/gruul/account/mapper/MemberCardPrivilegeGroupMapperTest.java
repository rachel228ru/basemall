package com.medusa.gruul.account.mapper;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.medusa.gruul.account.api.entity.MemberCardPrivilegeGroup;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 会员卡与会员卡权益关联表 Mapper 接口
 * </p>
 *
 * @author zhaokw
 * @since 2020-07-04
 */
@RunWith(SpringRunner.class)
@MybatisPlusTest
@ActiveProfiles("unit-test")
public class MemberCardPrivilegeGroupMapperTest {

    @Resource
    private MemberCardPrivilegeGroupMapper memberCardPrivilegeGroupMapper;

    @Test
    public void testSelectMemberCardPrivilegeGroup() {
        String tenantId = "100002";
        Long memberCardId = 1L;
        Long privilegeId = 1L;
        MemberCardPrivilegeGroup memberCardPrivilegeGroup = memberCardPrivilegeGroupMapper.selectMemberCardPrivilegeGroup(tenantId, memberCardId, privilegeId);
        Assert.assertNotNull(memberCardPrivilegeGroup);
        System.out.println(memberCardPrivilegeGroup);
    }

    @Test
    public void testGetByMemberCardId() {
        String tenantId = "100002";
        Long memberCardId = 1L;
        List<MemberCardPrivilegeGroup> memberCardPrivilegeGroups = memberCardPrivilegeGroupMapper.getByMemberCardId(tenantId, memberCardId);
        Assert.assertNotNull(memberCardPrivilegeGroups);
        System.out.println(memberCardPrivilegeGroups);
    }

}
