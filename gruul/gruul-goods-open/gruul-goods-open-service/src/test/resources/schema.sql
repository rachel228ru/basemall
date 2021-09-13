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

 Date: 19/10/2020 10:23:16
*/

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_attribute_template
-- ----------------------------
DROP TABLE IF EXISTS t_attribute_template;
CREATE TABLE t_attribute_template (
  id bigint NOT NULL AUTO_INCREMENT,
  tenant_id varchar(32) ,
  create_time datetime DEFAULT NULL ,
  update_time datetime DEFAULT NULL ,
  is_deleted tinyint unsigned NOT NULL DEFAULT '0' ,
  shop_id varchar(64) ,
  parent_id bigint DEFAULT NULL ,
  name varchar(64) ,
  content text ,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for t_distribution_set
-- ----------------------------
DROP TABLE IF EXISTS t_distribution_set;
CREATE TABLE t_distribution_set (
  id bigint NOT NULL AUTO_INCREMENT,
  tenant_id varchar(32) ,
  create_time datetime DEFAULT NULL ,
  update_time datetime DEFAULT NULL ,
  is_deleted tinyint unsigned NOT NULL DEFAULT '0' ,
  shop_id varchar(64) ,
  product_id bigint DEFAULT NULL ,
  sku_id bigint DEFAULT NULL ,
  first_commission decimal(10,2) DEFAULT '0.00' ,
  second_commission decimal(10,2) DEFAULT '0.00' ,
  third_commission decimal(10,2) DEFAULT '0.00' ,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for t_member_price
-- ----------------------------
DROP TABLE IF EXISTS t_member_price;
CREATE TABLE t_member_price (
  id bigint NOT NULL AUTO_INCREMENT,
  tenant_id varchar(32) ,
  create_time datetime DEFAULT NULL ,
  update_time datetime DEFAULT NULL ,
  is_deleted tinyint unsigned NOT NULL DEFAULT '0' ,
  shop_id varchar(64) ,
  product_id bigint DEFAULT NULL ,
  sku_id bigint DEFAULT NULL ,
  member_level_id bigint DEFAULT '0' ,
  member_price decimal(10,2) DEFAULT '0.00' ,
  member_level_name varchar(20) ,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for t_product
-- ----------------------------
DROP TABLE IF EXISTS t_product;
CREATE TABLE t_product (
  id bigint NOT NULL AUTO_INCREMENT,
  tenant_id varchar(32) ,
  create_time datetime DEFAULT NULL ,
  update_time datetime DEFAULT NULL ,
  is_deleted tinyint unsigned NOT NULL DEFAULT '0' ,
  shop_id varchar(64) ,
  sorting_category_id bigint DEFAULT NULL ,
  provider_id bigint DEFAULT NULL ,
  attribute_id bigint DEFAULT NULL ,
  attribute_name varchar(64) ,
  freight_template_id bigint DEFAULT NULL ,
  sale_mode tinyint unsigned DEFAULT NULL ,
  limit_type tinyint unsigned DEFAULT NULL ,
  name varchar(64) ,
  pic varchar(512) ,
  wide_pic varchar(512) ,
  album_pics varchar(1024) ,
  video_url varchar(255) ,
  product_sn varchar(16) ,
  status tinyint unsigned DEFAULT NULL ,
  place tinyint(3) unsigned DEFAULT NULL ,
  csv_url varchar(255) ,
  domino_state tinyint unsigned DEFAULT '0' ,
  sort int DEFAULT '0' ,
  sale int DEFAULT '0' ,
  gift_point int DEFAULT '0' ,
  unit varchar(16) ,
  weight decimal(10,2) DEFAULT '0.00' ,
  service_ids varchar(64) ,
  detail text ,
  is_open_specs tinyint unsigned DEFAULT NULL ,
  attribute text ,
  sale_describe text ,
  score decimal(3,1) DEFAULT '0.0' ,
  add_launch_area tinyint(1) DEFAULT '0' ,
  launch_area varchar(255) ,
  commission_type tinyint(1) DEFAULT NULL ,
  commander_commission decimal(10,2) DEFAULT NULL ,
  distribution_mode tinyint(1) DEFAULT '0' ,
  use_member_price tinyint(1) DEFAULT '0' ,
  use_rebate tinyint(1) DEFAULT '0' ,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for t_product_attribute
-- ----------------------------
DROP TABLE IF EXISTS t_product_attribute;
CREATE TABLE t_product_attribute (
  id bigint NOT NULL AUTO_INCREMENT,
  tenant_id varchar(32) ,
  create_time datetime DEFAULT NULL ,
  update_time datetime DEFAULT NULL ,
  is_deleted tinyint unsigned NOT NULL DEFAULT '0' ,
  shop_id varchar(64) ,
  product_id bigint DEFAULT NULL ,
  name varchar(64) ,
  value varchar(128) ,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for t_product_show_category
-- ----------------------------
DROP TABLE IF EXISTS t_product_show_category;
CREATE TABLE t_product_show_category (
  id bigint NOT NULL AUTO_INCREMENT,
  tenant_id varchar(32) ,
  create_time datetime DEFAULT NULL ,
  update_time datetime DEFAULT NULL ,
  is_deleted tinyint unsigned NOT NULL DEFAULT '0' ,
  shop_id varchar(64) ,
  product_id bigint NOT NULL ,
  parent_id bigint NOT NULL ,
  show_category_id bigint DEFAULT NULL ,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for t_rebate_set
-- ----------------------------
DROP TABLE IF EXISTS t_rebate_set;
CREATE TABLE t_rebate_set (
  id bigint NOT NULL AUTO_INCREMENT,
  tenant_id varchar(32) DEFAULT NULL ,
  create_time datetime DEFAULT NULL ,
  update_time datetime DEFAULT NULL ,
  is_deleted tinyint unsigned NOT NULL DEFAULT '0' ,
  shop_id varchar(64) DEFAULT NULL ,
  rebate_ratio decimal(10,2) DEFAULT '0.00' ,
  balance_use_condition varchar(16) DEFAULT NULL ,
  rebate_condition varchar(16) DEFAULT NULL ,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for t_shopping_cart
-- ----------------------------
DROP TABLE IF EXISTS t_shopping_cart;
CREATE TABLE t_shopping_cart (
  id bigint NOT NULL AUTO_INCREMENT,
  tenant_id varchar(32) ,
  create_time datetime DEFAULT NULL ,
  update_time datetime DEFAULT NULL ,
  is_deleted tinyint unsigned NOT NULL DEFAULT '0' ,
  shop_id varchar(64) ,
  product_id bigint DEFAULT NULL ,
  user_id varchar(28) ,
  sku_id bigint NOT NULL ,
  goods_number int NOT NULL DEFAULT '0' ,
  activity_id bigint DEFAULT NULL ,
  activity_start_time datetime DEFAULT NULL ,
  activity_end_time datetime DEFAULT NULL ,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for t_show_category
-- ----------------------------
DROP TABLE IF EXISTS t_show_category;
CREATE TABLE t_show_category (
  id bigint NOT NULL AUTO_INCREMENT,
  tenant_id varchar(32) ,
  create_time datetime DEFAULT NULL ,
  update_time datetime DEFAULT NULL ,
  is_deleted tinyint unsigned NOT NULL DEFAULT '0' ,
  parent_id bigint DEFAULT NULL ,
  shop_id varchar(64) ,
  name varchar(64) ,
  level int DEFAULT NULL ,
  sort int DEFAULT NULL ,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for t_sku_rebate
-- ----------------------------
DROP TABLE IF EXISTS t_sku_rebate;
CREATE TABLE t_sku_rebate (
  id bigint NOT NULL AUTO_INCREMENT,
  tenant_id varchar(32) DEFAULT NULL ,
  create_time datetime DEFAULT NULL ,
  update_time datetime DEFAULT NULL ,
  is_deleted tinyint unsigned NOT NULL DEFAULT '0' ,
  shop_id varchar(64) DEFAULT NULL ,
  product_id bigint DEFAULT NULL ,
  sku_id bigint DEFAULT NULL ,
  rebate_price decimal(10,2) DEFAULT '0.00' ,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for t_sku_stock
-- ----------------------------
DROP TABLE IF EXISTS t_sku_stock;
CREATE TABLE t_sku_stock (
  id bigint NOT NULL AUTO_INCREMENT,
  tenant_id varchar(32) ,
  create_time datetime DEFAULT NULL ,
  update_time datetime DEFAULT NULL ,
  is_deleted tinyint unsigned NOT NULL DEFAULT '0' ,
  shop_id varchar(64) ,
  version int DEFAULT NULL ,
  product_id bigint DEFAULT NULL ,
  sku_code varchar(64) ,
  specs varchar(64) ,
  weight decimal(10,2) DEFAULT '0.00' ,
  pic varchar(255) ,
  price decimal(10,2) DEFAULT '0.00' ,
  original_price decimal(10,2) DEFAULT '0.00' ,
  stock int DEFAULT '0' ,
  low_stock int DEFAULT '0' ,
  sale int DEFAULT '0' ,
  per_limit int DEFAULT '0' ,
  gift_integration int DEFAULT '0' ,
  rebate_type bigint DEFAULT '0' ,
  member_type bigint DEFAULT '0' ,
  PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for t_sorting_category
-- ----------------------------
DROP TABLE IF EXISTS t_sorting_category;
CREATE TABLE t_sorting_category (
  id bigint NOT NULL AUTO_INCREMENT,
  tenant_id varchar(32) ,
  create_time datetime DEFAULT NULL ,
  update_time datetime DEFAULT NULL ,
  is_deleted tinyint unsigned NOT NULL DEFAULT '0' ,
  shop_id varchar(64) ,
  name varchar(64) ,
  sort int DEFAULT NULL ,
  PRIMARY KEY (id)
);


-- ----------------------------
-- Table structure for t_supplier
-- ----------------------------
DROP TABLE IF EXISTS t_supplier;
CREATE TABLE t_supplier (
  id bigint NOT NULL AUTO_INCREMENT,
  tenant_id varchar(32) ,
  create_time datetime DEFAULT NULL ,
  update_time datetime DEFAULT NULL ,
  is_deleted tinyint unsigned NOT NULL DEFAULT '0' ,
  shop_id varchar(64) ,
  user_id varchar(28) DEFAULT NULL ,
  supplier_sn varchar(16) ,
  name varchar(64) ,
  mobile bigint DEFAULT NULL ,
  province varchar(64) ,
  city varchar(64) ,
  country varchar(64) ,
  address varchar(255) ,
  area varchar(255) ,
  product_info varchar(512) ,
  status int DEFAULT NULL ,
  come_from int DEFAULT NULL ,
  score decimal(3,1) DEFAULT '0.0' ,
  template_id varchar(64) DEFAULT NULL ,
  PRIMARY KEY (id)
);