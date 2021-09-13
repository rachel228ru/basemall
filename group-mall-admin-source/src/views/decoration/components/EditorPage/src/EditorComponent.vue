<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-24 10:13:57
-->
<template>
  <!-- 组件管理页面 -->
  <div class="editor__component editor__component_new">
    <el-tabs type="border-card" v-model="activeName">
      <el-tab-pane label="基本组件" name="first"></el-tab-pane>
    </el-tabs>
    <div class="editor_component_wrap">
      <el-scrollbar style="height: 100%">
        <div class="editor_component_wrap_main">
          <el-row :gutter="15" v-show="activeName == 'first'">
            <el-col
              :span="12"
              v-for="component of components"
              :key="component.value"
            >
              <div
                class="component--item"
                @click="handleAddComponent(component)"
              >
                <div
                  class="iconfont component--item--icon"
                  :class="`icon${component.icon}`"
                ></div>
                <span>{{ component.label }}</span>
              </div>
            </el-col>
          </el-row>
        </div>
      </el-scrollbar>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Emit } from "vue-property-decorator";
import { IComponentItem, IComponent } from "./interfaces/component";
import EdtiorFormData from "./compontents/index/formModel";

@Component
export default class DecorationComponent extends Vue {
  activeName = "first";

  /** 普通组件 */
  components: IComponentItem[] = [
    {
      icon: "lunbotu",
      value: "HomeSwiper",
      label: "轮播图",
    },
    {
      icon: "lunbotu",
      value: "GoodSwiper",
      label: "商品轮播图",
    },
    {
      icon: "shangpin",
      value: "Goods",
      label: "商品",
    },
    {
      icon: "sousuo",
      value: "Search",
      label: "搜索",
    },
    {
      icon: "biaotilan",
      value: "TitleBar",
      label: "标题栏",
    },
    {
      icon: "zhanweifu",
      value: "BlankPaceholder",
      label: "空白占位",
    },
    {
      icon: "fengefu",
      value: "Separator",
      label: "分隔符",
    },
    {
      icon: "dianpudaohang",
      value: "StoreNavigation",
      label: "店铺导航",
    },
    {
      icon: "mofang",
      value: "CubeBox",
      label: "魔方",
    },
    {
      icon: "28fuwenbenkuang",
      value: "RichText",
      label: "富文本",
    },
    {
      icon: "tupian",
      value: "ImageCom",
      label: "图片",
    },
    {
      icon: "zhibo1",
      value: "VideoCom",
      label: "视频",
    },
  ];

  /**
   * 获取当前页面操作栏目
   */
  get activePageType() {
    return this.$STORE.decorationStore.activePageType;
  }

  @Emit("change")
  handleAddComponent(currentComponent: IComponentItem): IComponent {
    const Form = new EdtiorFormData();
    const FormData = Form[currentComponent.value];
    return { ...currentComponent, id: Date.now(), formData: new FormData() };
  }
}
</script>
<style lang="scss">
@import "@/assets/styles/decoration/editorPage";
.component--item--icon {
  font-size: 60px;
  color: #b2b2b2;
  text-align: center;
  margin-top: 20px;
}
.editor__component_new .is-horizontal {
  display: none;
  overflow-x: hidden;
}
</style>
