/*
 Navicat Premium Data Transfer

 Source Server         : 139.155.90.217
 Source Server Type    : MySQL
 Source Server Version : 80020
 Source Host           : 139.155.90.217:3305
 Source Schema         : gruul

 Target Server Type    : MySQL
 Target Server Version : 80020
 File Encoding         : 65001

 Date: 15/10/2020 09:13:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_logistics_address
-- ----------------------------
DROP TABLE IF EXISTS t_logistics_address;
CREATE TABLE t_logistics_address (
  id bigint NOT NULL AUTO_INCREMENT ,
  tenant_id varchar(30) DEFAULT NULL ,
  shop_id varchar(64) DEFAULT NULL ,
  name varchar(64) DEFAULT NULL ,
  province varchar(64) DEFAULT '' ,
  province_id varchar(20) DEFAULT '' ,
  city varchar(64) DEFAULT '' ,
  city_id varchar(20) DEFAULT NULL ,
  country varchar(64) DEFAULT NULL ,
  country_id varchar(20) DEFAULT NULL ,
  address varchar(512) NOT NULL ,
  zip_code varchar(64) NOT NULL ,
  phone varchar(45) NOT NULL,
  def_send int DEFAULT '0' ,
  def_receive int DEFAULT '0' ,
  is_deleted int NOT NULL DEFAULT '0' ,
  create_time datetime NOT NULL ,
  update_time timestamp NOT NULL  AS CURRENT_TIMESTAMP ,
  lat varchar(64) DEFAULT '' ,
  lng varchar(64) DEFAULT '' ,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for t_logistics_company
-- ----------------------------
DROP TABLE IF EXISTS t_logistics_company;
CREATE TABLE t_logistics_company (
  id bigint NOT NULL AUTO_INCREMENT,
  name varchar(20) NOT NULL ,
  code varchar(32) NOT NULL ,
  is_deleted int DEFAULT '0' ,
  create_time datetime DEFAULT NULL ,
  update_time timestamp NULL  AS CURRENT_TIMESTAMP ,
  tenant_id varchar(32) DEFAULT NULL,
  shop_id varchar(64) DEFAULT NULL,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for t_logistics_express
-- ----------------------------
DROP TABLE IF EXISTS t_logistics_express;
CREATE TABLE t_logistics_express (
  id bigint NOT NULL AUTO_INCREMENT ,
  tenant_id varchar(32) NOT NULL ,
  shop_id varchar(64) NOT NULL ,
  name varchar(32) NOT NULL ,
  code varchar(16) NOT NULL ,
  phone varchar(16) NOT NULL ,
  address_id bigint NOT NULL ,
  customer_name varchar(64) NOT NULL ,
  customer_password varchar(64) NOT NULL ,
  link_name varchar(16) NOT NULL ,
  link_tel varchar(16) NOT NULL ,
  status int NOT NULL ,
  is_deleted int NOT NULL DEFAULT '0' ,
  create_time datetime NOT NULL ,
  update_time timestamp NOT NULL  AS CURRENT_TIMESTAMP ,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for t_logistics_express_print
-- ----------------------------
DROP TABLE IF EXISTS t_logistics_express_print;
CREATE TABLE t_logistics_express_print (
  id bigint NOT NULL AUTO_INCREMENT ,
  tenant_id varchar(32) NOT NULL ,
  shop_id varchar(64) NOT NULL ,
  device_type varchar(10) NOT NULL ,
  device_model varchar(64) NOT NULL ,
  device_name varchar(64) NOT NULL ,
  device_no varchar(64) NOT NULL ,
  device_key varchar(64) NOT NULL ,
  status int NOT NULL ,
  is_deleted int NOT NULL DEFAULT '0' ,
  create_time datetime NOT NULL ,
  update_time timestamp NOT NULL  AS CURRENT_TIMESTAMP ,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for t_logistics_include_postage
-- ----------------------------
DROP TABLE IF EXISTS t_logistics_include_postage;
CREATE TABLE t_logistics_include_postage (
  id bigint NOT NULL AUTO_INCREMENT ,
  logistics_id bigint NOT NULL ,
  type int NOT NULL ,
  region text NOT NULL ,
  piece_num int DEFAULT NULL ,
  weight decimal(18,2) DEFAULT NULL ,
  amount_num decimal(18,2) DEFAULT NULL ,
  is_deleted int NOT NULL DEFAULT '0' ,
  create_time datetime NOT NULL ,
  update_time timestamp NOT NULL  AS CURRENT_TIMESTAMP ,
  tenant_id varchar(32) DEFAULT NULL,
  shop_id varchar(64) DEFAULT NULL,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for t_logistics_manage
-- ----------------------------
DROP TABLE IF EXISTS t_logistics_manage;
CREATE TABLE t_logistics_manage (
  id bigint NOT NULL ,
  tenant_id varchar(30) NOT NULL ,
  shop_id varchar(64) NOT NULL ,
  state int NOT NULL DEFAULT '0' ,
  deliver_build_time varchar(128) NOT NULL ,
  is_deleted int NOT NULL DEFAULT '0' ,
  create_time datetime NOT NULL ,
  update_time timestamp NOT NULL  AS CURRENT_TIMESTAMP ,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for t_logistics_shipping_model
-- ----------------------------
DROP TABLE IF EXISTS t_logistics_shipping_model;
CREATE TABLE t_logistics_shipping_model (
  id bigint NOT NULL AUTO_INCREMENT ,
  logistics_id bigint NOT NULL ,
  valuation_model int NOT NULL ,
  first_piece int DEFAULT NULL ,
  first_weight decimal(18,2) DEFAULT NULL ,
  first_amount decimal(18,2) DEFAULT NULL,
  second_piece int DEFAULT NULL ,
  second_weight decimal(18,2) DEFAULT NULL ,
  second_amount decimal(18,2) DEFAULT NULL ,
  region_json text NOT NULL ,
  is_deleted int NOT NULL DEFAULT '0' ,
  create_time datetime NOT NULL ,
  update_time timestamp NOT NULL  AS CURRENT_TIMESTAMP ,
  tenant_id varchar(32) ,
  shop_id varchar(64) ,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for t_logistics_shop
-- ----------------------------
DROP TABLE IF EXISTS t_logistics_shop;
CREATE TABLE t_logistics_shop (
  id bigint NOT NULL AUTO_INCREMENT ,
  tenant_id varchar(30) NOT NULL ,
  shop_id varchar(64) NOT NULL ,
  logistics_company_id bigint NOT NULL ,
  is_default int NOT NULL DEFAULT '0' ,
  is_deleted int NOT NULL DEFAULT '0' ,
  create_time datetime NOT NULL ,
  update_time timestamp NOT NULL  AS CURRENT_TIMESTAMP ,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for t_logistics_template
-- ----------------------------
DROP TABLE IF EXISTS t_logistics_template;
CREATE TABLE t_logistics_template (
  id bigint NOT NULL AUTO_INCREMENT,
  tenant_id varchar(30) DEFAULT NULL ,
  shop_id varchar(64) DEFAULT NULL ,
  name varchar(45) DEFAULT NULL ,
  is_incl_postage int NOT NULL DEFAULT '0' ,
  valuation_model int NOT NULL ,
  choice_condition_postage int NOT NULL DEFAULT '0' ,
  is_deleted int DEFAULT '0' ,
  create_time datetime DEFAULT NULL ,
  update_time timestamp NOT NULL  AS CURRENT_TIMESTAMP ,
  PRIMARY KEY (id)
);

SET FOREIGN_KEY_CHECKS = 1;