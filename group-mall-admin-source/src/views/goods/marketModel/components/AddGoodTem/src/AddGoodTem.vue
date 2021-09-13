<!--
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-07-17 13:29:52
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-24 14:52:16
-->
<template>
  <div>
    <el-tabs v-model="tagType">
      <el-tab-pane label="商品信息" name="0">
        <keep-alive>
          <GoodForm :from="from" ref="good" :tagType="tagType" />
        </keep-alive>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop, Ref } from "vue-property-decorator";
import GoodForm from "./GoodForm/index.js";

@Component({
  components: {
    GoodForm,
  },
})
export default class AddGoodItem extends Vue {
  @Prop({
    type: String,
    default() {
      return "";
    },
  })
  from!: string;

  @Ref()
  readonly good?: GoodForm;

  tagType = "0";
}
</script>
