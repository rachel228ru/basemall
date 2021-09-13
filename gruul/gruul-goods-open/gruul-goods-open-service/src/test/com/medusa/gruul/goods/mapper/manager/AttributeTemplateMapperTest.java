package com.medusa.gruul.goods.mapper.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.goods.api.model.param.manager.AttributeTemplateParam;
import com.medusa.gruul.goods.api.model.vo.manager.AttributeTemplateSecondVo;
import com.medusa.gruul.goods.api.model.vo.manager.AttributeTemplateVo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.List;

@RunWith(SpringRunner.class)
@MybatisPlusTest
@ActiveProfiles("unit-test")
public class AttributeTemplateMapperTest {

    @Resource
    private AttributeTemplateMapper attributeTemplateMapper;

    @Test
    public void queryAllAttributeTemplateList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        List<AttributeTemplateVo> attributeTemplateVos = attributeTemplateMapper.queryAllAttributeTemplateList();
        Assert.assertNotNull(attributeTemplateVos);
        System.out.println(attributeTemplateVos.toString());
    }

    @Test
    public void queryByParentId() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Long id = 541L;
        List<AttributeTemplateSecondVo> attributeTemplateSecondVos = attributeTemplateMapper.queryByParentId(id);
        Assert.assertNotNull(attributeTemplateSecondVos);
        System.out.println(attributeTemplateSecondVos.toString());
    }

    @Test
    public void queryByPrimaryKey() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Long id = 541L;
        AttributeTemplateVo attributeTemplateVo = attributeTemplateMapper.queryByPrimaryKey(id);
        Assert.assertNotNull(attributeTemplateVo);
        System.out.println(attributeTemplateVo.toString());
    }

    @Test
    public void queryAttributeTemplateList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        AttributeTemplateParam attributeTemplateParam = new AttributeTemplateParam();
        IPage<AttributeTemplateVo> page = new Page<>(attributeTemplateParam.getCurrent(), attributeTemplateParam.getSize());
        List<AttributeTemplateVo> attributeTemplateVos = attributeTemplateMapper.queryAttributeTemplateList(page, attributeTemplateParam);
        Assert.assertNotNull(attributeTemplateVos);
        System.out.println(attributeTemplateVos.toString());
    }

    @Test
    public void queryDefaultAttributeTemplateList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        AttributeTemplateVo attributeTemplateVo = attributeTemplateMapper.queryDefaultAttributeTemplateList();
        Assert.assertNotNull(attributeTemplateVo);
        System.out.println(attributeTemplateVo.toString());
    }
}