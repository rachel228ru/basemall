package com.medusa.gruul.account.mapper;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.medusa.gruul.account.model.dto.SetMemberDto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 会员卡信息表 Mapper 接口
 * </p>
 *
 * @author zhaokw
 * @since 2020-07-04
 */
@RunWith(SpringRunner.class)
@MybatisPlusTest
@ActiveProfiles("unit-test")
public class MemberCardInfoMapperTest {

    @Resource
    private MemberCardInfoMapper memberCardInfoMapper;

    @Test
    public void testSelectMemCardInfo() {
        SetMemberDto setMemberDto = new SetMemberDto();
        setMemberDto.setUserId("1")
                .setCode("176472")
                .setPhone("12354546746")
                .setRankCode("1");
        MemberCardInfo memberCardInfo = memberCardInfoMapper.selectMemCardInfo(setMemberDto);
        Assert.assertNotNull(memberCardInfo);
        System.out.println(memberCardInfo);
    }

    @Test
    public void testGetMemberCardRank() {
        String tenantId = "100002";
        List<MemberCardInfo> memberCardInfos = memberCardInfoMapper.getMemberCardRank(tenantId);
        Assert.assertNotNull(memberCardInfos);
        System.out.println(memberCardInfos);
    }

    @Test
    public void testGetMemberCardInfoByTenantId() {
        String tenantId = "100002";
        List<MemberCardInfo> memberCardInfos = memberCardInfoMapper.getMemberCardInfoByTenantId(tenantId);
        Assert.assertNotNull(memberCardInfos);
        System.out.println(memberCardInfos);
    }

    @Test
    public void testUpdateMemberCardInfoStatus() {
        String tenantId = "100002";
        MemberCardInfoRankStatusVo memberCardInfoRankStatusVo = new MemberCardInfoRankStatusVo();
        memberCardInfoRankStatusVo.setMemberCardInfoSwitch(1)
                .setRankCode("1")
                .setTenantId(tenantId);
        int update = memberCardInfoMapper.updateMemberCardInfoStatus(memberCardInfoRankStatusVo);
        Assert.assertNotNull(update);
        System.out.println(update);
    }


    @Test
    public void testGetMemberCardInfoList() {
        String tenantId = "100002";
        MemberCardInfoListDto memberCardInfoListDto = new MemberCardInfoListDto();
        memberCardInfoListDto.setTenantId(tenantId)
                .setOpenType(1);
        List<MemberCardInfo> memberCardInfoList = memberCardInfoMapper.getMemberCardInfoList(memberCardInfoListDto);
        Assert.assertNotNull(memberCardInfoList);
        System.out.println(memberCardInfoList);
    }

    @Test
    public void testGetApiMemberCardInfo() {
        MemberCardInfo memberCardInfo = memberCardInfoMapper.getApiMemberCardInfo(1L);
        Assert.assertNotNull(memberCardInfo);
        System.out.println(memberCardInfo);
    }


}
