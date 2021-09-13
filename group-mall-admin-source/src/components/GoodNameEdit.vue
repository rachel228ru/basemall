<!--
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-07-17 13:29:51
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-24 13:05:40
-->
<template>
  <!-- 编辑商品名称 -->
  <div class="goodNameEdit">
    <span class="goodNameEdit__text" v-if="!inputVisible" @click="showInput">
      <div class="goodNameEdit__text--name">{{ goodName }}</div>
      <i class="el-icon-edit" style="color: #2d8cf0"></i>
    </span>
    <el-input
      ref="textareaInput"
      v-if="inputVisible"
      v-model="editName"
      @blur="hindInpt"
      type="textarea"
      :rows="1"
      style="width: 120px"
      maxlength="30"
      placeholder="请输入商品名称"
    ></el-input>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop } from "vue-property-decorator";

@Component
export default class GoodNameEdit extends Vue {
  name = "GoodNameEdit";

  /** 商品名称 */
  @Prop({ type: String })
  readonly goodName!: string;

  /** 编辑的字段 */
  editName = "";

  /** 显示输入框 */
  inputVisible = false;

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
  // width: 108px;
  width: 120px;
  &__text {
    cursor: pointer;
    display: flex;
    &--name {
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
    }
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
</style>
