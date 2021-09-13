package com.medusa.gruul.account.service.impl;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.medusa.gruul.account.api.model.MemberCardInfoCenterDto;
import com.medusa.gruul.account.api.model.MemberTopCardInfoDto;
import com.medusa.gruul.account.model.dto.SetMemberDto;
import com.medusa.gruul.account.model.dto.UpgradeMemberRankDto;
import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.goods.api.constant.GoodsConstant;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@MybatisPlusTest
@ActiveProfiles("unit-test")
//引入需要创建的Bean
@Import({MemberInfoServiceImpl.class})
public class MemberInfoServiceImplTest {
    @Resource
    private IMemberInfoService memberInfoService;

    @Test
    public void testMemberList() {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        PageUtils<List<MemberListVo>> result = memberInfoService.memberList(null, null, null, null, null, null, null, null, null, 1, 20, 1);
        Assert.assertNotNull(result);
        System.out.println(result);
    }

    @Test
    public void testSetMember() {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        SetMemberDto setMemberDto = new SetMemberDto();
        setMemberDto.setUserId("1")
                .setCode("176472")
                .setPhone("12354546746")
                .setRankCode("1");
        memberInfoService.setMember(setMemberDto);
    }

    @Test
    public void testGetMemberCardRank() {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        List<MemberCardRankListVo> memberCardRankListVos = memberInfoService.getMemberCardRank();
        Assert.assertNotNull(memberCardRankListVos);
        System.out.println(memberCardRankListVos);
    }

    @Test
    public void testUpdateMemberExpireTime() {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        UpdateMemberExpireTimeDto updateMemberExpireTimeDto = new UpdateMemberExpireTimeDto();
        updateMemberExpireTimeDto.setMemberNumber("1000022007180430042")
                .setDate(1L)
                .setMemberExpireTime("2020-07-19");
        memberInfoService.updateMemberExpireTime(updateMemberExpireTimeDto);
    }

    @Test
    public void testUpgradeMemberRank() {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        UpgradeMemberRankDto upgradeMemberRankDto = new UpgradeMemberRankDto();
        upgradeMemberRankDto.setMemberNumber("1000022007215654613")
                .setRankCode("2")
                .setRankName("金卡会员");
        memberInfoService.upgradeMemberRank(upgradeMemberRankDto);
    }

    @Test
    public void testGetMemberInfo() {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        String userId = "1248795148536647680";
        MemberCardInfoCenterDto memberCardInfoCenterDto = memberInfoService.getMemberInfo(TenantContextHolder.getTenantId(), userId);
        Assert.assertNotNull(memberCardInfoCenterDto);
        System.out.println(memberCardInfoCenterDto);
    }

    @Test
    public void testGetTopMemberCardInfo() {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        MemberTopCardInfoDto memberTopCardInfoDto = memberInfoService.getTopMemberCardInfo(TenantContextHolder.getTenantId());
        Assert.assertNotNull(memberTopCardInfoDto);
        System.out.println(memberTopCardInfoDto);
    }

    @Test
    public void testGetMemberCardUpgrade() {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        String memberId = "1";
        MemberCardUpgradeVo memberCardUpgradeVo = memberInfoService.getMemberCardUpgrade(memberId);
        Assert.assertNotNull(memberCardUpgradeVo);
        System.out.println(memberCardUpgradeVo);
    }

    @Test
    public void testGetMemberRecord() {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        String userId = "1248795148536647680";
        PageUtils<List<MemberInnerListVo>> iPage = memberInfoService.getMemberRecord(1, 20, userId, null, null, null, null, null);
        Assert.assertNotNull(iPage);
        System.out.println(iPage);
    }

}
