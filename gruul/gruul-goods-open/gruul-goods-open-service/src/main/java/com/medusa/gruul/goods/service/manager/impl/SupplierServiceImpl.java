package com.medusa.gruul.goods.service.manager.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.account.api.feign.RemoteMiniAccountService;
import com.medusa.gruul.account.api.model.AccountInfoDto;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.StringUtil;
import com.medusa.gruul.common.core.util.SystemCode;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.goods.api.entity.Product;
import com.medusa.gruul.goods.api.entity.Supplier;
import com.medusa.gruul.goods.api.model.dto.manager.SupplierDto;
import com.medusa.gruul.goods.api.model.param.manager.SupplierParam;
import com.medusa.gruul.goods.api.model.vo.manager.SupplierVo;
import com.medusa.gruul.goods.mapper.manager.ProductMapper;
import com.medusa.gruul.goods.mapper.manager.SupplierMapper;
import com.medusa.gruul.goods.mq.Sender;
import com.medusa.gruul.goods.service.manager.ISupplierService;
import com.medusa.gruul.goods.web.enums.ProductStatusEnum;
import com.medusa.gruul.goods.web.enums.SupplierComFromEnum;
import com.medusa.gruul.goods.web.enums.SupplierStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 供应商 服务实现类
 *
 * @author kyl
 * @since 2019-10-06
 */
@Service
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, Supplier> implements ISupplierService {

    @Autowired
    private SupplierMapper supplierMapper;

    @Autowired
    private ProductMapper productMapper;

    @Resource
    private RemoteMiniAccountService remoteMiniAccountService;

    @Resource
    private Sender sender;

    /**
     * 获取所有供应商
     *
     * @return 供应商list对象
     */
    @Override
    public List<SupplierVo> getAllSupplierList() {
        return supplierMapper.queryAllSupplierList();
    }

    /**
     * 供应商分页列表
     *
     * @param supplierParam
     * @return 供应商分页对象
     */
    @Override
    public IPage<SupplierVo> getSupplierList(SupplierParam supplierParam) {
        IPage<SupplierVo> page = new Page<>(supplierParam.getCurrent(), supplierParam.getSize());
        return page.setRecords(supplierMapper.querySupplierList(page, supplierParam));
    }

    /**
     * 添加供应商
     *
     * @param supplierDto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSupplier(SupplierDto supplierDto) {
        //判断供应商手机号是否存在
        List<Supplier> sameSuppliers = supplierMapper.selectList(new QueryWrapper<Supplier>()
                .eq("mobile", supplierDto.getMobile()).orderByDesc("create_time"));

        ifExistSupplierMobile(sameSuppliers);
        //状态默认待审核
        if (supplierDto.getStatus() == null) {
            supplierDto.setStatus(SupplierStatusEnum.AUDITING.getStatus());
        }
        //评分默认5.0
        if (supplierDto.getScore() == null) {
            supplierDto.setScore(BigDecimal.valueOf(5.0));
        }
        //后台注册
        supplierDto.setComeFrom(SupplierComFromEnum.PC.getComeFrom());
        //供应商识别号生成
        String supplierSn;
        do {
            //生成一个16位的供货商识别号
            String date = DateUtil.format(new Date(), new SimpleDateFormat("yyyyMMdd"));
            Integer count = this.baseMapper.queryAllCount() + CommonConstants.NUMBER_ONE;
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
        int insert = supplierMapper.insert(supplier);
        if (insert == 0) {
            throw new ServiceException("新增失败！", SystemCode.DATA_ADD_FAILED_CODE);
        }
    }

    /**
     * 修改供应商
     *
     * @param supplierDto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSupplier(SupplierDto supplierDto) {
        //判断原供应商是否已被删除
        Supplier oldSupplier = supplierMapper.selectById(supplierDto.getId());
        if (BeanUtil.isEmpty(oldSupplier)) {
            throw new ServiceException("供应商已被删除！", SystemCode.DATA_NOT_EXIST_CODE);
        }
        //判断供应商手机号是否存在（排除自己）
        List<Supplier> sameSuppliers = supplierMapper.selectList(new QueryWrapper<Supplier>()
                .eq("mobile", supplierDto.getMobile())
                .ne("status", SupplierStatusEnum.CLOSED.getStatus())
                .ne("id", supplierDto.getId())
        );
        ifExistSupplierMobile(sameSuppliers);
        //更新
        Supplier supplier = supplierDto.coverSupplier();
        int update = supplierMapper.updateById(supplier);
        if (update == 0) {
            throw new ServiceException("更新失败！", SystemCode.DATA_UPDATE_FAILED_CODE);
        }
    }

    /**
     * 审核供应商 再调用微信订阅消息发送通知给客户
     *
     * @param supplierDto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkSupplier(SupplierDto supplierDto) {
        //判断原供应商是否已被删除
        Supplier oldSupplier = supplierMapper.selectById(supplierDto.getId());
        if (BeanUtil.isEmpty(oldSupplier)) {
            throw new ServiceException("供应商已被删除！", SystemCode.DATA_NOT_EXIST_CODE);
        }
        //判断供应商手机号是否存在（排除自己）
        List<Supplier> sameSuppliers = supplierMapper.selectList(new QueryWrapper<Supplier>()
                .eq("mobile", oldSupplier.getMobile())
                .ne("status", SupplierStatusEnum.CLOSED.getStatus())
                .ne("id", oldSupplier.getId())
        );
        ifExistSupplierMobile(sameSuppliers);
        //更新
        BeanUtil.copyProperties(supplierDto, oldSupplier);
        int update = supplierMapper.updateById(oldSupplier);
        if (update == 0) {
            throw new ServiceException("审核失败！", SystemCode.DATA_UPDATE_FAILED_CODE);
        }
        String templateId = oldSupplier.getTemplateId();
        if(StringUtil.isNotEmpty(templateId)){
            //审核后 调用消息订阅去发送审核消息通知
            String tenantId = TenantContextHolder.getTenantId();
            List<Integer> infos = new ArrayList<>(CommonConstants.NUMBER_ONE);
            infos.add(CommonConstants.NUMBER_FOUR);
            AccountInfoDto accountInfoDto = remoteMiniAccountService.accountInfo(oldSupplier.getUserId(), infos);
            if(BeanUtil.isEmpty(accountInfoDto)){
                throw new ServiceException("用户信息不存在！", SystemCode.DATA_EXISTED_CODE);
            }else{
                String openId = accountInfoDto.getMiniAccountOauths().getOpenId();
                sender.sendSupplierMessage(supplierDto, openId, tenantId, templateId);

            }
        }
    }

    /**
     * 删除供应商
     *
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSupplierList(Long[] ids) {
        //删除供应商下所有商品
        new LambdaUpdateChainWrapper<>(productMapper)
                .in(Product::getProviderId, Arrays.asList(ids))
                .set(Product::getStatus, ProductStatusEnum.SELL_OFF.getStatus())
                .set(Product::getProviderId, null).update();
        //删除供应商
        supplierMapper.deleteBatchIds(Arrays.asList(ids));
    }


    /**
     * 判断供应商手机号是否已存在
     *
     * @param sameSuppliers
     */
    private void ifExistSupplierMobile(List<Supplier> sameSuppliers) {
        if (CollectionUtil.isNotEmpty(sameSuppliers)) {
            Supplier supplier = sameSuppliers.get(0);
            if (!BeanUtil.isEmpty(supplier)) {
                switch (supplier.getStatus()) {
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
        }
    }

    /**
     * 根据供应商id数组查询供应商信息
     *
     * @param supplierIds
     * @return List<SupplierVo>
     */
    @Override
    public List<SupplierVo> getDataSetSupplierList(List<Long> supplierIds) {
        return supplierMapper.queryDataSetSupplierList(supplierIds);
    }

}
