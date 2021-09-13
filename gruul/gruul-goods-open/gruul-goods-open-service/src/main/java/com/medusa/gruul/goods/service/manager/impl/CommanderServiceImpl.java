package com.medusa.gruul.goods.service.manager.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.common.core.util.ResultList;
import com.medusa.gruul.common.core.util.StringUtil;
import com.medusa.gruul.common.core.util.SystemCode;
import com.medusa.gruul.common.data.tenant.ShopContextHolder;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.goods.api.constant.GoodsConstant;
import com.medusa.gruul.goods.api.constant.GoodsProductRedisKey;
import com.medusa.gruul.goods.api.constant.GoodsSkuStockRedisKey;
import com.medusa.gruul.goods.api.entity.*;
import com.medusa.gruul.goods.api.model.dto.manager.*;
import com.medusa.gruul.goods.api.model.param.manager.DiscountProductParam;
import com.medusa.gruul.goods.api.model.param.manager.ProductParam;
import com.medusa.gruul.goods.api.model.vo.api.ApiShoppingCartProductVo;
import com.medusa.gruul.goods.api.model.vo.manager.*;
import com.medusa.gruul.goods.mapper.manager.*;
import com.medusa.gruul.goods.service.manager.ICommanderService;
import com.medusa.gruul.goods.service.manager.IProductService;
import com.medusa.gruul.goods.util.CsvFileUtil;
import com.medusa.gruul.goods.web.enums.ProductStatusEnum;
import com.medusa.gruul.goods.web.enums.SaleModeEnum;
import com.medusa.gruul.order.api.feign.RemoteOrderService;
import com.medusa.gruul.order.api.model.ProductRateVo;
import com.medusa.gruul.oss.api.feign.RemoteSysOssService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * <p>
 * 商品信息 服务实现类
 * </p>
 *
 * @author alan
 * @since 2019-09-03
 */
@Service
public class CommanderServiceImpl  implements ICommanderService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CsvProductMapper csvProductMapper;

    @Autowired
    private ProductAttributeMapper productAttributeMapper;

    @Autowired
    private AttributeTemplateMapper attributeTemplateMapper;



    @Autowired
    private SaleModeMapper saleModeMapper;

    @Autowired
    private ShowCategoryMapper showCategoryMapper;

    @Autowired
    private SkuStockMapper skuStockMapper;

    @Autowired
    private ProductShowCategoryMapper productShowCategoryMapper;

    @Autowired
    private DiscountProductMapper discountProductMapper;


    @Resource
    private RemoteOrderService remoteOrderService;


    @Resource
    private RemoteSysOssService remoteSysOssService;




}
