package com.medusa.gruul.account.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.medusa.gruul.account.model.dto.*;
import com.medusa.gruul.account.model.vo.RegisterDataVo;
import com.medusa.gruul.goods.api.constant.GoodsConstant;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 会员信息表 Mapper 接口
 * </p>
 *
 * @author zhaokw
 * @since 2020-07-04
 */
@RunWith(SpringRunner.class)
@MybatisPlusTest
@ActiveProfiles("unit-test")
public class MemberInfoMapperTest {

    @Resource
    private MemberInfoMapper memberInfoMapper;

    @Test
    public void testSelectByMemberList() {
        String tenantId = "100002";
        int page = 1;
        int size = 20;
        String shopId = GoodsConstant.TEST_SHOP_ID;
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("shopId", shopId);
        IPage<MemberListDto> iPage = memberInfoMapper.selectByMemberList(new Page<MemberListDto>(page, size), paramMap);
        Assert.assertNotNull(iPage);
        System.out.println(iPage);
    }

    @Test
    public void testUpdateMemberExpireTime() {
        UpdateMemberExpireTimeDto updateMemberExpireTimeDto = new UpdateMemberExpireTimeDto();
        updateMemberExpireTimeDto.setMemberNumber("1000022007180430042")
                .setDate(1L)
                .setMemberExpireTime("2020-07-19");
        int update = memberInfoMapper.updateMemberExpireTime(updateMemberExpireTimeDto);
        Assert.assertNotNull(update);
        System.out.println(update);
    }

    @Test
    public void testUpdateApiMemberExpireTime() {
        UpdateApiMemberExpireTimeDto updateApiMemberExpireTimeDto = new UpdateApiMemberExpireTimeDto();
        updateApiMemberExpireTimeDto.setAvailableAmount(new BigDecimal(30000))
                .setMemberNumber("1000022007180430042")
                .setOpenType(2)
                .setUseAmount(new BigDecimal("118905.27"))
                .setDate(1L)
                .setMemberExpireTime("2020-07-19");
        int update = memberInfoMapper.updateApiMemberExpireTime(updateApiMemberExpireTimeDto);
        Assert.assertNotNull(update);
        System.out.println(update);
    }

    @Test
    public void testUpgradeMemberRank() {
        UpgradeMemberRankDto upgradeMemberRankDto = new UpgradeMemberRankDto();
        upgradeMemberRankDto.setMemberNumber("1000022007215654613")
                .setRankCode("2")
                .setRankName("金卡会员");
        int update = memberInfoMapper.upgradeMemberRank(upgradeMemberRankDto);
        Assert.assertNotNull(update);
        System.out.println(update);
    }

    @Test
    public void testGetExpireMemberInfo() {
        List<MemberInfo> memberInfos = memberInfoMapper.getExpireMemberInfo();
        Assert.assertNotNull(memberInfos);
        System.out.println(memberInfos);
    }

    @Test
    public void testGetMemberInfoByUserId() {
        String tenantId = "100002";
        String userId = "1248795148536647680";
        List<MemberInfo> memberInfos = memberInfoMapper.getMemberInfoByUserId(tenantId, userId);
        Assert.assertNotNull(memberInfos);
        System.out.println(memberInfos);
    }

    @Test
    public void testUpdateRegisterData() {
        RegisterDataVo registerDataVo = new RegisterDataVo();
        String tenantId = "100002";
        String userId = "1248795148536647680";
        registerDataVo.setCode("854545")
                .setPhone("13867567865");
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setTenantId(tenantId);
        memberInfo.setUserId(userId);
        int update = memberInfoMapper.updateRegisterData(memberInfo);
        Assert.assertNotNull(update);
        System.out.println(update);
    }

    @Test
    public void testUpgradeApiMemberRank() {
        UpgradeApiMemberRankDto upgradeApiMemberRankDto = new UpgradeApiMemberRankDto();
        upgradeApiMemberRankDto.setAvailableAmount(new BigDecimal(22))
                .setMemberNumber("1000022007215654613")
                .setOpenType(1)
                .setPaymentType(2)
                .setUseAmount(new BigDecimal("118825.27"))
                .setRankCode("2")
                .setRankName("金卡会员");
        int update = memberInfoMapper.upgradeApiMemberRank(upgradeApiMemberRankDto);
        Assert.assertNotNull(update);
        System.out.println(update);
    }

    @Test
    public void testGetCountByRankCode() {
        String tenantId = "100002";
        String rankCode = "1";
        int update = memberInfoMapper.getCountByRankCode(tenantId, rankCode);
        Assert.assertNotNull(update);
        System.out.println(update);
    }

    @Test
    public void testGetMemberRecord() {
        int page = 1;
        int size = 20;
        String shopId = GoodsConstant.TEST_SHOP_ID;
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("shopId", shopId);
        IPage<MemberInnerListVo> iPage = memberInfoMapper.getMemberRecord(new Page<MemberInnerListVo>(page, size), paramMap);
        Assert.assertNotNull(iPage);
        System.out.println(iPage);
    }


}
