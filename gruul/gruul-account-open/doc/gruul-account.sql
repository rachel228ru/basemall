DROP TABLE IF EXISTS `t_mini_account`;
CREATE TABLE `t_mini_account` (
                                  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id ',
                                  `user_id` varchar(28) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户id',
                                  `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '租户id',
                                  `country` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所在国家',
                                  `province` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所在省份',
                                  `city` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所在城市',
                                  `language` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所用的语言 en 英文 - zh_CN 简体中文 -zh_TW 繁体中文 \r',
                                  `nike_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户名称',
                                  `avatar_url` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '头像url',
                                  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '手机号码',
                                  `email` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邮箱',
                                  `gender` tinyint(1) DEFAULT '0' COMMENT '性别 0：未知、1：男、2：女',
                                  `first_login_time` datetime DEFAULT NULL COMMENT '首次登陆小程序时间',
                                  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                  `is_deleted` tinyint(1) DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除',
                                  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                  `whether_authorization` tinyint(1) DEFAULT NULL COMMENT '是否授权过小程序 false 未授权  turn授权过',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  KEY `user_id` (`user_id`) USING BTREE,
                                  KEY `tenant_id` (`tenant_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='小程序用户表';

-- ----------------------------
-- Table structure for t_mini_account_address
-- ----------------------------
DROP TABLE IF EXISTS `t_mini_account_address`;
CREATE TABLE `t_mini_account_address` (
                                          `id` bigint NOT NULL AUTO_INCREMENT,
                                          `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                          `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '租户id',
                                          `is_deleted` tinyint DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除',
                                          `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                          `user_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '收货人姓名',
                                          `phone` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '收货人联系电话',
                                          `post_code` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '邮编',
                                          `is_default` tinyint DEFAULT NULL COMMENT '是否默认 0-非默认 1-默认',
                                          `province` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT '0' COMMENT '省',
                                          `city` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT '0' COMMENT '市',
                                          `county` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT '0' COMMENT '区',
                                          `detail_Info` varchar(120) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT '' COMMENT '详细收货地址信息',
                                          `user_id` varchar(28) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户id',
                                          `location` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '经度,维度',
                                          PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='会员地址表';

-- ----------------------------
-- Table structure for t_mini_account_collect
-- ----------------------------
DROP TABLE IF EXISTS `t_mini_account_collect`;
CREATE TABLE `t_mini_account_collect` (
                                          `collect_id` bigint NOT NULL AUTO_INCREMENT COMMENT '收藏表id',
                                          `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户id',
                                          `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '租户id',
                                          `shop_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '商铺id区分城市合伙人',
                                          `product_id` bigint DEFAULT NULL COMMENT '商品id',
                                          `product_pic` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '商品主图',
                                          `product_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '商品名称',
                                          `status` int DEFAULT NULL COMMENT '商品状态  0-上架 1-下架 2-售罄',
                                          `product_price` decimal(10,2) DEFAULT NULL COMMENT '商品实际售价',
                                          `original_price` decimal(10,2) DEFAULT NULL COMMENT '指导价划线价',
                                          `ramark` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
                                          `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                          `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                          `is_deleted` tinyint(1) DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除',
                                          PRIMARY KEY (`collect_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户收藏表';

-- ----------------------------
-- Table structure for t_mini_account_extends
-- ----------------------------
DROP TABLE IF EXISTS `t_mini_account_extends`;
CREATE TABLE `t_mini_account_extends` (
                                          `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id ',
                                          `user_id` varchar(28) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户id',
                                          `last_deal_time` datetime DEFAULT NULL COMMENT '最后交易时间',
                                          `is_blacklist` tinyint DEFAULT NULL COMMENT '是否黑名单用户 0-否 1-是',
                                          `consume_num` int DEFAULT NULL COMMENT '消费次数',
                                          `community_id` bigint DEFAULT '0' COMMENT '所在小区id',
                                          `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
                                          `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                          `is_deleted` tinyint DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除',
                                          `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                          `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '租户id',
                                          `consume_totle_money` decimal(10,2) DEFAULT NULL COMMENT '交易总额',
                                          `join_blacklist_time` datetime DEFAULT NULL COMMENT '加入黑名单时间',
                                          `community_type` tinyint(1) DEFAULT '0' COMMENT '用户团长身份类型  1-团长  2-区域团长',
                                          `shop_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '商铺id(区分城市合伙人)',
                                          `shop_user_id` varchar(28) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '店铺用户id',
                                          `current_status` tinyint(1) DEFAULT '0' COMMENT '当前使用  0未使用 1-当前使用',
                                          `last_choose_lcation` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '经度,维度',
                                          `supply_bonus` decimal(10,2) unsigned zerofill DEFAULT '00000000.00' COMMENT '用户余额',
                                          `freeze_bonus` decimal(10,2) unsigned zerofill DEFAULT '00000000.00' COMMENT '冻结余额',
                                          `rebate_bonus` decimal(10,2) unsigned zerofill DEFAULT '00000000.00' COMMENT '返利金额',
                                          PRIMARY KEY (`id`) USING BTREE,
                                          KEY `user_id` (`user_id`) USING BTREE,
                                          KEY `community_id` (`community_id`) USING BTREE,
                                          KEY `shop_id` (`shop_id`) USING BTREE,
                                          KEY `tenant_id` (`tenant_id`) USING BTREE,
                                          KEY `shop_user_id` (`shop_user_id`) USING BTREE,
                                          KEY `is_deleted` (`is_deleted`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户信息扩展表';

-- ----------------------------
-- Table structure for t_mini_account_foot_mark
-- ----------------------------
DROP TABLE IF EXISTS `t_mini_account_foot_mark`;
CREATE TABLE `t_mini_account_foot_mark` (
                                            `footmark_id` bigint NOT NULL AUTO_INCREMENT COMMENT '足迹id',
                                            `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户id',
                                            `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '租户id',
                                            `shop_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '商铺id区分城市合伙人',
                                            `product_id` bigint DEFAULT NULL COMMENT '商品id',
                                            `product_pic` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '商品主图',
                                            `product_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '商品名称',
                                            `status` int DEFAULT NULL COMMENT '商品状态  0-上架 1-下架 2-售罄',
                                            `product_price` decimal(10,2) DEFAULT NULL COMMENT '商品实际售价',
                                            `original_price` decimal(10,2) DEFAULT NULL COMMENT '指导价划线价',
                                            `ramark` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
                                            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                            `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                            `is_deleted` tinyint(1) DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除',
                                            PRIMARY KEY (`footmark_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=333 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for t_mini_account_oauths
-- ----------------------------
DROP TABLE IF EXISTS `t_mini_account_oauths`;
CREATE TABLE `t_mini_account_oauths` (
                                         `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id ',
                                         `user_id` varchar(28) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户唯一id',
                                         `oauth_type` tinyint DEFAULT NULL COMMENT '授权类型 1-微信小程序,2-公众号',
                                         `open_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '第三方openId',
                                         `union_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '第三方unionid',
                                         `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '租户id',
                                         `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                         `is_deleted` tinyint DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除',
                                         `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                         PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户第三方Id表';

-- ----------------------------
-- Table structure for t_mini_account_restrict
-- ----------------------------
DROP TABLE IF EXISTS `t_mini_account_restrict`;
CREATE TABLE `t_mini_account_restrict` (
                                           `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id ',
                                           `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                           `is_deleted` tinyint DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除',
                                           `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                           `restrict_type` tinyint(1) DEFAULT NULL COMMENT '限制类型 1-下单 2-评论',
                                           `user_id` varchar(28) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户id',
                                           `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '租户id',
                                           PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='黑名单限制权限表';

-- ----------------------------
-- Table structure for t_mini_account_tag
-- ----------------------------
DROP TABLE IF EXISTS `t_mini_account_tag`;
CREATE TABLE `t_mini_account_tag` (
                                      `id` bigint NOT NULL AUTO_INCREMENT,
                                      `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '租户id',
                                      `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                      `tag_name` varchar(24) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '组名',
                                      `is_deleted` tinyint(1) DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除',
                                      `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                      PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户标签表';

-- ----------------------------
-- Table structure for t_mini_account_tag_group
-- ----------------------------
DROP TABLE IF EXISTS `t_mini_account_tag_group`;
CREATE TABLE `t_mini_account_tag_group` (
                                            `id` bigint NOT NULL AUTO_INCREMENT,
                                            `create_time` datetime NOT NULL COMMENT '创建时间',
                                            `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '租户id',
                                            `tag_id` int NOT NULL COMMENT '组id',
                                            `is_deleted` tinyint(1) DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除',
                                            `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                            `user_id` varchar(28) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户id',
                                            PRIMARY KEY (`id`) USING BTREE,
                                            KEY `tenant_id` (`tenant_id`) USING BTREE,
                                            KEY `user_id` (`user_id`) USING BTREE,
                                            KEY `is_deleted` (`is_deleted`) USING BTREE,
                                            KEY `tag_id` (`tag_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户所属分组表';

SET FOREIGN_KEY_CHECKS = 1;