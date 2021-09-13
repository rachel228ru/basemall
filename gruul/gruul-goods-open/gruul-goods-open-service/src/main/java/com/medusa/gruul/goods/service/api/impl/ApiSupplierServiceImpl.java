package com.medusa.gruul.goods.service.api.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.CurUserUtil;
import com.medusa.gruul.common.core.util.SystemCode;
import com.medusa.gruul.common.dto.CurUserDto;
import com.medusa.gruul.goods.api.constant.GoodsConstant;
import com.medusa.gruul.goods.api.entity.Supplier;
import com.medusa.gruul.goods.api.model.dto.api.ApiSupplierDto;
import com.medusa.gruul.goods.mapper.api.ApiSupplierMapper;
import com.medusa.gruul.goods.mapper.manager.SupplierMapper;
import com.medusa.gruul.goods.service.api.IApiSupplierService;
import com.medusa.gruul.goods.web.enums.SupplierComFromEnum;
import com.medusa.gruul.goods.web.enums.SupplierStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

/**
 * 小程序供应商 服务实现类
 *
 * @author: KYL
 * @since: 2019/11/13
 */
@Service
public class ApiSupplierServiceImpl extends ServiceImpl<ApiSupplierMapper, Supplier> implements IApiSupplierService {

    @Autowired
    private ApiSupplierMapper supplierMapper;

    @Autowired
    private SupplierMapper managerSupplierMapper;

    /**
     * 添加供应商
     *
     * @param supplierDto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSupplier(ApiSupplierDto supplierDto) {
        //判断供应商手机号是否存在
        List<Supplier> sameSuppliers = supplierMapper.selectList(new QueryWrapper<Supplier>()
                .eq("mobile", supplierDto.getMobile()).orderByDesc("create_time"));
        if (CollectionUtil.isNotEmpty(sameSuppliers)) {
            Supplier sameSupplier = sameSuppliers.get(0);
            //验证一天只能申请一次
            Long createTime = sameSupplier.getCreateTime().toInstant(ZoneOffset.of("+8")).toEpochMilli();
            Long nowTime = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
            Long between = nowTime - createTime;
            if (between < (GoodsConstant.HOUR_OF_DAY * GoodsConstant.SECOND_OF_HOUR)) {
                throw new ServiceException("同一个号码一天只能申请一次！", SystemCode.DATA_EXISTED_CODE);
            }
            //验证供应商状态
            switch (sameSupplier.getStatus()) {
                case 1:
                    throw new ServiceException("该手机号已是供应商！", SystemCode.DATA_EXISTED_CODE);
                case 2:
                    throw new ServiceException("该手机号已在申请中！", SystemCode.DATA_EXISTED_CODE);
                case 3:
                    throw new ServiceException("该手机号已是供应商！", SystemCode.DATA_EXISTED_CODE);
                default:
                    break;
            }
        }
        //状态默认待审核
        if (supplierDto.getStatus() == null) {
            supplierDto.setStatus(SupplierStatusEnum.AUDITING.getStatus());
        }
        //评分默认5.0
        if (supplierDto.getScore() == null) {
            supplierDto.setScore(BigDecimal.valueOf(5.0));
        }
        //小程序申请
        supplierDto.setComeFrom(SupplierComFromEnum.MP.getComeFrom());
        //供应商识别号生成
        String supplierSn;
        do {
            //生成一个16位的供货商识别号
            String date = DateUtil.format(new Date(), new SimpleDateFormat("yyyyMMdd"));
            Integer count = managerSupplierMapper.queryAllCount() + CommonConstants.NUMBER_ONE;
            String newCount = new DecimalFormat("000000").format(count);
            supplierSn = "GY" + date + newCount;
            //验证改id是否已经被使用
            Supplier supplierSearch = this.baseMapper.selectOne(new QueryWrapper<Supplier>().eq("supplier_sn", supplierSn));
            if (BeanUtil.isEmpty(supplierSearch)) {
                break;
            }
        } while (true);
        supplierDto.setSupplierSn(supplierSn);
        //新增
        Supplier supplier = supplierDto.coverSupplier();
        CurUserDto curUserDto = CurUserUtil.getHttpCurUser();
        String userId = curUserDto.getUserId();
        //小程序注册加入用户id与订阅消息模版id关联
        supplier.setUserId(userId);
        supplier.setTemplateId(supplierDto.getTemplateId());
        int insert = supplierMapper.insert(supplier);
        if (insert == 0) {
            throw new ServiceException("新增失败！", SystemCode.DATA_ADD_FAILED_CODE);
        }
    }
}
