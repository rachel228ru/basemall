package com.medusa.gruul.account.service.impl;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.medusa.gruul.account.model.vo.*;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.goods.api.constant.GoodsConstant;
import com.medusa.gruul.order.api.model.OrderVo;
import com.medusa.gruul.payment.api.model.dto.PayResultDto;
import com.medusa.gruul.payment.api.model.dto.WxPayNotifyResultDto;
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
@Import({MemberCardInfoServiceImpl.class})
public class MemberCardInfoServiceImplTest {
    @Resource
    private IMemberCardInfoService memberCardInfoService;

    @Test
    public void testSetMemberCardInfoStatus() {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        String tenantId = "100002";
        Integer memberCardInfoSwitch = 1;
        Integer memberModel = 0;
        MemberCardInfoStatusDto memberCardInfoStatusDto = new MemberCardInfoStatusDto();
        memberCardInfoStatusDto.setMemberModel(memberModel)
                .setMemberCardInfoSwitch(memberCardInfoSwitch)
                .setTenantId(tenantId);
        memberCardInfoService.setMemberCardInfoStatus(memberCardInfoStatusDto);
    }

    @Test
    public void testGetMemberCardInfo() {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        MemberCardInfoListVo result = memberCardInfoService.getMemberCardInfo();
        Assert.assertNotNull(result);
        System.out.println(result);
    }

    @Test
    public void testUpdateMemberCardInfoStatus() {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        List<MemberCardInfoRankStatusDto> memberCardInfoRankStatusDtoList = new ArrayList<>();
        MemberCardInfoRankStatusDto memberCardInfoRankStatusDto = new MemberCardInfoRankStatusDto();
        memberCardInfoRankStatusDto.setMemberCardInfoSwitch(1)
                .setRankCode("1");
        memberCardInfoRankStatusDtoList.add(memberCardInfoRankStatusDto);
        memberCardInfoService.updateMemberCardInfoStatus(memberCardInfoRankStatusDtoList);
    }

    @Test
    public void testSaveMemberCardInfo() {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        MemberCardInfoListVo memberCardInfoListVo = new MemberCardInfoListVo();
        List<MemberPrivilegeVo> memberPrivilegeVos = new ArrayList<>();
        MemberPrivilegeVo memberPrivilegeVo = new MemberPrivilegeVo();
        memberPrivilegeVo.setId(7L)
                .setPrivilegeName("急速售后")
                .setIsSelected(1)
                .setIcon("https://medusa-small-file-1253272780.cos.ap-shanghai.myqcloud.com/gruul/20200710/798a80e89e61489c944fcfe663aba04a.png")
                .setIllustrate("申请维权时，系统自动同意维权申请，无需等待人工审核")
                .setPType("6");
        memberPrivilegeVos.add(memberPrivilegeVo);
        memberPrivilegeVo.setId(5L)
                .setPrivilegeName("物流优惠")
                .setIsSelected(0)
                .setIcon("https://medusa-small-file-1253272780.cos.ap-shanghai.myqcloud.com/gruul/20200710/23535120aa0c41a784219313e0eb5c5d.png")
                .setIllustrate("物流配送方式订单享受包邮免配送费")
                .setPType("2");
        memberPrivilegeVos.add(memberPrivilegeVo);
        memberPrivilegeVo.setId(4L)
                .setPrivilegeName("积分加倍")
                .setIsSelected(0)
                .setIcon("https://medusa-small-file-1253272780.cos.ap-shanghai.myqcloud.com/gruul/20200710/0e60a7f6f029421d9289f7a4c85ec549.png")
                .setIllustrate("购买商品获得的积分加倍")
                .setPType("3");
        memberPrivilegeVos.add(memberPrivilegeVo);
        memberPrivilegeVo.setId(1L)
                .setPrivilegeName("商品折扣")
                .setIsSelected(1)
                .setIcon("https://medusa-small-file-1253272780.cos.ap-shanghai.myqcloud.com/gruul/20200613/fe61bc2880474a58a3ad368fa06372c0.png")
                .setIllustrate("商品折扣")
                .setPType("1")
                .setValue(new BigDecimal(12));
        memberPrivilegeVos.add(memberPrivilegeVo);
        List<IntegralVo> integralVos = new ArrayList<>();
        IntegralVo integralVo = new IntegralVo();
        integralVo.setId(1L)
                .setRankCode("1")
                .setRankName("普通会员")
                .setIsOpen(1)
                .setMemberPrivilegeVos(memberPrivilegeVos);
        integralVos.add(integralVo);
        List<MemberPrivilegeVo> memberPrivilegeVos2 = new ArrayList<>();
        MemberPrivilegeVo memberPrivilegeVo2 = new MemberPrivilegeVo();
        memberPrivilegeVo2.setId(7L)
                .setPrivilegeName("急速售后")
                .setIsSelected(1)
                .setIcon("https://medusa-small-file-1253272780.cos.ap-shanghai.myqcloud.com/gruul/20200710/798a80e89e61489c944fcfe663aba04a.png")
                .setIllustrate("申请维权时，系统自动同意维权申请，无需等待人工审核")
                .setPType("6");
        memberPrivilegeVos2.add(memberPrivilegeVo2);
        memberPrivilegeVo2.setId(5L)
                .setPrivilegeName("物流优惠")
                .setIsSelected(0)
                .setIcon("https://medusa-small-file-1253272780.cos.ap-shanghai.myqcloud.com/gruul/20200710/23535120aa0c41a784219313e0eb5c5d.png")
                .setIllustrate("物流配送方式订单享受包邮免配送费")
                .setPType("2");
        memberPrivilegeVos2.add(memberPrivilegeVo2);
        memberPrivilegeVo2.setId(4L)
                .setPrivilegeName("积分加倍")
                .setIsSelected(0)
                .setIcon("https://medusa-small-file-1253272780.cos.ap-shanghai.myqcloud.com/gruul/20200710/0e60a7f6f029421d9289f7a4c85ec549.png")
                .setIllustrate("购买商品获得的积分加倍")
                .setPType("3");
        memberPrivilegeVos2.add(memberPrivilegeVo2);
        memberPrivilegeVo2.setId(1L)
                .setPrivilegeName("商品折扣")
                .setIsSelected(1)
                .setIcon("https://medusa-small-file-1253272780.cos.ap-shanghai.myqcloud.com/gruul/20200613/fe61bc2880474a58a3ad368fa06372c0.png")
                .setIllustrate("商品折扣")
                .setPType("1")
                .setValue(new BigDecimal(12));
        memberPrivilegeVos2.add(memberPrivilegeVo2);
        integralVo.setId(2L)
                .setRankCode("2")
                .setRankName("金卡会员")
                .setIsOpen(1)
                .setMemberPrivilegeVos(memberPrivilegeVos2);
        integralVos.add(integralVo);
        List<MemberPrivilegeVo> memberPrivilegeVos3 = new ArrayList<>();
        MemberPrivilegeVo memberPrivilegeVo3 = new MemberPrivilegeVo();
        memberPrivilegeVo3.setId(7L)
                .setPrivilegeName("急速售后")
                .setIsSelected(1)
                .setIcon("https://medusa-small-file-1253272780.cos.ap-shanghai.myqcloud.com/gruul/20200710/798a80e89e61489c944fcfe663aba04a.png")
                .setIllustrate("申请维权时，系统自动同意维权申请，无需等待人工审核")
                .setPType("6");
        memberPrivilegeVos3.add(memberPrivilegeVo3);
        memberPrivilegeVo3.setId(5L)
                .setPrivilegeName("物流优惠")
                .setIsSelected(0)
                .setIcon("https://medusa-small-file-1253272780.cos.ap-shanghai.myqcloud.com/gruul/20200710/23535120aa0c41a784219313e0eb5c5d.png")
                .setIllustrate("物流配送方式订单享受包邮免配送费")
                .setPType("2");
        memberPrivilegeVos3.add(memberPrivilegeVo3);
        memberPrivilegeVo3.setId(4L)
                .setPrivilegeName("积分加倍")
                .setIsSelected(0)
                .setIcon("https://medusa-small-file-1253272780.cos.ap-shanghai.myqcloud.com/gruul/20200710/0e60a7f6f029421d9289f7a4c85ec549.png")
                .setIllustrate("购买商品获得的积分加倍")
                .setPType("3");
        memberPrivilegeVos3.add(memberPrivilegeVo3);
        memberPrivilegeVo3.setId(1L)
                .setPrivilegeName("商品折扣")
                .setIsSelected(1)
                .setIcon("https://medusa-small-file-1253272780.cos.ap-shanghai.myqcloud.com/gruul/20200613/fe61bc2880474a58a3ad368fa06372c0.png")
                .setIllustrate("商品折扣")
                .setPType("1")
                .setValue(new BigDecimal(12));
        memberPrivilegeVos3.add(memberPrivilegeVo3);
        integralVo.setId(3L)
                .setRankCode("3")
                .setRankName("砖石会员")
                .setIsOpen(1)
                .setMemberPrivilegeVos(memberPrivilegeVos3);
        integralVos.add(integralVo);
        memberCardInfoService.saveMemberCardInfo(memberCardInfoListVo);
    }

    @Test
    public void testConfirmPayment() {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        ConfirmPaymentVo confirmPaymentVo = new ConfirmPaymentVo();
        confirmPaymentVo.setAvailableAmount(new BigDecimal(12))
                .setMemberCardInfoId(42L)
                .setOpenType(1)
                .setPaymentType(1)
                .setUseAmount(new BigDecimal(0))
                .setUseInDate(360L);
        PayResultDto payResultDto = memberCardInfoService.confirmPayment(confirmPaymentVo);
        Assert.assertNotNull(payResultDto);
        System.out.println(payResultDto);
    }

    @Test
    public void testMemberCompleted() {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        WxPayNotifyResultDto wxPayNotifyResultDto = new WxPayNotifyResultDto();
        memberCardInfoService.memberCompleted(wxPayNotifyResultDto);
    }

    @Test
    public void testUpdateRegisterData() {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        RegisterDataVo registerDataVo = new RegisterDataVo();
        registerDataVo.setCode("854545")
                .setPhone("13867567865");
        MemberCardInfoResVo memberCardInfoResVo = memberCardInfoService.updateRegisterData(registerDataVo);
        Assert.assertNotNull(memberCardInfoResVo);
        System.out.println(memberCardInfoResVo);
    }

    @Test
    public void testMemberCenter() {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        MemberCenterVo memberCenterVo = memberCardInfoService.memberCenter();
        Assert.assertNotNull(memberCenterVo);
        System.out.println(memberCenterVo);
    }

    @Test
    public void testGetMemberCardRecord() {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        List<MemberCardRecordVo> memberCardRecordVos = memberCardInfoService.getMemberCardRecord();
        Assert.assertNotNull(memberCardRecordVos);
        System.out.println(memberCardRecordVos);
    }

    @Test
    public void testGetMemberCardInfoSwitch() {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        List<MemberSwitch> memberSwitchList = memberCardInfoService.getMemberCardInfoSwitch();
        Assert.assertNotNull(memberSwitchList);
        System.out.println(memberSwitchList);
    }

    @Test
    public void testGetRegisterForm() {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        List<MemberRegisterFormVo> memberRegisterFormVos = memberCardInfoService.getRegisterForm();
        Assert.assertNotNull(memberRegisterFormVos);
        System.out.println(memberRegisterFormVos);
    }

    @Test
    public void testGetMemberCardInfoList() {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        MemberCardInfoListDto memberCardInfoListDto = new MemberCardInfoListDto();
        memberCardInfoListDto.setTenantId(TenantContextHolder.getTenantId())
                .setOpenType(1);
        List<MemberCardInfoListResultDto> memberCardInfoListResultDtos = memberCardInfoService.getMemberCardInfoList(memberCardInfoListDto);
        Assert.assertNotNull(memberCardInfoListResultDtos);
        System.out.println(memberCardInfoListResultDtos);
    }


    @Test
    public void testMemberSwitch() {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        Integer flag = memberCardInfoService.memberSwitch(TenantContextHolder.getTenantId());
        Assert.assertNotNull(flag);
        System.out.println(flag);
    }



    @Test
    public void testMemberInit() {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        String jsonData = "{\"tenantId\":\"100002\"}";
        memberCardInfoService.memberInit(jsonData);
    }

}
