/*
 Navicat Premium Data Transfer

 Source Server         : local-mysql-5.7
 Source Server Type    : MySQL
 Source Server Version : 50727
 Source Host           : localhost:3306
 Source Schema         : gruul

 Target Server Type    : MySQL
 Target Server Version : 50727
 File Encoding         : 65001

 Date: 07/11/2019 22:08:11
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_payment
-- ----------------------------
CREATE TABLE `t_ent_pay` (
                             `id` bigint NOT NULL AUTO_INCREMENT COMMENT '@desc id',
                             `pay_channel` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '@desc 支付渠道',
                             `amount` int NOT NULL COMMENT '@desc 订单总金额，单位为分',
                             `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '@desc 企业付款备注',
                             `business_params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '@desc 附加数据,格式为json字符串,怎么发送怎么返回',
                             `spbill_create_ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc 终端ip',
                             `transaction_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '@desc 支付流水号',
                             `trade_status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc 交易状态：1、 发起支付 2、 交易支付成功 3、交易支付失败 4、交易同步返回成功单业务未成功',
                             `tenant_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc 租户标识',
                             `openid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc 用户标识',
                             `check_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc 校验用户姓名选项',
                             `re_user_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc 收款用户姓名',
                             `create_time` datetime DEFAULT NULL COMMENT '@desc 创建时间',
                             `update_time` datetime DEFAULT NULL COMMENT '@desc 修改时间',
                             `is_deleted` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '@desc 删除标识0否 1是',
                             `pay_time` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '@desc 支付成功时间',
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='服务商对个人支付表';

-- ----------------------------
-- Table structure for t_ent_pay_back_log
-- ----------------------------
DROP TABLE IF EXISTS `t_ent_pay_back_log`;
CREATE TABLE `t_ent_pay_back_log` (
                                      `id` bigint NOT NULL AUTO_INCREMENT COMMENT '@desc id',
                                      `ent_pay_id` bigint DEFAULT NULL COMMENT '@desc 关联支付单主键id',
                                      `pay_channel` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc 支付渠道:1-微信',
                                      `pay_channel_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc 支付渠道类型',
                                      `callback_context` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '@desc 回调信息json内容',
                                      `create_time` datetime DEFAULT NULL COMMENT '@desc 创建时间',
                                      `update_time` datetime DEFAULT NULL COMMENT '@desc 更新时间',
                                      `is_deleted` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc 删除标识',
                                      `tenant_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc 租户标识',
                                      PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='商户支付个人回调日志表';

-- ----------------------------
-- Table structure for t_payment
-- ----------------------------
DROP TABLE IF EXISTS `t_payment`;
CREATE TABLE `t_payment` (
                             `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
                             `is_deleted` tinyint(1) DEFAULT '0' COMMENT '删除状态：0->未删除；1->已删除',
                             `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                             `create_time` datetime DEFAULT NULL COMMENT '接受服务请求时间',
                             `timeout_express` datetime DEFAULT NULL COMMENT '该笔订单允许的最晚付款时间，逾期将关闭交易。',
                             `pay_channel` tinyint DEFAULT NULL COMMENT '支付渠道:1-微信',
                             `fee_type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '货币类型 CNY：人民币,符合ISO 4217标准的三位字母代码',
                             `total_fee` decimal(10,2) DEFAULT NULL COMMENT '订单总金额，单位为元，精确到小数点后两位',
                             `body` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '对一笔交易的具体描述信息',
                             `business_params` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '附加数据,格式为json字符串,怎么发送怎么返回',
                             `terminal_ip` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '终端ip',
                             `transaction_id` bigint DEFAULT NULL COMMENT '支付流水号',
                             `business_notify_url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '业务回调地址',
                             `third_party_notify_status` tinyint(1) DEFAULT '0' COMMENT '第三方回调是否已处理 0-未处理 1-已处理',
                             `business_notify_status` tinyint(1) DEFAULT NULL COMMENT '业务方是否已正确处理  0-未处理 1-已处理',
                             `trade_status` tinyint DEFAULT NULL COMMENT '交易状态：1（交易创建，等待买家付款）、2（未付款交易超时关闭）、3（交易支付成功）',
                             `third_party_notify_number` tinyint(1) DEFAULT NULL COMMENT '回调次数',
                             `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '租户标识',
                             `route_key` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '路由键',
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for t_payment_record
-- ----------------------------
DROP TABLE IF EXISTS `t_payment_record`;
CREATE TABLE `t_payment_record` (
                                    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
                                    `request_params` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '请求对象数据',
                                    `send_param` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '最终发送数据',
                                    `notify_param` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '第三方回调的数据',
                                    `payment_id` bigint DEFAULT NULL COMMENT '支付 记录表id',
                                    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for t_payment_refund
-- ----------------------------
DROP TABLE IF EXISTS `t_payment_refund`;
CREATE TABLE `t_payment_refund` (
                                    `order_id` bigint NOT NULL COMMENT '订单id',
                                    `asyn_request` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '请求退款值',
                                    `asyn_result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '请求退款同步返回值',
                                    `tenant_id` bigint DEFAULT NULL COMMENT '租户id',
                                    `syn_callback` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '回调数据',
                                    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                    `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                    `is_deleted` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标识',
                                    `id` bigint NOT NULL AUTO_INCREMENT,
                                    `route_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '路由键',
                                    PRIMARY KEY (`id`,`order_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='支付服务退款log';

-- ----------------------------
-- Table structure for t_payment_wechat
-- ----------------------------
DROP TABLE IF EXISTS `t_payment_wechat`;
CREATE TABLE `t_payment_wechat` (
                                    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
                                    `trade_type` int DEFAULT NULL COMMENT '交易类型 1-JSAPI支付（或小程序支付）、2-Native支付、3-app支付，4-H5支付，',
                                    `payment_id` bigint DEFAULT NULL COMMENT '主记录表id',
                                    `out_trade_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '业务订单号',
                                    `subject` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '商品的标题/交易标题/订单标题/订单关键字等。',
                                    `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '商户标识',
                                    `open_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户openId',
                                    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

