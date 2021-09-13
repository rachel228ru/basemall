package com.medusa.gruul.account.web.controller.manager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.medusa.gruul.account.model.vo.MemberPrivilegeVo;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.goods.api.constant.GoodsConstant;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @Description MemberCardInfoControllerTest 单元测试类
 * @Author zhaokw
 * @Date 10:05 2020\8\26 0023
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberCardInfoControllerTest {
    protected MockMvc mockMvc;
    @Autowired
    private WebApplicationContext applicationContext;

    @Before
    public void before() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.applicationContext).build();
    }

    @After
    public void after() {
    }

    /**
     * Method: getMemberCardInfoSwitch
     */
    @Test
    public void getMemberCardInfoSwitch() throws Exception {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        MvcResult result = mockMvc.perform(get("/manager/getMemberCardInfoSwitch"))
                .andExpect(status().isOk())// 模拟向testRest发送get请求
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                .andReturn();// 返回执行请求的结果
        System.out.println(result.getResponse().getContentAsString());
    }

    /**
     * Method: setMemberCardInfoStatus
     */
    @Test
    public void setMemberCardInfoStatus() throws Exception {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        MemberCardInfoStatusDto memberCardInfoStatusDto = new MemberCardInfoStatusDto();
        memberCardInfoStatusDto.setMemberCardInfoSwitch(1)
                .setMemberModel(0);
        String obj = JSONObject.toJSONString(memberCardInfoStatusDto);
        MvcResult result = mockMvc.perform(post("/manager/setMemberCardInfoStatus").content(obj))
                .andExpect(status().isOk())// 模拟向testRest发送post请求
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                .andReturn();// 返回执行请求的结果
        System.out.println(result.getResponse().getContentAsString());
    }

    /**
     * Method: updateMemberCardInfoStatus
     */
    @Test
    public void updateMemberCardInfoStatus() throws Exception {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        List<MemberCardInfoRankStatusDto> memberCardInfoRankStatusDtoList = new ArrayList<>();
        MemberCardInfoRankStatusDto memberCardInfoRankStatusDto = new MemberCardInfoRankStatusDto();
        memberCardInfoRankStatusDto.setMemberCardInfoSwitch(1)
                .setRankCode("1");
        memberCardInfoRankStatusDtoList.add(memberCardInfoRankStatusDto);
        String obj = JSONArray.toJSONString(memberCardInfoRankStatusDtoList);
        MvcResult result = mockMvc.perform(get("/manager/updateMemberCardInfoStatus").content(obj))
                .andExpect(status().isOk())// 模拟向testRest发送get请求
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                .andReturn();// 返回执行请求的结果
        System.out.println(result.getResponse().getContentAsString());
    }

    /**
     * Method: getMemberCardInfo
     */
    @Test
    public void getMemberCardInfo() throws Exception {
        ShopContextHolder.setShopId(GoodsConstant.TEST_SHOP_ID);
        TenantContextHolder.setTenantId(GoodsConstant.TEST_TENANT_ID);
        MvcResult result = mockMvc.perform(get("/manager/getMemberCardInfo"))
                .andExpect(status().isOk())// 模拟向testRest发送post请求
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                .andReturn();// 返回执行请求的结果
        System.out.println(result.getResponse().getContentAsString());
    }

    /**
     * Method: saveMemberCardInfo
     */
    @Test
    public void saveMemberCardInfo() throws Exception {
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
        memberCardInfoListVo.setUseInDate(365L)
                .setTitle("积分兑换")
                .setOpenType(2);
        String obj = JSONObject.toJSONString(memberCardInfoListVo);
        MvcResult result = mockMvc.perform(get("/manager/saveMemberCardInfo").content(obj))
                .andExpect(status().isOk())// 模拟向testRest发送get请求
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                .andReturn();// 返回执行请求的结果
        System.out.println(result.getResponse().getContentAsString());
    }


} 
