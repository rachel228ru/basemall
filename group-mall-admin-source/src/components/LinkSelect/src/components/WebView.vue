<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:06
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 11:08:44
-->
<template>
  <!-- 活动营销 -->
  <div>
    <el-input
      placeholder="请输入内容"
      v-model="webview"
      class="input-with-select"
    >
      <template slot="prepend">https://</template>
    </el-input>
    <div class="link-tips">
      注意事项 : 选择此项需要https协议的链接，其它网页需登录
      <a
        href="https://mp.weixin.qq.com/"
        target="_tartget"
        rel="noopener noreferrer"
      >
        小程序管理后台
        <span></span> </a
      >配置业务域名。
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop, PropSync, Watch } from "vue-property-decorator";
import LinkSelectItem from "./LinkSelectItem";

@Component
export default class ActivityMarket extends Vue {
  @PropSync("link", {
    type: Object,
    default: () => {
      return {
        id: null,
        type: null,
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

  webview = "";

  select = "1";

  mounted() {
    if (this.linkSelectItem.type === 6) {
      this.webview = this.linkSelectItem.url;
    }
  }

  @Watch("webview")
  selectHandle() {
    const webview = this.webview.replace(/https:\/\//, "");
    this.webview = webview;
    const currentItem = {
      id: 999,
      type: 6,
      name: "自定义链接",
      url: `https://${webview}`,
      append: "",
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
