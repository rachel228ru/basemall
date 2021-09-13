package com.medusa.gruul.goods.mq;

import com.medusa.gruul.goods.api.model.dto.api.ApiShoppingCartDto;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 生成购物车消息体
 * </p>
 *
 * @author lcysike
 * @since 2019-11-19
 */
@Data
public class ShoppingCartMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private ApiShoppingCartDto apiShoppingCartDto;

    private String tenantId;

    private String shopId;

    private String userId;

    private Long skuId;

}
