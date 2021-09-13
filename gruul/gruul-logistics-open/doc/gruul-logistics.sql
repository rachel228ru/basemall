
------------------------------logistics------------------------------
CREATE TABLE `t_logistics_address` (
                                       `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键 id',
                                       `tenant_id` varchar(30) DEFAULT NULL COMMENT '租户 id',
                                       `shop_id` varchar(64) DEFAULT NULL COMMENT '本店店铺id',
                                       `name` varchar(64) DEFAULT NULL COMMENT '发货人-名称',
                                       `province` varchar(64) DEFAULT '' COMMENT '省',
                                       `province_id` varchar(20) DEFAULT '' COMMENT '省id',
                                       `city` varchar(64) DEFAULT '' COMMENT '市',
                                       `city_id` varchar(20) DEFAULT NULL COMMENT '市id',
                                       `country` varchar(64) DEFAULT NULL COMMENT '县',
                                       `country_id` varchar(20) DEFAULT NULL COMMENT '县id',
                                       `address` varchar(512) NOT NULL COMMENT '发货地址',
                                       `zip_code` varchar(64) NOT NULL COMMENT '邮编号:',
                                       `phone` varchar(45) NOT NULL,
                                       `def_send` int DEFAULT '0' COMMENT '是否为默认发货地址: 0=不是,1=是',
                                       `def_receive` int DEFAULT '0' COMMENT '是否为默认退货地址: 0=不是,1=是',
                                       `is_deleted` int NOT NULL DEFAULT '0' COMMENT '是否删除: 0=未删除,1=已删除',
                                       `create_time` datetime NOT NULL COMMENT '创建时间',
                                       `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                       `lat` varchar(64) DEFAULT '' COMMENT '纬度',
                                       `lng` varchar(64) DEFAULT '' COMMENT '经度',
                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=117 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='物流发货地址';

CREATE TABLE `t_logistics_company` (
                                       `id` bigint NOT NULL AUTO_INCREMENT,
                                       `name` varchar(20) NOT NULL COMMENT '快递公司名称',
                                       `code` varchar(32) NOT NULL COMMENT '快递公司代码',
                                       `is_deleted` int DEFAULT '0' COMMENT '是否删除,0=正常;1=删除',
                                       `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                       `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                       `tenant_id` varchar(32) DEFAULT NULL,
                                       `shop_id` varchar(64) DEFAULT NULL,
                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='快递公司代码表';

CREATE TABLE `t_logistics_deliver` (
                                       `id` bigint NOT NULL COMMENT '主键 id',
                                       `tenant_id` varchar(30) NOT NULL COMMENT '租户 id',
                                       `shop_id` varchar(64) NOT NULL COMMENT '本店店铺id',
                                       `name` varchar(128) NOT NULL COMMENT '物流发货单名称',
                                       `code` bigint NOT NULL COMMENT '物流编号',
                                       `type` int NOT NULL COMMENT '发货单状态	: 1=代发货,2=待提货,3=已完成,4=已关闭',
                                       `is_deleted` int NOT NULL DEFAULT '0' COMMENT '是否删除 0=正常,1=已删除',
                                       `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                       `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `t_logistics_deliver_order_relation` (
                                                      `id` bigint NOT NULL COMMENT '主键id',
                                                      `tenant_id` varchar(32) NOT NULL COMMENT '租户id',
                                                      `shop_id` varchar(64) NOT NULL COMMENT '本店店铺id',
                                                      `logistics_id` bigint NOT NULL COMMENT '物流发货单id',
                                                      `order_id` bigint NOT NULL COMMENT '订单id',
                                                      `good_obj` varchar(1024) NOT NULL COMMENT '订单包含商品信息_json格式[{"id":12,"code":"2312"}]',
                                                      `logistics_name` varchar(64) NOT NULL DEFAULT '' COMMENT '物流公司名称',
                                                      `logistics_code` varchar(255) NOT NULL DEFAULT '' COMMENT '物流公司单号',
                                                      `is_send` int DEFAULT NULL COMMENT '是否发货,0=未发货,1=已发货',
                                                      `type` int DEFAULT NULL COMMENT '物流单,状态:1=代发货',
                                                      `create_time` datetime DEFAULT NULL,
                                                      `modify_time` datetime DEFAULT NULL,
                                                      PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='物流发货单关联 订单表';

CREATE TABLE `t_logistics_express` (
                                       `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键 id',
                                       `tenant_id` varchar(32) NOT NULL COMMENT '租户 id',
                                       `shop_id` varchar(64) NOT NULL COMMENT '本店店铺id',
                                       `name` varchar(32) NOT NULL COMMENT '快递公司名称',
                                       `code` varchar(16) NOT NULL COMMENT '快递公司code',
                                       `phone` varchar(16) NOT NULL COMMENT '快递公司客服电话',
                                       `address_id` bigint NOT NULL COMMENT '地址id 关联物流发货地址表id',
                                       `customer_name` varchar(64) NOT NULL COMMENT '客户id',
                                       `customer_password` varchar(64) NOT NULL COMMENT '客户密匙',
                                       `link_name` varchar(16) NOT NULL COMMENT '负责人',
                                       `link_tel` varchar(16) NOT NULL COMMENT '负责人电话',
                                       `status` int NOT NULL COMMENT '状态 默认为1（审核通过）',
                                       `is_deleted` int NOT NULL DEFAULT '0' COMMENT '是否删除: 0=未删除,1=已删除',
                                       `create_time` datetime NOT NULL COMMENT '创建时间',
                                       `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='物流公司发货地址设置';

CREATE TABLE `t_logistics_express_print` (
                                             `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键 id',
                                             `tenant_id` varchar(32) NOT NULL COMMENT '租户 id',
                                             `shop_id` varchar(64) NOT NULL COMMENT '本店店铺id',
                                             `device_type` varchar(10) NOT NULL COMMENT '设备类型',
                                             `device_model` varchar(64) NOT NULL COMMENT '设备型号',
                                             `device_name` varchar(64) NOT NULL COMMENT '设备名称',
                                             `device_no` varchar(64) NOT NULL COMMENT '打印机机身号',
                                             `device_key` varchar(64) NOT NULL COMMENT '打印机KEY',
                                             `status` int NOT NULL COMMENT '状态',
                                             `is_deleted` int NOT NULL DEFAULT '0' COMMENT '是否删除: 0=未删除,1=已删除',
                                             `create_time` datetime NOT NULL COMMENT '创建时间',
                                             `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                             PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='电子面单打印机设置';

CREATE TABLE `t_logistics_include_postage` (
                                               `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键 id',
                                               `logistics_id` bigint NOT NULL COMMENT 't_logistics_template 主键 id',
                                               `type` int NOT NULL COMMENT '包邮条件类型: 0= 按件数包邮,1=按重量包邮,2=按金额包邮,3=件数+金额 4=重量+金额 包邮',
                                               `region` json NOT NULL COMMENT '运送地区 json 格式 {"provinceid": ["cityId","cityId2"]}',
                                               `piece_num` int DEFAULT NULL COMMENT '包邮件数',
                                               `weight` decimal(18,2) DEFAULT NULL COMMENT '包邮重量 单位: 千克(kg)',
                                               `amount_num` decimal(18,2) DEFAULT NULL COMMENT '包邮金额',
                                               `is_deleted` int NOT NULL DEFAULT '0' COMMENT '是否删除 0=未删除,1=已删除',
                                               `create_time` datetime NOT NULL COMMENT '创建时间',
                                               `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                                               `tenant_id` varchar(32) DEFAULT NULL,
                                               `shop_id` varchar(64) DEFAULT NULL,
                                               PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='运费模板_包邮设置表';


CREATE TABLE `t_logistics_manage` (
                                      `id` bigint NOT NULL COMMENT '主键 id',
                                      `tenant_id` varchar(30) NOT NULL COMMENT '租户 id',
                                      `shop_id` varchar(64) NOT NULL COMMENT '本店店铺id',
                                      `state` int NOT NULL DEFAULT '0' COMMENT '是否开启物流配送;0=不开启,1=开启',
                                      `deliver_build_time` varchar(128) NOT NULL COMMENT '生成订单时间: HH:mm  格式: ["HH:mm","HH:mm","HH:mm","HH:mm"] 最多四个',
                                      `is_deleted` int NOT NULL DEFAULT '0' COMMENT '是否删除 0=未删除,1=已删除',
                                      `create_time` datetime NOT NULL COMMENT '创建时间',
                                      `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                                      PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='物流基础设置';

CREATE TABLE `t_logistics_shipping_model` (
                                              `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键 id',
                                              `logistics_id` bigint NOT NULL COMMENT '同 t_logistics_template 主键 id 一致.',
                                              `valuation_model` int NOT NULL COMMENT '计价方式: 1=按件数,2=按重量',
                                              `first_piece` int DEFAULT NULL COMMENT '首件数',
                                              `first_weight` decimal(18,2) DEFAULT NULL COMMENT '首重 单位: 千克(kg)',
                                              `first_amount` decimal(18,2) DEFAULT NULL,
                                              `second_piece` int DEFAULT NULL COMMENT '续件数量',
                                              `second_weight` decimal(18,2) DEFAULT NULL COMMENT '续重 单位 千克/kg',
                                              `second_amount` decimal(18,2) DEFAULT NULL COMMENT '续费, 单位: 元',
                                              `region_json` json NOT NULL COMMENT '运送地区 json 格式 {"provinceid": ["cityId","cityId2"]}',
                                              `is_deleted` int NOT NULL DEFAULT '0' COMMENT '是否删除 0=正常,1=已删除',
                                              `create_time` datetime NOT NULL COMMENT '创建时间',
                                              `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                              `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '租户id',
                                              `shop_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '商铺id',
                                              PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=409 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='物流基础运送方式表';

CREATE TABLE `t_logistics_shop` (
                                    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键 id',
                                    `tenant_id` varchar(30) NOT NULL COMMENT '租户 id',
                                    `shop_id` varchar(64) NOT NULL COMMENT '本店店铺id',
                                    `logistics_company_id` bigint NOT NULL COMMENT '物流公司表id',
                                    `is_default` int NOT NULL DEFAULT '0' COMMENT '是否删除 0=未删除,1=已删除',
                                    `is_deleted` int NOT NULL DEFAULT '0' COMMENT '是否删除 0=未删除,1=已删除',
                                    `create_time` datetime NOT NULL COMMENT '创建时间',
                                    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='店铺物流公司绑定表';


CREATE TABLE `t_logistics_template` (
                                        `id` bigint NOT NULL AUTO_INCREMENT,
                                        `tenant_id` varchar(30) DEFAULT NULL COMMENT '租户 id',
                                        `shop_id` varchar(64) DEFAULT NULL COMMENT '本店店铺id',
                                        `name` varchar(45) DEFAULT NULL COMMENT '模板名称',
                                        `is_incl_postage` int NOT NULL DEFAULT '0' COMMENT '是否包邮: 0=不包邮,1=包邮',
                                        `valuation_model` int NOT NULL COMMENT '计价方式: 1=按件数,2=按重量.',
                                        `choice_condition_postage` int NOT NULL DEFAULT '0' COMMENT '是否指定条件包邮: 0=不包邮,1=指定条件包邮',
                                        `is_deleted` int DEFAULT '0' COMMENT '是否删除,0=正常;1=删除',
                                        `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                        `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                        PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=199 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='运费模板_基础设置表';
