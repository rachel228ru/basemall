package com.medusa.gruul.goods.service.manager.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.common.core.util.SystemCode;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.goods.api.constant.GoodsConstant;
import com.medusa.gruul.goods.api.entity.Product;
import com.medusa.gruul.goods.api.entity.SaleMode;
import com.medusa.gruul.goods.api.entity.ShowCategory;
import com.medusa.gruul.goods.api.model.dto.manager.SaleModeDto;
import com.medusa.gruul.goods.api.model.param.manager.SaleModeParam;
import com.medusa.gruul.goods.api.model.vo.manager.SaleModeVo;
import com.medusa.gruul.goods.mapper.manager.ProductMapper;
import com.medusa.gruul.goods.mapper.manager.SaleModeMapper;
import com.medusa.gruul.goods.mapper.manager.ShowCategoryMapper;
import com.medusa.gruul.goods.service.manager.ISaleModeService;
import com.medusa.gruul.platform.api.feign.RemoteMiniInfoService;
import com.medusa.gruul.platform.api.model.dto.ShopPackageFunctionDto;
import com.medusa.gruul.shops.api.feign.RemoteShopsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 商品自定义专区 服务实现类
 * </p>
 *
 * @author lcysike
 * @since 2020-10-26
 */
@Service
public class SaleModeServiceImpl extends ServiceImpl<SaleModeMapper, SaleMode> implements ISaleModeService {

    @Autowired
    private SaleModeMapper saleModeMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ShowCategoryMapper showCategoryMapper;


    @Resource
    private RemoteMiniInfoService remoteMiniInfoService;

    @Resource
    private RemoteShopsService remoteShopsService;

    /**
     * 查询专区列表
     *
     * @return 商品专区list对象
     */
    @Override
    public IPage<SaleModeVo> getSaleModeList(SaleModeParam saleModeParam) {
        IPage<SaleModeVo> page = new Page<>(saleModeParam.getCurrent(), saleModeParam.getSize());
        return page.setRecords(this.baseMapper.querySaleModeList(page, saleModeParam));
    }

    /**
     * 查询专区列表
     *
     * @return 商品专区list对象
     */
    @Override
    public List<SaleModeVo> getAllSaleModeList() {
        return this.baseMapper.queryAllSaleModeList();
    }

    /**
     * 商品专区新增，先判断专区名称是否已存在
     * 门店版套餐店铺最多可拥有2个销售专区
     * 企业版套餐店铺最多可拥有8个销售专区
     * 其他套餐销售专区不做限制
     *
     * @param saleModeDtos
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSaleMode(List<SaleModeDto> saleModeDtos) {
        String shopId = ShopContextHolder.getShopId();
        String tenantId = TenantContextHolder.getTenantId();
        Result<ShopPackageFunctionDto> shopPackageFunctionDtoResult = remoteMiniInfoService.getShopFunction(tenantId);
        if(BeanUtil.isEmpty(shopPackageFunctionDtoResult)){
            throw new ServiceException("获取店铺套餐信息失败！", SystemCode.DATA_EXISTED.getCode());
        }
        Integer shopLevel = shopPackageFunctionDtoResult.getData().getCommunityPackagelevel();
        Integer count = this.baseMapper.selectCount(new QueryWrapper<SaleMode>().eq("tenant_id", tenantId).eq("shop_id", shopId));
        if(CommonConstants.NUMBER_ONE.equals(shopLevel) && count + saleModeDtos.size() > CommonConstants.NUMBER_TWO){
            throw new ServiceException("门店版只能创建两个销售专区", SystemCode.DATA_ADD_FAILED.getCode());
        }
        if(CommonConstants.NUMBER_TWO.equals(shopLevel) && count + saleModeDtos.size() > CommonConstants.NUMBER_EIGHT){
            throw new ServiceException("企业版只能创建八个销售专区", SystemCode.DATA_ADD_FAILED.getCode());
        }
        //取专区最大排序值
        Integer maxSort = this.baseMapper.queryMaxSort();
        for (SaleModeDto saleModeDto : saleModeDtos) {
            //保存专区
            SaleMode saleMode = ifExistSaleModeName(saleModeDto);
            if (ObjectUtil.isEmpty(maxSort)){
                maxSort = 0;
            }
            maxSort++;
            saleMode.setSort(maxSort);
            int insert = this.baseMapper.insert(saleMode);
            if (insert == 0) {
                throw new ServiceException("新增失败！", SystemCode.DATA_ADD_FAILED.getCode());
            }
        }
    }

    /**
     * 商品专区修改
     *
     * @param saleModeDto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSaleMode(SaleModeDto saleModeDto) {
        //判断原分类是否已被删除
        SaleMode oldSaleMode = saleModeMapper.selectById(saleModeDto.getId());
        if (BeanUtil.isEmpty(oldSaleMode)) {
            throw new ServiceException("该专区不存在！", SystemCode.DATA_NOT_EXIST_CODE);
        }
        SaleMode saleModeSearch = saleModeMapper.selectOne(new QueryWrapper<SaleMode>()
                .eq("mode_name", saleModeDto.getModeName())
                .ne("id", saleModeDto.getId()));
        if (!BeanUtil.isEmpty(saleModeSearch)) {
            throw new ServiceException("专区名称已存在！", SystemCode.DATA_EXISTED.getCode());
        }
        //专区保存
        SaleMode saleMode = saleModeDto.coverSaleMode();
        int update = saleModeMapper.updateById(saleMode);
        if (update == 0) {
            throw new ServiceException("修改失败！", SystemCode.DATA_UPDATE_FAILED.getCode());
        }
        String tenantId = TenantContextHolder.getTenantId();
        String shopId = ShopContextHolder.getShopId();
        // 同步修改装修名称
        boolean flag = remoteShopsService.updateSpecialArea(tenantId, shopId, oldSaleMode.getModeName(), saleModeDto.getModeName());
        if (!flag){
            throw  new ServiceException("装修同步修改专区名称失败，请稍后再试");
        }
    }

    /**
     * 商品专区删除
     *
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSaleMode(Long id) {
        SaleMode saleMode = saleModeMapper.selectById(id);
        if (BeanUtil.isEmpty(saleMode)) {
            throw new ServiceException("该专区不存在！", SystemCode.DATA_EXISTED.getCode());
        }
        //判断专区下是否有商品
        Integer count = productMapper.selectCount(new QueryWrapper<Product>().eq("sale_mode", id));
        if(count > 0){
            throw new ServiceException("该专区下有关联商品，不能删除！", SystemCode.DATA_EXISTED.getCode());
        }
        //删除专区下的展示分类与分拣分类
        showCategoryMapper.delete(new QueryWrapper<ShowCategory>().eq("sale_mode", id));
        //删除专区
        int delete = saleModeMapper.deleteById(id);
        if (delete == 0) {
            throw new ServiceException("删除失败！", SystemCode.DATA_DELETE_FAILED.getCode());
        }
        //调用店铺接口删除装修对应的专区
        if(!remoteShopsService.delShopRenovationPage(String.valueOf(id))){
            throw new ServiceException("装修专区删除失败！", SystemCode.DATA_DELETE_FAILED.getCode());
        }
    }

    /**
     * 修改专区排序
     *
     * @param saleModeDtos
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSaleModeSort(List<SaleModeDto> saleModeDtos) {
        saleModeDtos.stream().forEach(saleModeDto -> {
            boolean update = new LambdaUpdateChainWrapper<>(saleModeMapper)
                    .eq(SaleMode::getId, saleModeDto.getId())
                    .set(SaleMode::getSort, saleModeDto.getSort())
                    .update();
            if (!update) {
                throw new ServiceException("修改失败！", SystemCode.DATA_UPDATE_FAILED.getCode());
            }
        });
    }

    /**
     * 判断查询专区名称是否已存在
     *
     * @param saleModeDto
     * @return saleMode
     */
    private SaleMode ifExistSaleModeName(SaleModeDto saleModeDto) {
        SaleMode saleMode = this.baseMapper.selectOne(new QueryWrapper<SaleMode>().eq("mode_name", saleModeDto.getModeName()));
        if (!BeanUtil.isEmpty(saleMode)) {
            throw new ServiceException("专区名称已存在！", SystemCode.DATA_EXISTED.getCode());
        }
        saleMode = saleModeDto.coverSaleMode();
        return saleMode;
    }

}
