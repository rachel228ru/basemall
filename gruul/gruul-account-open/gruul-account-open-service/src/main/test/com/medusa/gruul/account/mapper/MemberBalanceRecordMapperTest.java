package com.medusa.gruul.account.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 账户余额明细表 Mapper 接口
 * </p>
 *
 * @author zhaokw
 * @since 2020-07-04
 */
@RunWith(SpringRunner.class)
@MybatisPlusTest
@ActiveProfiles("unit-test")
public class MemberBalanceRecordMapperTest {

    @Resource
    private MemberBalanceRecordMapper memberBalanceRecordMapper;

    @Test
    public void testGetMemberBalanceRecord() {
        Integer page = 1;
        Integer size = 20;
        Map<String, Object> paramMap = new HashMap<>();
        IPage<MemberBalanceRecordVo> memberBalanceRecords = memberBalanceRecordMapper.getMemberBalanceRecord(new Page<MemberBalanceRecordVo>(page, size), paramMap);
        Assert.assertNotNull(memberBalanceRecords);
        System.out.println(memberBalanceRecords);
    }

}
