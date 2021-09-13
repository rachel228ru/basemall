package com.medusa.gruul.goods.service.manager.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.medusa.gruul.account.api.feign.RemoteMiniAccountService;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.data.handler.IMetaObjectHandler;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.goods.api.model.dto.manager.SupplierDto;
import com.medusa.gruul.goods.api.model.param.manager.SupplierParam;
import com.medusa.gruul.goods.api.model.vo.manager.SupplierVo;
import com.medusa.gruul.goods.mq.Sender;
import com.medusa.gruul.goods.service.manager.ISupplierService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.ribbon.FeignRibbonClientAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@MybatisPlusTest
@ActiveProfiles("unit-test")
//引入需要创建的Bean
@Import({SupplierServiceImpl.class, IMetaObjectHandler.class})
@ImportAutoConfiguration({
        RedisAutoConfiguration.class,
        RabbitAutoConfiguration.class,
        RibbonAutoConfiguration.class,
        FeignRibbonClientAutoConfiguration.class,
        FeignAutoConfiguration.class})
public class SupplierServiceImplTest {

    @Resource
    private ISupplierService supplierService;

    @MockBean
    RemoteMiniAccountService remoteMiniAccountService;

    @MockBean
    Sender sender;

    @Test
    public void getAllSupplierList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        List<SupplierVo> supplierVos = supplierService.getAllSupplierList();
        Assert.assertNotNull(supplierVos);
        System.out.println(supplierVos.toString());
    }

    @Test
    public void getSupplierList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        SupplierParam supplierParam = new SupplierParam();
        IPage<SupplierVo> page = supplierService.getSupplierList(supplierParam);
        Assert.assertNotNull(page.getRecords());
        System.out.println(page.getRecords().toString());
    }

    @Test
    public void addSupplier() {
        SupplierDto supplierDto = new SupplierDto();
        supplierDto.setName("测试供应商");
        supplierDto.setMobile("13188888888");
        supplierDto.setProvince("浙江省");
        supplierDto.setCity("宁波市");
        supplierDto.setCountry("鄞州区");
        supplierDto.setAddress("测试地址");
        supplierDto.setArea("浙江省宁波市鄞州区测试地址");
        supplierDto.setProductInfo("测试产品");
        supplierDto.setComeFrom(1);
        try{
            supplierService.addSupplier(supplierDto);
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test
    public void updateSupplier() {
        SupplierDto supplierDto = new SupplierDto();
        supplierDto.setId(87L);
        supplierDto.setName("测试供应商");
        supplierDto.setMobile("13188888888");
        supplierDto.setProvince("浙江省");
        supplierDto.setCity("宁波市");
        supplierDto.setCountry("鄞州区");
        supplierDto.setAddress("测试地址");
        supplierDto.setArea("浙江省宁波市鄞州区测试地址");
        supplierDto.setProductInfo("测试产品");
        supplierDto.setComeFrom(1);
        try{
            supplierService.updateSupplier(supplierDto);
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test
    public void checkSupplier() {
        SupplierDto supplierDto = new SupplierDto();
        supplierDto.setId(87L);
        supplierDto.setName("测试供应商");
        supplierDto.setMobile("13188888888");
        supplierDto.setProvince("浙江省");
        supplierDto.setCity("宁波市");
        supplierDto.setCountry("鄞州区");
        supplierDto.setAddress("测试地址");
        supplierDto.setArea("浙江省宁波市鄞州区测试地址");
        supplierDto.setProductInfo("测试产品");
        supplierDto.setComeFrom(1);
        try{
            supplierService.checkSupplier(supplierDto);
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test
    public void deleteSupplierList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        Long[] ids = {87L, 88L};
        try{
            supplierService.deleteSupplierList(ids);
            Assert.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

    @Test
    public void getDataSetSupplierList() {
        TenantContextHolder.setTenantId("100002");
        ShopContextHolder.setShopId("100002100001");
        List<Long> supplierIds = new ArrayList<>(CommonConstants.NUMBER_TWO);
        supplierIds.add(87L);
        supplierIds.add(88L);
        List<SupplierVo> supplierVos = supplierService.getDataSetSupplierList(supplierIds);
        Assert.assertNotNull(supplierVos);
        System.out.println(supplierVos.toString());
    }
}