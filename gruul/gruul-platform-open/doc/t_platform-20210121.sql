/*
 Navicat Premium Data Transfer

 Source Server         : mds-开发环境
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : 111.231.208.4:3305
 Source Schema         : dev_gruul

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 21/01/2021 21:25:57
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_agent_account_record
-- ----------------------------
DROP TABLE IF EXISTS `t_agent_account_record`;
CREATE TABLE `t_agent_account_record`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `is_deleted` tinyint(0) NULL DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `agent_info_id` bigint(0) NULL DEFAULT NULL COMMENT 't_agent_info 表中id',
  `before_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '变更之前金额',
  `after_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '变更之后金额',
  `amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '变更金额',
  `type` decimal(10, 2) NULL DEFAULT NULL COMMENT '类型：1:充值收入2:消费支出3:提现支出',
  `business_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务订单号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `is_deleted`(`is_deleted`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代理商流水记录表结构' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_agent_bank
-- ----------------------------
DROP TABLE IF EXISTS `t_agent_bank`;
CREATE TABLE `t_agent_bank`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `is_deleted` tinyint(0) NULL DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `agent_info_id` bigint(0) NULL DEFAULT NULL COMMENT 't_agent_info 表中id',
  `card_phone` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '持卡人手机号',
  `card_name` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '持卡人姓名',
  `card_num` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '卡号',
  `card_bank_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开户银行',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `is_deleted`(`is_deleted`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 48 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代理商银行卡表结构' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_agent_info
-- ----------------------------
DROP TABLE IF EXISTS `t_agent_info`;
CREATE TABLE `t_agent_info`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `is_deleted` tinyint(0) NULL DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `account` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账户',
  `pwd` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `code` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邀请码',
  `link_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人',
  `region` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所在区域',
  `address` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系地址',
  `phone` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱地址',
  `type` int(0) NULL DEFAULT NULL COMMENT '1:代理商 2:直属渠道商 3:间接渠道商',
  `next_due_time` datetime(0) NULL DEFAULT NULL COMMENT '代理到期时间',
  `is_auto_times` int(0) NULL DEFAULT NULL COMMENT '满足代理目标到期自动续签时长 0不自动续期  1-自动续签',
  `discount` decimal(10, 2) NULL DEFAULT NULL COMMENT '折扣',
  `available_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '可用金额',
  `freeze_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '冻结金额',
  `sub_Ledger_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '已经分帐金额',
  `used_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '已使用金额',
  `agent_share_profit` int(0) NULL DEFAULT NULL COMMENT '代理商分润 单位%',
  `channel_share_profit` int(0) NULL DEFAULT NULL COMMENT '渠道商分润 单位%',
  `status` int(0) NULL DEFAULT NULL COMMENT ' 1:正常 2:冻结 3:停用',
  `parent_id` bigint(0) NULL DEFAULT NULL COMMENT '父节点id，间接渠道商时有值，代理商和直属渠道商时为0',
  `scale_code` varchar(24) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公司规模编号',
  `enterprise_type` varchar(24) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '企业类型编号',
  `enterprise` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '企业名称',
  `register_type` int(0) NULL DEFAULT NULL COMMENT '注册方式 1.官网自行注册  2.商户控制台注册',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `is_deleted`(`is_deleted`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 54 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代理商信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_agent_notice
-- ----------------------------
DROP TABLE IF EXISTS `t_agent_notice`;
CREATE TABLE `t_agent_notice`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `is_deleted` tinyint(0) NULL DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `agent_info_id` bigint(0) NULL DEFAULT NULL COMMENT 't_agent_info 表中id',
  `title` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '消息内容',
  `type` int(0) NULL DEFAULT NULL COMMENT '消息分类',
  `is_read` int(0) NULL DEFAULT NULL COMMENT '是否已读  0:未读 1:已读',
  `msg_type` int(0) NULL DEFAULT NULL COMMENT '消息类型  1-产品消息 2-财务消息',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `is_deleted`(`is_deleted`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 238 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代理商推送消息表结构' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_agent_package_order
-- ----------------------------
DROP TABLE IF EXISTS `t_agent_package_order`;
CREATE TABLE `t_agent_package_order`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `is_deleted` tinyint(0) NULL DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `agent_id` bigint(0) NULL DEFAULT NULL COMMENT '代理id',
  `order_num` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单编号',
  `account_id` bigint(0) NULL DEFAULT NULL COMMENT '商户id',
  `pay_type` int(0) NULL DEFAULT NULL COMMENT '1:余额支付2:扫码支付->微信3:扫码支付->支付宝4:汇款支付',
  `pay_time` datetime(0) NULL DEFAULT NULL COMMENT '支付时间',
  `paid_payable` decimal(10, 2) NULL DEFAULT NULL COMMENT '实付金额',
  `package_id` bigint(0) NULL DEFAULT NULL COMMENT '套餐 id',
  `package_data` varchar(526) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '下单时套餐完整数据',
  `status` int(0) NULL DEFAULT NULL COMMENT '订单支付状态 0:待处理1:处理中2:已经完成',
  `earn_profit_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '获利金额',
  `order_source` tinyint(1) NULL DEFAULT NULL COMMENT '订单来源 同套餐订单',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 74 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_agent_recharge
-- ----------------------------
DROP TABLE IF EXISTS `t_agent_recharge`;
CREATE TABLE `t_agent_recharge`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `is_deleted` tinyint(0) NULL DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `recharge_num` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '充值编号',
  `pay_type` int(0) NULL DEFAULT NULL COMMENT '1扫码支付->支付宝2扫码支付->微信 3汇款支付',
  `pay_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '充值金额',
  `pay_info` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '充值对象',
  `finish_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间(异步回调时间)',
  `status` int(0) NULL DEFAULT NULL COMMENT '0:充值中1:充值成功2:充值失败',
  `agent_info_id` bigint(0) NULL DEFAULT NULL COMMENT 't_agent_info 表中id',
  `merchant_id` bigint(0) NULL DEFAULT NULL COMMENT '商户id',
  `prepay_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '三方预交易标识',
  `audit_time` datetime(0) NULL DEFAULT NULL COMMENT '汇款支付审核时间',
  `account_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '账户余额',
  `auditor_status` tinyint(1) NULL DEFAULT NULL COMMENT '审核状态：0:待审核 1:审核通过 2:审核拒绝',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `is_deleted`(`is_deleted`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 72 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代理商充值记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_agent_rule
-- ----------------------------
DROP TABLE IF EXISTS `t_agent_rule`;
CREATE TABLE `t_agent_rule`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `is_deleted` tinyint(0) NULL DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `agent_info_id` bigint(0) NULL DEFAULT NULL COMMENT 't_agent_info 表中id',
  `rule_type` int(0) NULL DEFAULT NULL COMMENT '代理目标  1.销售总单数 2.销售总额',
  `target_time` int(0) NULL DEFAULT NULL COMMENT '完成目标时间节点,单位月',
  `target_value` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '完成目标所需值',
  `rule_class` int(0) NULL DEFAULT NULL COMMENT '规则类型 1目标规则  2-升级条件',
  `use_rule` int(0) NULL DEFAULT NULL COMMENT '是否使用规则 0-未使用 1-已使用',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `is_deleted`(`is_deleted`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 193 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代理目标' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_agent_statistics
-- ----------------------------
DROP TABLE IF EXISTS `t_agent_statistics`;
CREATE TABLE `t_agent_statistics`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `is_deleted` tinyint(0) NULL DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `agent_info_id` bigint(0) NULL DEFAULT NULL COMMENT 't_agent_info 表中id',
  `total_sale_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '累计销售总金额',
  `total_sale_count` int(0) NULL DEFAULT NULL COMMENT '累计销售总单数',
  `commission_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '佣金金额',
  `channal_count` int(0) NULL DEFAULT NULL COMMENT '下集渠道商数',
  `shop_info_count` int(0) NULL DEFAULT NULL COMMENT '商户数量',
  `app_info_count` int(0) NULL DEFAULT NULL COMMENT '店铺数量',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `is_deleted`(`is_deleted`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代理商统计信息表结构' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_agent_transfer_log
-- ----------------------------
DROP TABLE IF EXISTS `t_agent_transfer_log`;
CREATE TABLE `t_agent_transfer_log`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `is_deleted` tinyint(0) NULL DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `info_id` bigint(0) NULL DEFAULT NULL COMMENT '被迁移的id',
  `info_type` tinyint(0) NULL DEFAULT NULL COMMENT '被迁移对象 1.商户 2.渠道商',
  `to_type` tinyint(0) NULL DEFAULT NULL COMMENT '迁移对象 1-变为代理商  2-变为渠道商 3-渠道商迁移至某代理',
  `to_id` bigint(0) NULL DEFAULT NULL COMMENT '迁移后的对象id',
  `transfer_time` datetime(0) NULL DEFAULT NULL COMMENT '迁移时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `is_deleted`(`is_deleted`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 244 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '关系迁移日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_agent_verify
-- ----------------------------
DROP TABLE IF EXISTS `t_agent_verify`;
CREATE TABLE `t_agent_verify`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `is_deleted` tinyint(0) NULL DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `phone` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `link_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人',
  `region` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所在区域',
  `address` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系地址',
  `status` int(0) NULL DEFAULT NULL COMMENT '0:待审核1:审核通过:审核拒绝',
  `code` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邀请码',
  `enterprise_type` varchar(24) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '企业类型编号',
  `enterprise` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '企业名称',
  `register_type` int(0) NULL DEFAULT NULL COMMENT '注册方式 1.官网自行注册  2.商户控制台注册',
  `scale_code` varchar(24) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公司规模编号',
  `account_id` bigint(0) NULL DEFAULT NULL COMMENT '商户id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `is_deleted`(`is_deleted`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 55 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代理申请审核表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_agent_withdraw_order
-- ----------------------------
DROP TABLE IF EXISTS `t_agent_withdraw_order`;
CREATE TABLE `t_agent_withdraw_order`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `is_deleted` tinyint(0) NULL DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `agent_info_id` bigint(0) NULL DEFAULT NULL COMMENT 't_agent_info 表中id',
  `type` int(0) NULL DEFAULT NULL COMMENT '提现方式  1-微信  2-支付宝  3-银行卡',
  `order_num` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '我方交易流水号',
  `name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提现姓名',
  `bank_name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开户行名称',
  `bank_num` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行卡号或者支付宝微信账号',
  `amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '提现金额',
  `status` int(0) NULL DEFAULT NULL COMMENT '0:待审核 1:审核拒绝 2:已打款',
  `agent_type` int(0) NULL DEFAULT NULL COMMENT '1代理商2直属渠道商3间接渠道商',
  `account` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账户',
  `link_name` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `is_deleted`(`is_deleted`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '提现工单表结构' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_mini_audit_record
-- ----------------------------
DROP TABLE IF EXISTS `t_mini_audit_record`;
CREATE TABLE `t_mini_audit_record`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `template_id` bigint(0) NOT NULL COMMENT '代码库中的代码模版ID',
  `app_id` varchar(60) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '授权APPID',
  `audit_id` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审核编号',
  `audit_status` int(0) NULL DEFAULT NULL COMMENT '审核状态 0审核通过,1审核失败，2审核中',
  `release_state` int(0) NULL DEFAULT 0 COMMENT '发布状态 0-未发布 1-已发布',
  `version_id` bigint(0) NULL DEFAULT 0 COMMENT '当前模板代码版本id(t_platform_shop_template_detail)',
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '审核失败原因',
  `is_deleted` tinyint(1) NULL DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 51 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '小程序审核记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_mini_auth_info
-- ----------------------------
DROP TABLE IF EXISTS `t_mini_auth_info`;
CREATE TABLE `t_mini_auth_info`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '租户id',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `nick_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '授权方昵称',
  `head_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '授权方头像',
  `service_type_info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '授权方类型0代表订阅号，1代表由历史老帐号升级后的订阅号，2代表服务号 3代码小程序',
  `verify_type_info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '授权方认证类型，-1代表未认证，0代表微信认证，1代表新浪微博认证，2代表腾讯微博认证，3代表已资质认证通过但还未通过名称认证，4代表已资质认证通过、还未通过名称认证，但通过了新浪微博认证，5代表已资质认证通过、还未通过名称认证，但通过了腾讯微博认证',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '原始ID',
  `principal_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '主体名称',
  `signature` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '帐号介绍',
  `alias` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '授权方公众号所设置的微信号，可能为空',
  `business_info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用以了解以下功能的开通状况（0代表未开通，1代表已开通）： open_store:是否开通微信门店功能 open_scan:是否开通微信扫商品功能 open_pay:是否开通微信支付功能 open_card:是否开通微信卡券功能 open_shake:是否开通微信摇一摇功能',
  `qrcode_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '二维码图片的URL',
  `authorization_info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '授权信息',
  `authorization_appid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '授权方appid',
  `miniProgramInfo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '可根据这个字段判断是否为小程序类型授权',
  `func_info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '授权给开发者的权限集列表',
  `is_deleted` tinyint(1) NULL DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '小程序授权时信息(只在授权流程中获取或更新)' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_mini_auth_token
-- ----------------------------
DROP TABLE IF EXISTS `t_mini_auth_token`;
CREATE TABLE `t_mini_auth_token`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '租户id',
  `authorizer_appid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '授权方appid',
  `authorizer_access_token` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '授权方令牌',
  `expires_in` datetime(0) NULL DEFAULT NULL COMMENT '令牌到期时间',
  `authorizer_refresh_token` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '刷新令牌',
  `func_info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '授权给开发者的权限集列表',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint(1) NULL DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 70 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '接口调用凭据和授权信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_mini_change_record
-- ----------------------------
DROP TABLE IF EXISTS `t_mini_change_record`;
CREATE TABLE `t_mini_change_record`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '租户id',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `mini_info_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '小程序id',
  `change_type` tinyint(1) NULL DEFAULT NULL COMMENT '变更类型 0-续费 1-更改套餐 2-禁用或开启  3-信息更改',
  `old_data` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '老数据',
  `new_data` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '新数据',
  `is_deleted` tinyint(1) NULL DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '小程序变更记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_mini_experience
-- ----------------------------
DROP TABLE IF EXISTS `t_mini_experience`;
CREATE TABLE `t_mini_experience`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id ',
  `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '租户id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `is_deleted` tinyint(1) NULL DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `experience_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '体验者',
  `mini_id` bigint(0) NULL DEFAULT NULL COMMENT '小程序id',
  `userstr` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'userstr',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '小程序体验者表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_mini_info
-- ----------------------------
DROP TABLE IF EXISTS `t_mini_info`;
CREATE TABLE `t_mini_info`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '租户id 授权小程序时生成 随机18个字符生成',
  `account_id` bigint(0) NULL DEFAULT NULL COMMENT '拥有者id',
  `mini_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '小程序名称',
  `app_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'appID',
  `authorizer_flag` tinyint(1) NULL DEFAULT 0 COMMENT '授权状态    0-未授权 1-授权',
  `expiration_time` datetime(0) NULL DEFAULT NULL COMMENT '到期时间',
  `forbid_status` tinyint(1) NULL DEFAULT 0 COMMENT '是否禁用  0-未禁用 1-已禁用',
  `account_type` tinyint(1) NULL DEFAULT NULL COMMENT '帐号类型（1：订阅号，2：服务号，3：小程序）',
  `principal_type` tinyint(0) NULL DEFAULT NULL COMMENT '主体类型（1：企业）',
  `principal_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '主体名称',
  `qualification_verify` tinyint(1) NULL DEFAULT NULL COMMENT '是否资质认证（true：是，false：否）若是，拥有微信认证相关的权限。',
  `signature` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '功能介绍',
  `signature_modify_used_count` tinyint(1) NULL DEFAULT NULL COMMENT '功能介绍已使用修改次数（本月）',
  `head_image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像url',
  `head_modify_used_count` tinyint(1) NULL DEFAULT NULL COMMENT '头像已使用修改次数（本年）',
  `head_modify_quota` tinyint(1) NULL DEFAULT NULL COMMENT '头像修改次数总额度（本年）',
  `mini_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '小程序码',
  `qrcode` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '小程序二维码',
  `experience_code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '体验二维码',
  `certificate_path` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商户支付证书路径',
  `mch_key` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '支付秘钥',
  `mch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商户号',
  `run_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '运行状态  0-未上传代码 1-已上传代码',
  `auth_time` datetime(0) NULL DEFAULT NULL COMMENT '授权时间(t_miniauth_info表中的创建时间)',
  `remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '后台备注',
  `current_version_id` bigint(0) NULL DEFAULT NULL COMMENT '当前模板代码版本id',
  `template_detail_minis_id` bigint(0) NULL DEFAULT NULL COMMENT '当前模板代码版本中的某个小程序版本id(t_platform_shop_template_detail_minis表id)',
  `upload_template_id` bigint(0) NULL DEFAULT NULL COMMENT '最新上传的模板id',
  `current_aidit_id` bigint(0) NULL DEFAULT NULL COMMENT '当前版本审核记录id',
  `aidit_id` bigint(0) NULL DEFAULT NULL COMMENT '审核中版本审核记录id',
  `is_deleted` tinyint(1) NULL DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `service_type_info` tinyint(0) NULL DEFAULT NULL COMMENT '授权方类型0代表订阅号，1代表由历史老帐号升级后的订阅号，2代表服务号 3代码小程序',
  `verify_type_info` tinyint(0) NULL DEFAULT NULL COMMENT '授权方认证类型，-1代表未认证，0代表微信认证，1代表新浪微博认证，2代表腾讯微博认证，3代表已资质认证通过但还未通过名称认证，4代表已资质认证通过、还未通过名称认证，但通过了新浪微博认证，5代表已资质认证通过、还未通过名称认证，但通过了腾讯微博认证',
  `business_info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用以了解以下功能的开通状况（0代表未开通，1代表已开通）： open_store:是否开通微信门店功能 open_scan:是否开通微信扫商品功能 open_pay:是否开通微信支付功能 open_card:是否开通微信卡券功能 open_shake:是否开通微信摇一摇功能',
  `user_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '原始id',
  `signature_modify_quota` tinyint(1) NULL DEFAULT NULL COMMENT '功能介绍修改次数总额度（本月）',
  `realname_status` tinyint(1) NULL DEFAULT NULL COMMENT '实名验证状态（1：实名验证成功，2：实名验证中，3：实名验证失败） 0-false 1-true',
  `naming_verify` tinyint(1) NULL DEFAULT NULL COMMENT '是否名称认证（true：是，false：否）对于公众号（订阅号、服务号），是名称认证，微信客户端才会有微信认证打勾的标识。',
  `annual_review` tinyint(1) NULL DEFAULT NULL COMMENT '是否需要年审（true：是，false：否）（qualification_verify = true时才有该字段）',
  `annual_review_begin_time` datetime(0) NULL DEFAULT NULL COMMENT '年审开始时间，时间戳（qualification_verify = true时才有该字段）',
  `annual_review_end_time` datetime(0) NULL DEFAULT NULL COMMENT '年审截止时间，时间戳（qualification_verify = true时才有该字段）',
  `default_status` tinyint(1) NULL DEFAULT NULL COMMENT '是否默认 0否 1默认',
  `alias` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公众号绑定的微信账号',
  `combo_id` bigint(0) NULL DEFAULT NULL COMMENT '套餐id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 94 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '小程序基本信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_mini_mp_conf
-- ----------------------------
DROP TABLE IF EXISTS `t_mini_mp_conf`;
CREATE TABLE `t_mini_mp_conf`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `is_deleted` tinyint(1) NULL DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除 ',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '租户id',
  `app_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公众号appId',
  `app_secret` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公众号appSecret',
  `app_aes_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公众号aeskey',
  `app_token` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公众号appToken',
  `mini_id` bigint(0) NULL DEFAULT NULL COMMENT '小程序id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '小程序关联公众号配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_mini_subscriberi_base
-- ----------------------------
DROP TABLE IF EXISTS `t_mini_subscriberi_base`;
CREATE TABLE `t_mini_subscriberi_base`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `is_deleted` tinyint(1) NULL DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除 ',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `use_type` tinyint(1) NULL DEFAULT NULL COMMENT '使用类型 1-买家通知 2-商家通知',
  `version` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '对应版本',
  `message_type` tinyint(1) NULL DEFAULT NULL COMMENT '消息类别 1-订单消息 2-售后消息 3-用户消息 4-营销活动',
  `template_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '默认模板id, 目前仅商家通知使用公众号通知才有默认模板id',
  `title` varchar(28) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标题名称',
  `t_id` int(0) NULL DEFAULT NULL COMMENT '订阅模板标题id',
  `type` tinyint(1) NULL DEFAULT NULL COMMENT '订阅模板订阅类型 1-永久订阅  2-一次性订阅 3-公众号模板消息',
  `category_id` int(0) NULL DEFAULT NULL COMMENT '订阅模板所属二级行业类目id',
  `k_ids` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '订阅模板-kid按顺序分割,例子1,3,5',
  `rules` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '订阅模板-参数规则名跟kid排序相对应amount,thingthing',
  `first_class` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '订阅模板一级类目名称',
  `first_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '订阅模板一级行业类目id',
  `second_class` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '订阅模板二级类目名称',
  `example_json` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '提供预览值',
  `send_rule` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '推送规则',
  `mini_open` tinyint(1) NULL DEFAULT NULL COMMENT '小程序订阅消息是否开启 0-关闭  1-开启',
  `code_open` tinyint(1) NULL DEFAULT NULL COMMENT '短信模板消息是否开启 0-关闭  1-开启',
  `mp_open` tinyint(1) NULL DEFAULT NULL COMMENT '公众号模板消息是否开启 0-关闭  1-开启',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1621 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订阅模板基础表提供版本默认值' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_mini_subscriberi_message
-- ----------------------------
DROP TABLE IF EXISTS `t_mini_subscriberi_message`;
CREATE TABLE `t_mini_subscriberi_message`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `is_deleted` tinyint(1) NULL DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除 ',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '租户id',
  `message_type` tinyint(0) NULL DEFAULT NULL COMMENT '消息类别 1-订单消息 2-售后消息 3-用户消息 4-营销活动',
  `msg_title` varchar(28) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '消息标题',
  `template_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模板消息id',
  `use_status` tinyint(1) NULL DEFAULT NULL COMMENT '开启状态 0-未使用  1-使用',
  `k_ids` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'kid按顺序分割,例子1,3,5',
  `rules` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '参数规则名跟kid排序相对应amount1,thing3,thing5',
  `example_json` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '提供预览值',
  `second_class` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '二级类目名称',
  `mini_id` bigint(0) NULL DEFAULT NULL COMMENT '小程序表id',
  `send_rule` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '推送规则',
  `use_type` tinyint(1) NULL DEFAULT NULL COMMENT '使用类型 1-买家通知 2-商家通知',
  `version` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '对应版本',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 260 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '小程序拥有的订阅消息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_platform_account_balance_record
-- ----------------------------
DROP TABLE IF EXISTS `t_platform_account_balance_record`;
CREATE TABLE `t_platform_account_balance_record`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `is_deleted` tinyint(0) NULL DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `account_id` bigint(0) NULL DEFAULT NULL COMMENT '商户id',
  `consumption_type` tinyint(0) NULL DEFAULT NULL COMMENT '操作类型 1:充值 2:套餐购买 3:套餐续费',
  `order_num` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '消费订单号(跟着操作类型走)',
  `before_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '变更之前金额',
  `after_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '变更之后金额',
  `amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '变更金额',
  `type` tinyint(1) NULL DEFAULT NULL COMMENT '变更类型：1:收入2:支出',
  `invoice_status` tinyint(1) NULL DEFAULT NULL COMMENT '开票状态  0-未开票 1-已开票',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `is_deleted`(`is_deleted`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 155 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '账号余额明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_platform_account_info
-- ----------------------------
DROP TABLE IF EXISTS `t_platform_account_info`;
CREATE TABLE `t_platform_account_info`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '不加密的密码-临时用',
  `passwd` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '账号登录密码-加密',
  `salt` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '盐值',
  `city` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所在城市',
  `language` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所用的语言 en 英文 - zh_CN 简体中文 -zh_TW 繁体中文 \r',
  `last_login_time` datetime(0) NULL DEFAULT NULL COMMENT '最后登录时间',
  `access_token` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '登录token-微信网页应用',
  `refresh_token` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '刷新token-微信网页应用 refresh_token拥有较长的有效期（30天），当refresh_token失效的后，需要用户重新授权。',
  `nike_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名称',
  `avatar_url` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像url',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号码',
  `email` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '电子发票接收邮箱',
  `gender` tinyint(1) NULL DEFAULT 0 COMMENT '性别 0：未知、1：男、2：女',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `is_deleted` tinyint(1) NULL DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `open_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '第三方平台openid',
  `union_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '第三方平台unionid,针对一个微信开放平台帐号下的应用',
  `province` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '省份',
  `country` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '国家',
  `privilege` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户特权信息，json数组，如微信沃卡用户为（chinaunicom）',
  `ref_expires_time` datetime(0) NULL DEFAULT NULL COMMENT 'refresh_token到期时间',
  `access_expires_time` datetime(0) NULL DEFAULT NULL COMMENT 'access_token到期时间',
  `forbid_status` tinyint(1) NULL DEFAULT 0 COMMENT '禁用状态 0-正常 1-禁用',
  `subject_id` bigint(0) NULL DEFAULT NULL COMMENT '所属主体账号id,也就是平台管理的id',
  `bind_mini_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '小程序唯一id',
  `account_type` tinyint(1) NULL DEFAULT NULL COMMENT '账号类型  0-商户账号, 1-店铺名下账号',
  `region` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '区域',
  `address` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地址',
  `last_login_shop_id` bigint(0) NULL DEFAULT NULL COMMENT '最后一次进入的店铺id',
  `bind_mini_shop_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '小程序用户唯一id',
  `balance` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '用户余额',
  `agent_id` bigint(0) NULL DEFAULT NULL COMMENT '所属代理',
  `comment_text` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `me_agent_id` bigint(0) NULL DEFAULT NULL COMMENT '用户的代理账号id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 75 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '平台与租户平台用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_platform_account_recharge
-- ----------------------------
DROP TABLE IF EXISTS `t_platform_account_recharge`;
CREATE TABLE `t_platform_account_recharge`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `is_deleted` tinyint(0) NULL DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `account_id` bigint(0) NULL DEFAULT NULL COMMENT '商户id',
  `recharge_num` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '充值编号',
  `pay_num` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支付单号',
  `pay_type` int(0) NULL DEFAULT NULL COMMENT '1扫码支付->支付宝2扫码支付->微信3汇款支付',
  `finish_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间(异步回调时间)',
  `pay_amount` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支付余额',
  `pay_info` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '付款方信息(json)',
  `account_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '账户余额',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '充值状态: 0:生成订单 1:充值中 2:充值成功 ',
  `pay_source` tinyint(1) NULL DEFAULT NULL COMMENT '充值源头 1.商户  2.代理商 3.渠道商',
  `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '租户id 授权小程序时生成 随机18个字符生成',
  `prepay_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '三方预交易标识',
  `audit_time` datetime(0) NULL DEFAULT NULL COMMENT '汇款支付审核时间',
  `invoice_status` tinyint(1) NULL DEFAULT NULL COMMENT '开票状态  0-未开票 1-已开票',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `is_deleted`(`is_deleted`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 165 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '充值订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_platform_code_version
-- ----------------------------
DROP TABLE IF EXISTS `t_platform_code_version`;
CREATE TABLE `t_platform_code_version`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `pc_version` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'pc后台版本',
  `wx_version` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '微信小程序版本',
  `mobile_version` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '微信小程序版本',
  `level` tinyint(0) NULL DEFAULT NULL COMMENT '1 标准版，2企业版，3旗舰版',
  `type` tinyint(0) NULL DEFAULT NULL COMMENT '分类类型：1 系统模版 2 定制模版',
  `mobile_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '移动web路径',
  `pc_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'PC端后台url',
  `template_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模版编号',
  `is_deleted` tinyint(0) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '店铺信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_platform_default_value
-- ----------------------------
DROP TABLE IF EXISTS `t_platform_default_value`;
CREATE TABLE `t_platform_default_value`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `unique_identification` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '默认值唯一标识',
  `version` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '默认值版本',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `is_deleted` tinyint(1) NULL DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `build_action` tinyint(1) NULL DEFAULT NULL COMMENT '默认值生成方式 1-发送指队列 2-url地址 3-自行查询',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求地址',
  `mq` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'mq信息json',
  `kv` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT 'key-value默认值json字符串',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 85 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '默认数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_platform_libraries_info
-- ----------------------------
DROP TABLE IF EXISTS `t_platform_libraries_info`;
CREATE TABLE `t_platform_libraries_info`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '基础库名称',
  `type` tinyint(0) NULL DEFAULT NULL COMMENT '基础库类型 1通用 2 定制',
  `status` tinyint(0) NULL DEFAULT NULL COMMENT '1正常 2异常，3已下线',
  `version` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '版本号',
  `count` int(0) NULL DEFAULT NULL COMMENT '服务数量',
  `category_type` tinyint(0) NULL DEFAULT NULL COMMENT '分类类型：1 业务基础库 2 支撑基础库',
  `belong_id` bigint(0) NULL DEFAULT NULL COMMENT '所属支撑基础库id ，只有是业务基础库时，才有此值',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '服务说明',
  `is_deleted` tinyint(0) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `uniqueness` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '唯一标识',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 66 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '基础库信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_platform_log
-- ----------------------------
DROP TABLE IF EXISTS `t_platform_log`;
CREATE TABLE `t_platform_log`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `log_text` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `out_id` bigint(0) NULL DEFAULT NULL COMMENT '外键',
  `type` tinyint(0) NULL DEFAULT NULL COMMENT '1 支撑基础库更新 2 业务基础库更新 3 模版更新 ',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `modify_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `is_delete` tinyint(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '日志更新表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_platform_pay_config
-- ----------------------------
DROP TABLE IF EXISTS `t_platform_pay_config`;
CREATE TABLE `t_platform_pay_config`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `certificate_path` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商户支付证书路径',
  `mch_key` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '支付秘钥',
  `mch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商户号',
  `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '租户id',
  `is_deleted` tinyint(0) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `ips_mer_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '环迅商户号',
  `ips_acc_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '环迅商户账户编号',
  `ips_certificate_psw` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '环迅ipsCertificatePsw证书密码',
  `ips_rsa_public_key` varchar(355) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '环迅公钥',
  `ips_rsa_private_key` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '环迅私钥',
  `ips_aes` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '环迅AES秘钥',
  `ips_sha` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '环迅SHA公钥',
  `sxf_org_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '随行付合作机构id',
  `sxf_acc_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信商户号入驻商户编号',
  `sxf_certificate_psw` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信商户号证书密码',
  `sxf_public` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '微信商户号公钥',
  `sxf_private_key` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '微信商户号秘钥',
  `sft_terminal_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '盛付通终端号',
  `sft_md5` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '盛付通秘钥',
  `sft_channel_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '盛付通代理商商户编号',
  `sft_sub_merchant_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '盛付通线下交易子商户号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 207 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_platform_server_cfg
-- ----------------------------
DROP TABLE IF EXISTS `t_platform_server_cfg`;
CREATE TABLE `t_platform_server_cfg`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `realm_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '域名',
  `ip` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'ip',
  `ram_size` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '内存大小',
  `hard_disk_size` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '硬盘大小',
  `cpu_size` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'cpu大小',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `is_delete` tinyint(0) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `modify_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `service_id` bigint(0) NULL DEFAULT NULL COMMENT '服务信息表id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '服务器配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_platform_service_info
-- ----------------------------
DROP TABLE IF EXISTS `t_platform_service_info`;
CREATE TABLE `t_platform_service_info`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '服务名称',
  `type` tinyint(0) NULL DEFAULT NULL COMMENT '服务类型 1通用 2 定制',
  `status` tinyint(0) NULL DEFAULT NULL COMMENT '1正常 2异常',
  `version` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '服务版本',
  `count` int(0) NULL DEFAULT NULL COMMENT '服务数量',
  `libraries_info_id` bigint(0) NULL DEFAULT NULL COMMENT 'oundation_libraries id 区分 业务基础库和支撑基础库',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '服务说明',
  `is_deleted` tinyint(0) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 254 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '服务信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_platform_shop_deploy
-- ----------------------------
DROP TABLE IF EXISTS `t_platform_shop_deploy`;
CREATE TABLE `t_platform_shop_deploy`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `server_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '服务器IP',
  `domain_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '服务器域名',
  `cdn_cfg` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'cdn配置json',
  `cdn_type` tinyint(0) NULL DEFAULT NULL COMMENT '0不使用，1七牛云，2阿里云，腾讯云',
  `is_deleted` tinyint(0) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `shop_id` bigint(0) NULL DEFAULT NULL COMMENT '店铺id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 397 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '店铺部署信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_platform_shop_info
-- ----------------------------
DROP TABLE IF EXISTS `t_platform_shop_info`;
CREATE TABLE `t_platform_shop_info`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `logo_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '店铺首页图片',
  `shop_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '店铺名称',
  `status` tinyint(0) NULL DEFAULT 1 COMMENT '0审核中，1部署中\n2正常 ，3已打烊，4禁用',
  `due_time` datetime(0) NULL DEFAULT NULL COMMENT '到期时间',
  `is_due` tinyint(0) NULL DEFAULT 0 COMMENT '是否到期 0 不是 1是',
  `shop_template_id` bigint(0) NULL DEFAULT NULL COMMENT 't_platform_shop_template_info 模版id',
  `account_id` bigint(0) NULL DEFAULT NULL COMMENT '商户idt_platform_account_info的id',
  `agree_on` tinyint(0) NULL DEFAULT NULL COMMENT '是否同意笔歌协议 0未同意，1已同意',
  `package_id` bigint(0) NULL DEFAULT NULL COMMENT '套餐id',
  `is_deleted` tinyint(0) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `create_join` tinyint(0) NULL DEFAULT NULL COMMENT '创建入口 0-admin 1-商家控制台',
  `is_privatization_deployment` tinyint(0) NULL DEFAULT NULL COMMENT '是否私有化部署（0否 1是）',
  `business_hours` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '营业时间,自行分割',
  `shop_phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '店铺电话',
  `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '租户id',
  `bind_mini_id` bigint(0) NULL DEFAULT NULL COMMENT '绑定的小程序信息id',
  `bind_mp_id` bigint(0) NULL DEFAULT NULL COMMENT '绑定的公众号信息id',
  `shop_template_detail_id` bigint(0) NULL DEFAULT NULL COMMENT '当前使用的模版版本id',
  `certificate_path` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商户支付证书路径',
  `mch_key` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '支付秘钥',
  `mch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商户号',
  `pay_type` tinyint(1) NULL DEFAULT 1 COMMENT '支付类型 1-微信支付 2-环迅支付 3-随行支付',
  `package_order_id` bigint(0) NULL DEFAULT NULL COMMENT '套餐订购表id',
  `mini_bottom_log` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '小程序底部打标',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 397 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '店铺信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_platform_shop_message
-- ----------------------------
DROP TABLE IF EXISTS `t_platform_shop_message`;
CREATE TABLE `t_platform_shop_message`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `is_deleted` tinyint(1) NULL DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除 ',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `use_type` tinyint(1) NULL DEFAULT NULL COMMENT '使用类型 1-买家通知 2-商家通知',
  `version` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '对应版本',
  `message_type` tinyint(1) NULL DEFAULT NULL COMMENT '消息类别 1-订单消息 2-售后消息 3-用户消息 4-营销活动',
  `mini_open` tinyint(1) NULL DEFAULT NULL COMMENT '小程序订阅消息是否开启 0-关闭  1-开启',
  `mini_msg` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '小程序订阅模板',
  `code_open` tinyint(1) NULL DEFAULT NULL COMMENT '短信模板消息是否开启 0-关闭  1-开启',
  `code_msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '短信消息',
  `mp_open` tinyint(1) NULL DEFAULT NULL COMMENT '公众号是否发送  0关闭- 1-开启',
  `mp_msg` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公众号消息',
  `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '租户id',
  `mark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '消息标识',
  `mini_template_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '小程序模板消息id',
  `code_template_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '短信模板id',
  `mp_template_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公众号模板id',
  `title` varchar(28) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标题名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2460 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '店铺消息配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_platform_shop_template_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_platform_shop_template_detail`;
CREATE TABLE `t_platform_shop_template_detail`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `version` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '版本号',
  `libraries_info_id` bigint(0) NULL DEFAULT NULL COMMENT '业务基础库id',
  `libraries_info_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业务基础库名称',
  `code_templete_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '小程序代码模版id',
  `code_templete_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '小程序代码模版名称',
  `pc_termina_url` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'pc端路径',
  `pc_termina_version` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'pc端版本',
  `mobile_terminal_url` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '移动端路径',
  `mobile_terminal_version` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '移动端版本',
  `server_count` int(0) NULL DEFAULT NULL COMMENT '服务数量',
  `shop_template_id` bigint(0) NULL DEFAULT NULL COMMENT '店铺模版表id',
  `is_deleted` tinyint(0) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `version_log` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新日志',
  `pc_url_map` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'pc端关联页面,json存储,键值对',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 80 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '店铺模版详情表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_platform_shop_template_detail_minis
-- ----------------------------
DROP TABLE IF EXISTS `t_platform_shop_template_detail_minis`;
CREATE TABLE `t_platform_shop_template_detail_minis`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '表id',
  `is_deleted` tinyint(0) NULL DEFAULT NULL COMMENT '删除状态',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `code_templete_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '小程序模版库中templateId',
  `code_templete_version` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模版库中自定义版本号',
  `version_explain` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '版本说明',
  `shop_template_detail_id` bigint(0) NULL DEFAULT NULL COMMENT '店铺模版详情表id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '店铺模版详情小程序版本子表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_platform_shop_template_info
-- ----------------------------
DROP TABLE IF EXISTS `t_platform_shop_template_info`;
CREATE TABLE `t_platform_shop_template_info`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '店铺模版名称',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模版编号',
  `type` tinyint(1) NULL DEFAULT NULL COMMENT '分类类型：1 系统模版 2 定制模版',
  `shop_template_type` tinyint(1) NULL DEFAULT NULL COMMENT '模版应用类型：1商城，2社区拼团，3门店版',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '服务说明',
  `is_deleted` tinyint(1) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '店铺模版表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_sys_shop_invoice_order
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_shop_invoice_order`;
CREATE TABLE `t_sys_shop_invoice_order`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `is_deleted` tinyint(1) NULL DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `type` tinyint(1) NULL DEFAULT NULL COMMENT '发票类型：1增值税专用发票、2普通发票 3专业发票',
  `invoice_rise_type` tinyint(1) NULL DEFAULT NULL COMMENT '抬头类型：1个人或事业单位，2企业',
  `invoice_rise_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '抬头名称',
  `invoice_taxpayer_num` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '纳税人识别号',
  `amount` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发票金额',
  `email` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件地址',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '审核状态：0待审核，1已审核',
  `order_type` tinyint(1) NULL DEFAULT NULL COMMENT '订单类型 1-充值订单  2-套餐订购订单',
  `order_id` bigint(0) NULL DEFAULT NULL COMMENT '订购订单id',
  `account_id` bigint(0) NULL DEFAULT NULL COMMENT '商户id',
  `audit_time` datetime(0) NULL DEFAULT NULL COMMENT '审核时间',
  `number_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发票编号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `is_deleted`(`is_deleted`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '发票工单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_sys_shop_invoice_rise
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_shop_invoice_rise`;
CREATE TABLE `t_sys_shop_invoice_rise`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `is_deleted` tinyint(0) NULL DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `account_id` bigint(0) NULL DEFAULT NULL COMMENT '商户id',
  `head_type` int(0) NULL DEFAULT NULL COMMENT '抬头类型：1个人或事业单位，2企业',
  `invoice_rise_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '抬头名称',
  `invoice_taxpayer_num` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '纳税人识别号',
  `remark` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `default_status` tinyint(1) NULL DEFAULT 0 COMMENT '是否默认使用 0-否  1-是',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `is_deleted`(`is_deleted`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户发票抬头表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_sys_shop_package
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_shop_package`;
CREATE TABLE `t_sys_shop_package`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `is_deleted` tinyint(0) NULL DEFAULT 0 COMMENT '删除状态：0->未删除；1->已删除',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '套餐名称',
  `remark` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '套餐说明',
  `level` int(0) NULL DEFAULT NULL COMMENT '套餐等级',
  `package_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '套餐价格',
  `package_price_unit` tinyint(1) NULL DEFAULT NULL COMMENT '套餐使用价格单位 1天，2月，3年',
  `discounts_json` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '优惠价json y-年 m-月 d-天  [{\"unit\":\"y\",\"price\":100}]',
  `functionDesc` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '功能描述',
  `open_state` int(0) NULL DEFAULT 0 COMMENT '套餐开启状态 0-关闭  1-开启',
  `template_version_id` bigint(0) NULL DEFAULT NULL COMMENT '模版版本id',
  `operate_id` bigint(0) NULL DEFAULT NULL COMMENT '上一次操作人id',
  `operate_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上一次操作人名称',
  `template_id` bigint(0) NULL DEFAULT NULL COMMENT '模版id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `is_deleted`(`is_deleted`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 122 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '店铺套餐' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_sys_shop_package_order
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_shop_package_order`;
CREATE TABLE `t_sys_shop_package_order`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `is_deleted` tinyint(0) NULL DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `account_id` bigint(0) NULL DEFAULT NULL COMMENT '商户id',
  `tenant_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '店铺租户id',
  `shop_template_info_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '店铺模版表id',
  `order_num` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单编号',
  `package_id` bigint(0) NULL DEFAULT NULL COMMENT '套餐 id',
  `package_data` varchar(526) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '下单时套餐完整数据',
  `order_type` tinyint(0) NULL DEFAULT NULL COMMENT '订单类型 1-订购  2-续费  3-升级',
  `package_time` int(0) NULL DEFAULT NULL COMMENT '套餐时长',
  `package_price_unit` tinyint(1) NULL DEFAULT NULL COMMENT '套餐价格单位 1天，2月，3年',
  `package_start_time` datetime(0) NULL DEFAULT NULL COMMENT '套餐开始时间',
  `package_end_time` datetime(0) NULL DEFAULT NULL COMMENT '套餐结束时间',
  `package_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '套餐价格',
  `amount_payable` decimal(10, 2) NULL DEFAULT NULL COMMENT '应付金额',
  `paid_payable` decimal(10, 2) NULL DEFAULT NULL COMMENT '实付金额',
  `pay_type` int(0) NULL DEFAULT NULL COMMENT '1:余额支付2:扫码支付->微信3:扫码支付->支付宝4:汇款支付',
  `status` int(0) NULL DEFAULT NULL COMMENT '订单支付状态 0:待处理1:处理中2:已经完成',
  `pay_info` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行卡支付时填写支付方信息json',
  `relauditor_id` bigint(0) NULL DEFAULT NULL COMMENT '审核人id',
  `auditor_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审核人名称',
  `auditor_status` int(0) NULL DEFAULT NULL COMMENT '审核状态：0:待审核 1:审核通过 2:审核拒绝',
  `is_agreed` int(0) NULL DEFAULT NULL COMMENT '是否同意协议',
  `is_automatic_deduction` int(0) NULL DEFAULT NULL COMMENT '是否同意标准版自动扣除续费',
  `is_received` int(0) NULL DEFAULT NULL COMMENT '是否收到汇款 0:未收到 1:已收到',
  `invoice_status` tinyint(1) NULL DEFAULT NULL COMMENT '开票状态  0-未开票 1-已开票',
  `order_source` tinyint(1) NULL DEFAULT NULL COMMENT '订单来源  0-用户购买 1-管理台购买',
  `audit_time` datetime(0) NULL DEFAULT NULL COMMENT '汇款支付审核时间',
  `agent_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `package_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '套餐名称',
  `shop_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '店铺名称',
  `template_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '店铺模板名称(店铺类型)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `is_deleted`(`is_deleted`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 434 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '店铺套餐订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_system_conf
-- ----------------------------
DROP TABLE IF EXISTS `t_system_conf`;
CREATE TABLE `t_system_conf`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `param_key` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'key',
  `param_value` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT 'value',
  `status` tinyint(0) NULL DEFAULT 1 COMMENT '状态   0：隐藏   1：显示',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态：0->未删除；1->已删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_template_msg_send_record
-- ----------------------------
DROP TABLE IF EXISTS `t_template_msg_send_record`;
CREATE TABLE `t_template_msg_send_record`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `is_deleted` tinyint(1) NULL DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除 ',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '租户id',
  `use_type` tinyint(1) NULL DEFAULT NULL COMMENT '使用类型 1-买家通知 2-商家通知',
  `message_type` tinyint(0) NULL DEFAULT NULL COMMENT '消息类别 1-订单消息 2-售后消息 3-用户消息 4-营销活动',
  `send_json` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '发送内容json',
  `send_status` tinyint(0) NULL DEFAULT NULL COMMENT '发送状态 0-发送失败  1-发送成功',
  `failure_reason` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '失败原因',
  `use_teamplate_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '使用的模板消息id',
  `mini_id` bigint(0) NULL DEFAULT NULL COMMENT '小程序表id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '模板消息发送记录' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
