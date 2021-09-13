<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-23 20:21:32
-->
<template>
  <div class="classifica__list-page">
    <ClassifiedGoods :formData="formData"></ClassifiedGoods>
  </div>
</template>
<script lang="ts">
import { Vue, Prop, Component } from "vue-property-decorator";
import BusinessSuper from "./../BusinessSuper";
import ClassifiedGoods from "./ClassifiedGoods.vue";

@Component({
  components: {
    ClassifiedGoods,
  },
})
export default class ClassificationList extends Vue {
  @Prop()
  formData!: BusinessSuper;
}
</script>
