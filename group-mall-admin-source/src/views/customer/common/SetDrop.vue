<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-31 15:06:50
-->
<template>
  <div class="container">
    <div class="wrap">
      <div class="wrap__set" @click="handleClick">{{ setName }}</div>
      <el-dropdown @command="handleCommand" trigger="click">
        <div :class="isStyle ? 'wrap__set--newSelect' : 'wrap__set--select'">
          <div class="wrap__set--apart">|</div>
          ...
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
import { GoodSelectOperate } from "@/views/goods/marketModel/components/goodListType";
@Component
export default class SetDrop extends Vue {
  @Prop({ default: "编辑" })
  setName!: string;

  @Prop()
  dropdownList!: GoodSelectOperate[];

  @Prop({ default: false })
  isStyle!: boolean;

  /**
   * 点击的下拉项
   */

  handleCommand(val: string) {
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
    padding: 0 8px;
    border: 1px solid #eaf5fe;
    display: flex;
    height: 30px;
    align-items: center;
    justify-content: center;
    // border-radius: 3px 0 0 3px;
    border-right: none;
    border-top-left-radius: 50px;
    border-bottom-left-radius: 50px;
    background-color: #eaf5fe;
    color: #2e99f3;

    &:hover {
      cursor: pointer;
    }

    @include m(apart) {
      margin-left: -10px;
      margin-top: 2px;
      margin-right: 10px;
    }

    @include m(newSelect) {
      width: 30px;
      height: 30px;
      border: 1px solid #eaf5fe;
      border-left: none;
      display: flex;
      justify-content: center;
      cursor: pointer;
      // border-radius: 0 3px 3px 0;
      border-top-right-radius: 50px;
      border-bottom-right-radius: 50px;
      background-color: #eaf5fe;
      color: #2e99f3;
      font-weight: bold;
    }

    @include m(select) {
      width: 30px;
      height: 30px;
      border: 1px solid #eaf5fe;
      border-left: none;
      display: flex;
      justify-content: center;
      padding: 2px 2px 0px 0px;
      cursor: pointer;
      // border-radius: 0 3px 3px 0;
      border-top-right-radius: 50px;
      border-bottom-right-radius: 50px;
      background-color: #eaf5fe;
      color: #2e99f3;
      font-weight: bold;
    }
  }
}
</style>
