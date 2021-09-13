<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-23 18:03:06
-->
<template>
  <!-- 搜索 -->
  <el-form :model="formData2" label-width="100px">
    <el-form-item label="选中样式">
      <el-radio-group v-model="formData2.showStyle">
        <el-radio
          v-for="item in showStyles"
          :key="item.value"
          :label="item.value"
          >{{ item.label }}</el-radio
        >
      </el-radio-group>
    </el-form-item>
    <el-form-item label="预设关键字">
      <el-input
        v-model="formData2.keyWord"
        :maxlength="8"
        placeholder="最多显示8个字"
      ></el-input>
    </el-form-item>
    <el-form-item label="热门搜索">
      <div v-for="(item, i) in formData2.hotWord" :key="i" class="item">
        <span class="el-icon-remove-outline icon" @click="remove(i)"></span>
        <el-input
          v-model="formData2.hotWord[i]"
          :maxlength="8"
          placeholder="最多显示8个字"
        ></el-input>
        <span class="el-icon-circle-plus-outline icon" @click="add"></span>
      </div>
      <div>{{ `搜索热词关键字(${formData2.hotWord.length}/10)` }}</div>
    </el-form-item>
  </el-form>
</template>

<script lang="ts">
import { Vue, Component, PropSync } from "vue-property-decorator";
import Search from "./Search";

interface Sytles {
  label: string;
  value: string;
}

@Component
export default class SearchForm extends Vue {
  @PropSync("formData", {
    type: Object,
    default() {
      return null;
    },
  })
  formData2!: Search;

  showStyles: Sytles[] = [
    {
      label: "样式一",
      value: "is-style-one",
    },
    {
      label: "样式二",
      value: "is-style-two",
    },
    {
      label: "样式三",
      value: "is-style-three",
    },
  ];

  /**
   * @LastEditors: chuyinlong
   * @description: 添加搜索热词
   */
  add() {
    if (this.formData2.hotWord.length === 10) {
      this.$message.warning("搜索热词最多为10个！");
      return;
    }
    this.formData2.hotWord.push("预设搜索热词");
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 删除搜索热词
   * @param {number} i
   */
  remove(i: number) {
    if (this.formData2.hotWord.length === 1) {
      this.$message.warning("搜索热词最少为1个！");
      return;
    }
    this.formData2.hotWord.splice(i, 1);
  }
}
</script>
<style lang="scss" scope>
/deep/ .el-input {
  display: inline;
}

.item {
  display: flex;
  margin-bottom: 5px;
  .icon {
    color: blue;
    cursor: pointer;
    line-height: 35px;
    padding: 0 10px;
    font-size: 15px;
  }
  .el-icon-remove-outline {
    color: red;
  }
}
</style>
