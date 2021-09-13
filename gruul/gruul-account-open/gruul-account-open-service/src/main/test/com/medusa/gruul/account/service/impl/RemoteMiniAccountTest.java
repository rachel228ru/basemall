package com.medusa.gruul.account.service.impl;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.medusa.gruul.account.api.model.MemberCardInfoCenterDto;
import com.medusa.gruul.account.api.model.MemberTopCardInfoDto;
import com.medusa.gruul.account.web.remote.RemoteMiniAccount;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.goods.api.constant.GoodsConstant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@MybatisPlusTest
@ActiveProfiles("unit-test")
//引入需要创建的Bean
@Import({MemberInfoServiceImpl.class,
        MemberBalanceRecordServiceImpl.class,
        MemberCardInfoServiceImpl.class
})
public class RemoteMiniAccountTest {
    @MockBean
    RemoteMiniAccount remoteMiniAccount;

    @Resource
    private IMemberInfoService memberInfoService;
    @Resource
    private IMemberBalanceRecordService memberBalanceRecordService;
    @Resource
    private IMemberCardInfoService memberCardInfoService;

    @Test
    public void testGetMemberInfo() {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        String userId = "1248795148536647680";
        MemberCardInfoCenterDto memberCardInfoCenterDto = new MemberCardInfoCenterDto();
        Mockito.when(remoteMiniAccount.getMemberInfo(TenantContextHolder.getTenantId(), userId)).thenReturn(memberCardInfoCenterDto);
        memberInfoService.getMemberInfo(TenantContextHolder.getTenantId(), userId);
    }

    @Test
    public void testGetTopMemberCardInfo() {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        String userId = "1248795148536647680";
        MemberTopCardInfoDto memberTopCardInfoDto = new MemberTopCardInfoDto();
        Mockito.when(remoteMiniAccount.getTopMemberCardInfo(TenantContextHolder.getTenantId())).thenReturn(memberTopCardInfoDto);
        memberInfoService.getTopMemberCardInfo(TenantContextHolder.getTenantId());
    }

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
        Boolean result = true;
        Mockito.when(remoteMiniAccount.memberBalanceModify(memberBalanceUpdateDto)).thenReturn(result);
        memberBalanceRecordService.memberBalanceModify(memberBalanceUpdateDto);
    }

    @Test
    public void testMemberSwitch() {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        Integer result = 1;
        Mockito.when(remoteMiniAccount.memberSwitch(TenantContextHolder.getTenantId())).thenReturn(result);
        memberCardInfoService.memberSwitch(TenantContextHolder.getTenantId());
    }

}
