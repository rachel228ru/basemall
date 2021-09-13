package com.medusa.gruul.common.data.tenant;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.verify.TenantConfigProperties;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.StringValue;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 租户维护处理器
 * @Author: alan
 * @Date: 2019/7/13 19:32
 */
@Slf4j
public class GruulShopHandler implements TenantLineHandler {
    @Autowired
    private TenantConfigProperties properties;

    /**
     * 获取租户值
     * <p>
     * TODO 校验租户状态
     *
     * @return 租户值
     */
    @Override
    public Expression getTenantId() {
        String shopId = ShopContextHolder.getShopId();
        log.debug("当前商铺为 >> {}", shopId);

        if (shopId == null) {
            return new NullValue();
        }
        return new StringValue(shopId);
    }

    /**
     * 获取租户字段名
     *
     * @return 租户字段名
     */
    @Override
    public String getTenantIdColumn() {
        return properties.getShop_column();
    }

    /**
     * 根据表名判断是否进行过滤
     *
     * @param tableName 表名
     * @return 是否进行过滤
     */
    @Override
    public boolean ignoreTable(String tableName) {
        String shopId = ShopContextHolder.getShopId();
        // 租户中ID 为空，查询全部，不进行过滤
        if (shopId == null || shopId.equals(CommonConstants.DEFAULT_SHOP_ID) || !properties.getUse_shop()) {
            return Boolean.TRUE;
        }

        return properties.getTables().contains(tableName);
    }
}
