<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-26 15:21:06
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-31 15:06:47
-->
<template>
  <div class="container">
    <div class="wrap">
      <div class="wrap__set" @click="handleClick">{{ setName }}</div>
      <el-dropdown @command="handleCommand" trigger="click">
        <div class="wrap__set--select">
          <i class="el-icon-more" />
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item
              v-for="item in dropdownList"
              :key="item.command"
              :disabled="item.disabled"
              v-show="item.show"
              :command="item.command"
              >{{ item.text }}</el-dropdown-item
            >
          </el-dropdown-menu>
        </div>
      </el-dropdown>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop } from "vue-property-decorator";

@Component
export default class SetDrop extends Vue {
  @Prop({ default: "编辑" })
  setName!: string;

  @Prop()
  dropdownList!: any[];

  /**
   * 点击的下拉项
   */
  handleCommand(val: any) {
    this.$emit("command", val);
  }

  /**
   * 点击的设置按钮
   */
  handleClick() {
    this.$emit("setClick");
  }
}
</script>

<style lang="scss" scoped>
.container {
  display: inline-block;
}
@include b(wrap) {
  display: flex;
  font-size: 12px;

  @include e(set) {
    padding: 10px;
    border: 1px solid #696c72;
    display: flex;
    height: 35px;
    align-items: center;
    justify-content: center;
    border-radius: 5px 0 0 5px;

    &:hover {
      cursor: pointer;
    }

    @include m(select) {
      width: 30px;
      height: 35px;
      border: 1px solid #696c72;
      border-left: none;
      display: flex;
      justify-content: center;
      flex-direction: column;
      cursor: pointer;
      border-radius: 0 3px 3px 0;
    }
  }
}
</style>
