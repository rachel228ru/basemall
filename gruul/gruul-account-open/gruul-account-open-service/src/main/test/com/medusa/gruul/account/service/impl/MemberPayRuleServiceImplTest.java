package com.medusa.gruul.account.service.impl;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.goods.api.constant.GoodsConstant;
import com.medusa.gruul.payment.api.model.dto.PayResultDto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@MybatisPlusTest
@ActiveProfiles("unit-test")
//引入需要创建的Bean
@Import({MemberPayRuleServiceImpl.class})
public class MemberPayRuleServiceImplTest {
    @Resource
    private IMemberPayRuleService memberPayRuleService;

    @Test
    public void testGetMemberPayRule() {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        List<MemberPayRule> result = memberPayRuleService.getMemberPayRule();
        Assert.assertNotNull(result);
        System.out.println(result);
    }

    @Test
    public void testSaveMemberPayRule() {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        List<MemberPayRule> memberPayRuleList = new ArrayList<>();
        MemberPayRule memberPayRule = new MemberPayRule();
        memberPayRule.setId(2L)
                .setState(1)
                .setDiscountAmount(new BigDecimal(20))
                .setPayAmount(new BigDecimal(100));
        memberPayRuleList.add(memberPayRule);
        MemberPayRule memberPayRule1 = new MemberPayRule();
        memberPayRule1.setId(3L)
                .setState(1)
                .setDiscountAmount(new BigDecimal(50))
                .setPayAmount(new BigDecimal(200));
        memberPayRuleList.add(memberPayRule1);
        MemberPayRule memberPayRule2 = new MemberPayRule();
        memberPayRule2.setId(6L)
                .setState(1)
                .setDiscountAmount(new BigDecimal(200))
                .setPayAmount(new BigDecimal(500));
        memberPayRuleList.add(memberPayRule2);
        memberPayRuleService.saveMemberPayRule(memberPayRuleList);
    }

    @Test
    public void testMemberPay() {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        String userId = "1248795148536647680";
        MemberPayDto memberPayDto = new MemberPayDto();
        memberPayDto.setPaymentType(1)
                .setDiscountAmount(new BigDecimal("0.01"))
                .setPayAmount(new BigDecimal("0.01"))
                .setUserId(userId)
                .setTenantId(TenantContextHolder.getTenantId());
        PayResultDto result = memberPayRuleService.memberPay(memberPayDto);
        Assert.assertNotNull(result);
        System.out.println(result);
    }
}
