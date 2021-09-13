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
import java.util.List;

@RunWith(SpringRunner.class)
@MybatisPlusTest
@ActiveProfiles("unit-test")
//引入需要创建的Bean
@Import({ApiMemberInfoServiceImpl.class})
public class ApiMemberInfoServiceImplTest {
    @Resource
    private IApiMemberInfoService apiMemberInfoService;

    @Test
    public void testGetMemberInfo() {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        List<ApiMemberCardInfoVo> result = apiMemberInfoService.getMemberInfo();
        Assert.assertNotNull(result);
        System.out.println(result);
    }

    @Test
    public void testUpdateMemberExpireTime() {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        String userId = "1248795148536647680";
        UpdateApiMemberExpireTimeDto updateApiMemberExpireTimeDto = new UpdateApiMemberExpireTimeDto();
        updateApiMemberExpireTimeDto.setAvailableAmount(new BigDecimal(30000))
                .setMemberNumber("1000022007180430042")
                .setOpenType(2)
                .setUseAmount(new BigDecimal("118905.27"))
                .setDate(1L)
                .setMemberExpireTime("2020-07-19")
                .setUserId(userId)
                .setTenantId(TenantContextHolder.getTenantId());
        PayResultDto payResultDto = apiMemberInfoService.updateMemberExpireTime(updateApiMemberExpireTimeDto);
        Assert.assertNotNull(payResultDto);
        System.out.println(payResultDto);
    }

    @Test
    public void testGetMemberCardUpgrade() {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        MemberCardUpgradeVo memberCardUpgradeVo = apiMemberInfoService.getMemberCardUpgrade("199");
        Assert.assertNotNull(memberCardUpgradeVo);
        System.out.println(memberCardUpgradeVo);
    }

    @Test
    public void testUpgradeMemberRank() {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        UpgradeApiMemberRankDto upgradeApiMemberRankDto = new UpgradeApiMemberRankDto();
        upgradeApiMemberRankDto.setAvailableAmount(new BigDecimal(22))
                .setMemberNumber("1000022007215654613")
                .setOpenType(1)
                .setPaymentType(2)
                .setUseAmount(new BigDecimal("118825.27"))
                .setRankCode("2")
                .setRankName("金卡会员");
        PayResultDto payResultDto = apiMemberInfoService.upgradeMemberRank(upgradeApiMemberRankDto);
        Assert.assertNotNull(payResultDto);
        System.out.println(payResultDto);
    }

}
