<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 11:36:06
-->
<template>
  <div class="decoration__page">
    <slot name="EditorControlPage"></slot>
    <div class="editor__preview_new">
      <PhoneView
        :custom-style="customStyle"
        :fold-menu="foldMenu"
        :head-style-config="headStyleConfig"
        :form-data="formData"
        :order-info="orderInfo"
        :expand-menu="expandMenu"
      ></PhoneView>
    </div>
    <div class="editor__form_wrap">
      <div slot="header" class="clearfix editor__form_header">
        <span>用户中心组件设置</span>
      </div>
      <div class="editor__form_wrap_main">
        <el-scrollbar style="height:100%">
          <div class="pd20">
            <transition name="el-fade-in-linear" mode="out-in">
              <el-row class="setting">
                <el-form ref="formData" :model="formData" label-width="80px">
                  <div class="setting__collapse setinfo">
                    <div
                      class="setting__collapse--header"
                      @click="collapseVisibleHandle('Info')"
                    >
                      <span>用户信息</span>
                      <span style="color: #9797A1;padding-left: 40px;"></span>
                    </div>
                    <div
                      class="setting__collapse--content"
                      style="padding:20px 0"
                      v-show="setVisible.setInfoVisible"
                    >
                      <el-form-item label="头部风格">
                        <el-radio-group v-model="formData.headStyle">
                          <el-radio :label="1">系统风格</el-radio>
                          <el-radio :label="2">自定义风格</el-radio>
                        </el-radio-group>
                      </el-form-item>
                      <el-row v-show="formData.headStyle === 2">
                        <el-form-item label="背景图片">
                          <upload-file
                            :img-url.sync="customStyle.backgroundImage"
                          ></upload-file>
                        </el-form-item>
                      </el-row>
                    </div>
                  </div>
                  <div class="setting__collapse setorder">
                    <div
                      class="setting__collapse--header"
                      @click="collapseVisibleHandle('Order')"
                    >
                      订单信息
                    </div>
                    <div
                      class="setting__collapse--content"
                      v-if="setVisible.setOrderVisible"
                    >
                      <div class="setorder__item">
                        <span class="setorder__item--text">
                          {{ orderInfo.waitIcon.name }}
                        </span>
                        <i
                          class="el-icon-edit-outline el-collapse-item__arrow setorder__item--icon"
                          @click="
                            orderClickHandle('waitIcon', orderInfo.waitIcon)
                          "
                        />
                      </div>
                      <div class="setorder__item">
                        <span class="setorder__item--text">
                          {{ orderInfo.waitDelivered.name }}
                        </span>
                        <i
                          class="el-icon-edit-outline el-collapse-item__arrow setorder__item--icon"
                          @click="
                            orderClickHandle(
                              'waitDelivered',
                              orderInfo.waitDelivered,
                            )
                          "
                        />
                      </div>
                      <div class="setorder__item">
                        <span class="setorder__item--text">
                          {{ orderInfo.deliveryIcon.name }}
                        </span>
                        <i
                          class="el-icon-edit-outline el-collapse-item__arrow setorder__item--icon"
                          @click="
                            orderClickHandle(
                              'deliveryIcon',
                              orderInfo.deliveryIcon,
                            )
                          "
                        />
                      </div>
                      <div class="setorder__item">
                        <span class="setorder__item--text">
                          {{ orderInfo.waitPickingIcon.name }}
                        </span>
                        <i
                          class="el-icon-edit-outline el-collapse-item__arrow setorder__item--icon"
                          @click="
                            orderClickHandle(
                              'waitPickingIcon',
                              orderInfo.waitPickingIcon,
                            )
                          "
                        />
                      </div>
                      <div class="setorder__item">
                        <span class="setorder__item--text">
                          {{ orderInfo.afterSaleIcon.name }}
                        </span>
                        <i
                          class="el-icon-edit-outline el-collapse-item__arrow setorder__item--icon"
                          @click="
                            orderClickHandle(
                              'afterSaleIcon',
                              orderInfo.afterSaleIcon,
                            )
                          "
                        />
                      </div>
                      <div class="setorder__item">
                        <span class="setorder__item--text">
                          {{ orderInfo.evaluateIcon.name }}
                        </span>
                        <i
                          class="el-icon-edit-outline el-collapse-item__arrow setorder__item--icon"
                          @click="
                            orderClickHandle(
                              'evaluateIcon',
                              orderInfo.evaluateIcon,
                            )
                          "
                        />
                      </div>
                    </div>
                  </div>
                  <div class="setting__collapse setmenu">
                    <div
                      class="setting__collapse--header"
                      @click="collapseVisibleHandle('Menu')"
                    >
                      菜单
                    </div>
                    <div
                      class="setting__collapse--content"
                      v-if="setVisible.setMenuVisible"
                    >
                      <div class="menu__form">
                        <div class="menu__form--item">
                          <label class="el-form-item__label">选择样式:</label>
                          <el-radio-group v-model="formData.menuStyle">
                            <el-radio :label="1">列表式</el-radio>
                            <el-radio :label="2">九宫格</el-radio>
                          </el-radio-group>
                        </div>
                        <div
                          class="menu__form--item"
                          style="padding-top: 30px;"
                        >
                          <label class="el-form-item__label">设置菜单:</label>
                          <span style="color: #9797A1;"
                            >长按点击可拖拽调整顺序</span
                          >
                        </div>
                      </div>

                      <!--列表式-->
                      <DragMenu
                        :menu-list.sync="expandMenu"
                        :menuStyle="1"
                        v-if="formData.menuStyle === 1"
                      >
                      </DragMenu>

                      <!--九宫格-->
                      <DragMenu
                        :menu-list.sync="foldMenu"
                        :split-flag="false"
                        :menuStyle="2"
                        v-if="formData.menuStyle === 2"
                      >
                      </DragMenu>
                    </div>
                  </div>
                </el-form>
              </el-row>
            </transition>
          </div>
        </el-scrollbar>
      </div>
    </div>
    <order-dialog
      :orderVisible.sync="orderVisible"
      @updateOrder="updateOrderHandle"
      :orderProp="orderProp"
    />
  </div>
</template>

<script lang="ts">
import { Vue, Component } from "vue-property-decorator";

import UserCenter, {
  MenuList,
} from "@/views/decoration/components/UserCenter/formModel/UserCenter";
import {
  CustomStyle,
  OrderInfo,
  OrderItem,
  OptionType,
} from "@/views/decoration/interfaces/UserCenter";
import {
  getUserCenterConfig,
  setUserCenterConfig,
} from "@/api/decoration/decoration";
import PhoneView from "@/views/decoration/components/UserCenter/PhoneView/PhoneView.vue";
import Menu from "@/views/decoration/components/UserCenter/Menu.vue";
import MenuItem from "@/views/decoration/components/UserCenter/MenuItem.vue";
import UploadFile from "@/views/decoration/components/UserCenter/UploadFile.vue";
import DragMenu from "@/views/decoration/components/UserCenter/DragMenu.vue";
import OrderDialog from "@/views/decoration/components/UserCenter/OrderDialog.vue";
import EditorControlPage from "@/views/decoration/components/EditorPage/src/EditorControlPage.vue";
interface ISetVisible {
  [x: string]: boolean;
}

@Component({
  components: {
    PhoneView,
    MenuItem,
    Menu,
    DragMenu,
    OrderDialog,
    UploadFile,
    EditorControlPage,
  },
})
export default class Page extends Vue {
  /** 配置数据 */
  formData: UserCenter = new UserCenter();

  setVisible: ISetVisible = {
    /** 用户折叠面板 */
    setInfoVisible: true,

    /** 订单折叠面板 */
    setOrderVisible: false,

    /** 菜单折叠面板 */
    setMenuVisible: false,
  };

  /** 订单设置弹窗 */
  orderVisible = false;

  /** 自定义样式数据 */
  customStyle: CustomStyle = {
    /** 背景图片 */
    backgroundImage: "",
  };

  /** 用户信息数据 */
  orderInfo: OrderInfo = {
    /*orderInfo* 售后 */
    afterSaleIcon: {
      name: "",
      url: "",
    },
    /** 待付款 */
    waitIcon: {
      name: "",
      url: "",
    },
    waitDelivered: {
      name: "",
      url: "",
    },
    /** 待提货 */
    waitPickingIcon: {
      name: "",
      url: "",
    },
    /** 配送中 */
    deliveryIcon: {
      name: "",
      url: "",
    },
    /** 评价中心 */
    evaluateIcon: {
      name: "",
      url: "",
    },
  };

  /** 展开式菜单数据 */
  expandMenu: MenuList[] = [];

  /** 折叠式菜单数据 */
  foldMenu: MenuList[] = [];

  /** 传入订单属性 */
  orderProp: OrderItem & OptionType = {
    key: "",
    name: "",
    url: "",
  };

  get headStyleConfig() {
    let config = {
      headBackGround: {},
    };
    // 系统风格
    if (this.formData.headStyle === 1) {
      config.headBackGround = {
        "background-color": "#FE4E63",
      };
    }
    if (this.formData.headStyle === 2) {
      config = {
        headBackGround: {
          "background-image": `url(${this.customStyle.backgroundImage})`,
        },
      };
    }
    return config;
  }

  mounted() {
    this.getAllSetting();
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 接口获取所有配置数据
   */

  async getAllSetting() {
    const result = await getUserCenterConfig();
    const { code, data } = result;
    if (code === 200) {
      this.formData = data;
      let orderInfo = JSON.parse(this.formData.orderInfo);
      this.orderInfo = {
        ...orderInfo,
        waitDelivered: {
          name: "待发货",
          url: orderInfo.waitDelivered ? orderInfo.waitDelivered.url : "",
        },
      }; // 订单信息获取接口数据
      this.customStyle = JSON.parse(this.formData.customStyle);
      this.getFinalExpandMenu();
      this.getFinalFoldMenu();
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 设置最终列表菜单接口数据
   */

  getFinalExpandMenu() {
    this.expandMenu = this.formData.menuVos
      .filter(item => {
        return item.styleType === 1;
      })
      .map(mapItem => {
        if (typeof mapItem.linkSelectItem === "string") {
          mapItem.linkSelectItem = JSON.parse(mapItem.linkSelectItem);
        }
        return mapItem;
      });
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 设置最终折叠式菜单接口数据
   */

  getFinalFoldMenu() {
    this.foldMenu = this.formData.menuVos
      .filter(item => {
        return item.styleType === 2;
      })
      .map(mapItem => {
        if (typeof mapItem.linkSelectItem === "string") {
          mapItem.linkSelectItem = JSON.parse(mapItem.linkSelectItem);
        }
        return mapItem;
      });
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 配置项收起展开
   * @param {string} type
   */
  collapseVisibleHandle(type: string) {
    const map = ["Info", "Order", "Menu"];
    map.forEach(name => {
      this.setVisible[`set${name}Visible`] = type === name;
    });
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 显示订单编辑详情弹窗
   */
  orderClickHandle(key: string, value: { name: string; url: string }) {
    this.orderProp.key = key;
    this.orderProp.name = value.name;
    this.orderProp.url = value.url;
    this.orderVisible = true;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 关闭订单编辑详情弹窗
   * @param {*} orderItem
   */
  updateOrderHandle(orderItem: { key: number; name: string; url: string }) {
    this.orderInfo[orderItem.key] = {
      name: orderItem.name,
      url: orderItem.url,
    };
  }

  beforeDestroy() {
    this.submit(1);
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 提交
   * @param {number} nomessage
   */
  async submit(nomessage?: number) {
    this.formData.customStyle = JSON.stringify(this.customStyle);
    this.formData.orderInfo = JSON.stringify(this.orderInfo);
    const expandMenu: MenuList[] = JSON.parse(JSON.stringify(this.expandMenu));
    const foldMenu: MenuList[] = JSON.parse(JSON.stringify(this.foldMenu));
    expandMenu.forEach((item: MenuList, index: number) => {
      item.sortIndex = index;
      if (item.linkSelectItem instanceof Object) {
        item.linkSelectItem = JSON.stringify(item.linkSelectItem);
      }
    });
    foldMenu.forEach((supMenu: MenuList) => {
      if (supMenu.linkSelectItem instanceof Object) {
        supMenu.linkSelectItem = JSON.stringify(supMenu.linkSelectItem);
      }
    });
    this.formData.menuVos = expandMenu.concat(foldMenu);
    try {
      const response = await setUserCenterConfig(this.formData);
      this.$emit("userCenterChang", false);
      // this.$parent.isSaveTip = false;
      if (response.code === 200) {
        if (!nomessage) {
          this.$message({
            message: "保存配置成功",
            type: "success",
          });
        }
        await this.getAllSetting();
      }
    } catch (e) {
      await this.getAllSetting();
    }
  }
}
</script>

<style lang="scss" scoped>
@import "../../assets/styles/decoration/userCenter";
@import "@/assets/styles/decoration/editorPage";

.decoration__page {
  position: fixed;
  width: calc(100vw - 70px);
  padding-bottom: 10px;
  padding-right: 10px;
  box-sizing: border-box;
  left: 70px;
  display: flex;
  justify-content: space-around;
}

#editor__component_position {
  width: 300px;
  height: calc(100vh - 70px);
  overflow: auto;
  color: #333;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  padding-bottom: 50px;
}

.setting {
  width: 100%;
  overflow: initial;
}

.decoration__page .usercenter {
  margin-top: 0;
}

.editor__preview_new {
  width: 340px;
  height: calc(100vh - 80px) !important;
  border: 1px solid #dcdfe6 !important;
  box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.12), 0 0 6px 0 rgba(0, 0, 0, 0.04);
  overflow: scroll;
}

.editor__form_wrap {
  width: 435px;
  height: calc(100vh - 80px) !important;
  padding-bottom: 0 !important;
  border: 1px solid #dcdfe6;
  box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.12), 0 0 6px 0 rgba(0, 0, 0, 0.04);
  display: flex;
  flex-direction: column;

  .editor__form_wrap_main {
    width: 100%;
    flex: 1;
    overflow: hidden;
  }

  .editor__form_header {
    padding: 18px 20px;
    border-bottom: 1px solid #dcdfe6;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
  }

  .pd20 {
    padding: 20px 20px 0 20px;
  }
}
</style>
