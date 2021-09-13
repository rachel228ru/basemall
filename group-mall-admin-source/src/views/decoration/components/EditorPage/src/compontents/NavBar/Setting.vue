<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-23 16:09:56
-->
<template>
  <!-- 导航 -->
  <el-form :model="formData" label-width="100px">
    <div
      class="setmenu__form--item"
      style="padding-top: 10px;position:relative"
    >
      <label class="el-form-item__label">选择样式:</label>
      <el-radio-group v-model="formData.codeStyle">
        <el-radio :label="1">普通样式</el-radio>
        <el-radio :disabled="(formData.menuList.length & 1) === 0" :label="2"
          >中间大图样式
          <i
            class="el-icon-warning-outline"
            style="cursor:pointer;"
            @mouseover="notice = true"
            @mouseout="notice = false"
          ></i
        ></el-radio>
      </el-radio-group>

      <span v-if="notice" style="position:absolute;top:40px;left:50px"
        >该模式适合单数导航，最多可添加5个，最少3个</span
      >
    </div>

    <div class="setmenu__form--item" style="padding-top: 0px;">
      <label class="el-form-item__label">添加导航:</label>
      <span style="color: #9797A1;fontSize:12px"
        >最多添加5个导航，鼠标拖拽调整导航顺序</span
      >
    </div>
    <div class="setmenu__form--item" style="padding-top: 0px;">
      <label class="el-form-item__label">未选中颜色:</label>
      <el-color-picker v-model="formData.defaultColor"></el-color-picker>
    </div>
    <div class="setmenu__form--item" style="padding-top: 0px;">
      <label class="el-form-item__label">已选择颜色:</label>
      <el-color-picker v-model="formData.selectColor"></el-color-picker>
    </div>

    <!-- 导航item -->

    <div
      v-for="(item, index) in formData.menuList"
      :key="index"
      :draggable="true"
      @dragstart="handleDragstart(index)"
      @dragover="handleDragover"
      @drop="handleDrop(index)"
    >
      <template>
        <NavBarItem
          :itemIndex="index"
          :formData="item"
          ref="navBar"
          @onSetHomeTab="setHomeTab(index)"
          @onDelect="delect(index)"
          @setDefIcon="setDefIcon(index)"
        />
      </template>
    </div>

    <!-- 添加导航按钮 -->
    <el-button
      style="marginTop:10px"
      @click="addNav()"
      v-if="formData.menuList.length <= 4"
      >添加导航</el-button
    >
  </el-form>
</template>

<script lang="ts">
import { Vue, Component, Prop, Ref } from "vue-property-decorator";
import NavBar from "./NavBar";
import NavBarItem from "./NavBarItem.vue";

@Component({
  components: {
    NavBarItem,
  },
})
export default class NavBarForm extends Vue {
  @Prop()
  formData!: NavBar;

  @Ref()
  readonly navBar!: NavBarItem;

  notice = false;

  /** 拖动的组件的下角标 */
  dragStarIndex = -1;

  /** 获取当前时间 */
  get now() {
    const date = new Date();
    const hour = date.getHours() > 9 ? date.getHours() : "0" + date.getHours();
    const minutes =
      date.getMinutes() > 9 ? date.getMinutes() : "0" + date.getMinutes();
    const now = `${hour}:${minutes}`;
    return now;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 新增导航
   */

  addNav() {
    this.formData.codeStyle = 1;
    if (this.formData.menuList.length <= 4) {
      const defaultTab = {
        text: "名称",
        id: "",
        iconType: 2,
        iconPath: "https://qiniu-app.qtshe.com/u391.png",
        selectedIconPath: "https://qiniu-app.qtshe.com/u391.png",
        linkSelectItem: "",
        sortIndex: 0,
        linkUrl: "",
        linkName: "",
        name: "",
        type: "",
        isHome: false,
        /** 系统图标 */
        defIcon: "",
        actIcon: "",
        isAdd: false, // 新增的导航是只能选择上传图片
      };
      this.formData.menuList.push(defaultTab);
      this.sortMenu();
    } else {
      this.$message({
        message: "底部导航最多4个",
        type: "warning",
      });
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 删除底部导航tab
   * @param {number} index
   */

  delect(index: number) {
    this.formData.codeStyle = 1;
    this.formData.menuList.splice(index, 1);
    this.sortMenu();
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 选择默认图标
   * @param {number} index
   */

  setDefIcon(index: number) {
    const icons = [
      {
        def: "https://oss-tencent.bgniao.cn/api/home_page.png",
        act: "https://oss-tencent.bgniao.cn/api/home_page1.png",
      },
      {
        def: "https://oss-tencent.bgniao.cn/api/shopping_mall1.png",
        act: "https://oss-tencent.bgniao.cn/api/shopping_mall.png",
      },
      {
        def: "https://oss-tencent.bgniao.cn/api/shopping_cart1.png",
        act: "https://oss-tencent.bgniao.cn/api/shopping_cart.png",
      },
      {
        def: "https://oss-tencent.bgniao.cn/api/my1.png",
        act: "https://oss-tencent.bgniao.cn/api/my.png",
      },
      {
        def:
          "https://medusa-small-file-1253272780.cos.ap-shanghai.myqcloud.com/gruul/20210112/b2ef1106af8b4a6c9b22ee0afac6195c.png",
        act:
          "https://medusa-small-file-1253272780.cos.ap-shanghai.myqcloud.com/gruul/20210112/613afce069ca4cfdb980cd446e0195f1.png",
      },
    ];
    this.formData.menuList[index].defIcon = icons[index].def;
    this.formData.menuList[index].actIcon = icons[index].act;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 底部导航排序
   */

  sortMenu() {
    this.formData.menuList.forEach((item, index) => {
      item.sortIndex = index;
    });
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 设为首页
   * @param {number} homeIndex
   */

  setHomeTab(homeIndex: number) {
    this.formData.menuList.forEach((item, index) => {
      if (homeIndex === index) {
        item.isHome = true;
      } else {
        item.isHome = false;
      }
    });
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 开始拖动，记录拖动的组件下角标
   * @param {number} i
   */

  handleDragstart(i: number) {
    this.dragStarIndex = i;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 阻止默认行为，否则drop事件不会触发
   * @param {*} e
   */

  handleDragover(e: { preventDefault: () => void }) {
    e.preventDefault();
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 被放置的容器触发事件，交换两个组件的位置
   * @param {number} i
   */

  handleDrop(i: number) {
    if (this.dragStarIndex === i) {
      return false;
    }
    const temp = this.formData.menuList.splice(
      this.dragStarIndex,
      1,
      this.formData.menuList[i],
    );
    this.formData.menuList.splice(i, 1, ...temp);
  }
}
</script>
<style lang="scss" scoped>
@import "@/assets/styles/decoration/navBar";
</style>
