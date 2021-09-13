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

 Date: 29/11/2019 22:18:33
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE `t_sms_order` (
                               `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                               `user_id` bigint NOT NULL COMMENT '商户id',
                               `sms_send_time` bigint NOT NULL COMMENT '短信发送时间',
                               `sms_send_type` tinyint NOT NULL COMMENT '1普通短信(待扩充)，2通知短信',
                               `sms_send_content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '短信内容',
                               `sms_send_zone` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '86' COMMENT '短信区号，默认中国86',
                               `sms_send_param` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '短信参数json',
                               `sms_send_mobiles` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '发送手机号',
                               `sms_send_count` int NOT NULL DEFAULT '1' COMMENT '短信条数,(不计算字数，目前商家最好自行去对应平台对账)',
                               `sms_send_success_count` int NOT NULL DEFAULT '0' COMMENT '短信成功条数)',
                               `sms_send_fail_count` int NOT NULL DEFAULT '0' COMMENT '短信失败条数)',
                               `sms_send_status` int NOT NULL DEFAULT '0' COMMENT '短信发送状态(0待发送，1发送中，2提交通道商，3通道商已返回,4已发送)',
                               `provider_id` bigint NOT NULL COMMENT '短信供应商id(t_sms_provider表中的id)',
                               `sms_type` tinyint DEFAULT '1' COMMENT '1腾讯 2阿里',
                               `is_deleted` tinyint(1) DEFAULT '0' COMMENT '删除状态：0->未删除；1->已删除',
                               `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                               `update_time` datetime DEFAULT NULL,
                               `template_id` bigint DEFAULT NULL COMMENT '模版id',
                               `sign_id` bigint DEFAULT NULL COMMENT '签名id',
                               PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='短信订单历史表';

-- ----------------------------
-- Table structure for t_sms_order_his_201912
-- ----------------------------
DROP TABLE IF EXISTS `t_sms_order_his_201912`;
CREATE TABLE `t_sms_order_his_201912` (
                                          `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                          `user_id` bigint NOT NULL COMMENT '商户id',
                                          `sms_send_time` bigint NOT NULL COMMENT '短信发送时间',
                                          `sms_send_type` tinyint NOT NULL COMMENT '1普通短信(待扩充)，2通知短信',
                                          `sms_send_content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '短信内容',
                                          `sms_send_zone` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '86' COMMENT '短信区号，默认中国86',
                                          `sms_send_param` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '短信参数json',
                                          `sms_send_mobiles` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '发送手机号json数组',
                                          `sms_send_count` int NOT NULL DEFAULT '1' COMMENT '短信条数,(不计算字数，目前商家最好自行去对应平台对账)',
                                          `sms_send_success_count` int NOT NULL COMMENT '短信成功条数)',
                                          `sms_send_fail_count` int NOT NULL COMMENT '短信失败条数)',
                                          `sms_send_status` int NOT NULL DEFAULT '1' COMMENT '短信发送状态(0待发送，1发送中，2提交通道商，3通道商已返回,4已发送)',
                                          `sms_provider_id` bigint NOT NULL COMMENT '短信供应商id(t_sms_provider表中的id)',
                                          `sms_type` tinyint DEFAULT '1' COMMENT '扩展字段',
                                          `is_deleted` tinyint(1) DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除',
                                          `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                          `update_time` datetime DEFAULT NULL,
                                          `template_id` bigint DEFAULT NULL COMMENT '模版id',
                                          `sign_id` bigint DEFAULT NULL COMMENT '签名id',
                                          PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='短信订单历史表';

-- ----------------------------
-- Table structure for t_sms_order_send_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_sms_order_send_detail`;
CREATE TABLE `t_sms_order_send_detail` (
                                           `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                           `user_id` bigint NOT NULL COMMENT '商户id',
                                           `sms_send_time` bigint NOT NULL COMMENT '短信发送时间',
                                           `sms_send_report_status` tinyint NOT NULL COMMENT '(0无，1发送中，2，发送失败，3发送成功)',
                                           `sms_send_report_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '第三方短信回调状态码',
                                           `sms_send_report_msg` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '第三方短信回调详情',
                                           `sms_send_mobile` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '发送手机号',
                                           `sms_send_report_sms_size` int NOT NULL COMMENT '第三方短信回调扣费条数',
                                           `sms_send_report_time` datetime NOT NULL COMMENT '短信回调时间',
                                           `sms_send_content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '短信内容',
                                           `is_deleted` tinyint(1) DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除',
                                           `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                           `update_time` datetime DEFAULT NULL,
                                           PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='短信订单历史表';

-- ----------------------------
-- Table structure for t_sms_order_send_detail_his_201912
-- ----------------------------
DROP TABLE IF EXISTS `t_sms_order_send_detail_his_201912`;
CREATE TABLE `t_sms_order_send_detail_his_201912` (
                                                      `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                                      `user_id` bigint NOT NULL COMMENT '商户id',
                                                      `sms_send_time` bigint NOT NULL COMMENT '短信发送时间',
                                                      `sms_send_report_status` tinyint NOT NULL COMMENT '(0无，1发送中，2，发送失败，3发送成功)',
                                                      `sms_send_report_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '第三方短信回调状态码',
                                                      `sms_send_report_msg` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '第三方短信回调详情',
                                                      `sms_send_mobile` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '发送手机号',
                                                      `sms_send_report_sms_size` int NOT NULL COMMENT '第三方短信回调扣费条数',
                                                      `sms_send_report_time` datetime NOT NULL COMMENT '短信回调时间',
                                                      `sms_send_content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '短信内容',
                                                      `is_deleted` tinyint(1) DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除',
                                                      `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                                      `update_time` datetime DEFAULT NULL,
                                                      PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='短信订单历史表';

-- ----------------------------
-- Table structure for t_sms_provider
-- ----------------------------
DROP TABLE IF EXISTS `t_sms_provider`;
CREATE TABLE `t_sms_provider` (
                                  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                  `user_id` bigint NOT NULL COMMENT '商户id',
                                  `sms_provider_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '短信供应商名称',
                                  `sms_provider_appId` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '短信供应商应用id',
                                  `sms_provider_app_secret` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '短信供应商应用密钥',
                                  `sms_provider_available_count` int NOT NULL COMMENT '短信供应商可用条数',
                                  `sms_provider_used_count` int NOT NULL COMMENT '短信供应商已用条数',
                                  `sms_provider_total_count` int NOT NULL COMMENT '短信供应商总条数',
                                  `sms_provider_status` tinyint(1) NOT NULL COMMENT '0正常，1停用',
                                  `is_deleted` tinyint(1) DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除',
                                  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                  `update_time` datetime DEFAULT NULL,
                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='短信订单历史表';

-- ----------------------------
-- Table structure for t_sms_sign
-- ----------------------------
DROP TABLE IF EXISTS `t_sms_sign`;
CREATE TABLE `t_sms_sign` (
                              `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                              `user_id` bigint NOT NULL COMMENT '商户id',
                              `sms_provider_id` bigint NOT NULL COMMENT '供应商idt_sms_provider的id',
                              `sms_sign` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '短信签名（每次发送之前都需提前报备，可通过接口或者手动去页面提交报备）',
                              `sms_sign_is_pass` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0未审核，1审核中，2审核通过，3审核不通过',
                              `sms_sign_remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '备注',
                              `is_deleted` tinyint(1) DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除',
                              `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                              `update_time` datetime DEFAULT NULL,
                              `sms_sign_type` int DEFAULT NULL COMMENT '\n    0：企事业单位的全称或简称。\n    1：工信部备案网站的全称或简称。\n    2：APP应用的全称或简称。\n    3：公众号或小程序的全称或简称。\n    4：电商平台店铺名的全称或简称。\n    5：商标名的全称或简称\n',
                              `sign_type` int DEFAULT NULL COMMENT '1阿里，2腾讯',
                              PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='短信签名表';

-- ----------------------------
-- Table structure for t_sms_template
-- ----------------------------
DROP TABLE IF EXISTS `t_sms_template`;
CREATE TABLE `t_sms_template` (
                                  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                  `user_id` bigint NOT NULL COMMENT '商户id',
                                  `sms_provider_id` bigint NOT NULL COMMENT '供应商idt_sms_provider的id',
                                  `sms_template_content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模版内容 （在第三方创建待模版内容）',
                                  `sms_template_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '模版编码（在第三方创建待模版编码）',
                                  `sms_template_is_pass` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0未审核，1审核中，2审核通过，3审核不通过',
                                  `sms_remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
                                  `is_deleted` tinyint DEFAULT '0' COMMENT '删除状态：0->未删除；1->已删除',
                                  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                  `update_time` datetime DEFAULT NULL,
                                  `template_type` int DEFAULT NULL COMMENT '类型1阿里，2腾讯',
                                  `sms_template_type` int DEFAULT NULL COMMENT '\n    0：验证码。\n    1：短信通知。\n    2：推广短信。\n    3：国际/港澳台消息。\n',
                                  `sms_template_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '模版名称',
                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='短信模版表';
