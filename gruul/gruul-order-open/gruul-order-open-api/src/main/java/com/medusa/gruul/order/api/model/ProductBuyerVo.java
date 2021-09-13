package com.medusa.gruul.order.api.model;

import lombok.Data;

import java.util.List;

@Data
public class ProductBuyerVo {

    private String productId;
    private List<BuyerVo> buyerList;
}
