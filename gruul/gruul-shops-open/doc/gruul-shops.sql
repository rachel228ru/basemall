
----------------------------- shops -----------------------------
CREATE TABLE `t_account_center` (
                                    `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                                    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                    `is_deleted` tinyint DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除',
                                    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                    `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '租户id',
                                    `head_style` tinyint(1) DEFAULT NULL COMMENT '头部风格 1-系统风格 2-自定义风格',
                                    `custom_style` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '自定义风格样式,json存储',
                                    `get_cart_text` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '领卡文案',
                                    `hide_cart_inlet` tinyint(1) DEFAULT NULL COMMENT '非会员隐藏领卡入口 0-隐藏 1-显示',
                                    `order_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '\r\n订单信息 json存储',
                                    `menu_style` tinyint(1) DEFAULT NULL COMMENT '菜单选择样式 1-展开式 2-折叠式',
                                    `code_style` tinyint(1) DEFAULT NULL COMMENT '提货码样式,1样式1  2样式2',
                                    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户中心配置';

-- ----------------------------
-- Table structure for t_account_center_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_account_center_menu`;
CREATE TABLE `t_account_center_menu` (
                                         `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                                         `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                         `is_deleted` tinyint DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除',
                                         `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                         `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '租户id',
                                         `menu_name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '菜单名称',
                                         `menu_icon_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '菜单url',
                                         `style_type` tinyint(1) DEFAULT NULL COMMENT '菜单样式类别 1-展开式 2-折叠式',
                                         `sort_index` tinyint DEFAULT NULL COMMENT '排序位置',
                                         `p_id` bigint DEFAULT NULL COMMENT '父级id',
                                         `hide_menu` tinyint(1) DEFAULT NULL COMMENT '0隐藏1显示',
                                         `allow_use` tinyint(1) DEFAULT NULL COMMENT '是否可用 0不可用 1-可用',
                                         `default_icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '默认图标',
                                         `split_flag` tinyint(1) DEFAULT NULL COMMENT '是否有分割线',
                                         `link_select_item` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '跳转类型json',
                                         PRIMARY KEY (`id`) USING BTREE,
                                         KEY `is_deleted` (`is_deleted`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1340 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户中心菜单配置';
-- ----------------------------
-- Table structure for t_shop_guide_page
-- ----------------------------
DROP TABLE IF EXISTS `t_shop_guide_page`;
CREATE TABLE `t_shop_guide_page` (
                                     `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                                     `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '网址路径',
                                     `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '路径',
                                     `link` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'path名称 用于比较',
                                     `app_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '小程序appid 用于跳转',
                                     `type` tinyint(1) DEFAULT NULL COMMENT ' 0为path 1为app_id',
                                     `shop_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '店铺id',
                                     `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '租户id',
                                     `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                     `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                     `is_deleted` tinyint(1) DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除',
                                     PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
-- ----------------------------
-- Table structure for t_shop_guide_page_switch
-- ----------------------------
DROP TABLE IF EXISTS `t_shop_guide_page_switch`;
CREATE TABLE `t_shop_guide_page_switch` (
                                            `id` bigint NOT NULL AUTO_INCREMENT,
                                            `is_open` tinyint(1) DEFAULT NULL COMMENT '引导页是否开启 0 -->未开启 1-->已开启',
                                            `shop_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '店铺id',
                                            `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '租户id',
                                            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                            `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                            `is_deleted` tinyint(1) DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除',
                                            PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
-- ----------------------------
-- Table structure for t_shops_menu_config
-- ----------------------------
DROP TABLE IF EXISTS `t_shops_menu_config`;
CREATE TABLE `t_shops_menu_config` (
                                       `id` bigint NOT NULL AUTO_INCREMENT COMMENT '@desc: id',
                                       `tenant_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '@desc  活动关联商家',
                                       `shop_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '@desc  页面关联店铺ID',
                                       `is_deleted` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '@desc  标识  0正常(def) 1已删除',
                                       `properties` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '@desc  组件属性 json串',
                                       `create_time` datetime DEFAULT NULL COMMENT '@desc  创建时间',
                                       `update_time` datetime DEFAULT NULL COMMENT '@desc  修改时间',
                                       `operator_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '@desc  操作人id',
                                       `operator_name` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '@desc  操作人name',
                                       PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='店铺合伙人菜单属性配置表';

-- ----------------------------
-- Table structure for t_shops_partner
-- ----------------------------
DROP TABLE IF EXISTS `t_shops_partner`;
CREATE TABLE `t_shops_partner` (
                                   `id` bigint NOT NULL AUTO_INCREMENT COMMENT '@desc id',
                                   `create_time` datetime DEFAULT NULL COMMENT '@desc 创建时间',
                                   `update_time` datetime DEFAULT NULL COMMENT '@desc 修改时间',
                                   `partner_id` bigint DEFAULT NULL COMMENT '@desc 总店id',
                                   `tenant_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc 租户id',
                                   `is_deleted` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc 删除标识',
                                   `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc 手机号',
                                   `name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc 姓名',
                                   `pass` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc 密码',
                                   `region` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc 地域',
                                   `province_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc  省code',
                                   `city_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc  市code',
                                   `area_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc  区域Code',
                                   `card_id_up` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc 身份证正面',
                                   `card_id_down` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc 身份证反面',
                                   `map_x` double DEFAULT NULL COMMENT '@desc 地图X',
                                   `map_y` double DEFAULT NULL COMMENT '@desc 地图Y',
                                   `partner_model` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc 合伙人模式  0加盟 1子公司',
                                   `approval_status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc 状态 0 未审核 1已审核 2 拒绝',
                                   `prohibit_status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc 禁用状态 0 未禁用 1 已禁用',
                                   `province_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc 省name',
                                   `city_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc 市name',
                                   `area_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc 区name',
                                   `shop_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc 本店店铺id',
                                   `invitation_code` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邀请码',
                                   `platform_id` bigint DEFAULT NULL COMMENT '@desc  平台用户id',
                                   PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for t_shops_renovation_assembly
-- ----------------------------
DROP TABLE IF EXISTS `t_shops_renovation_assembly`;
CREATE TABLE `t_shops_renovation_assembly` (
                                               `id` bigint NOT NULL AUTO_INCREMENT COMMENT '@desc: id',
                                               `tenant_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '@desc  活动关联商家',
                                               `shop_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '@desc  页面关联店铺ID',
                                               `page_id` bigint DEFAULT NULL COMMENT '@desc  所属页面ID',
                                               `is_deleted` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '@desc  标识  0正常(def) 1已删除',
                                               `properties` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '@desc  组件属性 json串',
                                               `create_time` datetime DEFAULT NULL COMMENT '@desc  创建时间',
                                               `update_time` datetime DEFAULT NULL COMMENT '@desc  修改时间',
                                               `operator_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '@desc  操作人id',
                                               `operator_name` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '@desc  操作人name',
                                               PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4802 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='店铺装修模板页面组件属性表';

-- ----------------------------
-- Table structure for t_shops_renovation_page
-- ----------------------------
DROP TABLE IF EXISTS `t_shops_renovation_page`;
CREATE TABLE `t_shops_renovation_page` (
                                           `id` bigint NOT NULL AUTO_INCREMENT COMMENT '	\r\n@desc: id',
                                           `tenant_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '@desc  活动关联商家',
                                           `shop_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '@desc  页面关联店铺ID',
                                           `template_id` bigint DEFAULT NULL COMMENT '@desc  所属模板ID',
                                           `is_deleted` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '@desc  逻辑删除标识  0正常(def) 1已删除',
                                           `page_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '@desc  自定义页面name ',
                                           `create_time` datetime DEFAULT NULL COMMENT '@desc  创建时间',
                                           `update_time` datetime DEFAULT NULL COMMENT '@desc  修改时间',
                                           `operator_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '@desc  操作人id',
                                           `operator_name` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '@desc  操作人name',
                                           `is_def` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '@desc  是否默认首页',
                                           `copy_plugin_flag` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '@desc  默认模板copy,控件json配置页面id对应关系',
                                           `type` tinyint(1) DEFAULT NULL COMMENT '@desc  分类页',
                                           `model_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '@desc  modelId',
                                           PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='店铺装修模板页面表';

-- ----------------------------
-- Table structure for t_shops_renovation_plugin
-- ----------------------------
DROP TABLE IF EXISTS `t_shops_renovation_plugin`;
CREATE TABLE `t_shops_renovation_plugin` (
                                             `id` bigint NOT NULL AUTO_INCREMENT COMMENT '@desc: id',
                                             `tenant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc  活动关联商家',
                                             `template_id` bigint DEFAULT NULL COMMENT '	@desc  所属模板ID',
                                             `shop_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc 页面关联店铺ID',
                                             `plugin_properties` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '@desc  控件',
                                             `is_deleted` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc  删除标识',
                                             `create_time` datetime DEFAULT NULL COMMENT '@desc  创建时间',
                                             `update_time` datetime DEFAULT NULL COMMENT '@desc  修改时间',
                                             `operator_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc  操作人id',
                                             `operator_name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc  操作人name',
                                             `plugin_name_cn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc  控件名称中文',
                                             `is_mandatory` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc  是否允许取消 0否 1是',
                                             `is_selection` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc  是否选中 0否 1是',
                                             `plugin_name_en` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc  控件名称英文',
                                             `spare` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc  备用字段',
                                             `copy_plugin_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc  默认模板copy,控件json配置页面id对应关系',
                                             PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='店铺装修页面全局控件属性表';

-- ----------------------------
-- Table structure for t_shops_renovation_template
-- ----------------------------
DROP TABLE IF EXISTS `t_shops_renovation_template`;
CREATE TABLE `t_shops_renovation_template` (
                                               `id` bigint NOT NULL AUTO_INCREMENT COMMENT '@desc id',
                                               `tenant_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '@desc 租户ID',
                                               `shop_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '	@desc 模板关联店铺ID',
                                               `is_deleted` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '@desc  逻辑删除标识  0正常(def) 1已删除',
                                               `type` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '@desc  模板类型 0自定义 1拼团 2商超',
                                               `colour` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '1' COMMENT '@desc 模板全局颜色 0红 1绿 2蓝',
                                               `online_status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '@desc 模板是否使用中 0 否, 1 是',
                                               `create_time` datetime DEFAULT NULL COMMENT '@desc 创建时间',
                                               `update_time` datetime DEFAULT NULL COMMENT '@desc 修改时间',
                                               `operator_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '@desc 操作人id',
                                               `operator_name` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '	\r\n@desc 操作人name',
                                               `is_dev_template` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '@desc 默认模板 1是 0否',
                                               `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '@desc 模板name',
                                               `is_copy_template` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '@desc 是否copy源模板 0否 1是',
                                               PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='店铺装修模板表';

-- ----------------------------
-- Table structure for t_shops_sales_config
-- ----------------------------
DROP TABLE IF EXISTS `t_shops_sales_config`;
CREATE TABLE `t_shops_sales_config` (
                                        `id` bigint NOT NULL AUTO_INCREMENT COMMENT '@desc id',
                                        `sales_volume` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc 销售额',
                                        `shares` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc 入股费百分比',
                                        `level` int DEFAULT NULL COMMENT '@desc 层级',
                                        `create_time` datetime DEFAULT NULL COMMENT '@desc 创建时间',
                                        `update_time` datetime DEFAULT NULL COMMENT '@desc 修改时间',
                                        `shop_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc 店铺id',
                                        `tenant_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc 租户id',
                                        `is_deleted` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc 删除标识',
                                        PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for t_shops_search_terms
-- ----------------------------
DROP TABLE IF EXISTS `t_shops_search_terms`;
CREATE TABLE `t_shops_search_terms` (
                                        `id` bigint NOT NULL COMMENT '@desc id',
                                        `shop_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc 商铺id',
                                        `create_time` datetime DEFAULT NULL COMMENT '@desc 创建时间',
                                        `update_time` datetime DEFAULT NULL COMMENT '@desc 修改时间',
                                        `is_deleted` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc 删除标识',
                                        `tenant_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc 租户id',
                                        `terms` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc 词语 多个以英文逗号分隔',
                                        `def_terms` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc 默认 搜索词语',
                                        `is_show` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '@desc 是否显示',
                                        PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='店铺默认搜索关键字';