<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:06
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 11:05:29
-->
<template>
  <!-- 活动营销 -->
  <div>
    <span style="color: #9797A1">AppId</span>
    <el-input
      placeholder="请输入小程序appId"
      v-model="appmodel"
      class="input-with-select"
      style="width:180px;margin-left:20px"
    >
    </el-input>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop, PropSync, Watch } from "vue-property-decorator";
import LinkSelectItem from "./LinkSelectItem";

@Component
export default class AppView extends Vue {
  @PropSync("link", {
    type: Object,
    default: () => {
      return {
        id: "",
        type: 0,
        name: "",
        url: "",
        append: "",
      };
    },
  })
  linkSelectItem!: LinkSelectItem;

  @Prop({
    type: Boolean,
  })
  visible = false;

  appmodel = "";

  mounted() {
    if (this.linkSelectItem.type === 7) {
      this.appmodel = this.linkSelectItem.url;
    }
  }

  @Watch("appmodel")
  selectHandle() {
    const currentItem = {
      id: 999,
      type: 7,
      name: "小程序",
      url: `${this.appmodel}`,
      append: "appmodel",
    };
    Object.assign(this.linkSelectItem, currentItem);
  }
}
</script>
<style scoped lang="scss">
.link-tips {
  color: #999999;
  text-align: left;
  padding: 10px;
  font-size: 12px;
  line-height: 20px;
  padding-left: 0;
  box-sizing: border-box;
  word-break: normal;
  a {
    color: #576b95;
  }
}
</style>
