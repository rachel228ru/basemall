<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:06
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 10:33:28
-->
<template>
  <!-- 链接选择器 -->
  <div>
    <div class="mb18">
      <slot name="radio">
        <el-radio-group v-model="radio" class="line" v-if="selectType === 1">
          <el-radio :label="1">系统链接</el-radio>
          <el-radio :label="2">自定义链接</el-radio>
          <el-radio :label="3">小程序</el-radio>
        </el-radio-group>
        <el-select v-model="radio" v-if="selectType === 2">
          <el-option label="系统链接" :value="1"></el-option>
          <el-option label="自定义链接" :value="2"></el-option>
          <el-option label="小程序" :value="3"></el-option>
        </el-select>
      </slot>
    </div>
    <slot name="input">
      <div @click="handleShow">
        <slot name="button">
          <el-button type="primary" v-if="radio === 1">添加链接</el-button>
        </slot>
      </div>
    </slot>
    <WebView v-if="radio === 2" :link="linkSelectItem"></WebView>
    <AppView v-if="radio === 3" :link="linkSelectItem"></AppView>
    <el-dialog
      title="链接选择器"
      :append-to-body="inner"
      :modal-append-to-body="true"
      :visible.sync="dialogVisible"
      width="850px"
    >
      <div style="margin: 0 20px">
        <el-tabs v-model="currentComponent">
          <el-tab-pane label="功能页面" name="FunctionPage"></el-tab-pane>
          <el-tab-pane label="商品" name="Goods" v-if="!noProTab"></el-tab-pane>
          <el-tab-pane
            label="商品专区"
            name="DisplayClassIfy"
            v-if="spellTab && secShow"
          ></el-tab-pane>
          <el-tab-pane label="自定义页面" name="CustomPage"></el-tab-pane>
        </el-tabs>
        <component
          ref="componentRef"
          :link="linkSelectItem"
          :visible="dialogVisible"
          :is="currentComponent"
          :noProTab="noProTab"
          :limitProTab="limitProTab"
        ></component>
      </div>
      <div slot="footer">
        <el-button @click="handleClose">取 消</el-button>
        <el-button type="primary" @click="handleChoose">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import {
  Vue,
  Component,
  Prop,
  Ref,
  Watch,
  PropSync,
} from "vue-property-decorator";
import LinkSelectItem, { typeNameMap } from "./components/LinkSelectItem";
import FunctionPage from "./components/FunctionPage.vue";
import Goods from "./components/Goods.vue";
import DisplayClassIfy from "./components/DisplayClassIfy.vue";
import CustomPage from "./components/CustomPage.vue";
import WebView from "./components/WebView.vue";
import AppView from "./components/AppView.vue";

/** 链接选择器 */
@Component({
  components: {
    FunctionPage,
    Goods,
    DisplayClassIfy,
    CustomPage,
    WebView,
    AppView,
  },
})
export default class LinkSelect extends Vue {
  @Prop({ default: false })
  inner!: boolean;

  @Prop({ default: false }) // 底部导航去掉商超
  noProTab!: boolean;

  @Prop({ default: true }) // 分类页海报去掉单独分类选择
  spellTab!: boolean;

  @Prop({ default: true }) //自定义底部导航选择范围限制
  limitProTab!: boolean;

  @Prop({ default: "none" })
  getFrom!: string;

  @Prop({ default: false })
  showClear!: boolean;

  @PropSync("link", {
    type: Object,
    default: () => {
      return {
        id: null,
        type: null,
        name: "",
        url: "",
        append: "",
        getFrom: "none",
      };
    },
  })
  linkSelectItem!: LinkSelectItem;

  /** 选择器样式 1:radio单选  2select下拉 */
  @Prop({ default: 1 })
  selectType!: number;

  @Prop()
  type!: number;

  name = "LinkSelect";

  /** 是否阻断不让选择商超 */
  secShow = true;

  /** 默认数组 */
  radio = 1;

  /** 当前显示的组件 */
  currentComponent = "FunctionPage";

  dialogVisible = false;

  /** 当前显示组件的引用 */
  @Ref() componentRef!: FunctionPage | DisplayClassIfy;

  @Watch("dialogVisible")
  handleVisibleChange(v: boolean) {
    if (v) {
      if (
        typeNameMap[this.linkSelectItem.type] &&
        typeNameMap[this.linkSelectItem.type].name
      ) {
        this.currentComponent = typeNameMap[this.linkSelectItem.type].name;
      }
    }
  }

  handleChoose() {
    if (this.linkSelectItem.id) {
      // this.$emit("change", this.linkSelectItem);
      this.$emit("select", this.linkSelectItem);
      this.handleClose();
    } else {
      this.$message.warning("请选择需要链接的页面");
    }
  }

  async handleShow() {
    // 设置专区商品只能出现在一个底部导航中
    const list =
      this.$parent.$parent.$parent.$parent.formData ||
      this.$parent.$parent.$parent.formData ||
      this.$parent.$parent.$parent.$parent.$parent;
    const menuList = list.menuList;
    if (menuList) {
      const listItem = menuList.filter(
        (item: { name: string }) => item.name === "mall",
      );
      if (this.link.append !== "mall" && listItem.length > 0) {
        this.secShow = false;
      }
    }
    // if (this.type === 1) return;
    this.dialogVisible = true;
    // todo 新增/选择菜单时清除上次选中状态
    if (this.showClear) {
      this.$nextTick(() => {
        if (this.componentRef.selectId) {
          this.componentRef.selectId = "";
        }
      });
    }
  }

  handleClose() {
    this.dialogVisible = false;
    this.$emit("select", this.linkSelectItem);
    this.$STORE.decorationStore.setLinkForm(this.getFrom);
  }

  mounted() {
    if (this.link.type === 7) {
      this.radio = 3;
    } else {
      this.radio = this.link.type === 6 ? 2 : 1;
    }
  }

  @Watch("link", { deep: true })
  changeRadio() {
    if (this.link.type === 7) {
      this.radio = 3;
    } else {
      this.radio = this.link.type === 6 ? 2 : 1;
    }
  }
}
</script>
<style scoped>
.mb18 {
  margin-bottom: 18px;
}

.line {
  display: flex;
  margin-top: 9px;
}

.el-radio {
  margin-right: 18px;
}

.el-dialog {
  width: 880px;
}
</style>
