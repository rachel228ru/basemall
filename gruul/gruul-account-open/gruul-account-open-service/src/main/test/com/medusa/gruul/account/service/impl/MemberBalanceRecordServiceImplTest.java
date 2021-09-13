package com.medusa.gruul.account.service.impl;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.goods.api.constant.GoodsConstant;
import com.medusa.gruul.order.api.model.OrderVo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@MybatisPlusTest
@ActiveProfiles("unit-test")
//引入需要创建的Bean
@Import({MemberBalanceRecordServiceImpl.class})
public class MemberBalanceRecordServiceImplTest {
    @Resource
    private IMemberBalanceRecordService memberBalanceRecordService;


    @Test
    public void testMemberBalanceModify() {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        MemberBalanceUpdateDto memberBalanceUpdateDto = new MemberBalanceUpdateDto();
        memberBalanceUpdateDto.setChangeType(0)
                .setDealType(1)
                .setDetailName("微信充值")
                .setMemberInfoId(1L)
                .setOutOrderId(1000022008011311013L)
                .setShopUserId("1248795148536647680")
                .setSource(1)
                .setUseBalance(new BigDecimal("1000.01"));
        Boolean result = memberBalanceRecordService.memberBalanceModify(memberBalanceUpdateDto);
        Assert.assertNotNull(result);
        System.out.println(result);
    }


}
