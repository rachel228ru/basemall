<!--
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-07-17 13:29:52
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-24 13:42:33
-->
<template>
  <el-card class="editor__from" shadow="never">
    <div slot="header" class="clearfix">
      <span>分享链接</span>
    </div>
    <transition name="el-fade-in-linear" mode="out-in"></transition>
    <p class="content">
      <span class="label">页面路径:</span>
      <el-input :value="copyLink" disabled />
    </p>
    <p class="content">
      <span class="label"></span>
      <el-button @click="handleCopy">点击复制</el-button>
    </p>
    <p>将获取的页面路径，添加至【微信公众平台】，或任何你想插入的地方</p>
  </el-card>
</template>

<script lang="ts">
import { Vue, Component } from "vue-property-decorator";

@Component({})
export default class EditorLinkAddress extends Vue {
  /**
   * 获取当前页面操作栏目
   */
  get copyLink() {
    return this.$STORE.decorationStore.copyLink;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 调起复制
   */

  handleCopy() {
    this.copy(this.copyLink);
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 复制内容
   * @param {string} data
   */

  copy(data: string) {
    const url = data;
    const oInput = document.createElement("input");
    oInput.value = url;
    document.body.appendChild(oInput);
    oInput.select(); // 选择对象;
    document.execCommand("Copy"); // 执行浏览器复制命令
    this.$message.success("复制成功");
    oInput.remove();
  }
}
</script>
<style lang="scss" scoped>
@import "@/assets/styles/decoration/editorPage";
.content {
  font-size: 14px;
  display: flex;
  padding: 20px 0;
  .label {
    width: 200px;
  }
}
</style>
