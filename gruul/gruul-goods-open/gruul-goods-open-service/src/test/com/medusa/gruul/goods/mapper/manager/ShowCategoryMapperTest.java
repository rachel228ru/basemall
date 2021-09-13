package com.medusa.gruul.goods.mapper.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.goods.api.model.param.manager.ShowCategoryParam;
import com.medusa.gruul.goods.api.model.vo.manager.ShowCategoryVo;
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
public class ShowCategoryMapperTest {

    @Resource
    private ShowCategoryMapper showCategoryMapper;

    @Test
    public void queryShowCategoryList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        ShowCategoryParam showCategoryParam = new ShowCategoryParam();
        IPage<ShowCategoryVo> page = new Page<>(showCategoryParam.getCurrent(), showCategoryParam.getSize());
        List<ShowCategoryVo> showCategoryVos = showCategoryMapper.queryShowCategoryList(page, showCategoryParam);
        Assert.assertNotNull(showCategoryVos);
        System.out.println(showCategoryVos.toString());
    }

    @Test
    public void queryAllShowCategoryList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        ShowCategoryParam showCategoryParam = new ShowCategoryParam();
        List<ShowCategoryVo> showCategoryVos = showCategoryMapper.queryAllShowCategoryList(showCategoryParam);
        Assert.assertNotNull(showCategoryVos);
        System.out.println(showCategoryVos.toString());
    }

    @Test
    public void getShowCategoryById() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        String id = "572";
        ShowCategoryVo showCategoryVo = showCategoryMapper.getShowCategoryById(id);
        Assert.assertNotNull(showCategoryVo);
        System.out.println(showCategoryVo.toString());
    }

    @Test
    public void queryDefaultShowCategory() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        ShowCategoryVo showCategoryVo = showCategoryMapper.queryDefaultShowCategory();
        Assert.assertNotNull(showCategoryVo);
        System.out.println(showCategoryVo.toString());
    }
}