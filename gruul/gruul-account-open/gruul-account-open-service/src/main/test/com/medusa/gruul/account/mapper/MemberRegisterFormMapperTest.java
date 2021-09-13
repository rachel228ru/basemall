package com.medusa.gruul.account.mapper;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.time.LocalDateTime;
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
public class MemberRegisterFormMapperTest {

    @Resource
    private MemberRegisterFormMapper memberRegisterFormMapper;

    @Test
    public void testSelectMemberRegisterForm() {
        String tenantId = "100002";
        List<MemberRegisterFormVo> memberRegisterFormVos = memberRegisterFormMapper.selectMemberRegisterForm(tenantId);
        Assert.assertNotNull(memberRegisterFormVos);
        System.out.println(memberRegisterFormVos);
    }

    @Test
    public void testQryFormById() {
        String tenantId = "100002";
        String formId = "1";
        MemberRegisterForm memberRegisterForm = memberRegisterFormMapper.qryFormById(formId, tenantId);
        Assert.assertNotNull(memberRegisterForm);
        System.out.println(memberRegisterForm);
    }


}
