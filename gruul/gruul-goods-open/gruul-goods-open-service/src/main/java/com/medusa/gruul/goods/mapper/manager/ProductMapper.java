package com.medusa.gruul.goods.mapper.manager;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.medusa.gruul.goods.api.entity.Product;
import com.medusa.gruul.goods.api.model.param.manager.ProductParam;
import com.medusa.gruul.goods.api.model.vo.manager.ProductVo;
import com.medusa.gruul.goods.api.model.vo.manager.SkuStockVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 商品信息 Mapper 接口
 * </p>
 *
 * @author alan
 * @since 2019-09-03
 */
@Repository
public interface ProductMapper extends BaseMapper<Product> {

    /**
     * 获取商品信息list
     *
     * @param page         分页对象
     * @param productParam 商品信息查询参数
     * @return 商品list信息
     */
    List<ProductVo> queryProductList(IPage page, @Param("productParam") ProductParam productParam);

    /**
     * 获取供应商商品信息list
     *
     * @param page         分页对象
     * @param productParam 商品信息查询参数
     * @return 商品list信息
     */
    List<ProductVo> querySupplierProductList(IPage page, @Param("productParam") ProductParam productParam);


    /**
     * 获取所有商品条数 货号生成使用
     *
     * @return 商品条数
     */
    @SqlParser(filter = true)
    Integer queryAllCount();

    /**
     * 获取商品详细信息
     *
     * @param id
     * @return 单个商品详细信息
     */
    ProductVo getProductById(@Param("id") Long id);

    /**
     * 获取商品展示分类与分拣分类详细信息
     *
     * @param id
     * @return 单个商品展示分类与分拣分类详细信息
     */
    ProductVo querySaleModeChangeProduct(@Param("id") Long id);




    /**
     * 获取商品信息sku
     *
     * @param id 商品id
     * @return 商品list信息
     */
    List<SkuStockVo> querySkuStock(@Param("id") Long id);

    /**
     * 追加销量
     *
     * @param productId
     * @param number
     * @return void
     * @Author alan
     * @Date 2019/10/4 17:17
     */
    @Update({"update t_product set sale = sale + #{number} where id = #{productId}"})
    void appendSale(@Param("productId") Long productId, @Param("number") Integer number);



    /**
     * 获取默认店铺里面的默认商品数据
     *
     * @return PointManage
     */
    List<ProductVo> queryDefaultProduct();
}
