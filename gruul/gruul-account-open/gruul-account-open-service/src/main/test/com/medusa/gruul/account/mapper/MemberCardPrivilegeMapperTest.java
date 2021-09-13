package com.medusa.gruul.account.mapper;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.medusa.gruul.account.model.vo.MemberPrivilegeVo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 会员卡权益表 Mapper 接口
 * </p>
 *
 * @author zhaokw
 * @since 2020-07-04
 */
@RunWith(SpringRunner.class)
@MybatisPlusTest
@ActiveProfiles("unit-test")
public class MemberCardPrivilegeMapperTest {

    @Resource
    private MemberCardPrivilegeMapper memberCardPrivilegeMapper;

    @Test
    public void testSelectMemberCardPrivilege() {
        String tenantId = "100002";
        List<MemberPrivilegeVo> memberPrivilegeVos = memberCardPrivilegeMapper.selectMemberCardPrivilege(tenantId);
        Assert.assertNotNull(memberPrivilegeVos);
        System.out.println(memberPrivilegeVos);
    }

    @Test
    public void testGetMemberCardPrivilege() {
        String tenantId = "100002";
        List<MemberCardPrivilege> memberCardPrivileges = memberCardPrivilegeMapper.getMemberCardPrivilege(tenantId);
        Assert.assertNotNull(memberCardPrivileges);
        System.out.println(memberCardPrivileges);
    }

}
