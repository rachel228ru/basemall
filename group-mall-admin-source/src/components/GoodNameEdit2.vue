<!--
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-07-17 13:29:51
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-13 18:44:20
-->
<template>
  <!-- 编辑商品名称 -->
  <div class="goodNameEdit">
    <span class="goodNameEdit__text" v-if="!inputVisible" @click="showInput">
      {{ goodName }}
      <i
        class="el-icon-edit"
        style="color:#2d8cf0"
        :class="{ active1: activePage && activePage.id == good.id }"
      ></i>
    </span>
    <el-input
      ref="textareaInput"
      v-if="inputVisible"
      v-model="editName"
      @blur="hindInpt"
      type="textarea"
      :rows="2"
      style="width:180px"
      maxlength="30"
      placeholder="请输入商品名称"
    ></el-input>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop } from "vue-property-decorator";

@Component
export default class GoodNameEdit extends Vue {
  name: "GoodNameEdit";

  /** 商品名称 */
  @Prop({ type: String }) readonly goodName!: string;

  @Prop({ type: Object }) readonly good!: any;

  /** 编辑的字段 */
  editName = "";

  /** 显示输入框 */
  inputVisible = false;

  get activePage() {
    return this.$STORE.decorationStore.activeTab
  }

  /**
   * 显示输入框
   */
  showInput() {
    this.inputVisible = true;
    this.editName = this.goodName;
    this.$nextTick(() => {
      const textareaInput = this.$refs.textareaInput as HTMLFormElement;
      textareaInput.$refs.textarea.focus();
    });
  }

  /**
   * 隐藏输入框
   */
  hindInpt() {
    this.inputVisible = false;
    const editName = this.editName;
    if (this.goodName === editName) {
      return;
    }
    if (editName !== "") {
      this.$emit("change", editName);
    } else {
      this.$message.warning("请填写商品名");
    }
  }
}
</script>

<style lang="scss">
.goodNameEdit {
  width: 170px;
  &__text {
    cursor: pointer;
  }
  i {
    margin-left: 4px;
    display: none;
  }
  &:hover {
    i {
      display: inline-block;
    }
  }
}
.active1 {
  display: inline-block !important;
}
</style>
