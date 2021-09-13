<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 15:39:27
-->
<template>
  <!-- 预览编辑页面 -->
  <div class="editor__preview">
    <div class="h100 w100" style="overflow: hidden">
      <el-scrollbar
        :class="[
          componentList &&
          componentList.length == 1 &&
          componentList[0].value == 'NavBar'
            ? 'preNavBar'
            : '',
        ]"
        style="height: 100%"
        :noresize="true"
        ref="myScrollbar"
      >
        <div
          v-for="(component, i) of componentList"
          :key="i"
          @click="handleCurrentComponent(component, i)"
          @mouseover="delFlag = i"
          @mouseleave="delFlag = -1"
          :draggable="true"
          @dragstart="handleDragstart(i)"
          @dragover="handleDragover"
          @drop="handleDrop(i)"
          :id="`editor-preview-com-${i}`"
          style="width: 373px; cursor: move"
          :class="[
            'component--item',
            component.value != undefined &&
            delFlag === i &&
            component.value !== 'SpellPage' &&
            component.value !== 'BusinessSuper'
              ? 'iscur__component--item'
              : '',
            curreentFlag === i &&
            component.value !== 'SpellPage' &&
            component.value !== 'BusinessSuper'
              ? 'select__component--item'
              : '',
          ]"
        >
          <el-popover
            trigger="click"
            v-show="
              activePageType !== 'bussiness' &&
                delFlag === i &&
                component.value !== 'SpellPage' &&
                component.value !== 'BusinessSuper'
            "
          >
            <p>该组件确定删除吗？</p>
            <div style="text-align: right; margin: 0">
              <el-button size="mini" type="text" @click="onDelTpi"
                >取消</el-button
              >
              <el-button type="primary" size="mini" @click="delCurrentCom(i)"
                >确定</el-button
              >
            </div>
            <i
              class="el-icon-circle-close component--item__icon"
              slot="reference"
            ></i>
          </el-popover>
          <component
            :is="component.value"
            :formData="component.formData"
          ></component>
        </div>
      </el-scrollbar>
    </div>
  </div>
</template>

<script lang="ts">
import { IComponent } from "./interfaces/component";

import {
  Vue,
  Component,
  PropSync,
  Emit,
  Watch,
  Ref,
} from "vue-property-decorator";
import preview from "./compontents/index/preview";

import Draggable from "vuedraggable";

@Component({
  components: {
    ...preview,
    Draggable,
  },
})
export default class DecorationPreview extends Vue {
  @PropSync("components", {
    type: Array,
    default() {
      return [];
    },
  })
  temComponentList!: IComponent[];

  componentList: IComponent[] = [];

  delFlag = -1;

  // 拖动的组件的下角标
  dragStarIndex = -1;

  // 选中的当前项
  curreentFlag = -1;

  visible = false;

  // 获取当前页面操作栏目
  get activePageType() {
    return this.$STORE.decorationStore.activePageType;
  }

  @Ref() myScrollbar!: any;

  @Watch("components", {
    deep: true,
  })
  componentsChange(v: IComponent[]) {
    if (Array.isArray(v) && v.length === 1) {
      const name = v[0].value;
      if (name === "BusinessSuper") {
        this.handleCurrentComponent(v[0], 0);
      }
    }
    this.componentList = v;
  }

  mounted() {
    this.componentList = { ...this.temComponentList };
    this.onWrapSroll();
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 监听组件设置切换
   * @param {IComponent}currentComponent
   * @param {number} i
   * @returns {IComponent}
   */

  @Emit("change")
  handleCurrentComponent(currentComponent: IComponent, i: number): IComponent {
    this.curreentFlag = i;
    this.$STORE.decorationStore.setActiveComIndex(i);
    return currentComponent;
  }

  onWrapSroll() {
    const scrollbarEl = this.myScrollbar.wrap;
    scrollbarEl.onscroll = () => {
      if (this.visible) {
        this.visible = false;
      }
    };
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 去除弹窗提示
   */

  onDelTpi() {
    if (this.visible) {
      this.visible = false;
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 删除当前组件
   * @param {number} i
   */

  @Emit("settingEmpty")
  delCurrentCom(i: number) {
    this.onDelTpi();
    this.componentList.splice(i, 1);
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 设置当前选中的组件
   * @param {number} i
   */

  setCurrentFlag(i: number) {
    this.curreentFlag = i;
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
    const temp = this.componentList[this.dragStarIndex];
    this.componentList[this.dragStarIndex] = this.componentList[i];
    this.componentList[i] = temp;
  }
}
</script>

<style lang="scss" scoped>
@import "@/assets/styles/decoration/editorPage";
</style>
