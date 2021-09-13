
------------------------------goods-api------------------------------
CREATE TABLE `t_product` (
                             `id` bigint NOT NULL AUTO_INCREMENT,
                             `tenant_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '租户ID',
                             `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                             `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                             `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '删除状态：0->未删除；1->已删除',
                             `shop_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '本店店铺id',
                             `provider_id` bigint DEFAULT NULL COMMENT '供应商id',
                             `attribute_id` bigint DEFAULT NULL COMMENT '属性模版id',
                             `attribute_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '属性模版名称',
                             `freight_template_id` bigint DEFAULT NULL COMMENT '运费模板id',
                             `sale_mode` bigint unsigned DEFAULT NULL COMMENT '销售专区(默认生成商超系统)',
                             `limit_type` tinyint unsigned DEFAULT NULL COMMENT '限购类型(默认统一限购，0--统一限购），1--多规格统一限购，2--多规格)',
                             `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品名称',
                             `pic` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '展示图片',
                             `wide_pic` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '宽屏展示图片',
                             `album_pics` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '画册图片，连产品图片限制为6张，以逗号分割',
                             `video_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '视频url',
                             `product_sn` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '货号',
                             `status` tinyint unsigned DEFAULT NULL COMMENT '状态(默认上架，0--下架（仓库中），1--上架，2--已售完)',
                             `place` tinyint unsigned DEFAULT NULL COMMENT '商品位置(默认线上，0--线上，1--素材库)',
                             `csv_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '电商平台链接',
                             `sort` int DEFAULT '0' COMMENT '排序',
                             `sale` int DEFAULT '0' COMMENT '销量',
                             `unit` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '单位',
                             `weight` decimal(10,2) DEFAULT '0.00' COMMENT '商品重量，默认为克',
                             `service_ids` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '以逗号分割的产品服务：无忧退货、快速退款、免费包邮',
                             `detail` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '商品详情',
                             `is_open_specs` tinyint unsigned DEFAULT NULL COMMENT '规格是否展开',
                             `attribute` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '销售属性',
                             `sale_describe` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '卖点描述',
                             `score` decimal(3,1) DEFAULT '0.0' COMMENT '评分',
                             `distribution_mode` tinyint(1) DEFAULT '0' COMMENT '配送方式(0--商家配送，1--快递配送)',
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='商品信息';

CREATE TABLE `t_shopping_cart` (
                                   `id` bigint NOT NULL AUTO_INCREMENT,
                                   `tenant_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '租户ID',
                                   `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                   `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                   `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '删除状态：0->未删除；1->已删除',
                                   `shop_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '本店店铺id',
                                   `product_id` bigint DEFAULT NULL COMMENT '产品id',
                                   `user_id` varchar(28) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户id',
                                   `sku_id` bigint NOT NULL COMMENT 'sku_id',
                                   `goods_number` int NOT NULL DEFAULT '0' COMMENT '商品数量',
                                   `activity_id` bigint DEFAULT NULL COMMENT '活动id',
                                   `activity_start_time` datetime DEFAULT NULL COMMENT '活动开始时间',
                                   `activity_end_time` datetime DEFAULT NULL COMMENT '活动结束时间',
                                   PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=457 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='商品购物车';


CREATE TABLE `t_sku_stock` (
                               `id` bigint NOT NULL AUTO_INCREMENT,
                               `tenant_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '租户ID',
                               `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                               `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                               `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '删除状态：0->未删除；1->已删除',
                               `shop_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '本店店铺id',
                               `version` int DEFAULT NULL COMMENT '版本号',
                               `product_id` bigint DEFAULT NULL COMMENT '产品id',
                               `sku_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'sku编码',
                               `specs` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '商品规格',
                               `weight` decimal(10,2) DEFAULT '0.00' COMMENT 'sku重量',
                               `pic` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '展示图片',
                               `price` decimal(10,2) DEFAULT '0.00' COMMENT '实售价',
                               `original_price` decimal(10,2) DEFAULT '0.00' COMMENT '指导价（划线价）',
                               `stock` int DEFAULT '0' COMMENT '库存',
                               `low_stock` int DEFAULT '0' COMMENT '预警库存',
                               `sale` int DEFAULT '0' COMMENT '销量',
                               `per_limit` int DEFAULT '0' COMMENT '限购数量',
                               `gift_integration` int DEFAULT '0' COMMENT '赠送的积分',
                               PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='sku库存';


------------------------------goods-manager------------------------------

CREATE TABLE `t_attribute_template` (
                                        `id` bigint NOT NULL AUTO_INCREMENT,
                                        `tenant_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '租户ID',
                                        `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                        `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                        `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '删除状态：0->未删除；1->已删除',
                                        `shop_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '本店店铺id',
                                        `parent_id` bigint DEFAULT NULL COMMENT '上级分类的编号：0表示一级分类',
                                        `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '模板名称',
                                        `content` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '内容',
                                        PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='属性模板';


CREATE TABLE `t_product_attribute` (
                                       `id` bigint NOT NULL AUTO_INCREMENT,
                                       `tenant_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '租户ID',
                                       `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                       `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                       `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '删除状态：0->未删除；1->已删除',
                                       `shop_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '本店店铺id',
                                       `product_id` bigint DEFAULT NULL COMMENT '产品id',
                                       `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '属性名称',
                                       `value` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '属性值',
                                       PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=119 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='商品属性';

CREATE TABLE `t_product_show_category` (
                                           `id` bigint NOT NULL AUTO_INCREMENT,
                                           `tenant_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '租户ID',
                                           `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                           `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                           `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '删除状态：0->未删除；1->已删除',
                                           `shop_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '本店店铺id',
                                           `product_id` bigint NOT NULL COMMENT '产品id',
                                           `parent_id` bigint NOT NULL COMMENT '上级分类的编号：0表示一级分类',
                                           `show_category_id` bigint DEFAULT NULL COMMENT '商品展示分类id',
                                           PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=153 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='商品展示分类';

CREATE TABLE `t_sale_mode` (
                               `id` bigint NOT NULL AUTO_INCREMENT,
                               `tenant_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '租户ID',
                               `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                               `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                               `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '删除状态：0->未删除；1->已删除',
                               `shop_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '本店店铺id',
                               `mode_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '专区名称',
                               `mode_type` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '专区类型',
                               `sort` tinyint DEFAULT NULL COMMENT '专区排序(按大小升序排序)',
                               PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='自定义专区';

CREATE TABLE `t_show_category` (
                                   `id` bigint NOT NULL AUTO_INCREMENT,
                                   `tenant_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '租户ID',
                                   `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                   `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                   `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '删除状态：0->未删除；1->已删除',
                                   `parent_id` bigint DEFAULT NULL COMMENT '上级分类的编号：0表示一级分类',
                                   `shop_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '本店店铺id',
                                   `sale_mode` bigint DEFAULT NULL COMMENT '销售专区',
                                   `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '分类名称',
                                   `level` int DEFAULT NULL COMMENT '分类级别：0->1级；1->2级',
                                   `sort` int DEFAULT NULL COMMENT '分类排序',
                                   PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='商品展示分类';


CREATE TABLE `t_supplier` (
                              `id` bigint NOT NULL AUTO_INCREMENT,
                              `tenant_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '租户ID',
                              `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                              `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                              `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '删除状态：0->未删除；1->已删除',
                              `shop_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '本店店铺id',
                              `user_id` varchar(28) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户id',
                              `supplier_sn` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '供应商识别号',
                              `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '供应商名称',
                              `mobile` bigint DEFAULT NULL COMMENT '手机号码',
                              `province` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '省',
                              `city` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '市',
                              `country` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '县',
                              `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '地区',
                              `area` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '完整地址',
                              `product_info` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '产品信息',
                              `status` int DEFAULT NULL COMMENT '状态(默认待审核，0--已关闭，1--已审核，2--待审核，3--禁用中)',
                              `come_from` int DEFAULT NULL COMMENT '注册来源(0--后台注册，1--小程序)',
                              `score` decimal(3,1) DEFAULT '0.0' COMMENT '评分',
                              `template_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '订阅消息模版id',
                              PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='供应商';