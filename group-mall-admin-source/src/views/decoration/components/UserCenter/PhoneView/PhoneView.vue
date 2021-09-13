<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 13:22:11
-->
<template>
  <div
    class="user"
    :style="{
      overflowY: showUserSetting ? 'hidden' : 'scroll',
      left: userPosition + 'px',
    }"
    @scroll="scrollHandle"
  >
    <div
      :class="`mask ${showUserSetting ? 'mask__show' : 'mask__hidden'}`"
      :style="{ top: scrollTop }"
    >
      <mask-setting
        @goBack="showUserSettingHandle"
        :show-address="showAddress"
      ></mask-setting>
    </div>
    <div class="user__radius" :style="headStyleConfig.headBackGround">
      <top-bar />
      <div class="center">个人中心</div>
      <div class="info">
        <img
          src="https://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTIj8fkACXBjG5oxepn1slUQm5ibBia0PvZPdo7Gib583vmDKuYxQOkuWsAd1eotA6fbLXMYiaXAdgRu5A/132"
          class="info__avatar"
          alt="info"
        />
        <div class="info__name">元宵汤圆水饺</div>
        <!-- <div
          @click="pickCodeClickHandle"
          class="info__qrcode"
          v-if="formData.codeStyle === 2 && formData.qrcodeVisible"
        >
        </div> -->
      </div>
      <div class="data">
        <div class="data__item">
          <div class="data__number">50</div>
          <div class="data__text">收藏</div>
        </div>
        <div>
          ｜
        </div>
        <div class="data__item">
          <div class="data__number">100</div>
          <div class="data__text">足迹</div>
        </div>
      </div>
    </div>
    <div class="user__cover">
      <div class="order">
        <div class="order__all">
          <div class="order__all--me">我的订单</div>
          <div class="order__all--check">
            查看全部订单
            <span>></span>
          </div>
        </div>
        <div class="order__quick">
          <div class="order__quick--item">
            <img
              :src="orderInfo.waitIcon.url"
              class="order__quick--img"
              alt="waitIcon"
            />
            <div class="order__quick--text">
              {{ orderInfo.waitIcon.name }}
            </div>
          </div>
          <div class="order__quick--item">
            <img
              :src="orderInfo.waitDelivered.url"
              class="order__quick--img"
              alt="waitIcon"
            />
            <div class="order__quick--text">
              {{ orderInfo.waitDelivered.name }}
            </div>
          </div>
          <div class="order__quick--item">
            <img
              :src="orderInfo.deliveryIcon.url"
              class="order__quick--img"
              alt="deliveryIcon"
            />
            <div class="order__quick--text">
              {{ orderInfo.deliveryIcon.name }}
            </div>
          </div>
          <div class="order__quick--item">
            <img
              :src="orderInfo.waitPickingIcon.url"
              class="order__quick--img"
              alt="wait"
            />
            <div class="order__quick--text">
              {{ orderInfo.waitPickingIcon.name }}
            </div>
          </div>
          <div class="order__quick--item">
            <img
              :src="orderInfo.afterSaleIcon.url"
              class="order__quick--img"
              alt="afterSaleIcon"
            />
            <div class="order__quick--text">
              {{ orderInfo.afterSaleIcon.name }}
            </div>
          </div>
          <div class="order__quick--item">
            <img
              :src="orderInfo.evaluateIcon.url"
              class="order__quick--img"
              alt="evaluateIcon"
            />
            <div class="order__quick--text">
              {{ orderInfo.evaluateIcon.name }}
            </div>
          </div>
        </div>
      </div>
      <div class="user__menu">
        <div class="user__menu--expand" v-if="formData.menuStyle === 1">
          <Menu>
            <div v-for="expandItem in expandMenu" :key="expandItem.id">
              <menu-item
                @click.native="menuItemClickHandle(expandItem.menuName, false)"
                v-if="expandItem.hideMenu"
                :class="{ splitFlag: expandItem.splitFlag }"
                :img-url="expandItem.menuIconUrl"
                >{{ expandItem.menuName }}
              </menu-item>
            </div>
          </Menu>
        </div>
        <GridMenu
          :grid-menu="foldMenu"
          v-if="formData.menuStyle === 2"
        ></GridMenu>
        <pick-code v-if="formData.codeStyle === 1 && formData.qrcodeVisible" />
        <div class="version">
          <div class="version__img">
            <img
              class="miniBottomLog"
              :src="miniBottomLog"
              alt="miniBottomLog"
            />
          </div>
          <div class="version__text">启山智软商城系统{{ version }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop } from "vue-property-decorator";

import UserCenter, {
  MenuList,
} from "@/views/decoration/components/UserCenter/formModel/UserCenter";
import {
  CustomStyle,
  OrderInfo,
} from "@/views/decoration/interfaces/UserCenter";
import Menu from "@/views/decoration/components/UserCenter/Menu.vue";
import MenuItem from "@/views/decoration/components/UserCenter/MenuItem.vue";
import GridMenu from "@/views/decoration/components/UserCenter/GridMenu.vue";
import MaskSetting from "@/views/decoration/components/UserCenter/PhoneView/MaskSetting.vue";
import TopBar from "@/views/decoration/components/UserCenter/PhoneView/TopBar.vue";
import { getShopInfo } from "@/api/businessCenter/setting";

@Component({
  components: {
    MenuItem,
    Menu,
    GridMenu,
    MaskSetting,
    TopBar,
  },
})
export default class PhoneView extends Vue {
  /** 配置数据 */
  @Prop({
    type: Object,
    default: () => {
      return new UserCenter();
    },
  })
  formData!: UserCenter;

  /** 自定义样式数据 */
  @Prop({
    type: Object,
    default: () => {
      return {
        /** 背景图片 */
        backgroundImage: "",
        /** 卡面颜色 */
        cardColor: "",
        /** 文字颜色 */
        textColor: "",
      };
    },
  })
  customStyle!: CustomStyle;

  /** 用户信息数据 */
  @Prop({
    type: Object,
    default: () => {
      return {
        /*orderInfo* 售后 */
        afterSaleIcon: {
          name: "",
          url: "",
        },
        waitDelivered: {
          name: "",
          url: "",
        },
        /** 待付款 */
        waitIcon: {
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
    },
  })
  orderInfo!: OrderInfo;

  /**
   * 展开式菜单数据
   */
  @Prop({
    default: () => {
      return [];
    },
  })
  expandMenu!: MenuList[];

  /**
   * 折叠式菜单数据
   */
  @Prop({
    default: () => {
      return [];
    },
  })
  foldMenu!: MenuList[];

  @Prop({
    type: Object,
    default: () => {
      return {
        headBackGround: {},
        cardColor: "#45403C",
        textColor: "#E4CB98",
      };
    },
  })
  headStyleConfig!: any;

  userPosition = 0;

  /** 用户手机样式滚动 */
  showUserSetting = false;

  /** 遮罩层弹窗是否显示地址管理 */
  showAddress = true;

  /** 滚动条距离顶部距离 */
  scrollTop = "";

  version = "";

  miniBottomLog =
    "https://medusa-small-file.oss-cn-hangzhou.aliyuncs.com/gruul/20200407/logo.png";

  created() {
    this.getInfo();
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 滚动事件
   * @param {*} e
   */

  scrollHandle(e: { target: { scrollTop: string } }) {
    this.scrollTop = e.target.scrollTop + "px";
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 设置遮罩层显示隐藏
   * @param {boolean} val
   */

  showUserSettingHandle(val: boolean) {
    this.showUserSetting = val;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 菜单项点击事件
   * @param {string} menuItemName
   * @param {boolean} type
   */

  menuItemClickHandle(menuItemName: string, type: boolean) {
    if (menuItemName === "设置") {
      this.showAddress = type;
      this.showUserSettingHandle(true);
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 获取页面设置数据
   */
  async getInfo() {
    try {
      let queryForm = await getShopInfo();
      this.version = queryForm.teamplateVersion;

      if (queryForm.miniBottomLog) {
        this.miniBottomLog = queryForm.miniBottomLog;
      }
    } catch (e) {
      this.$message({
        type: "warning",
        message: e,
      });
    }
  }
}
</script>

<style lang="scss" scoped>
@import "@/assets/styles/decoration/phoneView";
</style>
