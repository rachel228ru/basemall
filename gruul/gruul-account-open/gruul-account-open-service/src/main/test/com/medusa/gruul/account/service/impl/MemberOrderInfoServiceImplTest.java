package com.medusa.gruul.account.service.impl;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
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
@Import({MemberOrderInfoServiceImpl.class})
public class MemberOrderInfoServiceImplTest {
    @Resource
    private IMemberOrderInfoService memberOrderInfoService;

    @Test
    public void testGetMemberOrderInfo() {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        PageUtils<List<MemberOrderInfoResVo>> iPage = memberOrderInfoService.getMemberOrderInfo(null, null, 1, 20, null);
        Assert.assertNotNull(iPage);
        System.out.println(iPage);
    }

    @Test
    public void testUpdateNote() {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        UpdateOrderNoteVo updateOrderNoteVo = new UpdateOrderNoteVo();
        updateOrderNoteVo.setId(2204925L)
                .setNote("123333")
                .setState(0);
        memberOrderInfoService.updateNote(updateOrderNoteVo);
    }

}
