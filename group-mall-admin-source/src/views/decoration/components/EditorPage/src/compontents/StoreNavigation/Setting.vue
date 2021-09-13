<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-23 18:06:11
-->
<template>
  <!-- 店铺导航 -->
  <div>
    <el-form :model="formData" label-width="80px">
      <el-form-item label="店铺导航"
        >最多可添加10个，最少可添加4个</el-form-item
      >
    </el-form>
    <div
      v-for="(storeNavigationItem, index) in formData.storeNavigations"
      :key="index"
      :draggable="true"
      @dragstart="handleDragstart(index)"
      @dragover="handleDragover"
      @drop="handleDrop(index)"
    >
      <StoreNavigationItemFrom
        :key="index"
        :itemIndex="index"
        @onDelect="delect(index)"
        :formData="storeNavigationItem"
      />
    </div>

    <el-row
      v-if="this.formData.storeNavigations.length < 10"
      type="flex"
      align="middle"
      justify="space-between"
    >
      <el-button @click="handelAddNav">添加导航</el-button>
      <!-- <div>上下拖动可排序</div> -->
    </el-row>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop } from "vue-property-decorator";
import StoreNavigationItemFrom from "./StoreNavigationItem.vue";
import StoreNavigation from "./StoreNavigation";
import StoreNavigationItemTs from "./StoreNavigationItemTs";

@Component({
  components: {
    StoreNavigationItemFrom,
  },
})
export default class StoreNavigationForm extends Vue {
  @Prop()
  formData!: StoreNavigation;

  /** 拖动的组件的下角标 */
  dragStarIndex = -1;

  /**
   * @LastEditors: chuyinlong
   * @description: 添加导航
   */

  handelAddNav() {
    if (this.formData.storeNavigations.length >= 10) {
      this.$message.warning("最多可添加10个");
      return;
    }
    const itemTs: StoreNavigationItemTs = new StoreNavigationItemTs();
    this.formData.storeNavigations.push(itemTs);
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 删除底部导航tab
   * @param {number} index
   */

  delect(index: number) {
    this.formData.storeNavigations.splice(index, 1);
  }

  /**
   * 开始拖动，记录拖动的组件下角标
   */
  /**
   * @LastEditors: chuyinlong
   * @description:开始拖动，记录拖动的组件下角标
   * @param {number} i
   */

  handleDragstart(i: number) {
    this.dragStarIndex = i;
  }

  /**
   * 阻止默认行为，否则drop事件不会触发
   */
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
    const temp = this.formData.storeNavigations.splice(
      this.dragStarIndex,
      1,
      this.formData.storeNavigations[i],
    );
    this.formData.storeNavigations.splice(i, 1, ...temp);
  }
}
</script>
