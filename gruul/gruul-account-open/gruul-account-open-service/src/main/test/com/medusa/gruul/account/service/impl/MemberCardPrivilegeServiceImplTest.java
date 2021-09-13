package com.medusa.gruul.account.service.impl;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.medusa.gruul.account.service.IMemberCardPrivilegeService;
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
@Import({MemberCardPrivilegeServiceImpl.class})
public class MemberCardPrivilegeServiceImplTest {
    @Resource
    private IMemberCardPrivilegeService memberCardPrivilegeService;

    @Test
    public void testGetMemberCardPrivilege() {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        List<MemberCardPrivilege> result = memberCardPrivilegeService.getMemberCardPrivilege();
        Assert.assertNotNull(result);
        System.out.println(result);
    }

    @Test
    public void testUpdateMemberCardPrivilegeDto() {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        UpdateMemberCardPrivilegeDto updateMemberCardPrivilegeDto = new UpdateMemberCardPrivilegeDto();
        updateMemberCardPrivilegeDto.setId(7L)
                .setIllustrate("申请维权时，系统自动同意维权申请，无需等待人工审核")
                .setIcon("https://medusa-small-file-1253272780.cos.ap-shanghai.myqcloud.com/gruul/20200710/798a80e89e61489c944fcfe663aba04a.png")
                .setOpen(1)
                .setAllowDel(0);
        memberCardPrivilegeService.updateMemberCardPrivilege(updateMemberCardPrivilegeDto);
    }

    @Test
    public void testUpdateMemberCardPrivilegeStatus() {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        MemberCardPrivilegeStatusDto memberCardPrivilegeStatusDto = new MemberCardPrivilegeStatusDto();
        memberCardPrivilegeStatusDto.setId(7L)
                .setOpen(1);
        memberCardPrivilegeService.updateMemberCardPrivilegeStatus(memberCardPrivilegeStatusDto);
    }

    @Test
    public void testDeleteMemberCardPrivilege() {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        DeleteMemberCardPrivilegeDto deleteMemberCardPrivilegeDto = new DeleteMemberCardPrivilegeDto();
        deleteMemberCardPrivilegeDto.setId(7L)
                .setIsDeleted(1);
        memberCardPrivilegeService.deleteMemberCardPrivilege(deleteMemberCardPrivilegeDto);
    }

    @Test
    public void testInsertMemberCardPrivilege() {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        InsertMemberCardPrivilegeDto insertMemberCardPrivilegeDto = new InsertMemberCardPrivilegeDto();
        insertMemberCardPrivilegeDto.setIllustrate("自定义权益")
                .setIcon("https://medusa-small-file-1253272780.cos.ap-shanghai.myqcloud.com/gruul/20200710/798a80e89e61489c944fcfe663aba04a.png")
                .setOpen(1)
                .setAllowDel(0)
                .setPType("0")
                .setPrivilegeName("自定义权益");
        memberCardPrivilegeService.insertMemberCardPrivilege(insertMemberCardPrivilegeDto);
    }

}
